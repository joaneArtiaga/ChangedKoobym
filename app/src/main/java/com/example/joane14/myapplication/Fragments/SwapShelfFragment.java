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
import com.example.joane14.myapplication.Adapters.ShelfAdapter;
import com.example.joane14.myapplication.Adapters.SwapShelfAdapter;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SwapShelfFragment extends Fragment {

    private OnSwapShelfInteractionListener mListener;
    List<SwapDetail> swapDetailList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public SwapShelfFragment() {
    }


    public static SwapShelfFragment newInstance() {
        SwapShelfFragment fragment = new SwapShelfFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swap_shelf, container, false);

        swapDetailList = new ArrayList<SwapDetail>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recylcer_view_shelf_swap);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SwapShelfAdapter(swapDetailList);
        mRecyclerView.setAdapter(mAdapter);

        Log.d("RentShelf", "onCreateView");
        getMySwapBooks();
        return view;

    }

    public void getMySwapBooks() {
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
                swapDetailList.addAll(Arrays.asList(gson.fromJson(response, SwapDetail[].class)));
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
            mListener.onSwapClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSwapShelfInteractionListener) {
            mListener = (OnSwapShelfInteractionListener) context;
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


    public interface OnSwapShelfInteractionListener {
        void onSwapClick(Uri uri);
    }
}
