package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.ViewBookAct;
import com.example.joane14.myapplication.Adapters.DisplayBooksAdapter;
import com.example.joane14.myapplication.Adapters.LandingPageAdapter;
import com.example.joane14.myapplication.Adapters.PrefferedAdapter;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.GenreModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PreferencesFrag extends Fragment {

    List<BookOwnerModel> bookOwnerModelList;
    private GridView mGridView;
    private PrefferedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    User userObj;

    private OnFragmentInteractionListener mListener;

    public PreferencesFrag() {
        // Required empty public constructor
    }

    public static PreferencesFrag newInstance(Bundle args) {
        PreferencesFrag fragment = new PreferencesFrag();
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
        View view = inflater.inflate(R.layout.fragment_preferences, container, false);
        Log.d("preFrag", "inside");

        userObj = new User();
        this.userObj = (User) getArguments().getSerializable("userModelPass");
        Log.d("userModelPass PreFrag", userObj.toString());
        bookOwnerModelList = new ArrayList<BookOwnerModel>();

        mGridView = (GridView) view.findViewById(R.id.preffered_gridview);
        mAdapter = new PrefferedAdapter(getContext(), bookOwnerModelList);
        mGridView.setAdapter(mAdapter);
        Log.d("userId$#23", String.valueOf(userObj.getUserId()));
        getSuggested(userObj.getUserId());

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ClickedView", bookOwnerModelList.get(position).getBookObj().getBookTitle());
                if(bookOwnerModelList.get(position).getStatus().equals("Swap")){
                    getSwapDetail(bookOwnerModelList.get(position).getBookOwnerId());
                }else{
                    getRentalDetail(bookOwnerModelList.get(position).getBookOwnerId());
                }
//                Intent intent = new Intent(getContext(), ViewBookAct.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("viewBook",bookOwnerModelList.get(position));
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });

        return view;
    }

    private void getSwapDetail(int bookOwnerId){
//        String URL = "http://104.198.152.85/Koobym/rentalDetail/suggested/%d";
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/suggested/%d";
//        String URL = Constants.GET_BOOK_OWNER_SWAP_DETAIL+bookOwnerId;
        String URL = Constants.GET_BOOK_OWNER_SWAP_DETAIL+bookOwnerId;
//        URL = String.format(URL, userId);
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SwapDetailResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                SwapDetail swapDetails = gson.fromJson(response, SwapDetail.class);
                Intent intent = new Intent(getContext(), ViewBookAct.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("swapBook",swapDetails);
                intent.putExtras(bundle);
                startActivity(intent);
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
//                BookOwnerModel[] bookOwnerModels = gson.fromJson(response, BookOwnerModel[].class);
//                bookOwnerModelList.addAll(Arrays.asList(bookOwnerModels));
//                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(getContext()).add(stringRequest);
    }

    private void getRentalDetail(int bookOwnerId){
//        String URL = "http://104.198.152.85/Koobym/rentalDetail/suggested/%d";
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/suggested/%d";
        String URL = Constants.GET_BOOK_OWNER_RENTAL_DETAIL+bookOwnerId;
//        URL = String.format(URL, userId);
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RentalDetailResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                RentalDetail rentalDetails = gson.fromJson(response, RentalDetail.class);
                Intent intent = new Intent(getContext(), ViewBookAct.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("viewBook",rentalDetails);
                intent.putExtras(bundle);
                startActivity(intent);
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
//                BookOwnerModel[] bookOwnerModels = gson.fromJson(response, BookOwnerModel[].class);
//                bookOwnerModelList.addAll(Arrays.asList(bookOwnerModels));
//                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(getContext()).add(stringRequest);
    }

    private void getSuggested(int userId){
//        String URL = "http://104.198.152.85/Koobym/rentalDetail/suggested/%d";
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/suggested/%d";
        String URL = Constants.GET_RECOMMENDATION+userId;
        URL = String.format(URL, userId);
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Inside get suggested", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                BookOwnerModel[] bookOwnerModels = gson.fromJson(response, BookOwnerModel[].class);
                bookOwnerModelList.addAll(Arrays.asList(bookOwnerModels));
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
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
