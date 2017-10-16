package com.example.joane14.myapplication.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.joane14.myapplication.Activities.HistoryActivity;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class AddFbUser extends Fragment {

    private OnAddFbUserInteractionListener mListener;
    EditText mFirstName, mLastName, mUsername, mAddress, mEmail, mContactNumber, mBirthdate;
    User userModel;
    ImageView slctImage;
    ImageView imageView;
    String filename;
    private DatePicker datePicker;
    private Calendar calendar;
    DatePickerDialog.OnDateSetListener date;

    public AddFbUser() {
    }

    public static AddFbUser newInstance() {
        AddFbUser fragment = new AddFbUser();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_fb_user, container, false);

        calendar = Calendar.getInstance();

        Log.d("Inside", "onCreateView");

        userModel = new User();

//        String filename = "123-1501684832903Screenshot_20170802-014107.jpg";

        slctImage = (ImageView) view.findViewById(R.id.displayPic);
//        Picasso.with(getContext()).load(String.format(Constants.IMAGE_URL, filename)).fit().into(slctImage);
        Button mNextAdd = (Button) view.findViewById(R.id.btnNextAdd);


        userModel = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);

        if(userModel==null){
            Log.d("nullDiay", "o null jud");
        }else{
            Log.d("nullDiay", "dili man");
        }

        mFirstName = (EditText) view.findViewById(R.id.firstName);
        mLastName = (EditText) view.findViewById(R.id.lastName);
        mUsername = (EditText) view.findViewById(R.id.username);
        mAddress = (EditText) view.findViewById(R.id.address);
        mEmail = (EditText) view.findViewById(R.id.email);
        mContactNumber = (EditText) view.findViewById(R.id.contactNumber);
        this.mBirthdate = (EditText) view.findViewById(R.id.birthDate);

        Picasso.with(getContext()).load(userModel.getImageFilename()).fit().into(slctImage);

        mFirstName.setEnabled(false);
        mFirstName.setText(userModel.getUserFname());
        mLastName.setEnabled(false);
        mLastName.setText(userModel.getUserLname());
        mEmail.setEnabled(false);
        mEmail.setText(userModel.getEmail());

        date = new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("NewApi")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        mBirthdate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                new DatePickerDialog(getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        mNextAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                if (mFirstName.getText().length() == 0) {
                    mFirstName.setError("Field cannot be left empty.");
                }
                if (mLastName.getText().length() == 0) {
                    mLastName.setError("Field cannot be left empty.");
                }
                if (mUsername.getText().length() == 0) {
                    mUsername.setError("Field cannot be left empty.");
                }
                if (mAddress.getText().length() == 0) {
                    mAddress.setError("Field cannot be left empty.");
                }
                if (mEmail.getText().length() == 0) {
                    mEmail.setError("Field cannot be left empty.");
                }
                if (mContactNumber.getText().length() == 0) {
                    mContactNumber.setError("Field cannot be empty.");
                }

                if (mBirthdate.getText().length() == 0) {
                    mBirthdate.setError("Field cannot be empty");
                }

                if (mFirstName.getText().length() != 0 &&
                    mLastName.getText().length() != 0 && mUsername.getText().length() != 0 && mAddress.getText().length() != 0 &&
                    mEmail.getText().length() != 0 && mContactNumber.getText().length() != 0 && mBirthdate.getText().length() != 0) {
                    Log.d("Inside", "add Profile");
                    Log.d("Add Profile", "First Name:" + mFirstName.getText().toString());
                    Log.d("Add Profile", "Last Name:" + mLastName.getText().toString());
                    Log.d("Add Profile", "User Name:" + mUsername.getText().toString());
                    Log.d("Add Profile", "Address:" + mAddress.getText().toString());
                    Log.d("Add Profile", "Email:" + mEmail.getText().toString());
                    Log.d("Add Profile", "Contact Number:" + mContactNumber.getText().toString());



                    userModel.setUsername(mUsername.getText().toString());
                    userModel.setAddress(mAddress.getText().toString());
                    userModel.setEmail(mEmail.getText().toString());
                    userModel.setPhoneNumber(mContactNumber.getText().toString());
                    userModel.setPassword(" ");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = mBirthdate.getText().toString();
                    try {
                        userModel.setBirthdate(dateFormat.parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }

                    mListener.OnAddFbUser(userModel);

                }
            }
        });


        return view;
    }

    @SuppressLint("NewApi")
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        @SuppressLint({"NewApi", "LocalSuppress"})
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mBirthdate.setText(sdf.format(calendar.getTime()));
    }


    public void onButtonPressed(User user) {
        if (mListener != null) {
            mListener.OnAddFbUser(user);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddFbUserInteractionListener) {
            mListener = (OnAddFbUserInteractionListener) context;
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

    public interface OnAddFbUserInteractionListener {
        void OnAddFbUser(User user);
    }
}
