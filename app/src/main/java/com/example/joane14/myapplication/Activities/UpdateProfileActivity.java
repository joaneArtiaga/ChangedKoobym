package com.example.joane14.myapplication.Activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.util.Log.d;

/**
 * Created by Kimberly Cañedo on 06/10/2017.
 */

public class UpdateProfileActivity extends AppCompatActivity {

    DatePickerDialog.OnDateSetListener date;
    private Calendar calendar;
    EditText mBirtdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        calendar = Calendar.getInstance();

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        final EditText mFirstName = (EditText) findViewById(R.id.etFirstNameUP);
        final EditText mLastName = (EditText) findViewById(R.id.etLastNameUP);
        mBirtdate = (EditText) findViewById(R.id.etBirthdateUP);
        final EditText mAddress = (EditText) findViewById(R.id.etAddressUP);
        final EditText mUserName = (EditText) findViewById(R.id.etUserNameUP);
        final EditText mEmail = (EditText) findViewById(R.id.etMailUP);
        final EditText mContact = (EditText) findViewById(R.id.etContactUP);
        ImageButton mBtnUpdate = (ImageButton) findViewById(R.id.btnUpdateProfile);

        TextView mLoc1 = (TextView) findViewById(R.id.location1UP);
        TextView mLoc2 = (TextView) findViewById(R.id.location2UP);
        TextView mLoc3 = (TextView) findViewById(R.id.location3UP);

        User user = new User();

        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        mFirstName.setText(user.getUserFname());
        mLastName.setText(user.getUserLname());
        mBirtdate.setText(sdf.format(user.getBirthdate()));
        mAddress.setText(user.getAddress());
        mUserName.setText(user.getUsername());
        mEmail.setText(user.getEmail());
        mContact.setText(user.getPhoneNumber());

        List<LocationModel> location = new ArrayList<LocationModel>();

        location = user.getLocationArray();

        mLoc1.setText(location.get(0).getLocationName());
        mLoc2.setText(location.get(1).getLocationName());
        mLoc3.setText(location.get(2).getLocationName());

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
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User userToPut = new User();

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

                updateUser(userToPut);
            }
        });

    }

    public void updateUser(final User userModel){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
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

}