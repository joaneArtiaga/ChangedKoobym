package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.example.joane14.myapplication.Activities.HistoryActivity;
import com.example.joane14.myapplication.Activities.ProfileActivity;
import com.example.joane14.myapplication.Activities.TransactionActivity;
import com.example.joane14.myapplication.Activities.UserReviewActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Joane14 on 05/10/2017.
 */

public class ToReceiveRentAdapter extends RecyclerView.Adapter<ToReceiveRentAdapter.BookHolder> {

    public List<RentalHeader> bookList;
    RentalHeader rentalHeader;
    public Activity context;

    @Override
    public ToReceiveRentAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_activity_item, parent, false);

        this.context = (Activity) parent.getContext();
        rentalHeader = new RentalHeader();
        Log.d("ToRecevieAdapter","inside");
        ToReceiveRentAdapter.BookHolder dataObjectHolder = new ToReceiveRentAdapter.BookHolder(this.context, view);

        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ToReceiveRentAdapter(List<RentalHeader> myDataset) {
        bookList = myDataset;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ToReceiveRentAdapter.BookHolder holder, final int position) {


        holder.mBookTitle.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mBookDate.setText(bookList.get(position).getRentalTimeStamp());
        holder.mPrice.setText(String.valueOf(bookList.get(position).getRentalDetail().getCalculatedPrice()));
        holder.mBookRenter.setText(bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getUserFname()+" "+bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getUserLname());
        if(bookList.get(position).getDateDeliver()==null){
            Log.d("EndDateReceiveRent", "walay sulod");
        }else{
            holder.mDate.setText(bookList.get(position).getDateDeliver());
        }
        if(bookList.get(position).getMeetUp()==null){

        }else{
            holder.mTime.setText(bookList.get(position).getMeetUp().getUserDayTime().getTime().getStrTime());
            holder.mLocation.setText(bookList.get(position).getMeetUp().getLocation().getLocationName());
        }
//        if(bookList.get(position).getUserId()==null){
//            holder.mRenter.setText("Renter not Found");
//        }else{
//            holder.mRenter.setText(bookList.get(position).getUserId().getUserFname()+" "+bookList.get(position).getUserId().getUserLname());
//        }

//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = null;
//        String newDate="";
//        try {
//            date = df.parse(bookList.get(position).getRentalTimeStamp());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        @SuppressLint({"NewApi", "LocalSuppress"})
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(calendar.DATE, bookList.get(position).getRentalDetail().getDaysForRent());
//        newDate = df.format(calendar.getTime());
//
//        holder.mDaysRent.setText("This book should be returned on "+newDate);

//        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getUserId().getImageFilename())).fit().into(holder.mIvRenter);
        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBook);

//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

        holder.mBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userModelPass", bookList.get(position).getRentalDetail().getBookOwner().getUserObj());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.mBtnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookList.get(position).getStatus().equals("asdfasdfas")){

                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Alert!");
                    alertDialog.setMessage("The owner has not yet confirmed that he/she received the book already.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mBookTitle, mBookRenter, mBookDate, mPrice, mLocation, mTime, mDate;
        ImageView mIvBook;
        ImageButton mBtnProfile, mBtnMail, mBtnRate;
        RentalHeader rentalHeaderObj;
        Context context;


        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBookTitle = (TextView) itemView.findViewById(R.id.bookTitleBA);
            mDate = (TextView) itemView.findViewById(R.id.dateBA);
            mBookRenter = (TextView) itemView.findViewById(R.id.renterNameBA);
//            mRenter = (TextView) itemView.findViewById(R.id.toReceiveRenter);
            mBookDate = (TextView) itemView.findViewById(R.id.bookDateBA);
            mLocation = (TextView) itemView.findViewById(R.id.locationBA);
            mPrice = (TextView) itemView.findViewById(R.id.bookPriceBA);
            mTime = (TextView) itemView.findViewById(R.id.timeBA);
//            mIvRenter = (ImageView) itemView.findViewById(R.id.toReceiveRenterImage);
            mIvBook = (ImageView) itemView.findViewById(R.id.ivBookBA);
            mBtnProfile = (ImageButton) itemView.findViewById(R.id.profileBA);
            mBtnMail = (ImageButton) itemView.findViewById(R.id.notifyBA);
            mBtnRate = (ImageButton) itemView.findViewById(R.id.rateButtonBA);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rentalHeaderObj = new RentalHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    rentalHeaderObj = ToReceiveRentAdapter.this.bookList.get(position);
                    if(rentalHeaderObj==null){
                        Log.d("reaceiveRentAdapter", "is null");
                    }else{
                        Log.d("reaceiveRentAdapter", "is not null");
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

    public void updateReceive(int position, Boolean bool){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        rentalHeader = bookList.get(position);
        String status="";
        if(rentalHeader.getStatus().equals("Confirmation")){
            if(bool==true){
                status = "Approved";
            }else{
                status = "Rejected";
            }
        }else if(rentalHeader.getStatus().equals("Approved")){
            if(bool==true){
                status = "Received";
            }else{
                status = "Rejected";
            }
        }else{
            if(bool==true){
                status = "Complete";

            }else{
                status = "Rejected";
            }

        }

        String URL = Constants.UPDATE_RENTAL_HEADER+"/"+rentalHeader.getRentalHeaderId()+"/"+status;


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
        final String finalStatus = status;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RequestReceivedStatus", response);
                RentalHeader rentalHeaderModel = new RentalHeader();
                if(finalStatus.equals("Complete")){
                    incrementRenters(rentalHeader.getRentalDetail().getBookOwner());
                }else if(finalStatus.equals("Received")){
                    Intent intent = new Intent(context, BookReviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("rentalHeader", rentalHeader);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else if(finalStatus.equals("Approved")){
                    Intent intent = new Intent(context, TransactionActivity.class);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, HistoryActivity.class);
                    context.startActivity(intent);
                }

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

    public void incrementRenters(BookOwnerModel bookOwnerModel){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        String URL = Constants.INCREMENT_BOOK_OWNER+"/"+bookOwnerModel.getBookOwnerId();


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookOwnerModel);


        Log.d("LOG_VOLLEY", mRequestBody);
        Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RequestReceivedStatus", response);
                Intent intent = new Intent(context, UserReviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("rentalHeader", rentalHeader);
                intent.putExtras(bundle);
                context.startActivity(intent);
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
