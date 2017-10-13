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
import com.example.joane14.myapplication.Activities.BookReviewActivity;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.UserReviewActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.BookOwnerReview;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by Joane14 on 12/10/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.BookHolder> {

    public List<BookOwnerReview> bookList;
    BookOwnerReview bookOwnerReview;
    public Activity context;

    @Override
    public ReviewAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_reviews, parent, false);

        this.context = (Activity) parent.getContext();
        bookOwnerReview = new BookOwnerReview();
        Log.d("LandingPAgeAdapter","inside");
        ReviewAdapter.BookHolder dataObjectHolder = new ReviewAdapter.BookHolder(this.context, view);

        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ReviewAdapter(List<BookOwnerReview> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.BookHolder holder, final int position) {


        holder.mName.setText(bookList.get(position).getUser().getUserFname()+" "+bookList.get(position).getUser().getUserLname());
        holder.mComment.setText(bookList.get(position).getComment());
        holder.mRate.setText("Rated blank out of 10");
        Picasso.with(context).load(bookList.get(position).getUser().getImageFilename()).fit().into(holder.mProfPic);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mName, mComment, mRate;
        ImageView mProfPic;
        RentalHeader rentalHeaderObj;
        Context context;


        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mProfPic = (ImageView) itemView.findViewById(R.id.reviewProfPic);
            mName = (TextView) itemView.findViewById(R.id.reviewName);
            mComment = (TextView) itemView.findViewById(R.id.reviewComment);
            mRate = (TextView) itemView.findViewById(R.id.reviewRate);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rentalHeaderObj = new RentalHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
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

