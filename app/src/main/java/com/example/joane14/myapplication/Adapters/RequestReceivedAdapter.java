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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.TransactionActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joane14 on 07/10/2017.
 */

public class RequestReceivedAdapter extends RecyclerView.Adapter<RequestReceivedAdapter.BookHolder> {

    public List<RentalHeader> bookList;
    public Activity context;
    public String fromWhere;

    @Override
    public RequestReceivedAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_request_received, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("RequestReceived","inside");
        RequestReceivedAdapter.BookHolder dataObjectHolder = new RequestReceivedAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public RequestReceivedAdapter(List<RentalHeader> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(RequestReceivedAdapter.BookHolder holder, int position) {

        holder.mRequestor.setText(bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getUserFname()+" "+bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getUserLname());

        holder.mBookReq.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());
        Log.d("libroShet", String.valueOf(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookOriginalPrice()));

        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookReq);

//        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getImageFilename())).fit().into(holder.mIvRequestor);

        holder.mBtnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransactionActivity.class);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mRequestor, mBookReq;
        ImageView mIvRequestor, mIvBookReq, mBtnApprove, mBtnReject;
        RentalHeader rentalHeaderObj;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mRequestor = (TextView) itemView.findViewById(R.id.tvRRRequestor);
            mBookReq = (TextView) itemView.findViewById(R.id.tvRRBookReq);
            mIvBookReq = (ImageView) itemView.findViewById(R.id.ivRRBookReq);
            mIvRequestor = (ImageView) itemView.findViewById(R.id.ivRRRequestor);
            mBtnApprove = (ImageView) itemView.findViewById(R.id.rrBtnApprove);
            mBtnReject = (ImageView) itemView.findViewById(R.id.rrBtnReject);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rentalHeaderObj = new RentalHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    rentalHeaderObj = RequestReceivedAdapter.this.bookList.get(position);
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




}
