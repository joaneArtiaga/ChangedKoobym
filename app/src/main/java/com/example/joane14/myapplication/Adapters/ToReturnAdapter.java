package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.ProfileActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.VolleyUtil;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserNotification;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Joane14 on 05/10/2017.
 */

public class ToReturnAdapter extends RecyclerView.Adapter<ToReturnAdapter.BookHolder>{

    public List<RentalHeader> bookList;
    public Activity context;

    @Override
    public ToReturnAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_activity_item, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        ToReturnAdapter.BookHolder dataObjectHolder = new ToReturnAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ToReturnAdapter(List<RentalHeader> myDataset) {
        bookList = myDataset;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ToReturnAdapter.BookHolder holder, final int position) {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
        String currDAte = sdf.format(c);

        if(bookList.get(position).getRentalEndDate().equals(currDAte)){
            holder.mNotify.setVisibility(View.GONE);
        }



        holder.mBookTitle.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mRenter.setText("Available "+bookList.get(position).getRentalDetail().getDaysForRent()+" days for rent.");
        holder.mPrice.setText(String.valueOf(bookList.get(position).getRentalDetail().getCalculatedPrice()));
        holder.mBookDate.setText(bookList.get(position).getRentalTimeStamp());

        if(bookList.get(position).getRentalEndDate()==null){
            Log.d("EndDateReturnRent", "walay sulod");
        }else{
            holder.mDate.setText(bookList.get(position).getRentalEndDate());
        }


        if(bookList.get(position).getMeetUp()==null){

        }else{
            holder.mLocation.setText(bookList.get(position).getMeetUp().getLocation().getLocationName());
            holder.mTime.setText(bookList.get(position).getMeetUp().getUserDayTime().getTime().getStrTime());
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
//        holder.mDaysRent.setText("Due date of book will be on "+newDate);
//        Picasso.with(context).load(bookList.get(position).getUserId().getImageFilename()).fit().into(holder.mIvRenter);
        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
//        String currDate = datef.format(c.getTime());
//        Date d = null, d2 = null;
//        try {
//            d = datef.parse(newDate);
//            d2 = datef.parse(currDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        long diff = d2.getTime() - d.getTime();
//        long seconds = diff / 1000;
//        long minutes = seconds /60;
//        long hours = minutes / 60;
//        long days = hours /24;
//
//        Log.d("daysAvailable", String.valueOf(days));

        holder.mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userModelPass", bookList.get(position).getUserId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.mNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog ad = new AlertDialog.Builder(context).create();
                ad.setTitle("Alert!");
                ad.setMessage("Are you sure you can return the book earlier?");
                ad.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMailNotif(bookList.get(position));
                    }
                });
                ad.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        holder.mRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookList.get(position).getStatus().equals("return")){

                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Alert!");
                    alertDialog.setMessage("The renter has not yet confirmed that he/she received the book already.");
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

    private void sendMailNotif(RentalHeader rentalHeader){
//        String URL = "http://104.198.152.85/Koobym/rentalDetail/suggested/%d";
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/suggested/%d";
        String URL = Constants.SEND_NOTIF_MAIL+rentalHeader.getRentalHeaderId();
//        URL = String.format(URL, userId);
        Log.d("MailNotif", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("EarlyNotif", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                RentalHeader rentalHeaderMod = gson.fromJson(response, RentalHeader.class);

//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
//                BookOwnerModel[] bookOwnerModels = gson.fromJson(response, BookOwnerModel[].class);
//                bookOwnerModelList.addAll(Arrays.asList(bookOwnerModels));
//                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(context).add(stringRequest);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mBookTitle, mRenter, mBookDate, mPrice, mLocation, mTime, mDate;
        ImageView mIvBookImg;
        ImageButton mProfile, mNotify, mRate;
        RentalHeader rentalHeaderObj;

        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBookTitle = (TextView) itemView.findViewById(R.id.bookTitleBA);
            mDate = (TextView) itemView.findViewById(R.id.dateBA);
            mRenter = (TextView) itemView.findViewById(R.id.renterNameBA);
//            mRenter = (TextView) itemView.findViewById(R.id.toReceiveRenter);
            mBookDate = (TextView) itemView.findViewById(R.id.bookDateBA);
            mLocation = (TextView) itemView.findViewById(R.id.locationBA);
            mPrice = (TextView) itemView.findViewById(R.id.bookPriceBA);
            mTime = (TextView) itemView.findViewById(R.id.timeBA);
//            mIvRenter = (ImageView) itemView.findViewById(R.id.toReceiveRenterImage);
            mIvBookImg = (ImageView) itemView.findViewById(R.id.ivBookBA);
            mProfile = (ImageButton) itemView.findViewById(R.id.profileBA);
            mNotify = (ImageButton) itemView.findViewById(R.id.notifyBA);
            mRate = (ImageButton) itemView.findViewById(R.id.rateButtonBA);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rentalHeaderObj = new RentalHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    rentalHeaderObj = ToReturnAdapter.this.bookList.get(position);
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
}
