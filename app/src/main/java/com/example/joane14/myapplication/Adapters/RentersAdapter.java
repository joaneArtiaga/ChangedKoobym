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

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.ViewBookActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joane14 on 17/10/2017.
 */

public class RentersAdapter extends RecyclerView.Adapter<RentersAdapter.BookHolder>{

    public List<RentalHeader> bookList;
    public Activity context;


    @Override
    public RentersAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_dialog_renters, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        RentersAdapter.BookHolder dataObjectHolder = new RentersAdapter.BookHolder(this.context, view);
        return dataObjectHolder;
    }



    public RentersAdapter(List<RentalHeader> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(RentersAdapter.BookHolder holder, int position) {

        holder.mRenters.setText(bookList.get(position).getUserId().getUserFname()+" "+bookList.get(position).getUserId().getUserLname());
        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getUserId().getImageFilename())).fit().into(holder.mRenterImg);
//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mRenters;
        ImageView mRenterImg;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mRenters = (TextView) itemView.findViewById(R.id.renterName);
            mRenterImg = (ImageView) itemView.findViewById(R.id.rentersPic);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


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
