package com.example.joane14.myapplication.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.LandingPage;
import com.example.joane14.myapplication.Activities.RequestActivity;
import com.example.joane14.myapplication.Activities.TimeDateChooser;
import com.example.joane14.myapplication.Adapters.RequestRentAdapter;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserNotification;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.util.Log.d;

public class RequestRentFrag extends Fragment {

    private OnRequestRentInteractionListener mListener;
    List<RentalHeader> rentalHeaderList;
    private GridView mGridViewRent;
    private RequestRentAdapter mAdapterRent;
    private RecyclerView.LayoutManager mLayoutManagerRent;


    public RequestRentFrag() {
    }

    public static RequestRentFrag newInstance() {
        RequestRentFrag fragment = new RequestRentFrag();
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
        View view = inflater.inflate(R.layout.fragment_request_rent, container, false);

        rentalHeaderList = new ArrayList<RentalHeader>();
        mGridViewRent = (GridView) view.findViewById(R.id.rRent_gridview);
        mLayoutManagerRent = new LinearLayoutManager(getContext());
        mAdapterRent = new RequestRentAdapter(getContext(), rentalHeaderList);
        mGridViewRent.setAdapter(mAdapterRent);
        getRequestReceived();

        mGridViewRent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RentalHeader rh = rentalHeaderList.get(position);

                final Dialog dialogCustom = new Dialog(getContext());
                dialogCustom.setContentView(R.layout.request_rent_custom_dialog);
                TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleRequest);
                TextView mOwner = (TextView) dialogCustom.findViewById(R.id.requestor);
                TextView mDateRequest = (TextView) dialogCustom.findViewById(R.id.dateRequest);
                TextView mlocation = (TextView) dialogCustom.findViewById(R.id.locationRequest);
                TextView mTime = (TextView) dialogCustom.findViewById(R.id.timeRequest);
                TextView mPrice = (TextView) dialogCustom.findViewById(R.id.priceRequest);
                ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookRequest);
                Button btnAccept = (Button) dialogCustom.findViewById(R.id.btnRequestAccept);
                Button btnReject = (Button) dialogCustom.findViewById(R.id.btnRequestReject);


                Glide.with(getContext()).load(rh.getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                mTitle.setText(rh.getRentalDetail().getBookOwner().getBookObj().getBookTitle());
                mOwner.setText(rh.getUserId().getUserFname()+" "+rh.getUserId().getUserLname());
                mDateRequest.setText(rh.getDateDeliver());
                if(rh.getMeetUp()!=null){
                    mlocation.setText(rh.getMeetUp().getLocation().getLocationName());
                    mTime.setText(rh.getMeetUp().getUserDayTime().getTime().getStrTime());
                }
                mPrice.setText(rh.getRentalDetail().getCalculatedPrice()+"");

                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog ad = new AlertDialog.Builder(getContext()).create();
                        ad.setMessage("The renter will be notified.");
                        ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                acceptRequest(rh);
                            }
                        });
                        ad.show();
                    }
                });

                btnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialogCustom = new Dialog(getContext());
                        dialogCustom.setContentView(R.layout.reject_custom_dialog);
                        final EditText etReason = (EditText) dialogCustom.findViewById(R.id.etReason);
                        Button mSubmitReason = (Button) dialogCustom.findViewById(R.id.submitReject);

                        mSubmitReason.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(etReason.getText().length()==0){
                                    etReason.setError("Field should not be empty.");
                                }else{
                                    rh.setStatus("Rejected");
                                    String message = etReason.getText().toString();
                                    rejectRequest(rh, message);
                                }
                            }
                        });
                        dialogCustom.show();
                    }
                });
                dialogCustom.show();
            }
        });

        return view;
    }

    public void updateRentalExtra(UserNotification un){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        String URL = Constants.UPDATE_RENTAL_EXTRA+un.getUserNotificationId();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(un);


        Log.d("LOG_VOLLEY", mRequestBody);
        final User finalUser = user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("rejectRequestRentRes", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public void rejectRequest(final RentalHeader rentalHeader, final String message){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        String URL = Constants.REJECT_REQUEST_RENT+rentalHeader.getRentalHeaderId();

        Log.d("rejectRequestRentURL", URL);
        Log.d("rejectRequestRent", rentalHeader.toString());

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        final User finalUser = user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("rejectRequestRentRes", response);
                RentalHeader rentalHeaderModel = gson.fromJson(response, RentalHeader.class);

                UserNotification un = new UserNotification();
                un.setActionName("rental");
                un.setBookActionPerformedOn(rentalHeaderModel.getRentalDetail().getBookOwner());
                un.setExtraMessage(message);
                un.setUserPerformer(finalUser);
                un.setUser(rentalHeaderModel.getUserId());
                un.setActionStatus("Rejected");
                un.setActionId(Math.round(rentalHeaderModel.getRentalHeaderId()));

                addUserNotif(un);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public void addUserNotif(UserNotification userNotification) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.POST_USER_NOTIFICATION;


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(userNotification);


        d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("userNotificationPost", response);
                UserNotification un = gson.fromJson(response, UserNotification.class);
                updateRentalExtra(un);
                Intent intent = new Intent(getContext(), RequestActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public void acceptRequest(RentalHeader rentalHeader){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        String URL = Constants.ACCEPT_REQUEST_RENT+rentalHeader.getRentalHeaderId();

        Log.d("AcceptRequestRentURL", URL);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("AcceptRequestRentRes", response);
                RentalHeader rentalHeaderModel = gson.fromJson(response, RentalHeader.class);
                Intent intent = new Intent(getContext(), RequestActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public void getRequestReceived(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        String URL = Constants.GET_REQUEST_RECEIVED+user.getUserId();

        Log.d("RequestRent", URL);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(user);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseRequestReceived", response);
                rentalHeaderList.clear();
                rentalHeaderList.addAll(Arrays.asList(gson.fromJson(response, RentalHeader[].class)));
                mAdapterRent.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onRequestRentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRequestRentInteractionListener) {
            mListener = (OnRequestRentInteractionListener) context;
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

    public interface OnRequestRentInteractionListener {
        void onRequestRentInteraction(Uri uri);
    }
}
