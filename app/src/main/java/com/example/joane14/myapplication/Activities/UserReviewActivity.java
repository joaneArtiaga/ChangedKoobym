package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.DisplayBookReview;
import com.example.joane14.myapplication.Fragments.DisplayUserReview;
import com.example.joane14.myapplication.Model.BookOwnerRating;
import com.example.joane14.myapplication.Model.BookOwnerReview;
import com.example.joane14.myapplication.Model.Rate;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.Review;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserRating;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class UserReviewActivity extends AppCompatActivity implements DisplayUserReview.OnUserReviewInteractionListener {

    RentalHeader rentalHeader;
    User user;
    UserRating userRating;
    Review review;
    Rate rate;
    EditText mComment;
    Button mPostBtn;
    Spinner mSpinner;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review2);


        @SuppressLint({"NewApi", "LocalSuppress"}) final
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        user = new User();
        review = new Review();
        rate = new Rate();
        userRating = new UserRating();

        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        mComment = (EditText) findViewById(R.id.brComment);
        mPostBtn = (Button) findViewById(R.id.brBtnPost);
        mSpinner = (Spinner) findViewById(R.id.spinner1);

        rentalHeader = new RentalHeader();
        if(getIntent().getExtras().getSerializable("rentalHeader")!=null){
            rentalHeader = (RentalHeader) getIntent().getExtras().getSerializable("rentalHeader");
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", rentalHeader.getUserId());
            fragmentManager = getSupportFragmentManager();
            DisplayUserReview displayUserReview = DisplayUserReview.newInstance(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container_user_review, displayUserReview);
            fragmentTransaction.commit();

        }

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String mSelected = mSpinner.getSelectedItem().toString();
                Log.d("SelectedRate", mSelected);
                rate.setRateNumber(Integer.parseInt(mSelected));
                rate.setRateTimeStamp(date);
                userRating.setUser(user);
                userRating.setUserRater(rentalHeader.getRentalDetail().getBookOwner().getUserObj());
                userRating.setRate(rate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review.setReviewTimeStamp(date);
                userRating.setReview(review);
                userRating.setComment(mComment.getText().toString());

                addUserReview();
            }
        });
    }


    private void addUserReview() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_USER_RATE;





//        user.setDayTimeModel();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(userRating);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("bookOwnerReviewAdd", response);

                Intent intent = new Intent(UserReviewActivity.this, ProfileActivity.class);
                Bundle bundlePass = new Bundle();
                bundlePass.putSerializable("userModelPass", rentalHeader.getRentalDetail().getBookOwner().getUserObj());
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
    public void onUserReviewOnClick(Uri uri) {

    }
}
