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

import com.example.joane14.myapplication.Adapters.LandingPageAdapter;
import com.example.joane14.myapplication.Adapters.SwapCommentsAdapter;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DisplaySwapComments extends Fragment {

    private OnSwapCommentInteractionListener mListener;
    List<SwapComment> suggested;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwapDetail swapDetailObj;

    public DisplaySwapComments() {
    }


    public static DisplaySwapComments newInstance(Bundle args) {
        DisplaySwapComments fragment = new DisplaySwapComments();
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
        View view = inflater.inflate(R.layout.fragment_display_swap_comments, container, false);

        swapDetailObj = new SwapDetail();
        this.swapDetailObj = (SwapDetail) getArguments().getSerializable("swapComment");
        Log.d("swapComment", swapDetailObj.toString());
        suggested = swapDetailObj.getSwapComments();



        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_book_swap);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SwapCommentsAdapter(suggested);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSwapCommentOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSwapCommentInteractionListener) {
            mListener = (OnSwapCommentInteractionListener) context;
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

    public interface OnSwapCommentInteractionListener {
        void onSwapCommentOnClick(Uri uri);
    }
}
