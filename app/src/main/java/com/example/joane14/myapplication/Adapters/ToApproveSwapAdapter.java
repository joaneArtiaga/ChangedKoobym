package com.example.joane14.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.BookReviewActivity;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.TransactionActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by Joane14 on 11/10/2017.
 */

public class ToApproveSwapAdapter extends RecyclerView.Adapter<ToApproveSwapAdapter.BookHolder> {

    public List<SwapHeader> bookList;
    SwapHeader swapHeader;
    public Activity context;

    @Override
    public ToApproveSwapAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_to_receive_swap, parent, false);

        this.context = (Activity) parent.getContext();
        swapHeader = new SwapHeader();
        Log.d("LandingPAgeAdapter","inside");
        ToApproveSwapAdapter.BookHolder dataObjectHolder = new ToApproveSwapAdapter.BookHolder(this.context, view);

        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ToApproveSwapAdapter(List<SwapHeader> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(ToApproveSwapAdapter.BookHolder holder, final int position) {


        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
        if(bookList.get(position).getRequestedSwapDetail().getBookOwner().getUserObj().getUserId()==user.getUserId()){
            holder.mMyBook.setText(bookList.get(position).getRequestedSwapDetail().getBookOwner().getBookObj().getBookTitle());
            Glide.with(context).load(bookList.get(position).getRequestedSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mMyIvBookImg);
            Glide.with(context).load(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);
            holder.mBookRented.setText(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookTitle());
        }else{
            holder.mMyBook.setText(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookTitle());
            Glide.with(context).load(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mMyIvBookImg);
            Glide.with(context).load(bookList.get(position).getRequestedSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);
            holder.mBookRented.setText(bookList.get(position).getRequestedSwapDetail().getBookOwner().getBookObj().getBookTitle());
        }
        holder.mMU.setText(bookList.get(position).getLocation().getLocationName());
        holder.mPrice.setText(String.valueOf(bookList.get(position).getSwapDetail().getSwapPrice()));

        if(bookList.get(position).getUser()==null){
            holder.mRenter.setText("Renter not Found");
        }else{
            holder.mRenter.setText(bookList.get(position).getUser().getUserFname()+" "+bookList.get(position).getUser().getUserLname());
        }

        holder.mReminder.setText("Receive book on "+ bookList.get(position).getDateTimeStamp());


        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getUser().getImageFilename())).fit().into(holder.mIvRenter);

//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

        holder.mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapHeader = bookList.get(position);
                updateReceive(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mRenter, mBookRented, mReminder, mPrice, mMU, mDaysRent, mMyBook;
        ImageView mIvRenter, mIvBookImg, mMyIvBookImg;
        ImageView mBtnAccept;
        SwapHeader swapHeaderObj;
        Context context;


        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBtnAccept = (ImageView) itemView.findViewById(R.id.btnApprove);
            mRenter = (TextView) itemView.findViewById(R.id.toReceiveRenter);
            mReminder = (TextView) itemView.findViewById(R.id.toReceiveReminder);
            mMU = (TextView) itemView.findViewById(R.id.toReceiveMU);
            mPrice = (TextView) itemView.findViewById(R.id.toReceivePrice);
            mBookRented = (TextView) itemView.findViewById(R.id.toReceiveBook);
            mIvRenter = (ImageView) itemView.findViewById(R.id.toReceiveRenterImage);
            mIvBookImg = (ImageView) itemView.findViewById(R.id.toReceiveBookImage);
            mMyIvBookImg = (ImageView) itemView.findViewById(R.id.toReceiveMyBookImage);
            mMyBook = (TextView) itemView.findViewById(R.id.toReceiveMyBook);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swapHeaderObj = new SwapHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    swapHeaderObj = ToApproveSwapAdapter.this.bookList.get(position);
                    if(swapHeaderObj==null){
                        Log.d("rentalHeaderAdapter", "is null");
                    }else{
                        Log.d("rentalHeaderAdapter", "is not null");
                    }
//                    intent.putExtra("ViewBook", "fromAdapter");
//                    bundle.putSerializable("View", rentalDetailObj);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
                }
            });
        }

//        @Override
//        public void onClick(View v) {
//            int position = getAdapterPosition();
//
//
//            Log.d("AdapterPosition", "inside "+Integer.toString(position));
//
//        }
    }

    public void updateReceive(final int position){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
        swapHeader = bookList.get(position);
        String status="";
        if(swapHeader.getStatus().equals("APPROVED_BY_REQUESTOR")){
            status = "Approved";
            Intent intent = new Intent(context, TransactionActivity.class);
            context.startActivity(intent);
        }

        String URL = Constants.UPDATE_SWAP_HEADER+"/"+status+"/"+swapHeader.getSwapHeaderId();

        Log.d("Update SwapHeader URL", URL);


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeader);


        Log.d("LOG_VOLLEY_swapHeaderUD", mRequestBody);
        Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RequestReceivedStatus", response);

                updateBookOwner(position);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public void updateBookOwner(int position){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
        swapHeader = bookList.get(position);
        String status="";

        String URL = Constants.UPDATE_BOOK_OWNER+"/"+swapHeader.getSwapDetail().getBookOwner().getBookOwnerId()+"/"+user.getUserId();

        Log.d("Update SwapHeader URL", URL);


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeader);


        Log.d("LOG_VOLLEY_swapHeaderUD", mRequestBody);
        Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RequestReceivedStatus", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }



}

