package com.example.joane14.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.BookOwnerReview;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.UserRating;
import com.example.joane14.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joane14 on 12/10/2017.
 */

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.BookHolder> {

    public List<UserRating> bookList;
    UserRating userRating;
    public Activity context;

    @Override
    public UserReviewAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_user_reviews, parent, false);

        this.context = (Activity) parent.getContext();
        userRating = new UserRating();
        Log.d("LandingPAgeAdapter","inside");
        UserReviewAdapter.BookHolder dataObjectHolder = new UserReviewAdapter.BookHolder(this.context, view);

        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public UserReviewAdapter(List<UserRating> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(UserReviewAdapter.BookHolder holder, final int position) {


        if(bookList.get(position).getUserRater()==null){
            Log.d("User is ", "null");
        }else{
            Log.d("User is ", "not null");
        }
        holder.mName.setText(bookList.get(position).getUserRater().getUserFname()+" "+bookList.get(position).getUserRater().getUserLname());
        holder.mComment.setText(bookList.get(position).getComment());
        holder.mRate.setText("Rated "+String.valueOf(bookList.get(position).getRate().getRateNumber())+" out of 5");
        holder.mRating.setRating(Float.parseFloat(String.valueOf(bookList.get(position).getRate().getRateNumber())));
        Picasso.with(context).load(bookList.get(position).getUserRater().getImageFilename()).fit().into(holder.mProfPic);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mName, mComment, mRate;
        ImageView mProfPic;
        RatingBar mRating;
        RentalHeader rentalHeaderObj;
        Context context;


        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mProfPic = (ImageView) itemView.findViewById(R.id.reviewProfPic);
            mName = (TextView) itemView.findViewById(R.id.reviewName);
            mComment = (TextView) itemView.findViewById(R.id.reviewComment);
            mRate = (TextView) itemView.findViewById(R.id.reviewRate);
            mRating = (RatingBar) itemView.findViewById(R.id.urRating);
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


