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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Adapters.PrefferedAdapter;
import com.example.joane14.myapplication.Adapters.SearchAdapter;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SearchFragmentResult extends Fragment {

    List<BookOwnerModel> bookOwnerModelList;
    private GridView mGridView;
    private SearchAdapter mAdapter;

    private OnSearchListener mListener;

    public SearchFragmentResult() {
    }


    public static SearchFragmentResult newInstance(Bundle bundle) {
        SearchFragmentResult fragment = new SearchFragmentResult();
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
        View view = inflater.inflate(R.layout.fragment_search_fragment_result, container, false);

        String keyword = getArguments().getString("SearchKeyword");

        TextView mHeader = (TextView) view.findViewById(R.id.searchTitle);
        bookOwnerModelList = new ArrayList<BookOwnerModel>();

        mHeader.setText("Result of '"+keyword+"'");
        mGridView = (GridView) view.findViewById(R.id.search_gridview);
        mAdapter = new SearchAdapter(getContext(), bookOwnerModelList);
        mGridView.setAdapter(mAdapter);

        getSuggested(keyword);

        return view;
    }

    private void getSuggested(String keyword){
//        String URL = "http://104.198.152.85/Koobym/rentalDetail/suggested/%d";
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/suggested/%d";
        String URL = Constants.GET_SEARCH_RESULT+keyword;
        Log.d("SearchURL", URL);
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
            mListener.onSearchOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchListener) {
            mListener = (OnSearchListener) context;
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


    public interface OnSearchListener {
        void onSearchOnClick(Uri uri);
    }
}
