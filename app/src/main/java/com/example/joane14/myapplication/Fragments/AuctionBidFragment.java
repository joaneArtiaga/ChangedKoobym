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

import com.example.joane14.myapplication.Adapters.AuctionCommentsAdapter;
import com.example.joane14.myapplication.Adapters.SwapCommentsAdapter;
import com.example.joane14.myapplication.Model.AuctionComment;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.AuctionHeader;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.List;


public class AuctionBidFragment extends Fragment {

    private OnAuctionBidInteractionListener mListener;
    List<AuctionComment> suggested;
    AuctionHeader auctionHeader;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    AuctionDetailModel auctionDetailModel;


    public AuctionBidFragment() {
    }

    public static AuctionBidFragment newInstance(Bundle args) {
        AuctionBidFragment fragment = new AuctionBidFragment();
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
        View view = inflater.inflate(R.layout.fragment_auction_bid, container, false);

        auctionDetailModel = new AuctionDetailModel();
        this.auctionDetailModel = (AuctionDetailModel) getArguments().getSerializable("auctionComment");

        suggested = new ArrayList<AuctionComment>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_book_auction);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AuctionCommentsAdapter(suggested, auctionDetailModel);
        mRecyclerView.setAdapter(mAdapter);



        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAuctionBidClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAuctionBidInteractionListener) {
            mListener = (OnAuctionBidInteractionListener) context;
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

    public interface OnAuctionBidInteractionListener {
        void onAuctionBidClick(Uri uri);
    }
}
