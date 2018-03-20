package com.example.joane14.myapplication.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.example.joane14.myapplication.Adapters.HistoryRentAdapter;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HistoryRent extends Fragment {

    private OnHistoryRentInteractionListener mListener;

    List<RentalHeader> rentalHeaderList;
    private GridView mGridView;
    private HistoryRentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public HistoryRent() {
    }


    public static HistoryRent newInstance() {
        HistoryRent fragment = new HistoryRent();
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
        View view = inflater.inflate(R.layout.fragment_history_rent, container, false);

        rentalHeaderList = new ArrayList<RentalHeader>();
        mGridView = (GridView) view.findViewById(R.id.hRent_gridview);

        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new HistoryRentAdapter(getContext(), rentalHeaderList);
        mGridView.setAdapter(mAdapter);

        getHistory();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RentalHeader rentHead = rentalHeaderList.get(position);

                if(rentHead.getStatus().equals("Complete")){
                    final Dialog dialogCustom = new Dialog(getContext());
                    dialogCustom.setContentView(R.layout.rent_history_complete);
                    TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleDelivery);
                    TextView mOwner = (TextView) dialogCustom.findViewById(R.id.deliveredBy);
                    TextView mDateDelivered = (TextView) dialogCustom.findViewById(R.id.dateDelivery);
                    TextView mDateReturned = (TextView) dialogCustom.findViewById(R.id.dateReturned);
                    TextView mDateReq = (TextView) dialogCustom.findViewById(R.id.dateRequested);
                    TextView mDateApproved = (TextView) dialogCustom.findViewById(R.id.dateApproved);
                    TextView mDateReceived = (TextView) dialogCustom.findViewById(R.id.dateReceived);
                    TextView mDateComplete = (TextView) dialogCustom.findViewById(R.id.dateComplete);
                    ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookDelivery);
                    Button btnOkay = (Button) dialogCustom.findViewById(R.id.btnDeliveryOkay);

                    mDateReq.setText(rentHead.getRentalTimeStamp());
                    mDateApproved.setText(rentHead.getDateApproved());
                    mDateReceived.setText(rentHead.getDateReceived());
                    mDateComplete.setText(rentHead.getDateComplete());

                    Glide.with(getContext()).load(rentHead.getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                    mTitle.setText(rentHead.getRentalDetail().getBookOwner().getBookObj().getBookTitle());
                    mOwner.setText(rentHead.getRentalDetail().getBookOwner().getUserObj().getUserFname()+" "+rentHead.getRentalDetail().getBookOwner().getUserObj().getUserLname());
                    mDateDelivered.setText(rentHead.getDateDeliver());
                    mDateReturned.setText(rentHead.getRentalReturnDate().toString());

                    btnOkay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          dialogCustom.dismiss();
                        }
                    });
                    dialogCustom.show();
                }else{
                    final Dialog dialogCustom = new Dialog(getContext());
                    dialogCustom.setContentView(R.layout.rent_history_rejected);

                    TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleDelivery);
                    TextView mRejected = (TextView) dialogCustom.findViewById(R.id.rejectReason);
                    TextView mBookOwner = (TextView) dialogCustom.findViewById(R.id.bookOwner);
                    TextView mReqDate = (TextView) dialogCustom.findViewById(R.id.reqDate);
                    TextView mRejectDate = (TextView) dialogCustom.findViewById(R.id.rejectDate);
                    ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookDelivery);
                    Button btnOkay = (Button) dialogCustom.findViewById(R.id.btnDeliveryOkay);

                    Glide.with(getContext()).load(rentHead.getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                    mTitle.setText(rentHead.getRentalDetail().getBookOwner().getBookObj().getBookTitle());
                    mRejected.setText(rentHead.getRentalExtraMessage());
                    mBookOwner.setText(rentHead.getRentalDetail().getBookOwner().getUserObj().getUserFname()+" "+rentHead.getRentalDetail().getBookOwner().getUserObj().getUserLname());
                    mRejectDate.setText(rentHead.getDateRejected());
                    mReqDate.setText(rentHead.getRentalTimeStamp());

                    btnOkay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogCustom.dismiss();
                        }
                    });
                    dialogCustom.show();
                }
            }
        });
        return view;
    }

    public void getHistory(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        Log.d("rentHistory", String.valueOf(user.getUserId()));
        String URL = Constants.HISTORY_RENT_NAV+user.getUserId();
        Log.d("rentHistoryURL", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        final RentalHeader rentalHeader =new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("rentHistory", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("rentHistoryResponse", response);
//                RentalHeader rentalHeaderModel = gson.fromJson(response, RentalHeader.class);
                rentalHeaderList.clear();
                rentalHeaderList.addAll(Arrays.asList(gson.fromJson(response, RentalHeader[].class)));
                mAdapter.notifyDataSetChanged();
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
            mListener.onHistoryRentOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHistoryRentInteractionListener) {
            mListener = (OnHistoryRentInteractionListener) context;
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

    public interface OnHistoryRentInteractionListener {
        // TODO: Update argument type and name
        void onHistoryRentOnClick(Uri uri);
    }
}
