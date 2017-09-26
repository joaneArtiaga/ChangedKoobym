package com.example.joane14.myapplication.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Fragments.AddProfile;
import com.example.joane14.myapplication.Fragments.AddTimeFrag;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.Genre;
import com.example.joane14.myapplication.Model.DayTimeModel;
import com.example.joane14.myapplication.Model.GenreModel;
import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.MeetUpLocObj;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

public class SignUp extends AppCompatActivity implements
        Genre.OnFragmentInteractionListener,
        AddProfile.OnFragmentInteractionListener,
        AddTimeFrag.OnAddTimeInteractionListener{

    private List<GenreModel> genres;
    private List<LocationModel> locations;
    private List<DayTimeModel> dayTimeList;
    private FragmentManager fragmentManager;
    private User userModel;
    Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if(getIntent().getExtras().getString("Genre")!=null){
            fragmentManager = getSupportFragmentManager();
            Genre genreModel = new Genre();
            changeFragment(genreModel);
        }

        if(getIntent().getExtras().getString("AddTime")!=null){
            MeetUpLocObj meetUpLocObj = new MeetUpLocObj();
            mBundle = getIntent().getExtras();
            meetUpLocObj = (MeetUpLocObj) mBundle.getSerializable("locationObj");
            if(meetUpLocObj!=null){
                Log.d("MeetUpLocObj", "is not null");
                this.locations = meetUpLocObj.getLocationModelList();
            }else{
                Log.d("MeetUpLocObj", "is null");
            }
            if(getIntent().getExtras().getStringArrayList("listDay")!=null){
                Log.d("listDay", "is not null");
                Log.d("listDay", getIntent().getExtras().getStringArrayList("listDay").toString());
            }else{
                Log.d("listDay", "is null");
            }

            mBundle.putStringArrayList("listDay", getIntent().getExtras().getStringArrayList("listDay"));
            fragmentManager = getSupportFragmentManager();
            AddTimeFrag addTimeModel = new AddTimeFrag();
            addTimeModel.setArguments(mBundle);
            changeFragment(addTimeModel);
        }



    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getTag());
        fragmentTransaction.commit();
    }

    @Override
    public void onGenreSelected(List<GenreModel> genres) {
        this.genres = genres;
        Log.d("genreList", genres.toString());
        AddProfile addProfileFrag = new AddProfile();
        changeFragment(addProfileFrag);
    }

    @Override
    public void onUserSelected(User user) {
        mBundle = new Bundle();
        user.setGenreArray(this.genres);
        this.userModel = user;
        Log.d("Add Profile", "First Name:" + userModel.getUserFname());
        Log.d("Add Profile", "Last Name:" + userModel.getUserLname());
        Log.d("Add Profile", "User Name:" + userModel.getUsername());
        Log.d("Add Profile", "Address:" + userModel.getAddress());
        Log.d("Add Profile", "Email:" + userModel.getEmail());
        Log.d("Add Profile", "Contact Number:" + userModel.getPhoneNumber());
        Log.d("Add Profile", "Password:" + userModel.getPassword());


        Log.d("On User selected", "inside");

        register();

    }

    private void register() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://172.16.16.141:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        User user = new User();
        user.setUserFname(userModel.getUserFname());
        user.setUserLname(userModel.getUserLname());
        user.setAddress(userModel.getAddress());
        user.setEmail(userModel.getEmail());
        user.setUsername(userModel.getUsername());
        user.setPassword(userModel.getPassword());
        user.setBirthdate(userModel.getBirthdate());
        user.setImageFilename(userModel.getImageFilename());
        user.setPhoneNumber(userModel.getPhoneNumber());
        user.setGenreArray(genres);
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(user);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_VOLLEY", response);
                User user = gson.fromJson(response, User.class);
                Log.i("LOG_VOLLEY", user.getEmail());
                Log.i("LOG_VOLLEY", user.getUserFname());
                Log.i("LOG_VOLLEY", user.getUserLname());
                user.setGenreArray(genres);
                Intent intent = new Intent(SignUp.this, LandingPage.class);
                Bundle b = new Bundle();
                b.putBoolean("fromRegister", true);
                b.putSerializable("userModel", user);
                intent.putExtra("user",b);
                startActivity(intent);
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


    private void genreAdd() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "http://172.16.16.141:8080/Koobym/genre/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        User user = new User();
        user.setUserFname(userModel.getUserFname());
        user.setUserLname(userModel.getUserLname());
        user.setAddress(userModel.getAddress());
        user.setEmail(userModel.getEmail());
        user.setUsername(userModel.getUsername());
        user.setPassword(userModel.getPassword());
        user.setBirthdate(userModel.getBirthdate());
        user.setImageFilename(userModel.getImageFilename());
        user.setPhoneNumber(userModel.getPhoneNumber());
        user.setGenreArray(genres);
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(user);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_VOLLEY", response);
                User user = gson.fromJson(response, User.class);
                Log.i("LOG_VOLLEY", user.getEmail());
                Log.i("LOG_VOLLEY", user.getUserFname());
                Log.i("LOG_VOLLEY", user.getUserLname());
                user.setGenreArray(genres);
                Intent intent = new Intent(SignUp.this, LandingPage.class);
                Bundle b = new Bundle();
                b.putBoolean("fromRegister", true);
                b.putSerializable("userModel", user);
                intent.putExtra("user",b);
                startActivity(intent);
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

    @Override
    public void onAddTimeClickListener(List<DayTimeModel> listDayTimeModel) {
        Log.d("addTimeClickListerner", "inside");
        this.dayTimeList = listDayTimeModel;
        Genre genreModel = new Genre();
        changeFragment(genreModel);
    }
}
