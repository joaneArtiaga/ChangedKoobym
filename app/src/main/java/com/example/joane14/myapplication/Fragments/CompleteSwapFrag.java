package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joane14.myapplication.R;

public class CompleteSwapFrag extends Fragment {

    private OnCompleteSwapInteractionListener mListener;

    public CompleteSwapFrag() {
    }

    public static CompleteSwapFrag newInstance(String param1, String param2) {
        CompleteSwapFrag fragment = new CompleteSwapFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_swap, container, false);


        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCompleteSwapOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCompleteSwapInteractionListener) {
            mListener = (OnCompleteSwapInteractionListener) context;
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

    public interface OnCompleteSwapInteractionListener {
        void onCompleteSwapOnClick(Uri uri);
    }
}
