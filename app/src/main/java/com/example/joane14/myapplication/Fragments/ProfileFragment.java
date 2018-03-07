package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.ProfileActivity;
import com.example.joane14.myapplication.Activities.UpdateProfileActivity;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    FloatingActionButton mBtnAdd;
    String titleKeyword;
    User userObj;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
    }


    public static ProfileFragment newInstance(Bundle bundle) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userObj = new User();
        userObj = (User) getArguments().getSerializable("userDetails");

        if(userObj==null){
            Log.d("userObj", "null");
        }else{
            Log.d("userObj", "not null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.d("inside", "onCreateview profFrag");

        this.userObj = (User) getArguments().getSerializable("userDetails");
        Log.d("onCreateView profFrag", userObj.toString());

        TextView mName = (TextView) view.findViewById(R.id.tvName);
        TextView mBtnEdit = (TextView) view.findViewById(R.id.tvEditProfile);
        TextView mEmail = (TextView) view.findViewById(R.id.tvEmailProfile);

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", userObj);
        FragmentManager fragmentManager = getFragmentManager();
        ProfileFrag mrbf = ProfileFrag.newInstance(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_prof, mrbf);
        fragmentTransaction.commit();

        User userModel = new User();

        mEmail.setText(userObj.getEmail());
        ImageView profileImg = (ImageView) view.findViewById(R.id.profIvProf);
        mBtnAdd = (FloatingActionButton) view.findViewById(R.id.btnAdd);

        mName.setText(userObj.getUserFname()+" "+ userObj.getUserLname());
        Picasso.with(getContext()).load(userObj.getImageFilename()).fit().into(profileImg);

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Inside", "Floating Action listener");

                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                final EditText edittext = new EditText(getContext());
                edittext.setHint("Book Title");
                alert.setTitle("Enter Your Title of the Book");

                alert.setView(edittext);

                alert.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d("Inside", "onClickPositiveButton");
                        titleKeyword = edittext.getText().toString();
                        Log.d("Title Keyword", titleKeyword);

                        mListener.onAddBookSelected(titleKeyword);

                    }
                });

                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d("Inside", "onClickNegativeButton");
                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });

        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void onButtonPressed(String keyword) {
        if (mListener != null) {
            mListener.onAddBookSelected(keyword);
        }
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

        void onAddBookSelected(String keyword);
    }

}
