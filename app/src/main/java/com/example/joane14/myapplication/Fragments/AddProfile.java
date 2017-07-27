package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddProfile extends Fragment {

    private OnFragmentInteractionListener mListener;

    EditText mFirstName, mLastName, mUsername, mAddress, mEmail, mContactNumber, mPassword, mConfirmPassword, mBirthdate;
    Button mSubmit;
    User userModel;

    public AddProfile() {

    }


    public static AddProfile newInstance() {
        AddProfile fragment = new AddProfile();
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
        View view = inflater.inflate(R.layout.fragment_add_profile, container, false);

        Log.d("Inside", "onCreateView");

        userModel = new User();

        mSubmit = (Button) view.findViewById(R.id.btnSubmit);

        mFirstName = (EditText) view.findViewById(R.id.firstName);
        mLastName = (EditText) view.findViewById(R.id.lastName);
        mUsername = (EditText) view.findViewById(R.id.username);
        mAddress = (EditText) view.findViewById(R.id.address);
        mEmail = (EditText) view.findViewById(R.id.email);
        mContactNumber = (EditText) view.findViewById(R.id.contactNumber);
        mPassword = (EditText) view.findViewById(R.id.password);
        mConfirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        mBirthdate= (EditText) view.findViewById(R.id.birthDate);


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Inside", "add Profile");
                    Log.d("Add Profile", "First Name:"+mFirstName.getText().toString());
                    Log.d("Add Profile", "Last Name:"+mLastName.getText().toString());
                    Log.d("Add Profile", "User Name:"+mUsername.getText().toString());
                    Log.d("Add Profile", "Address:"+mAddress.getText().toString());
                    Log.d("Add Profile", "Email:"+mEmail.getText().toString());
                    Log.d("Add Profile", "Contact Number:"+mContactNumber.getText().toString());
                    Log.d("Add Profile", "Password:"+mPassword.getText().toString());

                userModel.setUserFname(mFirstName.getText().toString());
                userModel.setUserLname(mLastName.getText().toString());
                userModel.setUsername(mUsername.getText().toString());
                userModel.setAddress(mAddress.getText().toString());
                userModel.setEmail(mEmail.getText().toString());
                userModel.setPhoneNumber(mContactNumber.getText().toString());
                userModel.setPassword(mPassword.getText().toString());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = mBirthdate.getText().toString();
                try {
                    userModel.setBirthdate(dateFormat.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mListener.onUserSelected(userModel);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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


    public interface OnFragmentInteractionListener {
        void onUserSelected(User user);
    }
}
