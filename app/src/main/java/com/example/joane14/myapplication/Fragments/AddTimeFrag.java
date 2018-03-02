package com.example.joane14.myapplication.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.joane14.myapplication.Adapters.TimeDayAdapter;
import com.example.joane14.myapplication.Model.DayModel;
import com.example.joane14.myapplication.Model.DayTimeModel;
import com.example.joane14.myapplication.Model.MeetUpLocObj;
import com.example.joane14.myapplication.Model.TimeModel;
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
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    Button mNext, mAddTime;
    DayModel dayModel;
    TimeModel timeModel;
    UserDayTime userDayTime;
    EditText etTimeFrom, etTimeTo;
    TextView tvRemind;
    String daySelect;

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

        RecyclerView layoutFrag = (RecyclerView) view.findViewById(R.id.recycler_view_timeDay);

        bundle = new Bundle();
        bundle = getArguments();
        dayTimeModelArrayList = new ArrayList<DayTimeModel>();
        userDayTimeList = new ArrayList<UserDayTime>();
        mNext = (Button) view.findViewById(R.id.btnNextTime);
        mAddTime = (Button) view.findViewById(R.id.btnAddTime);
        tvRemind = (TextView) view.findViewById(R.id.remindTime);

        if(userDayTimeList.size()==0){
        }else{
            tvRemind.setVisibility(View.GONE);
        }

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

        mAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog();
            }
        });


        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddTimeClickListener(userDayTimeList);
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

        mAdapter = new TimeDayAdapter(userDayTimeList);
        recyclerView.setAdapter(mAdapter);

        Log.d("selectedDays", selectedDays.toString());
        return view;

    }

    public void customDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.time_date_dialog);
        dialog.setTitle("Choose Time and Day");
        final DayModel day;
        final TimeModel time;
        daySelect="";


        day = new DayModel();
        time = new TimeModel();

        // set the custom dialog components - text, image and button
        etTimeFrom = (EditText) dialog.findViewById(R.id.tcFrom);
        etTimeTo = (EditText) dialog.findViewById(R.id.tcTo);
        Button mBtnOkay = (Button) dialog.findViewById(R.id.btnOkay);
        Button mBtnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Spinner mSpinnerDay = (Spinner) dialog.findViewById(R.id.spinnerDay);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, selectedDays);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        if(adapter==null){
            Log.d("apater", "is null");
        }else{
            Log.d("apater", "is not null");
        }
        mSpinnerDay.setAdapter(adapter);

        mSpinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                daySelect = selectedDays.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTimePicker(0);
            }
        });
        etTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTimePicker(1);
            }
        });

        mBtnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDayTime udt = new UserDayTime();
                TimeModel time = new TimeModel();
                DayModel day = new DayModel();
                time.setStrTime(etTimeTo.getText().toString()+" - "+etTimeFrom.getText().toString());
                day.setStrDay(daySelect);
                udt.setDay(day);
                udt.setTime(time);
                userDayTimeList.add(udt);
                mAdapter.notifyDataSetChanged();
                tvRemind.setVisibility(View.GONE);
                dialog.cancel();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void CreateTimePicker(final int pos){

        userDayTime = new UserDayTime();

        final java.util.Calendar c = java.util.Calendar.getInstance();
        int mHour = c.get(java.util.Calendar.HOUR_OF_DAY);
        int mMinute = c.get(java.util.Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        int hour = hourOfDay;
                        int minutes = minute;
                        String timeSet = "";
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes ;
                        else
                            min = String.valueOf(minutes);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(hour).append(':')
                                .append(min ).append(" ").append(timeSet).toString();

                        String timeGiven = "";
                        timeGiven = hourOfDay + ":" + minute;
                        Log.d("time selected", timeGiven);

                        if(pos==0){
                            etTimeFrom.setText(aTime);
                        }else{
                            etTimeTo.setText(aTime);
                        }

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    public void onButtonPressed(List<UserDayTime> listDayTimeModel) {
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
        void onAddTimeClickListener(List<UserDayTime> userDayTimeList);
    }
}
