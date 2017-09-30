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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Adapters.LandingPageAdapter;
import com.example.joane14.myapplication.Adapters.ShelfAdapter;
import com.example.joane14.myapplication.Model.GenreModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RentShelfFragment extends Fragment {

    List<RentalDetail> rentalDetailList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private OnRentShelfInteractionListener mListener;

    public RentShelfFragment() {
    }


    public static RentShelfFragment newInstance() {
        RentShelfFragment fragment = new RentShelfFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RentShelf", "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent_shelf, container, false);


        rentalDetailList = new ArrayList<RentalDetail>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recylcer_view_shelf_rent);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ShelfAdapter(rentalDetailList);
        mRecyclerView.setAdapter(mAdapter);

        Log.d("RentShelf", "onCreateView");
        getBooks();
        return view;
    }

    public void getBooks() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        if(user==null){
            Log.d("User","is null");
        }else{
            Log.d("User","is not null");
        }
        String URL = Constants.RENT_BY_ID+user.getUserId();
        Log.d("RentById Url", URL);
//        String URL = Constants.WEB_SERVICE_URL+"genre/all";
        final Gson gson = new Gson();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.i("LOG_VOLLEY", response);
                Log.d("ResponseRent", "inside");
                Log.d("ResponseRent", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                rentalDetailList.addAll(Arrays.asList(gson.fromJson(response, RentalDetail[].class)));
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        });

        requestQueue.add(stringRequest);

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRentClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRentShelfInteractionListener) {
            mListener = (OnRentShelfInteractionListener) context;
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


    public interface OnRentShelfInteractionListener {
        void onRentClick(Uri uri);
    }
}
