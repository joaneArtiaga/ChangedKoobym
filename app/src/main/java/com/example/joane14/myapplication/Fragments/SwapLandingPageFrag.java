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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Adapters.LandingPageAdapter;
import com.example.joane14.myapplication.Adapters.SwapLPAdapter;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SwapLandingPageFrag extends Fragment {


    List<SwapDetail> swapDetails;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnSwapLPInteractionListener mListener;

    public SwapLandingPageFrag() {
    }
    public static SwapLandingPageFrag newInstance() {
        SwapLandingPageFrag fragment = new SwapLandingPageFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swap_landing_page, container, false);

        swapDetails = new ArrayList<SwapDetail>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_swapLP);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SwapLPAdapter(swapDetails);
        mRecyclerView.setAdapter(mAdapter);

        getAllSwap();
        return view;
    }

    private void getAllSwap(){
        String URL = Constants.GET_SWAP_DETAILS_ALL;
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/suggested/%d";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Inside get suggested", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                swapDetails.clear();
                swapDetails.addAll(Arrays.asList(gson.fromJson(response, SwapDetail[].class)));
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(getContext()).add(stringRequest);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSwapLPInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSwapLPInteractionListener) {
            mListener = (OnSwapLPInteractionListener) context;
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

    public interface OnSwapLPInteractionListener {
        // TODO: Update argument type and name
        void onSwapLPInteraction(Uri uri);
    }
}
