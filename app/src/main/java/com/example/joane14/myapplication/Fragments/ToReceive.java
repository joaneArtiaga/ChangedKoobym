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


public class ToReceive extends Fragment {

    private OnToReceiveInteractionListener mListener;

    public ToReceive() {
    }

    public static ToReceive newInstance() {
        ToReceive fragment = new ToReceive();
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
        View view = inflater.inflate(R.layout.fragment_to_receive3, container, false);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.toReceiveAuction, ToReceiveAuctionFragment.newInstance());
        ft.replace(R.id.toReceiveRent, ToReceiveRentFragment.newInstance());
        ft.replace(R.id.toReceiveSwap, ToReceiveSwapFrag.newInstance());
        ft.commit();

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onToReceiveOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnToReceiveInteractionListener) {
            mListener = (OnToReceiveInteractionListener) context;
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

    public interface OnToReceiveInteractionListener {
        // TODO: Update argument type and name
        void onToReceiveOnClick(Uri uri);
    }
}
