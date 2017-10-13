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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.ViewBookSwapActivity;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;

import java.util.List;

/**
 * Created by Joane14 on 13/10/2017.
 */

public class TimeDayAdapter extends RecyclerView.Adapter<TimeDayAdapter.BookHolder> {

    public List<UserDayTime> userDayTimeList;
    public Activity context;

    @Override
    public TimeDayAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_time_item, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        TimeDayAdapter.BookHolder dataObjectHolder = new TimeDayAdapter.BookHolder(this.context, view);
        return dataObjectHolder;
    }

    public TimeDayAdapter(List<UserDayTime> myDataset) {
        userDayTimeList = myDataset;
    }

    @Override
    public void onBindViewHolder(TimeDayAdapter.BookHolder holder, int position) {

        holder.mDay.setText(userDayTimeList.get(position).getDay().getStrDay());
        holder.mTime.setText(userDayTimeList.get(position).getTime().getStrTime());

    }

    @Override
    public int getItemCount() {
        return userDayTimeList.size();
    }


    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mDay, mTime;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mDay = (TextView) itemView.findViewById(R.id.tvDay);
            mTime = (TextView) itemView.findViewById(R.id.tvTime);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

}
