package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;

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
                .inflate(R.layout.card_to_deliver, parent, false);

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
    public void onBindViewHolder(ToReturnAdapter.BookHolder holder, int position) {


        holder.mBookRented.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mDaysRent.setText("Available "+bookList.get(position).getRentalDetail().getDaysForRent()+" days for rent.");
        holder.mMU.setText(bookList.get(position).getLocation().getLocationName());
        holder.mPrice.setText(String.valueOf(bookList.get(position).getRentalDetail().getCalculatedPrice()));



//        if(bookList.get(position).getUserId()==null){
//            holder.mRenter.setText("Renter not Found");
//        }else{
//            holder.mRenter.setText(bookList.get(position).getUserId().getUserFname()+" "+bookList.get(position).getUserId().getUserLname());
//        }

        holder.mReminder.setText("Deliver book "+bookList.get(position).getRentalTimeStamp());

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

        holder.mDaysRent.setText("Due date of book will be on "+newDate);
//        Picasso.with(context).load(bookList.get(position).getUserId().getImageFilename()).fit().into(holder.mIvRenter);
        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

        Calendar c = Calendar.getInstance();
        SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
        String currDate = datef.format(c.getTime());
        Date d = null, d2 = null;
        try {
            d = datef.parse(newDate);
            d2 = datef.parse(currDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = d2.getTime() - d.getTime();
        long seconds = diff / 1000;
        long minutes = seconds /60;
        long hours = minutes / 60;
        long days = hours /24;

        Log.d("daysAvailable", String.valueOf(days));

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mRenter, mBookRented, mReminder, mPrice, mMU, mDaysRent;
        ImageView mIvRenter, mIvBookImg;
        RentalHeader rentalHeaderObj;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
//            mRenter = (TextView) itemView.findViewById(R.id.deliverRenter);
            mReminder = (TextView) itemView.findViewById(R.id.deliverReminder);
            mMU = (TextView) itemView.findViewById(R.id.deliverMU);
            mDaysRent = (TextView) itemView.findViewById(R.id.deliverDaysForRent);
            mPrice = (TextView) itemView.findViewById(R.id.deliverPrice);
            mBookRented = (TextView) itemView.findViewById(R.id.deliverBook);
//            mIvRenter = (ImageView) itemView.findViewById(R.id.deliverRenterImage);
            mIvBookImg = (ImageView) itemView.findViewById(R.id.deliverBookImage);
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
