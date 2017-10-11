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
import com.example.joane14.myapplication.Activities.SwapBookChooser;
import com.example.joane14.myapplication.Adapters.SwapChooserAdapter;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class SwapChooserFrag extends Fragment {

    SwapComment swapCommentModel;
    SwapDetail swapDetailModel;
    SwapHeader swapHeaderModel;
    List<SwapDetail> suggested;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnSwapChooserInteractionListener mListener;

    public SwapChooserFrag() {
    }

    public static SwapChooserFrag newInstance(Bundle bundle) {
        SwapChooserFrag fragment = new SwapChooserFrag();
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
        View view = inflater.inflate(R.layout.fragment_swap_chooser, container, false);

        suggested = new ArrayList<SwapDetail>();


        this.swapCommentModel = (SwapComment) getArguments().getSerializable("swapComment");
        this.swapDetailModel = (SwapDetail) getArguments().getSerializable("swapDetail");
        this.swapHeaderModel = (SwapHeader) getArguments().getSerializable("swapHeader");

        if(swapCommentModel==null){
            Log.d("SwapCommentModel", "is null");
        }else{
            Log.d("SwapCommentModel", "is not null");
        }

        if(swapDetailModel==null){
            Log.d("SwapDetailModel", "is null");
        }else{
            Log.d("SwapDetailModel", "is not null");
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_swap_chooser);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SwapChooserAdapter(suggested,swapHeaderModel);
        mRecyclerView.setAdapter(mAdapter);


        getRecommendSwap();



        return view;
    }

    public void getRecommendSwap(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        String URL = Constants.RECOMMEND_SWAP_BOOK+"/"+swapCommentModel.getUser().getUserId()+"/"+swapDetailModel.getSwapPrice();

        Log.d("RecommendSwap Url", URL);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapDetailModel);


        Log.d("LOG_VOLLEY", mRequestBody);
        Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("SwapDetailResponse", response);
                if(response.equals("")){
                    Log.d("Available", "No books for swap");
                }else{
                    Log.d("Available","books for swap");
                }
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                suggested.clear();
                suggested.addAll(Arrays.asList(gson.fromJson(response, SwapDetail[].class)));
                mAdapter.notifyDataSetChanged();
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSwapChooserOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSwapChooserInteractionListener) {
            mListener = (OnSwapChooserInteractionListener) context;
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


    public interface OnSwapChooserInteractionListener {
        void onSwapChooserOnClick(Uri uri);
    }
}
