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
import com.example.joane14.myapplication.Adapters.HistoryAuctionAdapter;
import com.example.joane14.myapplication.Model.AuctionComment;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.AuctionHeader;
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

import static android.util.Log.d;

public class HistoryAuction extends Fragment {
    private OnHistoryAuctionInteractionListener mListener;
    List<AuctionHeader> auctionHeaderList;
    private GridView mGridView;
    private HistoryAuctionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public HistoryAuction() {
    }

    public static HistoryAuction newInstance() {
        HistoryAuction fragment = new HistoryAuction();
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
        View view = inflater.inflate(R.layout.fragment_history_auction, container, false);

        auctionHeaderList = new ArrayList<AuctionHeader>();
        mGridView = (GridView) view.findViewById(R.id.hAuction_gridview);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new HistoryAuctionAdapter(getContext(), auctionHeaderList);
        mGridView.setAdapter(mAdapter);

        getHistory();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getMaximumBid(auctionHeaderList.get(position));
            }
        });
        return view;

    }

    public void getMaximumBid(final AuctionHeader ah) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.GET_MAXIMUM_BID + ah.getAuctionDetail().getAuctionDetailId();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(ah.getAuctionDetail());

        d("maximumBid_VOLLEY", mRequestBody);
        int maxLogSize = 2000;
        for (int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("maximumBid", mRequestBody.substring(start, end));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("onResponse maxBid", "inside");
                Log.i("MaximumBid", response);

                List<AuctionComment> auctionHeaderModelMod = new ArrayList<AuctionComment>();
                auctionHeaderModelMod.clear();
                auctionHeaderModelMod.addAll(Arrays.asList(gson.fromJson(response, AuctionComment[].class)));


                final Dialog dialogCustom = new Dialog(getContext());
                dialogCustom.setContentView(R.layout.auction_history_complete);
                TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleDelivery);
                TextView mDateDelivered = (TextView) dialogCustom.findViewById(R.id.dateDelivery);
                TextView mPrice = (TextView) dialogCustom.findViewById(R.id.auctionPrice);
                ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookDelivery);
                Button btnOkay = (Button) dialogCustom.findViewById(R.id.btnDeliveryOkay);


                mPrice.setText("₱  "+auctionHeaderModelMod.get(0).getAuctionComment()+".00");
                Glide.with(getContext()).load(ah.getAuctionDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                mTitle.setText(ah.getAuctionDetail().getBookOwner().getBookObj().getBookTitle());
                mDateDelivered.setText(ah.getDateDelivered());

                btnOkay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogCustom.dismiss();
                    }
                });
                dialogCustom.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
                error.printStackTrace();
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

    public void getHistory(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        Log.d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.HISTORY_AUCTION_NAV+user.getUserId();
        Log.d("UserIdURL", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        final RentalHeader rentalHeader =new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseRequestReceived", response);
                auctionHeaderList.clear();
                auctionHeaderList.addAll(Arrays.asList(gson.fromJson(response, AuctionHeader[].class)));
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
            mListener.onHistoryAuctionOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHistoryAuctionInteractionListener) {
            mListener = (OnHistoryAuctionInteractionListener) context;
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

    public interface OnHistoryAuctionInteractionListener {
        void onHistoryAuctionOnClick(Uri uri);
    }
}
