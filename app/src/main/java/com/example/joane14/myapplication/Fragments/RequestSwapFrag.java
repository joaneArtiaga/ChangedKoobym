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
import com.example.joane14.myapplication.Activities.RequestActivity;
import com.example.joane14.myapplication.Adapters.RequestSwapAdapter;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapHeader;
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

public class RequestSwapFrag extends Fragment {

    List<SwapHeader> swapHeaderList;
    private GridView mGridViewSwap;
    private RequestSwapAdapter mAdapterRentSwap;
    private RecyclerView.LayoutManager mLayoutManagerSwap;

    private OnSwapRequestInteractionListener mListener;

    public RequestSwapFrag() {
    }

    public static RequestSwapFrag newInstance() {
        RequestSwapFrag fragment = new RequestSwapFrag();
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
        View view = inflater.inflate(R.layout.fragment_request_swap, container, false);

        swapHeaderList = new ArrayList<SwapHeader>();
        mGridViewSwap = (GridView) view.findViewById(R.id.swapRR_gridview);
        mLayoutManagerSwap = new LinearLayoutManager(getContext());
        mAdapterRentSwap = new RequestSwapAdapter(getContext(), swapHeaderList);
        mGridViewSwap.setAdapter(mAdapterRentSwap);

        getRequestReceivedSwap();

        mGridViewSwap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final SwapHeader rh = swapHeaderList.get(position);

                final Dialog dialogCustom = new Dialog(getContext());
                dialogCustom.setContentView(R.layout.request_swap_custom_dialog);
                TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleRequest);
                TextView mTitle2 = (TextView) dialogCustom.findViewById(R.id.bookTitleRequest2);
                TextView mOwner = (TextView) dialogCustom.findViewById(R.id.requestor);
                ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookRequest);
                ImageView ivBook2 = (ImageView) dialogCustom.findViewById(R.id.ivBookRequest2);
                Button btnAccept = (Button) dialogCustom.findViewById(R.id.btnRequestAccept);
                Button btnReject = (Button) dialogCustom.findViewById(R.id.btnRequestReject);


                Glide.with(getContext()).load(rh.getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);
                Glide.with(getContext()).load(rh.getRequestedSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook2);

                mTitle.setText(rh.getSwapDetail().getBookOwner().getBookObj().getBookTitle());
                mTitle2.setText(rh.getRequestedSwapDetail().getBookOwner().getBookObj().getBookTitle());
                mOwner.setText(rh.getUser().getUserFname()+" "+rh.getUser().getUserLname());

                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog ad = new AlertDialog.Builder(getContext()).create();
                        ad.setMessage("The requestor will be notified.");
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

    public void rejectRequest(final SwapHeader swapHeader, final String message){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        String URL = Constants.REJECT_REQUEST_SWAP+swapHeader.getSwapHeaderId();

        Log.d("rejectRequestRentURL", URL);
        Log.d("rejectRequestRent", swapHeader.toString());

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        final User finalUser = user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("rejectRequestRentRes", response);
                SwapHeader swapHeaderModel = gson.fromJson(response, SwapHeader.class);

                UserNotification un = new UserNotification();
                un.setActionName("swap");
                un.setBookActionPerformedOn(swapHeaderModel.getSwapDetail().getBookOwner());
                un.setExtraMessage(message);
                un.setUserPerformer(finalUser);
                un.setUser(swapHeaderModel.getUser());
                un.setActionStatus("Rejected");
                un.setActionId(Math.round(swapHeaderModel.getSwapHeaderId()));

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

    public void updateSwapExtra(UserNotification un){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        String URL = Constants.UPDATE_SWAP_EXTRA+un.getUserNotificationId();

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
                updateSwapExtra(un);
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

    public void acceptRequest(SwapHeader swapHeader){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        String URL = Constants.ACCEPT_REQUEST_SWAP+swapHeader.getSwapHeaderId();

        Log.d("AcceptRequestRentURL", URL);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("AcceptRequestSwapRes", response);
                SwapHeader swapHeaderModel = gson.fromJson(response, SwapHeader.class);
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

    public void getRequestReceivedSwap() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        String URL = Constants.SWAP_REQUEST_RECEIVED + user.getUserId();
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        Log.d("RequestSwap", URL);
        final SwapHeader swapHeader = new SwapHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeader);


        Log.d("RequestSwap", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("SwapRequestsRes", response);
                swapHeaderList.clear();
                swapHeaderList.addAll(Arrays.asList(gson.fromJson(response, SwapHeader[].class)));
                mAdapterRentSwap.notifyDataSetChanged();
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
            mListener.onSwapRequestInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSwapRequestInteractionListener) {
            mListener = (OnSwapRequestInteractionListener) context;
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


    public interface OnSwapRequestInteractionListener {
        void onSwapRequestInteraction(Uri uri);
    }
}
