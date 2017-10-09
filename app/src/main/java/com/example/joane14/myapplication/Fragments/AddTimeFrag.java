package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.joane14.myapplication.Activities.SignUp;
import com.example.joane14.myapplication.Adapters.TimeDayAdapter;
import com.example.joane14.myapplication.Model.DayTimeModel;
import com.example.joane14.myapplication.Model.MeetUpLocObj;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AddTimeFrag extends Fragment{

    private OnAddTimeInteractionListener mListener;

    Bundle bundle;
    ArrayList<String> selectedDays;
    ArrayList<DayTimeModel> dayTimeModelArrayList;
    List<UserDayTime> userDayTimeList;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    Button mNext;

    public AddTimeFrag() {
    }

    public static AddTimeFrag newInstance() {
        AddTimeFrag fragment = new AddTimeFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("inside onCreate", "AddTimeFrag");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_time, container, false);


        bundle = new Bundle();
        bundle = getArguments();
        dayTimeModelArrayList = new ArrayList<DayTimeModel>();
        userDayTimeList = new ArrayList<UserDayTime>();
        mNext = (Button) view.findViewById(R.id.btnNextTime);

        MeetUpLocObj meetUpLocObj = new MeetUpLocObj();
        meetUpLocObj = (MeetUpLocObj) bundle.getSerializable("meetUpLocObj");
        selectedDays = (ArrayList<String>) meetUpLocObj.getItemSelected();

        if(meetUpLocObj==null){
            Log.d("meetUpLocObj", "null");
        }else{
            Log.d("meetUpLocObj", "not null");
        }

        if(selectedDays.isEmpty()){
            Log.d("selectedDays", "null");
        }else{
            Log.d("selectedDays", "not null");

        }

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddTimeClickListener(dayTimeModelArrayList);
                Log.d("dayTimeModelArrayList", dayTimeModelArrayList.toString());
                Log.d("inside onclickListener", "mNext Button");
            }
        });

        for(int init=0; init<selectedDays.size(); init++){
            DayTimeModel dayTimeModel = new DayTimeModel();
            dayTimeModel.setDay(String.valueOf(selectedDays.get(init)));
            dayTimeModelArrayList.add(dayTimeModel);

        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_timeDay);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(dayTimeModelArrayList.isEmpty()){
            Log.d("dayTimeModelArrayList", "first is empty");
        }else{
            Log.d("dayTimeModelArrayList", "first is not empty");

        }

        adapter = new TimeDayAdapter(dayTimeModelArrayList, getContext());
        recyclerView.setAdapter(adapter);

        Log.d("selectedDays", selectedDays.toString());
        return view;

    }

    public void onButtonPressed(List<DayTimeModel> listDayTimeModel) {
        if (mListener != null) {
            mListener.onAddTimeClickListener(listDayTimeModel);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddTimeInteractionListener) {
            mListener = (OnAddTimeInteractionListener) context;
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

    public interface OnAddTimeInteractionListener {
        void onAddTimeClickListener(List<DayTimeModel> listDayTimeModel);
    }
}
