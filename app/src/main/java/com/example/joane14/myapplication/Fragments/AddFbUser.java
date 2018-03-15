package com.example.joane14.myapplication.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.example.joane14.myapplication.Activities.HistoryActivity;
import com.example.joane14.myapplication.Adapters.PlaceAutoCompleteAdapter;
import com.example.joane14.myapplication.Adapters.TimeDayAdapter;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddFbUser extends Fragment implements AdapterView.OnItemClickListener {

    private OnAddFbUserInteractionListener mListener;
    AutoCompleteTextView mAddress, mFirstLoc, mSecondLoc, mThirdLoc;
    EditText mFirstName, mLastName, mUsername, mEmail, mContactNumber, mBirthdate;
    User userModel;
    ImageView slctImage;
    ImageView imageView;
    String filename;
    private DatePicker datePicker;
    private Calendar calendar;
    DatePickerDialog.OnDateSetListener date;
    PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    ListPopupWindow placeAutoCompletePopupWindow;
    List<Place> places;
    EditText etTimeFrom, etTimeTo;
    List<String> selectedDays;
    List<UserDayTime> userDayTimeList;
    List<LocationModel> locationList;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    Boolean firstLocBool, secondLocBool, thirdLocBool, addressBool, mMondayBool, mTuesdayBool, mWednesdayBool, mThursdayBool, mFridayBool, mSaturdayBool, mSundayBool;
    UserDayTime userDayTime;
    boolean fromSelected;
    String locationName;


    public AddFbUser() {
    }

    public static AddFbUser newInstance() {
        AddFbUser fragment = new AddFbUser();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_fb_user, container, false);

        calendar = Calendar.getInstance();

        selectedDays = new ArrayList<String>();

        firstLocBool = false;
        secondLocBool = false;
        thirdLocBool = false;
        addressBool = false;
        mMondayBool = false;
        mTuesdayBool = false;
        mWednesdayBool = false;
        mThursdayBool = false;
        mFridayBool = false;
        mSaturdayBool = false;
        mSundayBool = false;
        fromSelected = false;
        locationName = "";

        locationList = new ArrayList<LocationModel>();
        userDayTimeList = new ArrayList<UserDayTime>();
        userDayTime = new UserDayTime();

        userModel = new User();


        slctImage = (ImageView) view.findViewById(R.id.displayPic);
        Button mNextAdd = (Button) view.findViewById(R.id.btnNextAdd);


        userModel = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);

        if(userModel==null){
            Log.d("nullDiay", "o null jud");
        }else{
            Log.d("nullDiay", "dili man");
        }

        mFirstName = (EditText) view.findViewById(R.id.firstName);
        mLastName = (EditText) view.findViewById(R.id.lastName);
        mUsername = (EditText) view.findViewById(R.id.username);
        mAddress = (AutoCompleteTextView) view.findViewById(R.id.address);
        mFirstLoc = (AutoCompleteTextView) view.findViewById(R.id.firstLoc);
        mSecondLoc = (AutoCompleteTextView) view.findViewById(R.id.secondLoc);
        mContactNumber = (EditText) view.findViewById(R.id.contactNumber);
        mThirdLoc = (AutoCompleteTextView) view.findViewById(R.id.thirdLoc);
        mEmail = (EditText) view.findViewById(R.id.email);
        mBirthdate = (EditText) view.findViewById(R.id.birthDate);
        Button mBtnAddTime = (Button) view.findViewById(R.id.addTime);

        final CheckBox mMonday = (CheckBox) view.findViewById(R.id.cbMonday);
        final CheckBox mTuesday = (CheckBox) view.findViewById(R.id.cbTuesday);
        final CheckBox mWednesday = (CheckBox) view.findViewById(R.id.cbWednesday);
        final CheckBox mThursday = (CheckBox) view.findViewById(R.id.cbThursday);
        final CheckBox mFriday = (CheckBox) view.findViewById(R.id.cbFriday);
        final CheckBox mSaturday = (CheckBox) view.findViewById(R.id.cbSaturday);
        final CheckBox mSunday = (CheckBox) view.findViewById(R.id.cbSunday);

        places = new ArrayList<>();
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(getContext(), places);
        placeAutoCompletePopupWindow = new ListPopupWindow(getContext());
        placeAutoCompletePopupWindow.setAdapter(placeAutoCompleteAdapter);
        placeAutoCompletePopupWindow.setAnchorView(mAddress);
        placeAutoCompletePopupWindow.setModal(false);
        placeAutoCompletePopupWindow.setOnItemClickListener(this);

        Picasso.with(getContext()).load(userModel.getImageFilename()).fit().into(slctImage);

        mFirstName.setEnabled(false);
        mFirstName.setText(userModel.getUserFname());
        mLastName.setEnabled(false);
        mLastName.setText(userModel.getUserLname());
        mEmail.setEnabled(false);
        mEmail.setText(userModel.getEmail());


        mSunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    selectedDays.add("Sunday");
                }else{
                    for(int init=0; init<selectedDays.size(); init++){
                        if(selectedDays.get(init).equals("Sunday")){
                            selectedDays.remove(init);
                            break;
                        }
                    }
                }
            }
        });
        mSaturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    selectedDays.add("Saturday");
                }else{
                    for(int init=0; init<selectedDays.size(); init++){
                        if(selectedDays.get(init).equals("Saturday")){
                            selectedDays.remove(init);
                            break;
                        }
                    }
                }
            }
        });
        mMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    selectedDays.add("Monday");
                }else{
                    for(int init=0; init<selectedDays.size(); init++){
                        if(selectedDays.get(init).equals("Monday")){
                            selectedDays.remove(init);
                            break;
                        }
                    }
                }
            }
        });

        mTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    selectedDays.add("Tuesday");
                }else{
                    for(int init=0; init<selectedDays.size(); init++){
                        if(selectedDays.get(init).equals("Tuesday")){
                            selectedDays.remove(init);
                            break;
                        }
                    }
                }
            }
        });

        mWednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    selectedDays.add("Wednesday");
                }else{
                    for(int init=0; init<selectedDays.size(); init++){
                        if(selectedDays.get(init).equals("Wednesday")){
                            selectedDays.remove(init);
                            break;
                        }
                    }
                }
            }
        });


        mThursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    selectedDays.add("Thursday");
                }else{
                    for(int init=0; init<selectedDays.size(); init++){
                        if(selectedDays.get(init).equals("Thursday")){
                            selectedDays.remove(init);
                            break;
                        }
                    }
                }

            }
        });

        mFriday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    selectedDays.add("Friday");
                }else{
                    for(int init=0; init<selectedDays.size(); init++){
                        if(selectedDays.get(init).equals("Friday")){
                            selectedDays.remove(init);
                            break;
                        }
                    }
                }
            }
        });


        mAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String val = editable.toString();
                if (!fromSelected) {
                    if (val.length() > 4) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PlacesUtility.getPredictions(getActivity(), val, new LatLng(10.289218, 123.857058), autocompleteplaceListener);
                                addressBool = true;
                            }
                        }).start();
                    }
                } else {
                    fromSelected = false;
                }
            }
        });

        mFirstLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String val = editable.toString();
                if (!fromSelected) {
                    if (val.length() > 4) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PlacesUtility.getPredictions(getActivity(), val, new LatLng(10.289218, 123.857058), autocompleteplaceListener);
                                firstLocBool = true;
                            }
                        }).start();
                    }
                } else {
                    fromSelected = false;
                }
            }
        });

        mSecondLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String val = editable.toString();
                if (!fromSelected) {
                    if (val.length() > 4) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                secondLocBool = true;
                                PlacesUtility.getPredictions(getActivity(), val, new LatLng(10.289218, 123.857058), autocompleteplaceListener);
                            }
                        }).start();
                    }
                } else {
                    fromSelected = false;
                }
            }
        });

        mThirdLoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final String val = editable.toString();
                if (!fromSelected) {
                    if (val.length() > 4) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                thirdLocBool = true;
                                PlacesUtility.getPredictions(getActivity(), val, new LatLng(10.289218, 123.857058), autocompleteplaceListener);
                            }
                        }).start();
                    }
                } else {
                    fromSelected = false;
                }
            }
        });


        date = new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("NewApi")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        mBirthdate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                new DatePickerDialog(getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        mBtnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDays.isEmpty()) {
                    AlertDialog ad = new AlertDialog.Builder(getContext()).create();
                    ad.setTitle("Alert!");
                    ad.setMessage("You need to choose your available days for meet up.");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }else{
                    customDialog();
                }
            }
        });

        mNextAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                if (mFirstName.getText().length() == 0) {
                    mFirstName.setError("Field cannot be left empty.");
                }
                if (mLastName.getText().length() == 0) {
                    mLastName.setError("Field cannot be left empty.");
                }
                if (mUsername.getText().length() == 0) {
                    mUsername.setError("Field cannot be left empty.");
                }
                if (mAddress.getText().length() == 0) {
                    mAddress.setError("Field cannot be left empty.");
                }
                if (mEmail.getText().length() == 0) {
                    mEmail.setError("Field cannot be left empty.");
                }
                if (mContactNumber.getText().length() == 0) {
                    mContactNumber.setError("Field cannot be empty.");
                }

                if (mBirthdate.getText().length() == 0) {
                    mBirthdate.setError("Field cannot be empty");
                }

                if (mFirstName.getText().length() != 0 &&
                    mLastName.getText().length() != 0 && mUsername.getText().length() != 0 && mAddress.getText().length() != 0 &&
                    mEmail.getText().length() != 0 && mContactNumber.getText().length() != 0 && mBirthdate.getText().length() != 0) {
                    Log.d("Inside", "add Profile");
                    Log.d("Add Profile", "First Name:" + mFirstName.getText().toString());
                    Log.d("Add Profile", "Last Name:" + mLastName.getText().toString());
                    Log.d("Add Profile", "User Name:" + mUsername.getText().toString());
                    Log.d("Add Profile", "Address:" + mAddress.getText().toString());
                    Log.d("Add Profile", "Email:" + mEmail.getText().toString());
                    Log.d("Add Profile", "Contact Number:" + mContactNumber.getText().toString());



                    userModel.setUsername(mUsername.getText().toString());
                    userModel.setAddress(mAddress.getText().toString());
                    userModel.setEmail(mEmail.getText().toString());
                    userModel.setPhoneNumber(mContactNumber.getText().toString());
                    userModel.setPassword("");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = mBirthdate.getText().toString();
                    try {
                        userModel.setBirthdate(dateFormat.parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }

                    mListener.OnAddFbUser(userModel, locationList, userDayTimeList);

                }
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_timeDay);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new TimeDayAdapter(userDayTimeList);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    public void customDialog() {
        final Dialog dialog = new Dialog(getContext());
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, selectedDays);
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


        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        fromSelected = true;
        locationName = places.get(i).getDescription();
        placeAutoCompletePopupWindow.dismiss();
        PlacesUtility.getPlaceDetails(getActivity(), places.get(i).getId(), getPlaceDetails);
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

                if (firstLocBool == true) {
                    Log.d("Loc", Double.toString(obj.getDouble("lat")));
                    Log.d("Loc", Double.toString(obj.getDouble("lng")));
                    mFirstLoc.setText(locationName);
                    firstLocBool = false;
                    LocationModel locationModel = new LocationModel();
                    locationModel.setLocationName(locationName);
                    locationModel.setLongitude(Double.toString(obj.getDouble("lng")));
                    locationModel.setLatitude(Double.toString(obj.getDouble("lat")));
                    locationModel.setStatus("MeetUp");
                    locationList.add(locationModel);
                }

                if (secondLocBool == true) {
                    Log.d("Loc", Double.toString(obj.getDouble("lat")));
                    Log.d("Loc", Double.toString(obj.getDouble("lng")));
                    mSecondLoc.setText(locationName);
                    secondLocBool = false;
                    LocationModel locationModel = new LocationModel();
                    locationModel.setLocationName(locationName);
                    locationModel.setLongitude(Double.toString(obj.getDouble("lng")));
                    locationModel.setLatitude(Double.toString(obj.getDouble("lat")));
                    locationModel.setStatus("MeetUp");
                    locationList.add(locationModel);
                }

                if (thirdLocBool == true) {
                    Log.d("Loc", Double.toString(obj.getDouble("lat")));
                    Log.d("Loc", Double.toString(obj.getDouble("lng")));
                    mThirdLoc.setText(locationName);
                    thirdLocBool = false;
                    LocationModel locationModel = new LocationModel();
                    locationModel.setLocationName(locationName);
                    locationModel.setLongitude(Double.toString(obj.getDouble("lng")));
                    locationModel.setLatitude(Double.toString(obj.getDouble("lat")));
                    locationModel.setStatus("MeetUp");
                    locationList.add(locationModel);
                }

                if (addressBool == true) {
                    Log.d("Loc", Double.toString(obj.getDouble("lng")));
                    Log.d("Loc", Double.toString(obj.getDouble("lng")));
                    mAddress.setText(locationName);
                    addressBool = false;
                    LocationModel locationModel = new LocationModel();
                    locationModel.setLocationName(locationName);
                    locationModel.setLongitude(Double.toString(obj.getDouble("lng")));
                    locationModel.setLatitude(Double.toString(obj.getDouble("lat")));
                    locationModel.setStatus("Address");
                    locationList.add(locationModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    @SuppressLint("NewApi")
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        @SuppressLint({"NewApi", "LocalSuppress"})
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mBirthdate.setText(sdf.format(calendar.getTime()));
    }


    public void onButtonPressed(User user, List<LocationModel> locList, List<UserDayTime> udtList) {
        if (mListener != null) {
            mListener.OnAddFbUser(user, locList, udtList);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddFbUserInteractionListener) {
            mListener = (OnAddFbUserInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddFbUserInteractionListener {
        void OnAddFbUser(User user, List<LocationModel> locList, List<UserDayTime> udtList);
    }
}
