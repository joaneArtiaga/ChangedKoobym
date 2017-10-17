package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Adapters.TimeDateAdapter;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeDateChooser extends AppCompatActivity {

    RentalDetail rentalDetail;
    List<UserDayTime> userDayTimeList;
    UserDayTime userDayTimeModel;
    RentalHeader rentalHeader;
    SwapDetail swapDetail;
    SwapHeader swapHeader;
    LocationModel locationChosen;
    User user;
    String nextDateStr, fromWhere;
    Date nextDate;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_date_chooser);

        fromWhere = "";
        @SuppressLint({"NewApi", "LocalSuppress"}) final
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Log.d("CurrentDate", date);

        userDayTimeList = new ArrayList<UserDayTime>();
        rentalHeader = new RentalHeader();
        locationChosen = new LocationModel();
        user = new User();
        nextDateStr = "";

        user = (User) SPUtility.getSPUtil(TimeDateChooser.this).getObject("USER_OBJECT", User.class);



        if(getIntent().getSerializableExtra("locationChose")!=null){
            locationChosen = (LocationModel) getIntent().getSerializableExtra("locationChose");
            Log.d("LocationChosen", locationChosen.getLocationName());
            Log.d("ChosenLat" + locationChosen.getLatitude(), "ChosenLong"+locationChosen.getLongitude());
        }else{
            Log.d("LocationChosen", "is null");
        }

        if(getIntent().getBundleExtra("confirm").getBoolean("fromSwap")==true){
            Log.d("So this ", "is swap");
            fromWhere = "swap";
            swapDetail = new SwapDetail();
            swapHeader = new SwapHeader();
            if(getIntent().getExtras().getSerializable("swapHeader")!=null){
                userDayTimeModel = new UserDayTime();
                swapHeader = (SwapHeader) getIntent().getSerializableExtra("swapHeader");
                swapDetail = (SwapDetail) getIntent().getExtras().getSerializable("swapDetail");
                Log.d("SwapFreakinHeader", swapHeader.getUser().getUserFname());
                Log.d("SwapFreakinHeader", swapHeader.getSwapDetail().getBookOwner().getBookObj().getBookTitle());
                for(int init=0; init<swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getDayTimeModel().size(); init++){
                    userDayTimeModel = swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getDayTimeModel().get(init);
                    userDayTimeList.add(userDayTimeModel);
                }
            }
        }

        if(getIntent().getBundleExtra("confirm").getBoolean("fromRent")== true){
            Log.d("So this ", "is rent");
            fromWhere = "rent";
            rentalDetail = new RentalDetail();
            if(getIntent().getExtras().getSerializable("rentDetail")!=null){
                userDayTimeModel = new UserDayTime();
                rentalDetail = (RentalDetail) getIntent().getExtras().getSerializable("rentDetail");
                Log.d("TimeDateChooser", rentalDetail.getBookOwner().getUserObj().getDayTimeModel().toString());


                for(int init=0; init<rentalDetail.getBookOwner().getUserObj().getDayTimeModel().size(); init++){
                    userDayTimeModel = (UserDayTime) rentalDetail.getBookOwner().getUserObj().getDayTimeModel().get(init);
                    userDayTimeList.add(userDayTimeModel);
                }

                if(!(userDayTimeList.isEmpty())){
                    Log.d("userDayTimeList","not empty");
                }else{
                    Log.d("userDayTimeList","empty");
                }
            }else{
                Log.d("rentalDetailPassed", "null");
            }
        }

        ListView list = (ListView) findViewById(R.id.myList);

        TimeDateAdapter mAdapter = new TimeDateAdapter(this, userDayTimeList);
        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if(userDayTimeList.get(position).getDay().getStrDay().equals("Monday")){
                    nextDate=getNextDate(java.util.Calendar.MONDAY);
                }else if(userDayTimeList.get(position).getDay().getStrDay().equals("Tuesday")){
                    nextDate=getNextDate(java.util.Calendar.TUESDAY);
                }else if(userDayTimeList.get(position).getDay().getStrDay().equals("Wednesday")){
                    nextDate=getNextDate(java.util.Calendar.WEDNESDAY);
                }else if(userDayTimeList.get(position).getDay().getStrDay().equals("Thursday")){
                    nextDate=getNextDate(java.util.Calendar.THURSDAY);
                }else if(userDayTimeList.get(position).getDay().getStrDay().equals("Friday")){
                    nextDate=getNextDate(java.util.Calendar.FRIDAY);
                }else if(userDayTimeList.get(position).getDay().getStrDay().equals("Saturday")){
                    nextDate=getNextDate(java.util.Calendar.SATURDAY);
                }else if(userDayTimeList.get(position).getDay().getStrDay().equals("Sunday")){
                    nextDate=getNextDate(java.util.Calendar.SUNDAY);
                }


                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                nextDateStr = format.format(nextDate);

                Log.d("ItemClicked", String.valueOf(position));
                Log.d("userDateList", userDayTimeList.get(position).getDay().getStrDay()+", "+userDayTimeList.get(position).getTime().getStrTime());
                Log.d("DateOwner", String.valueOf(userDayTimeList.get(position).getUserId()));

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimeDateChooser.this);
                alertDialogBuilder.setTitle("Are you sure you will be available at the time selected?");
                alertDialogBuilder.setMessage("Date:\t" + nextDateStr +
                        "\n\nDay:\t"+userDayTimeList.get(position).getDay().getStrDay()+
                        "\n\nTime:\t"+userDayTimeList.get(position).getTime().getStrTime());
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Log.d("DialogInterface", "inside");
                                showSummary(position);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });



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

    @SuppressLint("NewApi")
    public Date getNextDate(int dayOfWeek) {
        @SuppressLint({"NewApi", "LocalSuppress"})
        java.util.Calendar c = java.util.Calendar.getInstance();
        for ( int i = 0; i < 7; i++ ) {
            if ( c.get(java.util.Calendar.DAY_OF_WEEK) == dayOfWeek ) {
                return c.getTime();
            } else {
                c.add(java.util.Calendar.DAY_OF_WEEK, 1);
            }
        }
        return c.getTime();
    }



    public void showSummary(final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimeDateChooser.this);
        alertDialogBuilder.setTitle("Meet Up Summary");
        alertDialogBuilder.setMessage("Date:\t" +nextDateStr+
                "\n\nDay:\t"+userDayTimeList.get(position).getDay().getStrDay()+
                "\n\nTime:\t"+userDayTimeList.get(position).getTime().getStrTime()+
                "\n\nLocation:\t"+locationChosen.getLocationName());
        alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        if(fromWhere.equals("rent")){

                            rentalHeader.setStatus("Confirmation");
                            rentalHeader.setRentalDetail(rentalDetail);
                            rentalHeader.setUserId(user);
                            rentalHeader.setRentalTimeStamp(nextDateStr+" "+userDayTimeList.get(position).getTime().getStrTime());
                            rentalHeader.setTotalPrice((float) rentalDetail.getCalculatedPrice());
                            rentalHeader.setLocation(locationChosen);

//
                            Log.d("ONClickTime", "inside");
                            Log.d("RentalHeaderRent", rentalHeader.toString());


                            addRentalHeader();
                            Intent intent = new Intent(TimeDateChooser.this, RequestActivity.class);
                            startActivity(intent);
                        }else{
                            swapHeader.setStatus("Approved");
                            swapHeader.setDateTimeStamp(nextDateStr);
                            swapHeader.setLocation(locationChosen);
                            swapHeader.setUserDayTime(userDayTimeList.get(position));

                            addSwapHeader();
                            Log.d("SwapHeader dialog", "inside");
                        }

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    public void addSwapHeader(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.POST_SWAP_HEADER;

        SwapHeader swapToPost = new SwapHeader();
        swapToPost.setUser(new User());
        swapToPost.getUser().setUserId(swapHeader.getSwapDetail().getBookOwner().getUserObj().getUserId());
        swapToPost.setSwapDetail(new SwapDetail());
        swapToPost.setSwapDetail(swapHeader.getSwapDetail());
        swapToPost.setRequestedSwapDetail(new SwapDetail());
        swapToPost.setRequestedSwapDetail(swapDetail);
        swapToPost.setUserDayTime(new UserDayTime());
        swapToPost.getUserDayTime().setUserDayTimeId(swapHeader.getUserDayTime().getUserDayTimeId());
        swapToPost.setLocation(swapHeader.getLocation());
        swapToPost.setDateTimeStamp(nextDateStr);
        swapToPost.setStatus("APPROVED_BY_REQUESTOR");

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapToPost);

        d("swapHeader_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse addSwapH", "inside");
                Log.i("AddSwapHeader", response);
                Intent intent = new Intent(TimeDateChooser.this, RequestActivity.class);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public static void d(String TAG, String message) {
        int maxLogSize = 2000;
        for(int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            android.util.Log.d(TAG, message.substring(start, end));
        }
    }

    public void addRentalHeader(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_RENTAL_HEADER;

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);

        Log.d("RentalHeaderAdd", rentalHeader.toString());


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse addRentalH", "inside");
                Log.i("AddRentalHeader", response);
                Intent intent = new Intent(TimeDateChooser.this, RequestActivity.class);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }
}
