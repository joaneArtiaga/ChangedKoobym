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
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.HistoryActivity;
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

public class ApprovedSwap extends RecyclerView.Adapter<ApprovedSwap.BookHolder> {

    public List<SwapHeader> bookList;
    SwapHeader swapHeader;
    public Activity context;

    @Override
    public ApprovedSwap.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_approved_swap, parent, false);

        this.context = (Activity) parent.getContext();
        swapHeader = new SwapHeader();
        Log.d("LandingPAgeAdapter","inside");
        ApprovedSwap.BookHolder dataObjectHolder = new ApprovedSwap.BookHolder(this.context, view);

        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ApprovedSwap(List<SwapHeader> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(ApprovedSwap.BookHolder holder, final int position) {


        holder.mMyBook.setText(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mBookRented.setText(bookList.get(position).getRequestedSwapDetail().getBookOwner().getBookObj().getBookTitle());
        Glide.with(context).load(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mMyIvBookImg);
        Glide.with(context).load(bookList.get(position).getRequestedSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);
        holder.mMU.setText(bookList.get(position).getLocation().getLocationName());

        Log.d("PossibleSwap", bookList.get(position).getSwapDetail().getBookOwner().getUserObj().getUserFname());
        Log.d("PossibleSwap", bookList.get(position).getRequestedSwapDetail().getBookOwner().getUserObj().getUserFname());

        holder.mReminder.setText("Receive book on "+ bookList.get(position).getDateTimeStamp()+" at "+bookList.get(position).getUserDayTime().getDay().getStrDay()+", "+bookList.get(position).getUserDayTime().getTime().getStrTime());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mBookRented, mReminder, mPrice, mMU, mMyBook;
        ImageView mIvBookImg, mMyIvBookImg;
        SwapHeader swapHeaderObj;
        Context context;


        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;

            mReminder = (TextView) itemView.findViewById(R.id.toReceiveReminder);
            mMU = (TextView) itemView.findViewById(R.id.toReceiveMU);
            mPrice = (TextView) itemView.findViewById(R.id.toReceivePrice);
            mBookRented = (TextView) itemView.findViewById(R.id.toReceiveBook);
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
                    swapHeaderObj = ApprovedSwap.this.bookList.get(position);
                    if(swapHeaderObj==null){
                        Log.d("rentalHeaderAdapter", "is null");
                    }else{
                        Log.d("rentalHeaderAdapter", "is not null");
                    }
                }
            });
        }

    }


}

