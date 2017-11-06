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
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Adapters.LandingPageAdapter;
import com.example.joane14.myapplication.Adapters.MostRentedAdapter;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MostRentedBookFrag extends Fragment {

    private GridView mGridView;
    private MostRentedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    List<RentalDetail> forRents;

    private OnFragmentInteractionListener mListener;

    public MostRentedBookFrag() {
        // Required empty public constructor
    }


    public static MostRentedBookFrag newInstance() {
        MostRentedBookFrag fragment = new MostRentedBookFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




    private void getMostRented(){
        String URL = "http://192.168.1.7:8080/Koobym/rentalDetail/mostRented";
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/mostRented";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Inside get most rented", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                forRents.clear();
                forRents.addAll(Arrays.asList(gson.fromJson(response, RentalDetail[].class)));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_most_rented_book, container, false);

        forRents = new ArrayList<RentalDetail>();

        mGridView = (GridView) view.findViewById(R.id.most_rented_gridview);
        mAdapter = new MostRentedAdapter(getContext(), forRents);
        mGridView.setAdapter(mAdapter);
        getMostRented();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMostRentedListener(uri);
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMostRentedListener(Uri uri);
    }
}
