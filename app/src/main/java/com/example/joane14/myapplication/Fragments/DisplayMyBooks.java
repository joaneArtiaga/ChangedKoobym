package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.GridView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.ViewOwnBookAct;
import com.example.joane14.myapplication.Adapters.DisplayBooksAdapter;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DisplayMyBooks extends Fragment {

    private OnDisplayMyBooksInteractionListener mListener;
    User user;
    List<BookOwnerModel> bookOwnerModelList;
    private GridView mGridView;
    private DisplayBooksAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public DisplayMyBooks() {
    }

    public static DisplayMyBooks newInstance(Bundle bundle) {
        DisplayMyBooks fragment = new DisplayMyBooks();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_my_books, container, false);

        user = new User();
        user = (User) getArguments().getSerializable("user");
        Log.d("TheUser", user.getUserFname());


        bookOwnerModelList = new ArrayList<BookOwnerModel>();
        mGridView = (GridView) view.findViewById(R.id.display_books_gridview);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new DisplayBooksAdapter(getContext(), bookOwnerModelList);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DisplayGrid", bookOwnerModelList.get(position).getBookObj().getBookTitle());
                if(bookOwnerModelList.get(position).getStatus().equals("Rent")){
                    getRentalDetail(bookOwnerModelList.get(position).getBookOwnerId());
                }else if(bookOwnerModelList.get(position).getStatus().endsWith("Swap")) {
                    getSwapDetail(bookOwnerModelList.get(position).getBookOwnerId());
                }else if(bookOwnerModelList.get(position).getStatus().equals("Auction")){
                    getAuctionDetail(bookOwnerModelList.get(position).getBookOwnerId());
                }else{
                    Intent intent = new Intent(getContext(), ViewOwnBookAct.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notAdBook",bookOwnerModelList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        getMyBooks();
        return view;
    }

    private void getAuctionDetail(int bookOwnerId){

        String URL = Constants.GET_BOOK_OWNER_AUCTION_DETAIL+bookOwnerId;
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AuctionDetailResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                AuctionDetailModel auctionDetailModel= gson.fromJson(response, AuctionDetailModel.class);
                Intent intent = new Intent(getContext(), ViewOwnBookAct.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("auctionBook",auctionDetailModel);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(getContext()).add(stringRequest);
    }

    private void getSwapDetail(int bookOwnerId){
        String URL = Constants.GET_BOOK_OWNER_SWAP_DETAIL+bookOwnerId;
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SwapDetailResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                SwapDetail swapDetails = gson.fromJson(response, SwapDetail.class);
                Intent intent = new Intent(getContext(), ViewOwnBookAct.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("swapBook",swapDetails);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(getContext()).add(stringRequest);
    }

    private void getRentalDetail(int bookOwnerId){
        String URL = Constants.GET_BOOK_OWNER_RENTAL_DETAIL+bookOwnerId;
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RentalDetailResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                RentalDetail rentalDetails = gson.fromJson(response, RentalDetail.class);
                Intent intent = new Intent(getContext(), ViewOwnBookAct.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("viewBook",rentalDetails);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(getContext()).add(stringRequest);
    }

    public void getMyBooks(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Log.d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_MY_BOOKS+"/"+user.getUserId();

        Log.d("UserReview URL", URL);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookOwnerModelList);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("displayMyBooks", response);
                bookOwnerModelList.clear();
                bookOwnerModelList.addAll(Arrays.asList(gson.fromJson(response, BookOwnerModel[].class)));
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
            mListener.onDisplayMyBooksOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDisplayMyBooksInteractionListener) {
            mListener = (OnDisplayMyBooksInteractionListener) context;
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

    public interface OnDisplayMyBooksInteractionListener {
        void onDisplayMyBooksOnClick(Uri uri);
    }
}
