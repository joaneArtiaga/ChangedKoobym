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

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.ProfileActivity;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;

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


        holder.mBookTitle.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mBookDate.setText(bookList.get(position).getRentalTimeStamp());
        holder.mPrice.setText(String.valueOf(bookList.get(position).getRentalDetail().getCalculatedPrice()));
        holder.mRenterName.setText(bookList.get(position).getUserId().getUserFname()+" "+bookList.get(position).getUserId().getUserLname());
        if(bookList.get(position).getDateDeliver()==null){
            Log.d("EndDateDeliverRent", "walay sulod");
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
        newDate = df.format(calendar.getTime());

//        Picasso.with(context).load(bookList.get(position).getUserId().getImageFilename()).fit().into(holder.mIvRenter);
        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

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


        if(bookList.get(position).getStatus().equals("")){
            holder.mRate.setImageResource(R.drawable.torate);
        }else{
            holder.mRate.setImageResource(R.drawable.notrate);
        }

        holder.mRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookList.get(position).getStatus().equals(bookList.get(position).getStatus())){
                    Log.d("rateButton", "inside");
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
                    alertDialog.show();
                }
            }
        });
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
