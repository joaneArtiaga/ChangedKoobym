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

import com.example.joane14.myapplication.Adapters.UserDayTimeAdapter;
import com.example.joane14.myapplication.Adapters.UserDayTimeUpdateAdapter;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayUserDayTimeUpdate extends Fragment {

    private OnUserDayTimeInteractionListener mListener;
    static User userModel;
    static List<UserDayTime> udtList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public DisplayUserDayTimeUpdate() {
    }

    public static DisplayUserDayTimeUpdate newInstance(List<UserDayTime> udtListModel) {
        udtList = udtListModel;
        DisplayUserDayTimeUpdate fragment = new DisplayUserDayTimeUpdate();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_user_day_time, container, false);

        Log.d("userDayTimeDisplay", "inside");
        List<UserDayTime> userDayTimeList = new ArrayList<UserDayTime>();

        for(int init=0; init<udtList.size(); init++){
            if(udtList.get(init).getDay().getStrDay().equals("Monday")||udtList.get(init).getDay().getStrDay().equals("Tuesday")
                    ||udtList.get(init).getDay().getStrDay().equals("Wednesday")||udtList.get(init).getDay().getStrDay().equals("Thursday")
                    ||udtList.get(init).getDay().getStrDay().equals("Friday")||udtList.get(init).getDay().getStrDay().equals("Saturday")
                    ||udtList.get(init).getDay().getStrDay().equals("Sunday")){
                userDayTimeList.add(udtList.get(init));
            }
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_profile_userdaytime);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new UserDayTimeUpdateAdapter(getActivity(), userDayTimeList);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onUserDayTimeInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserDayTimeInteractionListener) {
            mListener = (OnUserDayTimeInteractionListener) context;
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

    public interface OnUserDayTimeInteractionListener {
        void onUserDayTimeInteraction(Uri uri);
    }
}
