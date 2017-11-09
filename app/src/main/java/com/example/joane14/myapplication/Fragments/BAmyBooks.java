package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.joane14.myapplication.Adapters.BookActivityMyBooksAdapter;
import com.example.joane14.myapplication.Adapters.BookActivityRequestAdapter;
import com.example.joane14.myapplication.Model.BookActivity;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class BAmyBooks extends Fragment {

    private GridView mGridView;
    private List<BookActivity> bookActivityList;
    private BookActivityMyBooksAdapter mAdapter;

    private OnMyBookListener mListener;

    public BAmyBooks() {
    }


    public static BAmyBooks newInstance() {
        BAmyBooks fragment = new BAmyBooks();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bamy_books, container, false);

        bookActivityList = new ArrayList<BookActivity>();

        User user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        mGridView = (GridView) view.findViewById(R.id.my_books_gridview);
        mAdapter = new BookActivityMyBooksAdapter(getContext(), bookActivityList);
        mGridView.setAdapter(mAdapter);

        getSuggested(user.getUserId());


        return view;
    }

    private void getSuggested(int userId){
//        String URL = "http://104.198.152.85/Koobym/rentalDetail/suggested/%d";
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/suggested/%d";
        String URL = Constants.GET_BOOK_ACTIVITY_MY_BOOKS+userId;
        URL = String.format(URL, userId);
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Inside get suggested", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                BookActivity[] bookActivities = gson.fromJson(response, BookActivity[].class);
                bookActivityList.addAll(Arrays.asList(bookActivities));
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
            mListener.onMyBookOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMyBookListener) {
            mListener = (OnMyBookListener) context;
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


    public interface OnMyBookListener {
        void onMyBookOnClick(Uri uri);
    }
}
