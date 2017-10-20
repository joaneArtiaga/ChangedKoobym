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
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Joane14 on 05/10/2017.
 */

public class CompleteRenterAdapter extends RecyclerView.Adapter<CompleteRenterAdapter.BookHolder> {

    public List<RentalHeader> bookList;
    public Activity context;

    @Override
    public CompleteRenterAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_complete, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        CompleteRenterAdapter.BookHolder dataObjectHolder = new CompleteRenterAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public CompleteRenterAdapter(List<RentalHeader> myDataset) {
        bookList = myDataset;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(CompleteRenterAdapter.BookHolder holder, int position) {


        holder.mBookRented.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());
//        holder.mRenterText.setText("Renter");

//        if(bookList.get(position).getUserId()==null){
//            holder.mRenter.setText("Renter not Found");
//        }else{
//            holder.mRenter.setText(bookList.get(position).getUserId().getUserFname()+" "+bookList.get(position).getUserId().getUserLname());
//        }

        holder.mReceiveDate.setText(bookList.get(position).getRentalTimeStamp());

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
        holder.mReturnDate.setText(newDate);


//        Picasso.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getImageFilename()).fit().into(holder.mIvRenter);
        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

        holder.mReceiveTime.setText(bookList.get(position).getUserDayTime().getDay().getStrDay()+", "+bookList.get(position).getUserDayTime().getTime().getStrTime());
        holder.mReturnTime.setText(bookList.get(position).getUserDayTime().getDay().getStrDay()+", "+bookList.get(position).getUserDayTime().getTime().getStrTime());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mRenter, mBookRented, mReceiveDate, mReceiveTime, mReturnDate, mReturnTime , mRenterText;
        ImageView mIvRenter, mIvBookImg;
        RentalHeader rentalHeaderObj;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
//            mRenter = (TextView) itemView.findViewById(R.id.completeRenter);
//            mRenterText = (TextView) itemView.findViewById(R.id.textRenter);
            mReceiveDate = (TextView) itemView.findViewById(R.id.receivedDate);
            mReceiveTime = (TextView) itemView.findViewById(R.id.receivedTime);
            mReturnDate = (TextView) itemView.findViewById(R.id.returnedDate);
            mReturnTime = (TextView) itemView.findViewById(R.id.returnedTime);
            mBookRented = (TextView) itemView.findViewById(R.id.completeBook);
//            mIvRenter = (ImageView) itemView.findViewById(R.id.completeRenterImage);
            mIvBookImg = (ImageView) itemView.findViewById(R.id.completeBookImage);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rentalHeaderObj = new RentalHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    rentalHeaderObj = CompleteRenterAdapter.this.bookList.get(position);
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
