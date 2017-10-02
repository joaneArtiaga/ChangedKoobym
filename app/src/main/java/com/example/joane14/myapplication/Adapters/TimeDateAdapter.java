package com.example.joane14.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;

import java.util.List;

/**
 * Created by Joane14 on 02/10/2017.
 */

public class TimeDateAdapter extends BaseAdapter {

    private Context context;
    private List<UserDayTime> userDayTimeList;

    public TimeDateAdapter(Context context, List<UserDayTime> userDayTimeList){
        this.context = context;
        this.userDayTimeList = userDayTimeList;
    }

    @Override
    public int getCount() {
        return userDayTimeList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.list_time_row, null);
        }

        TextView mDay = (TextView) vi.findViewById(R.id.tdDay);
        TextView mDate = (TextView) vi.findViewById(R.id.tdDate);
        TextView mTime = (TextView) vi.findViewById(R.id.tdTime);

        UserDayTime userDayTimeModel = new UserDayTime();
        userDayTimeModel = userDayTimeList.get(position);


        mDay.setText(userDayTimeModel.getDay().getStrDay());
        mTime.setText(userDayTimeModel.getTime().getStrTime());
        mDate.setText("FREEZE");


        return vi;
    }
}
