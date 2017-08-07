package com.example.joane14.myapplication.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class AddProfile extends Fragment {

    private OnFragmentInteractionListener mListener;

    EditText mFirstName, mLastName, mUsername, mAddress, mEmail, mContactNumber, mPassword, mConfirmPassword, mBirthdate;
    Button mNextAdd;
    User userModel;
    ImageView slctImage;
    Calendar myCalendar;
    String filename;


    DatePickerDialog.OnDateSetListener date;


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

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

//        String filename = "123-1501684832903Screenshot_20170802-014107.jpg";

        slctImage = (ImageView) view.findViewById(R.id.displayPic);
//        Picasso.with(getContext()).load(String.format(Constants.IMAGE_URL, filename)).fit().into(slctImage);
        mNextAdd = (Button) view.findViewById(R.id.btnNextAdd);

        mFirstName = (EditText) view.findViewById(R.id.firstName);
        mLastName = (EditText) view.findViewById(R.id.lastName);
        mUsername = (EditText) view.findViewById(R.id.username);
        mAddress = (EditText) view.findViewById(R.id.address);
        mEmail = (EditText) view.findViewById(R.id.email);
        mContactNumber = (EditText) view.findViewById(R.id.contactNumber);
        mPassword = (EditText) view.findViewById(R.id.password);
        mConfirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        mBirthdate = (EditText) view.findViewById(R.id.birthDate);

        mBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        slctImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });
        mNextAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(mPassword.getText().toString().equals(mConfirmPassword.getText().toString()))) {
                    Log.d("Password" + mPassword.getText().toString(), "Confirm Password" + mConfirmPassword.getText().toString());
                    mConfirmPassword.setError("Passwords should match.");
                }
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
                if (mPassword.getText().length() == 0) {
                    mPassword.setError("Field cannot be empty");
                }
                if (mConfirmPassword.getText().length() == 0) {
                    mConfirmPassword.setError("Field cannot be empty");
                }
                if (mBirthdate.getText().length() == 0) {
                    mBirthdate.setError("Field cannot be empty");
                }

                if (mPassword.getText().toString().equals(mConfirmPassword.getText().toString()) && mFirstName.getText().length() != 0 &&
                        mLastName.getText().length() != 0 && mUsername.getText().length() != 0 && mAddress.getText().length() != 0 &&
                        mEmail.getText().length() != 0 && mContactNumber.getText().length() != 0 && mPassword.getText().length() != 0 &&
                        mConfirmPassword.getText().length() != 0 && mBirthdate.getText().length() != 0) {
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

    private void updateLabel() {
        String myFormat = "YYYY-MM-DD"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mBirthdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void openImageChooser() {
        ImagePicker.create(this)
                .single()
                .start(1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", "inside");
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
            for (Image image : images) {
                Picasso.with(getContext()).load(new File(image.getPath())).fit().into(slctImage);
                final String path = image.getPath();
                uploadFile(path);
            }
        }
    }

    String uploadUrl = "http://192.168.1.134:8080/Koobym/image/upload";

    private void uploadFile(final String path) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, uploadUrl, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                // parse success output
                Log.d("RESULT OF UOPLOAD", resultResponse);
                userModel.setImageFilename(resultResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    File file = new File(path);
                    Log.d("FILE NAME = ", file.getName());
                    params.put("file", new DataPart(file.getName(), FileUtils.readFileToByteArray(new File(path)), "image/jpeg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return params;
            }
        };


        requestQueue.add(multipartRequest);
    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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
