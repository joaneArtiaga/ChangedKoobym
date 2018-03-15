package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CountdownFrag extends Fragment {

    TextView txtTimerDay, txtTimerHour, txtTimerMinute, txtTimerSec, tvEvent;
    Handler handler;
    Runnable runnable;
    AuctionDetailModel auctionDetailModel;

    private OnCountdownInteractionListener mListener;

    public CountdownFrag() {
    }

    public static CountdownFrag newInstance(Bundle bundle) {
        CountdownFrag fragment = new CountdownFrag();
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
        View view = inflater.inflate(R.layout.fragment_countdown, container, false);

        auctionDetailModel = (AuctionDetailModel) getArguments().getSerializable("auctionBook");
        if(auctionDetailModel!=null){
            Log.d("auctionCountdown", "not empty");
        }else{
            Log.d("auctionCountdown", "empty");
        }

        Log.d("Countdown", "inside");
        txtTimerDay = (TextView) view.findViewById(R.id.txtTimerDay);
        txtTimerHour = (TextView) view.findViewById(R.id.txtTimerHour);
        txtTimerMinute = (TextView) view.findViewById(R.id.txtTimerMinute);
        txtTimerSec = (TextView) view.findViewById(R.id.txtTimerSecond);


        countDownStart();

        return view;
    }

    public void countDownStart() {
        final String dateEnder = auctionDetailModel.getEndDate();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd");
                    // Please here set your event date//YYYY-MM-DD
                    Date futureDate = dateFormat.parse(dateEnder);
                    Log.d("dateEnder", dateEnder);
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        txtTimerDay.setText("" + String.format("%02d", days));
                        txtTimerHour.setText("" + String.format("%02d", hours));
                        txtTimerMinute.setText(""
                                + String.format("%02d", minutes));
                        txtTimerSec.setText(""
                                + String.format("%02d", seconds));
                    } else {
                        tvEvent.setVisibility(View.VISIBLE);
                        tvEvent.setText("The event started!");
                        textViewGone();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
    }

    public void textViewGone() {
        getView().findViewById(R.id.LinearLayout10).setVisibility(View.GONE);
        getView().findViewById(R.id.LinearLayout11).setVisibility(View.GONE);
        getView().findViewById(R.id.LinearLayout12).setVisibility(View.GONE);
        getView().findViewById(R.id.LinearLayout13).setVisibility(View.GONE);
        getView().findViewById(R.id.textView1).setVisibility(View.GONE);
        getView().findViewById(R.id.textView2).setVisibility(View.GONE);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCountdownOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCountdownInteractionListener) {
            mListener = (OnCountdownInteractionListener) context;
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

    public interface OnCountdownInteractionListener {
        // TODO: Update argument type and name
        void onCountdownOnClick(Uri uri);
    }
}
