package com.example.joane14.myapplication.Fragments;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Adapters.AuctionCommentsAdapter;
import com.example.joane14.myapplication.Adapters.SwapCommentsAdapter;
import com.example.joane14.myapplication.Model.AuctionComment;
import com.example.joane14.myapplication.Model.AuctionCommentDetail;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.AuctionHeader;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserRating;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class AuctionBidFragment extends Fragment {

    private OnAuctionBidInteractionListener mListener;
    List<AuctionCommentDetail> suggested;
    AuctionHeader auctionHeader;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    AuctionDetailModel auctionDetailModel;


    public AuctionBidFragment() {
    }

    public static AuctionBidFragment newInstance(Bundle args) {
        Log.d("walayComment", "BESH");

        AuctionBidFragment fragment = new AuctionBidFragment();
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
        View view = inflater.inflate(R.layout.fragment_auction_bid, container, false);

        auctionDetailModel = new AuctionDetailModel();
        this.auctionDetailModel = (AuctionDetailModel) getArguments().getSerializable("auctionComment");

        if(auctionDetailModel==null){
            Log.d("walayComment", "BESH");
        }


        suggested = new ArrayList<AuctionCommentDetail>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_book_auction);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AuctionCommentsAdapter(suggested, auctionDetailModel);
        mRecyclerView.setAdapter(mAdapter);

        getAllBids();


        return view;
    }

    public void getAllBids(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        Log.d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_AUCTION_BID+auctionDetailModel.getAuctionDetailId();
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        Log.d("UserReview URL", URL);
        final RentalHeader rentalHeader =new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseRequestReceived", response);
//                RentalHeader rentalHeaderModel = gson.fromJson(response, RentalHeader.class);
                suggested.clear();
                suggested.addAll(Arrays.asList(gson.fromJson(response, AuctionCommentDetail[].class)));
                Collections.sort(suggested, new Comparator<AuctionCommentDetail>() {
                    @Override
                    public int compare(AuctionCommentDetail o1, AuctionCommentDetail o2) {
                        if(o1.getAuctionComment().getAuctionComment()>o2.getAuctionComment().getAuctionComment()){
                            return 1;
                        }
                        if(o1.getAuctionComment().getAuctionComment()<o2.getAuctionComment().getAuctionComment()){
                            return -1;
                        }
                        return 0;
                    }
                });
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
            mListener.onAuctionBidClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAuctionBidInteractionListener) {
            mListener = (OnAuctionBidInteractionListener) context;
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

    public interface OnAuctionBidInteractionListener {
        void onAuctionBidClick(Uri uri);
    }
}
