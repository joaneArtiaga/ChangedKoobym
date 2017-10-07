package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.icu.text.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.joane14.myapplication.Activities.TimeDateChooser;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Joane14 on 02/10/2017.
 */

public class TimeDateAdapter extends BaseAdapter {

    private Context context;
    private List<UserDayTime> userDayTimeList;
    public static List<String> dateOfDay;
    private Date nextDate;
    public static String nextDateStr;

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

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        UserDayTime userDayTime = new UserDayTime();
        userDayTime = userDayTimeList.get(position);

        dateOfDay = new ArrayList<String>();

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.list_time_row, null);
        }

        if(userDayTime.getDay().getStrDay().equals("Monday")){
            nextDate=getNextDate(java.util.Calendar.MONDAY);
        }else if(userDayTime.getDay().getStrDay().equals("Tuesday")){
            nextDate=getNextDate(java.util.Calendar.TUESDAY);
        }else if(userDayTime.getDay().getStrDay().equals("Wednesday")){
            nextDate=getNextDate(java.util.Calendar.WEDNESDAY);
        }else if(userDayTime.getDay().getStrDay().equals("Thursday")){
            nextDate=getNextDate(java.util.Calendar.THURSDAY);
        }else if(userDayTime.getDay().getStrDay().equals("Friday")){
            nextDate=getNextDate(java.util.Calendar.FRIDAY);
        }else if(userDayTime.getDay().getStrDay().equals("Saturday")){
            nextDate=getNextDate(java.util.Calendar.SATURDAY);
        }else if(userDayTime.getDay().getStrDay().equals("Sunday")){
            nextDate=getNextDate(java.util.Calendar.SUNDAY);
        }

        TextView mDay = (TextView) vi.findViewById(R.id.tdDay);
        TextView mDate = (TextView) vi.findViewById(R.id.tdDate);
        TextView mTime = (TextView) vi.findViewById(R.id.tdTime);

        UserDayTime userDayTimeModel = new UserDayTime();
        userDayTimeModel = userDayTimeList.get(position);


        mDay.setText(userDayTimeModel.getDay().getStrDay());
        mTime.setText(userDayTimeModel.getTime().getStrTime());

        nextDateStr = DateFormat.getDateInstance(DateFormat.MEDIUM).format(nextDate);
        dateOfDay.add(nextDateStr);
        mDate.setText(nextDateStr);




        return vi;
    }



    @SuppressLint("NewApi")
    public Date getNextDate(int dayOfWeek) {
        @SuppressLint({"NewApi", "LocalSuppress"})
        Calendar c = Calendar.getInstance();
        for ( int i = 0; i < 7; i++ ) {
            if ( c.get(Calendar.DAY_OF_WEEK) == dayOfWeek ) {
                return c.getTime();
            } else {
                c.add(Calendar.DAY_OF_WEEK, 1);
            }
        }
        return c.getTime();
    }
}
