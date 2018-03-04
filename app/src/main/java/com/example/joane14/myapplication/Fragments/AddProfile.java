package com.example.joane14.myapplication.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
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
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class AddProfile extends Fragment implements AdapterView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    EditText mFirstName, mLastName, mUsername, mEmail, mContactNumber, mPassword, mConfirmPassword, mBirthdate;
    AutoCompleteTextView mAddress, mFirstLoc, mSecondLoc, mThirdLoc;
    Button mNextAdd;
    UserDayTime userDayTime;
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

    Boolean firstLocBool, secondLocBool, thirdLocBool, addressBool, mMondayBool, mTuesdayBool, mWednesdayBool, mThursdayBool, mFridayBool;

    public AddProfile() {

    }


    public static AddProfile newInstance() {
        AddProfile fragment = new AddProfile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_profile, container, false);

        calendar = Calendar.getInstance();

        Log.d("Inside", "onCreateView");

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

        locationList = new ArrayList<LocationModel>();
        userDayTimeList = new ArrayList<UserDayTime>();
        userDayTime = new UserDayTime();

        userModel = new User();
//        String filename = "123-1501684832903Screenshot_20170802-014107.jpg";

        slctImage = (ImageView) view.findViewById(R.id.displayPic);
//        Picasso.with(getContext()).load(String.format(Constants.IMAGE_URL, filename)).fit().into(slctImage);
        mNextAdd = (Button) view.findViewById(R.id.btnNextAdd);

        mFirstName = (EditText) view.findViewById(R.id.firstName);
        mLastName = (EditText) view.findViewById(R.id.lastName);
        mUsername = (EditText) view.findViewById(R.id.username);
        mAddress = (AutoCompleteTextView) view.findViewById(R.id.address);
        mFirstLoc = (AutoCompleteTextView) view.findViewById(R.id.firstLoc);
        mSecondLoc = (AutoCompleteTextView) view.findViewById(R.id.secondLoc);
        mThirdLoc = (AutoCompleteTextView) view.findViewById(R.id.thirdLoc);
        Button mBtnAddTime = (Button) view.findViewById(R.id.addTime);

        final CheckBox mMonday = (CheckBox) view.findViewById(R.id.cbMonday);
        final CheckBox mTuesday = (CheckBox) view.findViewById(R.id.cbTuesday);
        final CheckBox mWednesday = (CheckBox) view.findViewById(R.id.cbWednesday);
        final CheckBox mThursday = (CheckBox) view.findViewById(R.id.cbThursday);
        final CheckBox mFriday = (CheckBox) view.findViewById(R.id.cbFriday);

        places = new ArrayList<>();
        placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(getContext(), places);
        placeAutoCompletePopupWindow = new ListPopupWindow(getContext());
        placeAutoCompletePopupWindow.setAdapter(placeAutoCompleteAdapter);
        placeAutoCompletePopupWindow.setAnchorView(mAddress);
        placeAutoCompletePopupWindow.setModal(false);
        placeAutoCompletePopupWindow.setOnItemClickListener(this);

        mMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mMondayBool == true) {
                    for (int init = 0; init < 5; init++) {
                        if (selectedDays.get(init).equals("Monday")) {
                            selectedDays.remove(init);
                        }
                    }
                    mMondayBool = false;
                }else {
                    selectedDays.add("Monday");
                    mMondayBool = true;
                }
            }
        });

        mTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mTuesdayBool == true) {
                    for (int init = 0; init < 5; init++) {
                        if (selectedDays.get(init).equals("Tuesday")) {
                            selectedDays.remove(init);
                        }
                    }
                    mTuesdayBool = false;
                } else {
                    selectedDays.add("Tuesday");
                    mTuesdayBool = true;
                }
            }
        });

        mWednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mWednesdayBool == true) {
                    for (int init = 0; init < 5; init++) {
                        if (selectedDays.get(init).equals("Wednesday")) {
                            selectedDays.remove(init);
                        }
                        mWednesdayBool = false;
                    }
                } else {
                    mThursdayBool = true;
                    selectedDays.add("Wednesday");
                }
            }
        });


        mThursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mThursdayBool == true) {
                    for (int init = 0; init < 5; init++) {
                        if (selectedDays.get(init).equals("Thursday")) {
                            selectedDays.remove(init);
                        }
                    }
                    mThursdayBool = false;
                } else {
                    mThursdayBool = true;
                    selectedDays.add("Thursday");
                }

            }
        });

        mFriday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mFridayBool == true) {
                    for (int init = 0; init < 5; init++) {
                        if (selectedDays.get(init).equals("Friday")) {
                            selectedDays.remove(init);
                        }
                    }
                    mFridayBool = false;
                } else {
                    mFridayBool = true;
                    selectedDays.add("Friday");
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

        mEmail = (EditText) view.findViewById(R.id.email);
        mContactNumber = (EditText) view.findViewById(R.id.contactNumber);
        mPassword = (EditText) view.findViewById(R.id.password);
        mConfirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        this.mBirthdate = (EditText) view.findViewById(R.id.birthDate);


        date = new DatePickerDialog.OnDateSetListener() {

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
            @Override
            public void onClick(View view) {

                new DatePickerDialog(getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        slctImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
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
            @Override
            public void onClick(View view) {

                if (!(mPassword.getText().toString().equals(mConfirmPassword.getText().toString()))) {
                    Log.d("Password" + mPassword.getText().toString(), "Confirm Password" + mConfirmPassword.getText().toString());
                    mConfirmPassword.setError("Passwords should match.");
                }
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
                if (mPassword.getText().length() == 0) {
                    mPassword.setError("Field cannot be empty");
                }
                if (mConfirmPassword.getText().length() == 0) {
                    mConfirmPassword.setError("Field cannot be empty");
                }
                if (mBirthdate.getText().length() == 0) {
                    mBirthdate.setError("Field cannot be empty");
                }

                if (mFirstLoc.getText().length() == 0) {
                    mFirstLoc.setError("Field cannot be empty");
                }
                if (mSecondLoc.getText().length() == 0) {
                    mSecondLoc.setError("Field cannot be empty");
                }
                if (mThirdLoc.getText().length() == 0) {
                    mThirdLoc.setError("Field cannot be empty");
                }

                if (!(mMonday.isChecked()) && !(mTuesday.isChecked()) && !(mWednesday.isChecked()) && !(mThursday.isChecked()) && !(mFriday.isChecked())) {
                    AlertDialog ad = new AlertDialog.Builder(getContext()).create();
                    ad.setTitle("Alert!");
                    ad.setMessage("Should choose available days for meet up.");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }


                if (mPassword.getText().toString().equals(mConfirmPassword.getText().toString()) && mFirstName.getText().length() != 0 &&
                        mLastName.getText().length() != 0 && mUsername.getText().length() != 0 && mAddress.getText().length() != 0 &&
                        mEmail.getText().length() != 0 && mContactNumber.getText().length() != 0 && mPassword.getText().length() != 0 &&
                        mConfirmPassword.getText().length() != 0 && mBirthdate.getText().length() != 0) {
                    Log.d("Inside", "add Profile");
                    Log.d("Add Profile", "First Name:" + mFirstName.getText().toString());
                    Log.d("Add Profile", "Last Name:" + mLastName.getText().toString());
                    Log.d("Add Profile", "User Name:" + mUsername.getText().toString());
                    Log.d("Add Profile", "Address:" + mAddress.getText().toString());
                    Log.d("Add Profile", "Email:" + mEmail.getText().toString());
                    Log.d("Add Profile", "Contact Number:" + mContactNumber.getText().toString());
                    Log.d("Add Profile", "Password:" + mPassword.getText().toString());


                    userModel.setUserFname(mFirstName.getText().toString());
                    userModel.setUserLname(mLastName.getText().toString());
                    userModel.setUsername(mUsername.getText().toString());
                    userModel.setAddress(mAddress.getText().toString());
                    userModel.setEmail(mEmail.getText().toString());
                    userModel.setPhoneNumber(mContactNumber.getText().toString());
                    userModel.setPassword(mPassword.getText().toString());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = mBirthdate.getText().toString();
                    try {
                        userModel.setBirthdate(dateFormat.parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    mListener.onUserSelected(userModel, locationList, userDayTimeList);

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

        // set the custom dialog components - text, image and button
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
                time.setStrTime(etTimeTo.getText().toString() + " - " + etTimeFrom.getText().toString());
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

//        userDayTime = new UserDayTime();

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


    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mBirthdate.setText(sdf.format(calendar.getTime()));
    }

    private void openImageChooser() {
        ImagePicker.create(this)
                .single()
                .start(1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", "inside");
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
            for (Image image : images) {
                Picasso.with(getContext()).load(new File(image.getPath())).fit().into(slctImage);
                final String path = image.getPath();
                uploadFile(path);
            }
        }
    }


//    String uploadUrl = Constants.WEB_SERVICE_URL+"image/upload";

    private void uploadFile(final String path) {

        String uploadUrl = Constants.UPLOAD_IMAGE;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadUrl, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                // parse success output
                Log.d("RESULT OF UOPLOAD", resultResponse);
                userModel.setImageFilename(resultResponse);

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


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    private String getPathFromURI(Uri selectedImageUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        fromSelected = true;
        if (firstLocBool == true) {
            Log.d("Loc", String.valueOf(places.get(i).getLatitude()));
            Log.d("Loc", String.valueOf(places.get(i).getLongitude()));
            mFirstLoc.setText(places.get(i).getDescription());
            firstLocBool = false;
            LocationModel locationModel = new LocationModel();
            locationModel.setLocationName(places.get(i).getDescription());
            locationModel.setLongitude(Double.toString(places.get(i).getLongitude()));
            locationModel.setLatitude(Double.toString(places.get(i).getLatitude()));
            locationModel.setStatus("Address");
            locationList.add(locationModel);
        }

        if (secondLocBool == true) {
            Log.d("Loc", String.valueOf(places.get(i).getLatitude()));
            Log.d("Loc", String.valueOf(places.get(i).getLongitude()));
            mSecondLoc.setText(places.get(i).getDescription());
            secondLocBool = false;
            LocationModel locationModel = new LocationModel();
            locationModel.setLocationName(places.get(i).getDescription());
            locationModel.setLongitude(Double.toString(places.get(i).getLongitude()));
            locationModel.setLatitude(Double.toString(places.get(i).getLatitude()));
            locationModel.setStatus("MeetUp");
            locationList.add(locationModel);
        }

        if (thirdLocBool == true) {
            Log.d("Loc", String.valueOf(places.get(i).getLatitude()));
            Log.d("Loc", String.valueOf(places.get(i).getLongitude()));
            mThirdLoc.setText(places.get(i).getDescription());
            thirdLocBool = false;
            LocationModel locationModel = new LocationModel();
            locationModel.setLocationName(places.get(i).getDescription());
            locationModel.setLongitude(Double.toString(places.get(i).getLongitude()));
            locationModel.setLatitude(Double.toString(places.get(i).getLatitude()));
            locationModel.setStatus("MeetUp");
            locationList.add(locationModel);
        }

        if (addressBool == true) {
            Log.d("Loc", String.valueOf(places.get(i).getLatitude()));
            Log.d("Loc", places.get(i).getLongitude()+"");
            mAddress.setText(places.get(i).getDescription());
            addressBool = false;
            LocationModel locationModel = new LocationModel();
            locationModel.setLocationName(places.get(i).getDescription());
            locationModel.setLongitude(Double.toString(places.get(i).getLongitude()));
            locationModel.setLatitude(Double.toString(places.get(i).getLatitude()));
            locationModel.setStatus("MeetUp");
            locationList.add(locationModel);
        }

        placeAutoCompletePopupWindow.dismiss();
        PlacesUtility.getPlaceDetails(getActivity(), places.get(i).getId(), getPlaceDetails);
    }

    boolean fromSelected;

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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public interface OnFragmentInteractionListener {
        void onUserSelected(User user, List<LocationModel> listLoc, List<UserDayTime> listDayTime);
    }
}
