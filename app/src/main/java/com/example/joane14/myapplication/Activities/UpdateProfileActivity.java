package com.example.joane14.myapplication.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.joane14.myapplication.Adapters.PlaceAutoCompleteAdapter;
import com.example.joane14.myapplication.Adapters.TimeDayAdapter;
import com.example.joane14.myapplication.Adapters.UserDayTimeUpdateAdapter;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.DisplayUserDayTimeUpdate;
import com.example.joane14.myapplication.Fragments.VolleyMultipartRequest;
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.Model.DayModel;
import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.Place;
import com.example.joane14.myapplication.Model.TimeModel;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.PlacesUtility;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.util.Log.d;

/**
 * Created by Kimberly Cañedo on 06/10/2017.
 */

public class UpdateProfileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, DisplayUserDayTimeUpdate.OnUserDayTimeInteractionListener{

    DatePickerDialog.OnDateSetListener date;
    private Calendar calendar;
    EditText mBirtdate;
    AutoCompleteTextView mFirst, mSecond, mThird, mAddress;
    ListPopupWindow placeAutoCompletePopupWindow;
    PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    List<Place> places;
    Boolean mFirstBool, mSecondBool, mThirdBool, fromSelected, mAddressBool;
    int mAddressPos;
    List<LocationModel> location;
    List<Integer> locPos;
    User userToPut;
    User user;
    ImageView ivProfile;
    List<String> selectedDays;
    List<UserDayTime> userDayTimeList;
    EditText etTimeFrom, etTimeTo;
    UserDayTime userDayTime;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        calendar = Calendar.getInstance();

        mAddressPos = 0;
        userToPut = new User();

        fromSelected =false;
        mFirstBool = false;
        mSecondBool = false;
        mThirdBool = false;
        mAddressBool = false;
        selectedDays = new ArrayList<String>();
        userDayTimeList = new ArrayList<UserDayTime>();

        locPos = new ArrayList<Integer>();

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        final EditText mFirstName = (EditText) findViewById(R.id.etFirstNameUP);
        final EditText mLastName = (EditText) findViewById(R.id.etLastNameUP);
        mBirtdate = (EditText) findViewById(R.id.etBirthdateUP);
        mAddress = (AutoCompleteTextView) findViewById(R.id.etAddressUP);
        final EditText mUserName = (EditText) findViewById(R.id.etUserNameUP);
        final EditText mEmail = (EditText) findViewById(R.id.etMailUP);
        final EditText mContact = (EditText) findViewById(R.id.etContactUP);
        Button mBtnUpdate = (Button) findViewById(R.id.btnUpdateProfile);
        ivProfile = (ImageView) findViewById(R.id.ivProfileUP);
        Button mBtnAddTime = (Button) findViewById(R.id.addTime);

        mFirst = (AutoCompleteTextView) findViewById(R.id.location1UP);
        mSecond = (AutoCompleteTextView) findViewById(R.id.location2UP);
        mThird = (AutoCompleteTextView) findViewById(R.id.location3UP);

        user = new User();

        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        mFirstName.setText(user.getUserFname());
        mLastName.setText(user.getUserLname());
        mBirtdate.setText(sdf.format(user.getBirthdate()));
        mAddress.setText(user.getAddress());
        mUserName.setText(user.getUsername());
        mEmail.setText(user.getEmail());
        mContact.setText(user.getPhoneNumber());

        places = new ArrayList<>();
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, places);
        placeAutoCompletePopupWindow = new ListPopupWindow(this);
        placeAutoCompletePopupWindow.setAdapter(placeAutoCompleteAdapter);
        placeAutoCompletePopupWindow.setAnchorView(mAddress);
        placeAutoCompletePopupWindow.setModal(false);
        placeAutoCompletePopupWindow.setOnItemClickListener(this);
        location = new ArrayList<LocationModel>();

        location = user.getLocationArray();


        for(int init=0; init<location.size(); init++){
            if(location.get(init).getStatus().equals("MeetUp")){
                locPos.add(init);
            }else{
                mAddressPos = init;
            }
        }

        mFirst.setText(location.get(locPos.get(0)).getLocationName());
        mSecond.setText(location.get(locPos.get(1)).getLocationName());
        mThird.setText(location.get(locPos.get(2)).getLocationName());

        Glide.with(this).load(user.getImageFilename()).centerCrop().into(ivProfile);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
        mAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String val = editable.toString();
                if (!fromSelected) {
                    if (val.length() > 4) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PlacesUtility.getPredictions(UpdateProfileActivity.this, val, new LatLng(10.289218, 123.857058), autocompleteplaceListener);
                                mAddressBool = true;
                            }
                        }).start();
                    }
                } else {
                    fromSelected = false;
                }
            }
        });
        mFirst.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String val = editable.toString();
                if (!fromSelected) {
                    if (val.length() > 4) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PlacesUtility.getPredictions(UpdateProfileActivity.this, val, new LatLng(10.289218, 123.857058), autocompleteplaceListener);
                                mFirstBool = true;
                            }
                        }).start();
                    }
                } else {
                    fromSelected = false;
                }
            }
        });

        mSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String val = editable.toString();
                if (!fromSelected) {
                    if (val.length() > 4) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PlacesUtility.getPredictions(UpdateProfileActivity.this, val, new LatLng(10.289218, 123.857058), autocompleteplaceListener);
                                mSecondBool = true;
                            }
                        }).start();
                    }
                } else {
                    fromSelected = false;
                }
            }
        });

        mThird.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String val = editable.toString();
                if (!fromSelected) {
                    if (val.length() > 4) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PlacesUtility.getPredictions(UpdateProfileActivity.this, val, new LatLng(10.289218, 123.857058), autocompleteplaceListener);
                                mThirdBool = true;
                            }
                        }).start();
                    }
                } else {
                    fromSelected = false;
                }
            }
        });

        mBirtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateProfileActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        final User finalUser = user;
        final User finalUser1 = user;
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userToPut = finalUser;
                userToPut.setUserFname(mFirstName.getText().toString());
                userToPut.setUserLname(mLastName.getText().toString());
                try {
                    userToPut.setBirthdate(sdf.parse(mBirtdate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                userToPut.setAddress(mAddress.getText().toString());
                userToPut.setUsername(mUserName.getText().toString());
                userToPut.setEmail(mEmail.getText().toString());
                userToPut.setPhoneNumber(mContact.getText().toString());
                userToPut.setLocationArray(location);

                userToPut.setDayTimeModel(userDayTimeList);
                userToPut.setGenreArray(finalUser1.getGenreArray());
                userToPut.setImageFilename(finalUser1.getImageFilename());
                updateUser(userToPut);
            }
        });

        mBtnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    customDialog();
            }
        });

        for(int init=0; init<user.getDayTimeModel().size(); init++){
            if(user.getDayTimeModel().get(init).getDay().getStrDay().equals("Monday")
                    ||user.getDayTimeModel().get(init).getDay().getStrDay().equals("Tuesday")
                    ||user.getDayTimeModel().get(init).getDay().getStrDay().equals("Wednesday")
                    ||user.getDayTimeModel().get(init).getDay().getStrDay().equals("Thursday")
                    ||user.getDayTimeModel().get(init).getDay().getStrDay().equals("Friday")
                    ||user.getDayTimeModel().get(init).getDay().getStrDay().equals("Saturday")
                    ||user.getDayTimeModel().get(init).getDay().getStrDay().equals("Sunday")){
                userDayTimeList.add(user.getDayTimeModel().get(init));
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_timeDay);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(UpdateProfileActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new UserDayTimeUpdateAdapter(this, userDayTimeList);
        recyclerView.setAdapter(mAdapter);

    }

    public void customDialog() {
        final Dialog dialog = new Dialog(UpdateProfileActivity.this);
        dialog.setContentView(R.layout.time_date_dialog);
        dialog.setTitle("Choose Time and Day");
        final DayModel day;
        final TimeModel time;


        day = new DayModel();
        time = new TimeModel();

        etTimeFrom = (EditText) dialog.findViewById(R.id.tcFrom);
        etTimeTo = (EditText) dialog.findViewById(R.id.tcTo);
        Button mBtnOkay = (Button) dialog.findViewById(R.id.btnOkay);
        Button mBtnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Spinner mSpinnerDay = (Spinner) dialog.findViewById(R.id.spinnerDay);

        if (selectedDays.isEmpty()) {
            Log.d("selectedDays", "empty");
        } else {
            Log.d("selectedDays", "not empty");
        }

        selectedDays.add("Monday");
        selectedDays.add("Tuesday");
        selectedDays.add("Wednesday");
        selectedDays.add("Thursdday");
        selectedDays.add("Friday");
        selectedDays.add("Saturday");
        selectedDays.add("Sunday");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateProfileActivity.this, android.R.layout.simple_dropdown_item_1line, selectedDays);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        if (adapter == null) {
            Log.d("apater", "is null");
        } else {
            Log.d("apater", "is not null");
        }
        mSpinnerDay.setAdapter(adapter);

        mSpinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day.setStrDay(selectedDays.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTimePicker(0);
            }
        });
        etTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTimePicker(1);
            }
        });

        mBtnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDayTime = new UserDayTime();
                time.setStrTime(etTimeFrom.getText().toString() + " - " + etTimeTo.getText().toString());
                userDayTime.setDay(day);
                userDayTime.setTime(time);
                userDayTimeList.add(userDayTime);
                mAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void CreateTimePicker(final int pos) {

        final java.util.Calendar c = java.util.Calendar.getInstance();
        int mHour = c.get(java.util.Calendar.HOUR_OF_DAY);
        int mMinute = c.get(java.util.Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateProfileActivity.this,
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
                        } else if (hour == 12) {
                            timeSet = "PM";
                        } else {
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes;
                        else
                            min = String.valueOf(minutes);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(hour).append(':')
                                .append(min).append(" ").append(timeSet).toString();

                        String timeGiven = "";
                        timeGiven = hourOfDay + ":" + minute;
                        Log.d("time selected", timeGiven);

                        if (pos == 0) {
                            etTimeFrom.setText(aTime);
                        } else {
                            etTimeTo.setText(aTime);
                        }

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    Response.Listener<String> autocompleteplaceListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("response", response);
            places.clear();
            places.addAll(PlacesUtility.parsePredictionResult(response));
            changeSuggestedPlaces();
        }
    };

    private void changeSuggestedPlaces() {
        placeAutoCompletePopupWindow.show();
        placeAutoCompleteAdapter.notifyDataSetChanged();
        Log.d("size of result", Integer.toString(places.size()));
    }

    private void openImageChooser() {
        ImagePicker.create(this)
                .single()
                .start(1);
    }

    private void uploadFile(final String path) {

        String uploadUrl = Constants.UPLOAD_IMAGE;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadUrl, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                // parse success output
                Log.d("RESULT OF UOPLOAD", resultResponse);
                user.setImageFilename(resultResponse);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    File file = new File(path);
                    Log.d("FILE NAME = ", file.getName());
                    params.put("file", new DataPart(file.getName(), FileUtils.readFileToByteArray(new File(path)), "image/jpeg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return params;
            }
        };
        requestQueue.add(multipartRequest);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", "inside");
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
            for (Image image : images) {
                Picasso.with(getApplicationContext()).load(new File(image.getPath())).fit().into(ivProfile);
                final String path = image.getPath();
                uploadFile(path);
            }
        }
    }

    public void updateUser(final User userModel){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = Constants.UPDATE_USER;

        final User user = (User) SPUtility.getSPUtil(UpdateProfileActivity.this).getObject("USER_OBJECT", User.class);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(userModel);


        d("BookOwnerPostVolley", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("UserPutRes", response);

                AlertDialog ad = new AlertDialog.Builder(UpdateProfileActivity.this).create();
                ad.setTitle("Successful");
                ad.setMessage("Updated your profile successful!");
                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userModelPass", userModel);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                ad.show();
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

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mBirtdate.setText(sdf.format(calendar.getTime()));
        Log.d("sdf", sdf.format(calendar.getTime()).toString());
    }

    String locationName;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fromSelected = true;

        locationName = places.get(position).getDescription();
        placeAutoCompletePopupWindow.dismiss();
        PlacesUtility.getPlaceDetails(UpdateProfileActivity.this, places.get(position).getId(), getPlaceDetails);
    }

    Response.Listener<String> getPlaceDetails = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("place details = ", response);
            try {
                JSONObject object = new JSONObject(response);
                JSONObject obj = object.getJSONObject("result");
                obj = obj.getJSONObject("geometry");
                obj = obj.getJSONObject("location");
                Log.d("lat", Double.toString(obj.getDouble("lat")));
                Log.d("lng", Double.toString(obj.getDouble("lng")));

                if (mFirstBool == true) {
                    Log.d("Loc", Double.toString(obj.getDouble("lat")));
                    Log.d("Loc", Double.toString(obj.getDouble("lng")));
                    mFirst.setText(locationName);
                    mFirstBool = false;
                    location.get(locPos.get(0)).setLocationName(locationName);
                    location.get(locPos.get(0)).setLongitude(Double.toString(obj.getDouble("lng")));
                    location.get(locPos.get(0)).setLatitude(Double.toString(obj.getDouble("lat")));
                }

                if (mSecondBool == true) {
                    Log.d("Loc", Double.toString(obj.getDouble("lat")));
                    Log.d("Loc", Double.toString(obj.getDouble("lng")));
                    mSecond.setText(locationName);
                    mSecondBool = false;
                    location.get(locPos.get(1)).setLocationName(locationName);
                    location.get(locPos.get(1)).setLongitude(Double.toString(obj.getDouble("lng")));
                    location.get(locPos.get(1)).setLatitude(Double.toString(obj.getDouble("lat")));
                }

                if (mThirdBool == true) {
                    Log.d("Loc", Double.toString(obj.getDouble("lat")));
                    Log.d("Loc", Double.toString(obj.getDouble("lng")));
                    mThird.setText(locationName);
                    mThirdBool = false;
                    location.get(locPos.get(2)).setLocationName(locationName);
                    location.get(locPos.get(2)).setLongitude(Double.toString(obj.getDouble("lng")));
                    location.get(locPos.get(2)).setLatitude(Double.toString(obj.getDouble("lat")));
                }

                if (mAddressBool == true) {
                    Log.d("Loc", Double.toString(obj.getDouble("lng")));
                    Log.d("Loc", Double.toString(obj.getDouble("lng")));
                    mAddress.setText(locationName);
                    mAddressBool = false;
                    location.get(mAddressPos).setLocationName(locationName);
                    location.get(mAddressPos).setLongitude(Double.toString(obj.getDouble("lng")));
                    location.get(mAddressPos).setLatitude(Double.toString(obj.getDouble("lat")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onUserDayTimeInteraction(Uri uri) {

    }
}