package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;

public class AboutProfFrag extends Fragment {

    private OnAboutProfInteractionListener mListener;
    User user;

    public AboutProfFrag() {
    }

    public static AboutProfFrag newInstance(Bundle bundle) {
        AboutProfFrag fragment = new AboutProfFrag();
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
        View view = inflater.inflate(R.layout.fragment_about_prof, container, false);

        user = (User) getArguments().getSerializable("user");

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAboutProfOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAboutProfInteractionListener) {
            mListener = (OnAboutProfInteractionListener) context;
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

    public interface OnAboutProfInteractionListener {
        void onAboutProfOnClick(Uri uri);
    }
}
