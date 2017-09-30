package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userObj = new User();
            this.userObj = (User) getArguments().getSerializable("userDetails");
            Log.d("onCreate profFrag", userObj.toString());
        Log.d("inside", "onCreate profFrag");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.d("inside", "onCreateview profFrag");

        this.userObj = (User) getArguments().getSerializable("userDetails");
        Log.d("onCreateView profFrag", userObj.toString());

        TextView mName = (TextView) view.findViewById(R.id.tvName);
        TextView mEmail = (TextView) view.findViewById(R.id.tvEmailProfile);
        TextView mAddress = (TextView) view.findViewById(R.id.tvAddress);
        TextView mBirthDate = (TextView) view.findViewById(R.id.tvBirthDate);
        TextView mPhone = (TextView) view.findViewById(R.id.tvPhoneNumber);

        User userModel = new User();

        userModel = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);


        mEmail.setText(userModel.getEmail());
        mPhone.setText(userModel.getPhoneNumber());
        mBirthDate.setText(userModel.getBirthdate().toString());
        mAddress.setText(userModel.getAddress());
        ImageView profileImg = (ImageView) view.findViewById(R.id.profileDisplayPic);
        mBtnAdd = (FloatingActionButton) view.findViewById(R.id.btnAdd);

        mName.setText(userModel.getUserFname()+" "+ userModel.getUserLname());
//        mEmail.setText();
//        Picasso.with(getContext()).load(String.format(Constants.IMAGE_URL, userObj.getImageFilename())).fit().into(profileImg);
        Glide.with(getContext()).load(userModel.getImageFilename()).centerCrop().into(profileImg);


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
//                        //What ever you want to do with the value
//                        Editable YouEditTextValue = edittext.getText();
//                        //OR
//                        String YouEditTextValue
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onAddBookSelected(String keyword);
    }

}
