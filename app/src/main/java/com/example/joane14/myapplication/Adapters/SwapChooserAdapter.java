package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.example.joane14.myapplication.Activities.MyShelf;
import com.example.joane14.myapplication.Activities.RequestActivity;
import com.example.joane14.myapplication.Activities.SwapBookChooser;
import com.example.joane14.myapplication.Activities.SwapMeetUpChooser;
import com.example.joane14.myapplication.Activities.TimeDateChooser;
import com.example.joane14.myapplication.Activities.TransactionActivity;
import com.example.joane14.myapplication.Activities.ViewBookSwapActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.util.Log.d;

/**
 * Created by Joane14 on 08/10/2017.
 */

public class SwapChooserAdapter extends RecyclerView.Adapter<SwapChooserAdapter.BookHolder> {

    public List<SwapDetail> bookList;
    SwapDetail swapDetailObj;
    SwapHeader swapHeader;
    public Activity context;


    @Override
    public SwapChooserAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item_swap, parent, false);

        this.context = (Activity) parent.getContext();
        d("LandingPAgeAdapter","inside");
        SwapChooserAdapter.BookHolder dataObjectHolder = new SwapChooserAdapter.BookHolder(this.context, view);
        return dataObjectHolder;
    }



    public SwapChooserAdapter(List<SwapDetail> myDataset, SwapHeader swapBook) {
        bookList = myDataset;
        this.swapHeader = swapBook;
    }

    @Override
    public void onBindViewHolder(SwapChooserAdapter.BookHolder holder, final int position) {


        d("Inside", "SwapBookChooser");
        swapHeader.setRequestedSwapDetail(bookList.get(position));
        String author = "";
        holder.mBookTitle.setText(bookList.get(position).getBookOwner().getBookObj().getBookTitle());
        holder.mBookPrice.setText(bookList.get(position).getBookOwner().getBookObj().getBookOriginalPrice().toString());
//        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getUser().getImageFilename())).fit().into(holder.mSwapCommenterImg);
//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());
        Glide.with(context).load(bookList.get(position).getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mBookPic);

        if(bookList.get(position).getBookOwner().getBookObj().getBookAuthor().size()!=0){
            for(int init=0; init<bookList.get(position).getBookOwner().getBookObj().getBookAuthor().size(); init++){
                if(!(bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                    author+=bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                    if(!(bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                        author+=bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                        if(init+1<bookList.get(position).getBookOwner().getBookObj().getBookAuthor().size()){
                            author+=", ";
                        }
                    }
                }
            }
        }else{
            author="Unknown Author";
        }

        holder.mBookAuthor.setText(author);


    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mBookTitle, mBookAuthor, mBookPrice;
        ImageView mBookPic;
        SwapDetail swapDetail;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBookTitle = (TextView) itemView.findViewById(R.id.swapBookTitle);
            mBookAuthor = (TextView) itemView.findViewById(R.id.swapAuthor);
            mBookPrice = (TextView) itemView.findViewById(R.id.swapBookPrice);
            mBookPic = (ImageView) itemView.findViewById(R.id.swapBookPic);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swapDetail = new SwapDetail();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    showConfirmation(position);
//                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(SwapChooserAdapter.this.context, ViewBookSwapActivity.class);
//                    swapDetail = SwapChooserAdapter.this.bookList.get(position);
//                    if(swapDetail==null){
//                        Log.d("rentalDetailAdapter", "is null");
//                    }else{
//                        Log.d("rentalDetailAdapter", "is not null");
//                    }
//                    intent.putExtra("ViewBookSwap", "fromAdapter");
//                    bundle.putSerializable("ViewSwap", swapDetail);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
                }
            });
        }

    }

    public void showConfirmation(final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Meet Up Summary");
        alertDialogBuilder.setMessage("Are you sure you want to swap your book "+swapHeader.getSwapDetail().getBookOwner().getBookObj().getBookTitle()+" " +
                "with "+bookList.get(position).getBookOwner().getUserObj().getUserFname()+" "+bookList.get(position).getBookOwner().getUserObj().getUserLname()+
                "'s book, "+bookList.get(position).getBookOwner().getBookObj().getBookTitle()+"?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


//                        Intent intent = new Intent(context, SwapMeetUpChooser.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("swapHeader", swapHeader);
//                        bundle.putSerializable("swapDetail", bookList.get(position));
//                        intent.putExtras(bundle);
//                        context.startActivity(intent);

                        addSwapHeader(position);

                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {


            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressLint("NewApi")
    public void addSwapHeader(int position){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.POST_SWAP_HEADER;

        String nextDateStr = "";

        Calendar c = Calendar.getInstance();
        @SuppressLint({"NewApi", "LocalSuppress"})
        DateFormat format = new android.icu.text.SimpleDateFormat("yyyy-MM-dd");
        nextDateStr = format.format(c.getTime());

        SwapHeader swapToPost = new SwapHeader();
        swapToPost.setUser(new User());
        swapToPost.getUser().setUserId(swapHeader.getSwapDetail().getBookOwner().getUserObj().getUserId());
        swapToPost.setSwapDetail(new SwapDetail());
        swapToPost.setSwapDetail(swapHeader.getSwapDetail());
        swapToPost.setRequestedSwapDetail(new SwapDetail());
        swapToPost.setRequestedSwapDetail(bookList.get(position));
        swapToPost.setDateTimeStamp(nextDateStr);
        swapToPost.setStatus("Request");
        swapToPost.setDateRequest(nextDateStr);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapToPost);

        int maxLogize = 1000;
        for(int i=0; i<=mRequestBody.length()/maxLogize; i++){
            int start = i * maxLogize;
            int end = (i+1) * maxLogize;
            end = end > mRequestBody.length()? mRequestBody.length() : end;
            Log.d("AddSwapHeaderVolley", mRequestBody.substring(start, end));
        }
        d("swapHeaderAdd", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("onResponse addSwapH", "inside");
                Log.i("AddSwapHeader", response);
                SwapHeader swapHeaderPost = gson.fromJson(response, SwapHeader.class);

                updateSwapHeader(swapHeaderPost);

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

    public void updateSwapHeader(SwapHeader swapHeaderModel){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

        String URL = Constants.UPDATE_SWAP_HEADER+"/"+"Request/"+swapHeaderModel.getSwapHeaderId();

        d("putRentalHeader", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeaderModel);


        d("LOG_VOLLEY", mRequestBody);
        final User finalUser = user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("rentalHeaderResponseUD", response);
                RentalHeader rentalHeaderMod = gson.fromJson(response, RentalHeader.class);

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
