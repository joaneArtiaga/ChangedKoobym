package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.ToReceiveFrag;
import com.example.joane14.myapplication.Model.RentalHeader;
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
 * Created by Joane14 on 05/10/2017.
 */

public class ToReceiveAdapter extends RecyclerView.Adapter<ToReceiveAdapter.BookHolder> {

    public List<RentalHeader> bookList;
    RentalHeader rentalHeader;
    public Activity context;

    @Override
    public ToReceiveAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_to_receive, parent, false);

        this.context = (Activity) parent.getContext();
        rentalHeader = new RentalHeader();
        Log.d("LandingPAgeAdapter","inside");
        ToReceiveAdapter.BookHolder dataObjectHolder = new ToReceiveAdapter.BookHolder(this.context, view);

        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ToReceiveAdapter(List<RentalHeader> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(ToReceiveAdapter.BookHolder holder, final int position) {


        holder.mBookRented.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mDaysRent.setText("Available "+bookList.get(position).getRentalDetail().getDaysForRent()+" days for rent.");
        holder.mMU.setText(bookList.get(position).getLocation().getLocationName());
        holder.mPrice.setText(String.valueOf(bookList.get(position).getRentalDetail().getCalculatedPrice()));

        if(bookList.get(position).getUser()==null){
            holder.mRenter.setText("Renter not Found");
        }else{
            holder.mRenter.setText(bookList.get(position).getUser().getUserFname()+" "+bookList.get(position).getUser().getUserLname());
        }

        holder.mReminder.setText("Return book and receive by DATE.");


        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getImageFilename())).fit().into(holder.mIvRenter);
        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

        holder.mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rentalHeader = bookList.get(position);
                updateReceive(rentalHeader);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mRenter, mBookRented, mReminder, mPrice, mMU, mDaysRent;
        ImageView mIvRenter, mIvBookImg;
        ImageView mBtnAccept;
        RentalHeader rentalHeaderObj;
        Context context;


        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBtnAccept = (ImageView) itemView.findViewById(R.id.btnApprove);
            mRenter = (TextView) itemView.findViewById(R.id.toReceiveRenter);
            mReminder = (TextView) itemView.findViewById(R.id.toReceiveReminder);
            mMU = (TextView) itemView.findViewById(R.id.toReceiveMU);
            mDaysRent = (TextView) itemView.findViewById(R.id.toReceiveDaysForRent);
            mPrice = (TextView) itemView.findViewById(R.id.toReceivePrice);
            mBookRented = (TextView) itemView.findViewById(R.id.toReceiveBook);
            mIvRenter = (ImageView) itemView.findViewById(R.id.toReceiveRenterImage);
            mIvBookImg = (ImageView) itemView.findViewById(R.id.toReceiveBookImage);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rentalHeaderObj = new RentalHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    rentalHeaderObj = ToReceiveAdapter.this.bookList.get(position);
                    if(rentalHeaderObj==null){
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

    public String getDetails(int position){
        String result = "";

        result = bookList.get(position).toString();



        return result;

    }


    public void updateReceive(RentalHeader rentalHeader){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.UPDATE_RENTAL_HEADER;

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);
        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

        Log.d("RentalHeaderReceive",rentalHeader.getUser().toString());
        if(rentalHeader.getUser().getUserId()==user.getUserId()){
            rentalHeader.setStatus("To Return");
        }

        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseRentalHeader", response);
                ToReceiveFrag toReceiveFrag = new ToReceiveFrag();
                toReceiveFrag.refreshAdapter();
//                User user = gson.fromJson(response, User.class);
//                Log.i("LOG_VOLLEY", user.getEmail());
//                Log.i("LOG_VOLLEY", user.getUserFname());
//                Log.i("LOG_VOLLEY", user.getUserLname());
//                user.setGenreArray(genres);
//                Intent intent = new Intent(SignUp.this, LandingPage.class);
//                Bundle b = new Bundle();
//                b.putBoolean("fromRegister", true);
//                b.putSerializable("userModel", user);
//                intent.putExtra("user",b);
//                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
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
