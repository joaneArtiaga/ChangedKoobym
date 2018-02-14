package com.example.joane14.myapplication.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AboutProfFrag extends Fragment {

    private OnAboutProfInteractionListener mListener;
    User user;
    TextView mAddress, mPhone, mBirth, mRate;

    public AboutProfFrag() {
    }

    public static AboutProfFrag newInstance(Bundle bundle) {
        AboutProfFrag fragment = new AboutProfFrag();
        fragment.setArguments(bundle);
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
        View view = inflater.inflate(R.layout.fragment_about_prof, container, false);

        user = (User) getArguments().getSerializable("user");

        getUserRating();

        mAddress = (TextView) view.findViewById(R.id.profAddress);
        mRate = (TextView) view.findViewById(R.id.profRating);
        mPhone = (TextView) view.findViewById(R.id.profPhone);
        mBirth = (TextView) view.findViewById(R.id.profBirthDate);


        mAddress.setText(user.getAddress());
        mPhone.setText(user.getPhoneNumber());


        @SuppressLint({"NewApi", "LocalSuppress"})
//        android.icu.text.DateFormat dateFormat = android.icu.text.DateFormat.getDateInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        mBirth.setText(dateFormat.format(user.getBirthdate()));
        mBirth.setText(sdf.format(user.getBirthdate()));
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        FragmentManager fragmentManager = getFragmentManager();
        DisplayUserReview mrbf = DisplayUserReview.newInstance(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_userReview, mrbf);
        fragmentTransaction.commit();

        return view;
    }


    private void getUserRating() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.GET_USER_RATING+"/"+user.getUserId();


//        user.setDayTimeModel();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(user);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("userRating", response);

                mRate.setText(response);

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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAboutProfOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAboutProfInteractionListener) {
            mListener = (OnAboutProfInteractionListener) context;
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

    public interface OnAboutProfInteractionListener {
        void onAboutProfOnClick(Uri uri);
    }
}
