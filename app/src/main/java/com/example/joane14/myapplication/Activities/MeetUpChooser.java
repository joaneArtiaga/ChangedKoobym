package com.example.joane14.myapplication.Activities;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.DayModel;
import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.MeetUp;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.TimeModel;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.joane14.myapplication.Activities.TimeDateChooser.d;

public class MeetUpChooser extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    List<LocationModel> locationModelList;
    RentalHeader rentalHeader;
    MeetUp meetUp;
    TextView mTimeUntil, mTimeFrom;
    UserDayTime udt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_up_chooser);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rentalHeader = new RentalHeader();
        meetUp = new MeetUp();

        udt = new UserDayTime();


        if(getIntent().getExtras().getSerializable("rentalHeader")!=null){
            this.rentalHeader = (RentalHeader) getIntent().getExtras().getSerializable("rentalHeader");
            if(rentalHeader==null){
                Log.d("rentalHeaderNull","true");
            }else{
                Log.d("rentalHeaderNull",rentalHeader.toString());
            }
            Log.d("MeetUpChooser", String.valueOf(rentalHeader.getRentalDetail().getBookOwner().getUserObj().getUserId()));
            locationModelList = rentalHeader.getRentalDetail().getBookOwner().getUserObj().getLocationArray();
            for(int init = 0; init<locationModelList.size(); init++){
                Log.d("MeetUpChooser Location", locationModelList.get(init).getLocationName());
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Marker mapMarker;


        LatLng location = null;

        for (int init=0; init<locationModelList.size(); init++){

            String lati, longi;
            Double latitude, longitude;

            lati = locationModelList.get(init).getLatitude();
            longi = locationModelList.get(init).getLongitude();
            latitude = Double.parseDouble(lati);
            longitude = Double.parseDouble(longi);
            Log.d("Latitude "+latitude.toString(), "Longitude "+longitude.toString());
            Log.d("LocationName "+locationModelList.get(init).getLocationName(), "ArrayPosition "+init);
            location = new LatLng(latitude,longitude);
            mapMarker = mMap.addMarker(new MarkerOptions().position(location).title(locationModelList.get(init).getLocationName()));

            mapMarker.showInfoWindow();

            mHashMap.put(mapMarker, init);
            Log.d("MarkerPosition", String.valueOf(mapMarker.getPosition()));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MeetUpChooser.this);
                alertDialogBuilder.setTitle("Selected Location");
                alertDialogBuilder.setMessage(marker.getTitle());
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                final int position = mHashMap.get(marker);
                                Log.d("MarkerPosition", String.valueOf(position));
                                if(getIntent().getExtras().getBoolean("return")==true){
                                    final Dialog dialogCustom = new Dialog(MeetUpChooser.this);
                                    LayoutInflater inflater = (LayoutInflater) MeetUpChooser.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                                    View layout = inflater.inflate(R.layout.return_custom_dialog, (ViewGroup) findViewById(R.id.seekbar_layout));

                                    dialogCustom.setContentView(layout);

                                    mTimeFrom = (TextView) dialogCustom.findViewById(R.id.tvTimeReturnFrom);
                                    mTimeUntil = (TextView) dialogCustom.findViewById(R.id.tvTimeReturnUntil);
                                    Spinner mSpinDate = (Spinner) dialogCustom.findViewById(R.id.spinnerReturn);
                                    Button mBtnOkay = (Button) dialogCustom.findViewById(R.id.btnOkayReturn);
                                    Button mBtnCancel = (Button) dialogCustom.findViewById(R.id.btnCancelReturn);

                                    final List<String> dateReturn = new ArrayList<String>();

                                    Calendar cal = Calendar.getInstance();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String currDate = sdf.format(cal.getTime());

                                    mTimeFrom.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CreateTimePicker(0);
                                        }
                                    });

                                    mTimeUntil.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            CreateTimePicker(1);
                                        }
                                    });


                                        for(int init=1; init<rentalHeader.getRentalDetail().getDaysForRent(); init++){
                                            cal.add(Calendar.DATE, 1);
                                            dateReturn.add(sdf.format(cal.getTime()));
                                        }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, dateReturn);
                                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                                    mSpinDate.setAdapter(adapter);
                                    mSpinDate.setSelection(0);
                                    mSpinDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            DayModel dayModel = new DayModel();

                                            Log.d("pickedDate", dateReturn.get(position));

                                            dayModel.setStrDay(dateReturn.get(position));
                                            udt.setDay(dayModel);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                    mBtnCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogCustom.dismiss();
                                        }
                                    });
                                    mBtnOkay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            meetUp.setLocation(locationModelList.get(position));
                                            String timeStr = mTimeFrom.getText().toString()+" - "+mTimeUntil.getText().toString();
                                            TimeModel timeModel = new TimeModel();
                                            timeModel.setStrTime(timeStr);
                                            udt.setTime(timeModel);
                                            meetUp.setUserDayTime(udt);
                                            rentalHeader.setReturnMeetUp(meetUp);
                                            Log.d("unsaSulod", rentalHeader.toString());
                                            addMeetUp(rentalHeader, meetUp);
//                                            updateRentalHeader();
                                        }
                                    });
                                    dialogCustom.show();

//                                    Window window = dialogCustom.getWindow();
//                                    window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
                                }else if(getIntent().getExtras().getBoolean("return")==false){
                                    Intent intent = new Intent(MeetUpChooser.this,TimeDateChooser.class);
                                    Bundle mBundle = new Bundle();
                                    mBundle.putSerializable("rentHeader", rentalHeader);
                                    mBundle.putBoolean("fromRent", true);
                                    mBundle.putSerializable("locationChose", locationModelList.get(position));
                                    intent.putExtra("confirm", mBundle);
                                    meetUp.setLocation(locationModelList.get(position));
                                    mBundle.putSerializable("meetUp", meetUp);
                                    intent.putExtras(mBundle);
                                    startActivity(intent);
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return false;
            }
        });

    }

    public void addUserDayTime(final RentalHeader rh, MeetUp meetUp, UserDayTime udtModel){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        String URL = Constants.ADD_MEET_UP;

        d("postMeetUp", URL);
        d("insideMeetUp", meetUp.toString());
//        String URL = Constants.WEB_SERVICE_URL+"user/add";


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(meetUp);


        d("addMeetUp", mRequestBody);
        final User finalUser = user;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("meetUpAdded", response);
                MeetUp meetUpMod = gson.fromJson(response, MeetUp.class);
                updateRentalHeaderReturn(rh, meetUpMod);

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

    public void addMeetUp(final RentalHeader rh, MeetUp meetUp){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        String URL = Constants.ADD_MEET_UP;

        d("postMeetUp", URL);
        d("insideMeetUp", meetUp.toString());
//        String URL = Constants.WEB_SERVICE_URL+"user/add";


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(meetUp);


        d("addMeetUp", mRequestBody);
        final User finalUser = user;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("meetUpAdded", response);
                MeetUp meetUpMod = gson.fromJson(response, MeetUp.class);
                updateRentalHeaderReturn(rh, meetUpMod);

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

    public void updateRentalHeaderReturn(RentalHeader rentToPut, MeetUp meetUpToPut){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        String URL = Constants.SET_RETURN+rentToPut.getRentalHeaderId()+"/"+meetUpToPut.getMeetUpId();

        d("putRentalHeaderReturn", URL);
        d("insideRentHeader", rentToPut.toString());
//        d("insidePutMeetUp", meetUpToPut.toString());
        if(meetUpToPut.getLocation()==null){
            Log.d("walaySulodLocation", "true");
        }else{
            Log.d("walaySulodLocation", "false");
        }
//        String URL = Constants.WEB_SERVICE_URL+"user/add";
//        d("insidePutMeetUp", meetUpToPut.toString());


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentToPut);


        d("updateRentalHeader", mRequestBody);
        final User finalUser = user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("rentalHeaderUPDATED", response);
//                RentalHeader rentalHeaderMod = gson.fromJson(response, RentalHeader.class);
//
                AlertDialog ad = new AlertDialog.Builder(MeetUpChooser.this).create();
                ad.setMessage("The owner has been notified.");
                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MeetUpChooser.this, BookActActivity.class);
                        startActivity(intent);
                    }
                });
                ad.show();

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

    public void CreateTimePicker(final int pos){


        final java.util.Calendar c = java.util.Calendar.getInstance();
        int mHour = c.get(java.util.Calendar.HOUR_OF_DAY);
        int mMinute = c.get(java.util.Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(MeetUpChooser.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        int hour = hourOfDay;
                        int minutes = minute;
                        String timeSet = "";
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes ;
                        else
                            min = String.valueOf(minutes);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(hour).append(':')
                                .append(min ).append(" ").append(timeSet).toString();

                        String timeGiven = "";
                        timeGiven = hourOfDay + ":" + minute;
                        Log.d("time selected", timeGiven);

                        if(pos==0){
                            mTimeFrom.setText(aTime);
                        }else{
                            mTimeUntil.setText(aTime);
                        }
                        TimeModel timeModel = new TimeModel();
                        timeModel.setStrTime(aTime);
                        udt.setTime(timeModel);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}
