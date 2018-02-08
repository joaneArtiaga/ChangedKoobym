package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joane14.myapplication.R;


public class ToDeliver extends Fragment {
    private OnToDeliverInteractionListener mListener;

    public ToDeliver() {
    }

    public static ToDeliver newInstance() {
        ToDeliver fragment = new ToDeliver();
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
        View view = inflater.inflate(R.layout.fragment_to_deliver2, container, false);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.toDeliverSwap, ToDeliverSwapFrag.newInstance());
        ft.replace(R.id.toDeliverAuction, ToDeliverAuctionFragment.newInstance());
        ft.replace(R.id.toDeliverRent, ToDeliverRentFragment.newInstance());
        ft.commit();

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.oToDeliverOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnToDeliverInteractionListener) {
            mListener = (OnToDeliverInteractionListener) context;
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


    public interface OnToDeliverInteractionListener {
        void oToDeliverOnClick(Uri uri);
    }
}
