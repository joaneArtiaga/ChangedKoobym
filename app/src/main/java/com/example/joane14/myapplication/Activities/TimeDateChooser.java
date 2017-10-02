package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.joane14.myapplication.Adapters.TimeDateAdapter;
import com.example.joane14.myapplication.Adapters.TimeDayAdapter;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeDateChooser extends AppCompatActivity {

    RentalDetail rentalDetail;
    List<UserDayTime> userDayTimeList;
    UserDayTime userDayTimeModel;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_date_chooser);

        userDayTimeList = new ArrayList<UserDayTime>();


        rentalDetail = new RentalDetail();
        if(getIntent().getExtras().getSerializable("rentalDetail")!=null){
            userDayTimeModel = new UserDayTime();
            rentalDetail = (RentalDetail) getIntent().getExtras().getSerializable("rentalDetail");
            Log.d("TimeDateChooser", rentalDetail.getBookOwner().getUserObj().getDayTimeModel().toString());


            for(int init=0; init<rentalDetail.getBookOwner().getUserObj().getDayTimeModel().size(); init++){
                userDayTimeModel = (UserDayTime) rentalDetail.getBookOwner().getUserObj().getDayTimeModel().get(init);
                userDayTimeList.add(userDayTimeModel);
            }

//            userDayTimeModel.setUserDayTimeId(((UserDayTime) rentalDetail.getBookOwner().getUserObj().getDayTimeModel()).getUserDayTimeId());
//            userDayTimeModel.setDay(((UserDayTime) rentalDetail.getBookOwner().getUserObj().getDayTimeModel()).getDay());
//            userDayTimeModel.setTime(((UserDayTime) rentalDetail.getBookOwner().getUserObj().getDayTimeModel()).getTime());
//            userDayTimeModel.setUserId(((UserDayTime) rentalDetail.getBookOwner().getUserObj().getDayTimeModel()).getUserId());
//            userDayTimeList.add(userDayTimeModel);
        }

        ListView list = (ListView) findViewById(R.id.myList);

        TimeDateAdapter mAdapter = new TimeDateAdapter(this, userDayTimeList);
        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ItemClicked", String.valueOf(position));
                Log.d("userDateList", userDayTimeList.get(position).getDay().getStrDay()+", "+userDayTimeList.get(position).getTime().getStrTime());
                Log.d("DateOwner", String.valueOf(userDayTimeList.get(position).getUserId()));
            }
        });


            @SuppressLint({"NewApi", "LocalSuppress"})
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Log.d("CurrentDate", date);

            @SuppressLint({"NewApi", "LocalSuppress"})
            Calendar mCalendar = Calendar.getInstance();
            @SuppressLint({"NewApi", "LocalSuppress"})
            int i = mCalendar.get(Calendar.WEEK_OF_MONTH);
            mCalendar.set(Calendar.WEEK_OF_MONTH, ++i);
            int dayvalues=mCalendar.get(Calendar.DAY_OF_WEEK);

            Log.d("WeekOfMonth", String.valueOf(i));
            Log.d("dayValues", String.valueOf(dayvalues));


            String stringDay="";

            switch (dayvalues){
                case Calendar.MONDAY: stringDay = "Monday";
                    break;
                case Calendar.TUESDAY: stringDay = "Tuesday";
                    break;
                case Calendar.WEDNESDAY: stringDay = "Wednesday";
                    break;
                case Calendar.THURSDAY: stringDay = "Thursday";
                    break;
                case Calendar.FRIDAY: stringDay = "Friday";
                    break;
                case Calendar.SATURDAY: stringDay = "Saturday";
                    break;
                case Calendar.SUNDAY: stringDay = "Sunday";
                    break;
            }

        TextView mTitle = (TextView) findViewById(R.id.tdMyDate);
        mTitle.setText("Today is "+stringDay+", "+date);




    }
}
