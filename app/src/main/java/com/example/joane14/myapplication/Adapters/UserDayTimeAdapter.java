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
import android.widget.TextView;

import com.example.joane14.myapplication.Model.AuctionComment;
import com.example.joane14.myapplication.Model.AuctionCommentDetail;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.AuctionHeader;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joane14 on 06/10/2017.
 */

public class UserDayTimeAdapter extends RecyclerView.Adapter<UserDayTimeAdapter.BookHolder> {

    public List<UserDayTime> udtList;
    public Activity context;


    @Override
    public UserDayTimeAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_userdaytime, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        UserDayTimeAdapter.BookHolder dataObjectHolder = new UserDayTimeAdapter.BookHolder(this.context, view);
        return dataObjectHolder;
    }



    public UserDayTimeAdapter(Activity context, List<UserDayTime> myDataset) {
        udtList = myDataset;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(UserDayTimeAdapter.BookHolder holder, final int position) {

        holder.mDate.setText(udtList.get(position).getDay().getStrDay());
        holder.mTime.setText(udtList.get(position).getTime().getStrTime());

    }

    @Override
    public int getItemCount() {
        return udtList.size();
    }
    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mDate, mTime;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mDate = (TextView) itemView.findViewById(R.id.udtDate);
            mTime = (TextView) itemView.findViewById(R.id.udtTime);


        }
    }

}
