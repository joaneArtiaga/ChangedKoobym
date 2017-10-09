package com.example.joane14.myapplication.Adapters;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.joane14.myapplication.Activities.LocationChooser;
import com.example.joane14.myapplication.Model.DayModel;
import com.example.joane14.myapplication.Model.DayTimeModel;
import com.example.joane14.myapplication.Model.TimeModel;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Joane14 on 17/09/2017.
 */

public class TimeDayAdapter extends RecyclerView.Adapter<TimeDayAdapter.MyViewHolder>{

    private ArrayList<DayTimeModel> dayTimeModelArrayList;
    List<UserDayTime> userDayTimeList;
    UserDayTime userDayTime;
    DayModel dayModel;
    TimeModel timeModel;
    private static MyClickListener myClickListener;
    Context context;
    private int mHour, mMinute;



    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mDay;
        ListView listView;
        ImageButton addImgBtn;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.mDay = (TextView) itemView.findViewById(R.id.tvDay);
            this.listView = (ListView) itemView.findViewById(R.id.timeList);
            this.addImgBtn = (ImageButton) itemView.findViewById(R.id.imgBtnAdd);

        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public TimeDayAdapter(ArrayList<DayTimeModel> dayTimeModelArrayList, Context context) {
        this.dayTimeModelArrayList = dayTimeModelArrayList;
        this.context = context;
        if(dayTimeModelArrayList.size()==0){
            Log.d("dayTimeModelArrayList", "is empty");
        }else{
//            Log.d("dayTimeModelArrayList", dayTimeModelArrayList.toString());
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_time_item, parent, false);

        MyViewHolder dataObjectHolder = new MyViewHolder(view);

        userDayTime = new UserDayTime();
        userDayTimeList = new ArrayList<UserDayTime>();

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final DayTimeModel dtm = dayTimeModelArrayList.get(position);
        holder.mDay.setText(dayTimeModelArrayList.get(position).getDay().toString());

        if(dtm.getTime()==null){
            dtm.setTime(new ArrayList<String>());
            Log.d("TimeList","is empty");
        }else{
            Log.d("TimeList","is not empty");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,dtm.getTime() );
        holder.listView.setAdapter(adapter);
        holder.listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();

                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                v.onTouchEvent(event);


                return true;

            }
        });



        holder.addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTimePicker(dtm);
            }
        });

    }

    public void addItem(DayTimeModel dataObj, int index) {
        dayTimeModelArrayList.add(index, dataObj);
        notifyItemInserted(index);
    }


    public void deleteItem(int index) {
        dayTimeModelArrayList.remove(index);
        notifyItemRemoved(index);
    }

    public void CreateTimePicker(final DayTimeModel dtm){


        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String timeGiven = "";
                        timeGiven = hourOfDay + ":" + minute;
                        Log.d("time selected", timeGiven);


                        dayModel = new DayModel();
                        timeModel = new TimeModel();

                        dayModel.setStrDay(dtm.getDay());
                        timeModel.setStrTime(timeGiven);

                        userDayTime.setDay(dayModel);
                        userDayTime.setTime(timeModel);

                        userDayTimeList.add(userDayTime);


                        dtm.getTime().add(timeGiven);

                        notifyDataSetChanged();
                        if(dayTimeModelArrayList.isEmpty()){
                            Log.d("timeDayList", "is empty");
                        }else{
                            Log.d("timeDayList", dtm.getTime().toString());
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public int getItemCount() {
        return dayTimeModelArrayList.size();
    }


    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
