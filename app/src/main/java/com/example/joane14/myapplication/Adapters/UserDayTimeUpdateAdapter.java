package com.example.joane14.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;

import java.util.List;

/**
 * Created by Joane14 on 06/10/2017.
 */

public class UserDayTimeUpdateAdapter extends RecyclerView.Adapter<UserDayTimeUpdateAdapter.BookHolder> {

    public List<UserDayTime> udtList;
    public Activity context;


    @Override
    public UserDayTimeUpdateAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_userdaytime_update, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        UserDayTimeUpdateAdapter.BookHolder dataObjectHolder = new UserDayTimeUpdateAdapter.BookHolder(this.context, view);
        return dataObjectHolder;
    }



    public UserDayTimeUpdateAdapter(Activity context, List<UserDayTime> myDataset) {
        udtList = myDataset;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(UserDayTimeUpdateAdapter.BookHolder holder, final int position) {

        holder.mDate.setText(udtList.get(position).getDay().getStrDay());
        holder.mTime.setText(udtList.get(position).getTime().getStrTime());
        holder.mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                udtList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, udtList.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return udtList.size();
    }
    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mDate, mTime;
        ImageButton mClose;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mDate = (TextView) itemView.findViewById(R.id.udtDate);
            mTime = (TextView) itemView.findViewById(R.id.udtTime);
            mClose = (ImageButton) itemView.findViewById(R.id.closeBtnUdt);
        }
    }

}
