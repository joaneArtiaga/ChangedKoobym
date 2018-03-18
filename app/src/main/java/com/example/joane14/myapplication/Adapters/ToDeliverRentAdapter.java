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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.BookActActivity;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.ProfileActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.VolleyUtil;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Joane14 on 03/02/2018.
 */

public class ToDeliverRentAdapter extends RecyclerView.Adapter<ToDeliverRentAdapter.BookHolder> {

    public List<RentalHeader> bookList;
    public Activity context;

    @Override
    public ToDeliverRentAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_activity_item, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("ToDeliverRentAdapter","inside");
        ToDeliverRentAdapter.BookHolder dataObjectHolder = new ToDeliverRentAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ToDeliverRentAdapter(List<RentalHeader> myDataset) {
        bookList = myDataset;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final ToDeliverRentAdapter.BookHolder holder, final int position) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currDate = sdf.format(cal.getTime());

        holder.mNotify.setVisibility(View.GONE);

        holder.mBookTitle.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mBookDate.setText(bookList.get(position).getRentalTimeStamp());
        holder.mPrice.setText("₱  "+holder.mPrice.getText().toString()+"   "+String.valueOf(bookList.get(position).getRentalDetail().getCalculatedPrice()));
        holder.mRenterName.setText(bookList.get(position).getUserId().getUserFname()+" "+bookList.get(position).getUserId().getUserLname());
        if(bookList.get(position).getDateDeliver()==null){
            Log.d("EndDateDeliverRent", "walay sulod");
        }else{
            holder.mDate.setText(bookList.get(position).getDateDeliver()+"  ");
        }
        if(bookList.get(position).getMeetUp()==null){

        }else{
            holder.mTime.setText(bookList.get(position).getMeetUp().getUserDayTime().getTime().getStrTime());
            holder.mLocation.setText(bookList.get(position).getMeetUp().getLocation().getLocationName());
        }


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        String newDate="";
        try {
            date = df.parse(bookList.get(position).getRentalTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint({"NewApi", "LocalSuppress"})
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE, bookList.get(position).getRentalDetail().getDaysForRent());
        final Calendar c = Calendar.getInstance();
        newDate = df.format(c.getTime());

        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

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

        Date dateToCompare = new Date();
        try {
            dateToCompare = df.parse(bookList.get(position).getDateDeliver());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("CurrentDate: "+newDate, "ReturnDate: "+bookList.get(position).getDateDeliver());
        Log.d("returnedBoolean", dateToCompare.compareTo(c.getTime())+"");
        if(dateToCompare.after(c.getTime())|| newDate.equals(bookList.get(position).getDateDeliver())){
            holder.mRate.setImageResource(R.drawable.checkbookact);
        }else{
            holder.mRate.setImageResource(R.drawable.notrate);
        }

        final String finalNewDate = newDate;
        final Date finalDateToCompare = dateToCompare;
        holder.mRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalDateToCompare.after(c.getTime())|| finalNewDate.equals(bookList.get(position).getDateDeliver())){
                    AlertDialog ad = new AlertDialog.Builder(context).create();
                    ad.setMessage("Will notify the renter of your book delivery.");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delivered(bookList.get(position));
                        }
                    });
                    ad.show();
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Alert!");
                    alertDialog.setMessage("Not yet time to deliver.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });
    }

    private void delivered(final RentalHeader rentalHeader){
        String URL = Constants.RENT_DELIVERED+rentalHeader.getRentalHeaderId();
        Log.d("deliverToReceiveURL", URL);
        Log.d("deliverToReceive", rentalHeader.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("deliverToReceiveRes", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                RentalHeader rentalHeaderMod = gson.fromJson(response, RentalHeader.class);

                Intent intent = new Intent(context, BookActActivity.class);
                context.startActivity(intent);

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
        TextView mBookTitle, mRenterName, mBookDate, mPrice, mLocation, mTime, mDate;
        ImageView mIvBookImg;
        RentalHeader rentalHeaderObj;
        ImageButton mProfile, mNotify, mRate;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
//            mRenter = (TextView) itemView.findViewById(R.id.deliverRenter);
            mBookTitle = (TextView) itemView.findViewById(R.id.bookTitleBA);
            mDate = (TextView) itemView.findViewById(R.id.dateBA);
            mRenterName = (TextView) itemView.findViewById(R.id.renterNameBA);
            mBookDate = (TextView) itemView.findViewById(R.id.bookDateBA);
            mPrice = (TextView) itemView.findViewById(R.id.bookPriceBA);
            mLocation = (TextView) itemView.findViewById(R.id.locationBA);
            mTime = (TextView) itemView.findViewById(R.id.timeBA);
//            mIvRenter = (ImageView) itemView.findViewById(R.id.deliverRenterImage);
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
                    rentalHeaderObj = ToDeliverRentAdapter.this.bookList.get(position);
                    if(rentalHeaderObj==null){
                        Log.d("deliverRent", "is null");
                    }else{
                        Log.d("deliverRent", rentalHeaderObj.toString());
                    }
//                    intent.putExtra("ViewBook", "fromAdapter");
//                    bundle.putSerializable("View", rentalDetailObj);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
                }
            });
        }

    }

    public String getDetails(int position){
        String result = "";

        result = bookList.get(position).toString();



        return result;

    }
}
