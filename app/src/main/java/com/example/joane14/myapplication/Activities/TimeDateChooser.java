package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Adapters.TimeDateAdapter;
import com.example.joane14.myapplication.Adapters.TimeDayAdapter;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
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
    LocationModel locationChosen;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_date_chooser);

        @SuppressLint({"NewApi", "LocalSuppress"}) final
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Log.d("CurrentDate", date);

        userDayTimeList = new ArrayList<UserDayTime>();
        rentalHeader = new RentalHeader();
        locationChosen = new LocationModel();



        if(getIntent().getSerializableExtra("locationChose")!=null){
            locationChosen = (LocationModel) getIntent().getSerializableExtra("locationChose");
            Log.d("LocationChosen", locationChosen.getLocationName());
            Log.d("ChosenLat" + locationChosen.getLatitude(), "ChosenLong"+locationChosen.getLongitude());
        }else{
            Log.d("LocationChosen", "is null");
        }

        rentalDetail = new RentalDetail();
        if(getIntent().getExtras().getSerializable("rentalDetail")!=null){
            userDayTimeModel = new UserDayTime();
            rentalDetail = (RentalDetail) getIntent().getExtras().getSerializable("rentalDetail");
            Log.d("TimeDateChooser", rentalDetail.getBookOwner().getUserObj().getDayTimeModel().toString());


            for(int init=0; init<rentalDetail.getBookOwner().getUserObj().getDayTimeModel().size(); init++){
                userDayTimeModel = (UserDayTime) rentalDetail.getBookOwner().getUserObj().getDayTimeModel().get(init);
                userDayTimeList.add(userDayTimeModel);
            }

        }

        ListView list = (ListView) findViewById(R.id.myList);

        TimeDateAdapter mAdapter = new TimeDateAdapter(this, userDayTimeList);
        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("ItemClicked", String.valueOf(position));
                Log.d("userDateList", userDayTimeList.get(position).getDay().getStrDay()+", "+userDayTimeList.get(position).getTime().getStrTime());
                Log.d("DateOwner", String.valueOf(userDayTimeList.get(position).getUserId()));

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimeDateChooser.this);
                alertDialogBuilder.setTitle("Are you sure you will be available at the time selected?");
                alertDialogBuilder.setMessage("Date:\tFREEZE\n" +
                        "\nDay:\t"+userDayTimeList.get(position).getDay().getStrDay()+
                        "\n\nTime:\t"+userDayTimeList.get(position).getTime().getStrTime());
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                rentalHeader.setUser((User) SPUtility.getSPUtil(TimeDateChooser.this).getObject("USER_OBJECT", User.class));
                                rentalHeader.setRentalTimeStamp(date);
                                rentalHeader.setTotalPrice((float) rentalDetail.getCalculatedPrice());

                                Log.d("ONClickTime", "inside");
                                Log.d("RentalHeaderRent", rentalHeader.toString());

                                showSummary(position, date);
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

//        if (mCalendar.get(Calendar.DAY_OF_WEEK) != dayvalues) {
//            mCalendar.add(Calendar.DAY_OF_MONTH, (dayvalues + 7 - mCalendar.get(Calendar.DAY_OF_WEEK)) % 7);
//        } else {
//            int minOfDay = mCalendar.get(Calendar.HOUR_OF_DAY) * 60 + mCalendar.get(Calendar.MINUTE);
//            if (minOfDay >= hour * 60 + minute)
//                cal.add(Calendar.DAY_OF_MONTH, 7); // Bump to next week
//        }

        TextView mTitle = (TextView) findViewById(R.id.tdMyDate);
        mTitle.setText("Today is "+stringDay+", "+date);

    }

    public void showSummary(int position, final String date){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimeDateChooser.this);
        alertDialogBuilder.setTitle("Meet Up Summary");
        alertDialogBuilder.setMessage("Date:\tFREEZE\n" +
                "\nDay:\t"+userDayTimeList.get(position).getDay().getStrDay()+
                "\n\nTime:\t"+userDayTimeList.get(position).getTime().getStrTime()+
                "\n\nLocation:\t"+locationChosen.getLocationName());
        alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        rentalHeader.setStatus("Confirmation");
                        rentalHeader.setRentalDetail(rentalDetail);
                        rentalHeader.setUser((User) SPUtility.getSPUtil(TimeDateChooser.this).getObject("USER_OBJECT", User.class));
                        rentalHeader.setRentalTimeStamp(date);
                        rentalHeader.setTotalPrice((float) rentalDetail.getCalculatedPrice());

                        Log.d("ONClickTime", "inside");
                        Log.d("RentalHeaderRent", rentalHeader.toString());

                        addRentalHeader();
                        Intent intent = new Intent(TimeDateChooser.this, RequestActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void addRentalHeader(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_RENTAL_HEADER;
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

//        User user = new User();
//        user.setUserFname(userModel.getUserFname());
//        user.setUserLname(userModel.getUserLname());
//        user.setAddress(userModel.getAddress());
//        user.setEmail(userModel.getEmail());
//        user.setUsername(userModel.getUsername());
//        user.setPassword(userModel.getPassword());
//        user.setBirthdate(userModel.getBirthdate());
//        user.setImageFilename(userModel.getImageFilename());
//        user.setPhoneNumber(userModel.getPhoneNumber());
//        user.setGenreArray(genres);
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseRentalHeader", response);
                Intent intent = new Intent(TimeDateChooser.this, RequestActivity.class);
                startActivity(intent);
//                User user = gson.fromJson(response, User.class);
//                Log.i("LOG_VOLLEY", user.getEmail());
//                Log.i("LOG_VOLLEY", user.getUserFname());
//                Log.i("LOG_VOLLEY", user.getUserLname());
//                user.setGenreArray(genres);
//                Intent intent = new Intent(SignUp.this, LandingPage.class);
//                Bundle b = new Bundle();
//                b.putBoolean("fromRegister", true);
//                b.putSerializable("userModel", user);
//                intent.putExtra("user",b);
//                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
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
