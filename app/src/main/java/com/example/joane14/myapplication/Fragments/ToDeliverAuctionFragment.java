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
import com.example.joane14.myapplication.Adapters.ToDeliverAuctionAdapter;
import com.example.joane14.myapplication.Adapters.ToDeliverRentAdapter;
import com.example.joane14.myapplication.Model.AuctionHeader;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class ToDeliverAuctionFragment extends Fragment {

    private OnToDeliverAuctionInteractionListener mListener;
    List<AuctionHeader> auctionHeaderList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ToDeliverAuctionFragment() {
    }

    public static ToDeliverAuctionFragment newInstance() {
        ToDeliverAuctionFragment fragment = new ToDeliverAuctionFragment();
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
        View view = inflater.inflate(R.layout.fragment_to_deliver_auction, container, false);

        Log.d("TodeliverAuction", "inside");

        auctionHeaderList = new ArrayList<AuctionHeader>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_to_deliver_auction);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ToDeliverAuctionAdapter(auctionHeaderList);
        mRecyclerView.setAdapter(mAdapter);

        getDeliveries();

        return view;
    }

    public void getDeliveries(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        Log.d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.TO_DELIVER_AUCTION+user.getUserId();
        Log.d("UserIdURL", URL);

        final RentalHeader rentalHeader =new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseRequestReceived", response);
//                RentalHeader rentalHeaderModel = gson.fromJson(response, RentalHeader.class);
                auctionHeaderList.clear();
                auctionHeaderList.addAll(Arrays.asList(gson.fromJson(response, AuctionHeader[].class)));
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
            mListener.onToDeliverAuctionOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnToDeliverAuctionInteractionListener) {
            mListener = (OnToDeliverAuctionInteractionListener) context;
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

    public interface OnToDeliverAuctionInteractionListener {
        void onToDeliverAuctionOnClick(Uri uri);
    }
}
