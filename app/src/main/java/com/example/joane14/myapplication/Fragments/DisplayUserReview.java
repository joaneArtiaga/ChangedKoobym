package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Adapters.ReviewAdapter;
import com.example.joane14.myapplication.Adapters.UserReviewAdapter;
import com.example.joane14.myapplication.Model.BookOwnerReview;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserRating;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class DisplayUserReview extends Fragment {



    User user;
    List<UserRating> userRatingList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnUserReviewInteractionListener mListener;

    public DisplayUserReview() {
    }

    public static DisplayUserReview newInstance(Bundle bundle) {
        DisplayUserReview fragment = new DisplayUserReview();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_user_review, container, false);

        user = new User();
        user = (User) getArguments().getSerializable("user");

        userRatingList = new ArrayList<UserRating>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_user_reviews);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UserReviewAdapter(userRatingList);
        mRecyclerView.setAdapter(mAdapter);

        getUserRating();
        return view;
    }

    public void getUserRating(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Log.d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_USER_REVIEWS+"/"+user.getUserId();

        Log.d("UserReview URL", URL);
        final RentalHeader rentalHeader =new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseRequestReceived", response);
                userRatingList.clear();
                userRatingList.addAll(Arrays.asList(gson.fromJson(response, UserRating[].class)));
                mAdapter.notifyDataSetChanged();
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
            mListener.onUserReviewOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserReviewInteractionListener) {
            mListener = (OnUserReviewInteractionListener) context;
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

    public interface OnUserReviewInteractionListener {
        void onUserReviewOnClick(Uri uri);
    }
}
