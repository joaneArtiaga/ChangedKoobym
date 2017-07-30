package com.example.joane14.myapplication.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.facebook.internal.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static android.content.ContentValues.TAG;

public class AddProfile extends Fragment {

    private OnFragmentInteractionListener mListener;

    EditText mFirstName, mLastName, mUsername, mAddress, mEmail, mContactNumber, mPassword, mConfirmPassword, mBirthdate;
    Button mSubmit;
    User userModel;
    ImageView slctImage;


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

        slctImage = (ImageView) view.findViewById(R.id.displayPic);
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


        slctImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(mPassword.getText().toString().equals(mConfirmPassword.getText().toString()))){
                    Log.d("Password"+mPassword.getText().toString(), "Confirm Password"+mConfirmPassword.getText().toString());
                    mConfirmPassword.setError("Passwords should match.");
                }
                if(mFirstName.getText().length()==0){
                    mFirstName.setError("Field cannot be left empty.");
                }
                if(mLastName.getText().length()==0){
                    mLastName.setError("Field cannot be left empty.");
                }
                if(mUsername.getText().length()==0){
                    mUsername.setError("Field cannot be left empty.");
                }
                if(mAddress.getText().length()==0){
                    mAddress.setError("Field cannot be left empty.");
                }
                if(mEmail.getText().length()==0){
                    mEmail.setError("Field cannot be left empty.");
                }
                if(mContactNumber.getText().length()==0){
                    mContactNumber.setError("Field cannot be empty.");
                }
                if(mPassword.getText().length()==0){
                    mPassword.setError("Field cannot be empty");
                }
                if(mConfirmPassword.getText().length()==0){
                    mConfirmPassword.setError("Field cannot be empty");
                }
                if(mBirthdate.getText().length()==0){
                    mBirthdate.setError("Field cannot be empty");
                }

                if(mPassword.getText().toString().equals(mConfirmPassword.getText().toString())&&mFirstName.getText().length()!=0&&
                        mLastName.getText().length()!=0&&mUsername.getText().length()!=0&&mAddress.getText().length()!=0&&
                        mEmail.getText().length()!=0&&mContactNumber.getText().length()!=0&&mPassword.getText().length()!=0&&
                        mConfirmPassword.getText().length()!=0&&mBirthdate.getText().length()!=0){
                Log.d("Inside", "add Profile");
                Log.d("Add Profile", "First Name:" + mFirstName.getText().toString());
                Log.d("Add Profile", "Last Name:" + mLastName.getText().toString());
                Log.d("Add Profile", "User Name:" + mUsername.getText().toString());
                Log.d("Add Profile", "Address:" + mAddress.getText().toString());
                Log.d("Add Profile", "Email:" + mEmail.getText().toString());
                Log.d("Add Profile", "Contact Number:" + mContactNumber.getText().toString());
                Log.d("Add Profile", "Password:" + mPassword.getText().toString());

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
        }
        });

        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView
                    slctImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private String getPathFromURI(Uri selectedImageUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
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
