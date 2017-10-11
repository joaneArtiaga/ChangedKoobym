package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joane14.myapplication.R;


public class ToReceiveSwap extends Fragment {
    private OnToReceiveSwapInteractionListener mListener;

    public ToReceiveSwap() {
    }

    public static ToReceiveSwap newInstance(String param1, String param2) {
        ToReceiveSwap fragment = new ToReceiveSwap();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_receive_swap2, container, false);


        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onToReceiveSwapOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnToReceiveSwapInteractionListener) {
            mListener = (OnToReceiveSwapInteractionListener) context;
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

    public interface OnToReceiveSwapInteractionListener {
        void onToReceiveSwapOnClick(Uri uri);
    }
}
