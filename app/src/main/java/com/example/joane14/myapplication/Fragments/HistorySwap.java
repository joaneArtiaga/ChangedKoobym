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
import android.widget.ListView;
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
import com.example.joane14.myapplication.Adapters.HistorySwapAdapter;
import com.example.joane14.myapplication.Adapters.SwapRequestAdapter;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.SwapHeaderDetail;
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

public class HistorySwap extends Fragment {
    private OnHistorySwapInteractionListener mListener;

    List<SwapHeader> swapHeaderList;
    List<SwapHeader> swapHeaderListComplete;
    private GridView mGridView;
    private HistorySwapAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public HistorySwap() {
    }

    public static HistorySwap newInstance() {
        HistorySwap fragment = new HistorySwap();
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
        View view = inflater.inflate(R.layout.fragment_history_swap, container, false);

        swapHeaderList = new ArrayList<SwapHeader>();
        swapHeaderListComplete = new ArrayList<SwapHeader>();

        mGridView = (GridView) view.findViewById(R.id.hSwap_gridview);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new HistorySwapAdapter(getContext(), swapHeaderListComplete);
        mGridView.setAdapter(mAdapter);

        getHistory();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SwapHeader swapHeader = swapHeaderListComplete.get(position);

                if(swapHeader.getStatus().equals("Complete")){
                    final Dialog dialogCustom = new Dialog(getContext());
                    dialogCustom.setContentView(R.layout.swap_history_complete);

                    TextView mTitleSwap = (TextView) dialogCustom.findViewById(R.id.bookTitleSwap);
                    TextView mSwappedWith = (TextView) dialogCustom.findViewById(R.id.swappedWith);
                    ImageView ivBookSwap = (ImageView) dialogCustom.findViewById(R.id.ivSwap);
                    Button btnOkay = (Button) dialogCustom.findViewById(R.id.btnDeliveryOkay);
                    ListView ly = (ListView) dialogCustom.findViewById(R.id.listSwap);

                    List<SwapHeaderDetail> newSHD = new ArrayList<SwapHeaderDetail>();

                    newSHD = swapHeader.getSwapHeaderDetail();
                    List<SwapHeaderDetail> shdList = new ArrayList<SwapHeaderDetail>();

                    for(int init=0; init<newSHD.size(); init++){
                        if(newSHD.get(init).getSwapType().equals("Requestor")){
                            shdList.add(newSHD.get(init));
                        }
                    }


                    final SwapRequestAdapter adapter = new SwapRequestAdapter(getActivity(), shdList);

                    ly.setAdapter(adapter);
                    Glide.with(getContext()).load(swapHeader.getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBookSwap);

                    mTitleSwap.setText(swapHeader.getSwapDetail().getBookOwner().getBookObj().getBookTitle());

                    User user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);

                    String message ="";
                    if(swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getUserId()!=user.getUserId()){
//                        mSwappedWith.setText(swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getUserFname()+" "+swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getUserLname());
                        message = "Swapped by "+swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getUserFname()+" "+swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getUserLname()+" on "+swapHeader.getDateReceived();
                    }else{
//                        mSwappedWith.setText(swapHeader.getSwapDetail().getBookOwner().getUserObj().getUserFname()+" "+swapHeader.getSwapDetail().getBookOwner().getUserObj().getUserLname());
                        message = "Swapped by "+swapHeader.getSwapDetail().getBookOwner().getUserObj().getUserFname()+" "+swapHeader.getSwapDetail().getBookOwner().getUserObj().getUserLname()+" on "+swapHeader.getDateReceived();
                    }

                    mSwappedWith.setText(message);

                    btnOkay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogCustom.dismiss();
                        }
                    });
                    dialogCustom.show();
                }else{
                    final Dialog dialogCustom = new Dialog(getContext());
                    dialogCustom.setContentView(R.layout.swap_history_rejected);
                    TextView mTitleSwap = (TextView) dialogCustom.findViewById(R.id.bookTitleSwap);
                    TextView mRejection = (TextView) dialogCustom.findViewById(R.id.rejectReason);
                    ImageView ivBookSwap = (ImageView) dialogCustom.findViewById(R.id.ivSwap);
                    Button btnOkay = (Button) dialogCustom.findViewById(R.id.btnDeliveryOkay);
                    ListView ly = (ListView) dialogCustom.findViewById(R.id.listSwap);

                    List<SwapHeaderDetail> newSHD = new ArrayList<SwapHeaderDetail>();

                    newSHD = swapHeader.getSwapHeaderDetail();

                    List<SwapHeaderDetail> shdList = new ArrayList<SwapHeaderDetail>();

                    for(int init=0; init<newSHD.size(); init++){
                        if(newSHD.get(init).getSwapType().equals("Requestor")){
                            shdList.add(newSHD.get(init));
                        }
                    }

                    final SwapRequestAdapter adapter = new SwapRequestAdapter(getActivity(), shdList);

                    ly.setAdapter(adapter);


                    Glide.with(getContext()).load(swapHeader.getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBookSwap);

                    mTitleSwap.setText(swapHeader.getSwapDetail().getBookOwner().getBookObj().getBookTitle());
                    mRejection.setText(swapHeader.getSwapExtraMessage());

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
        Log.d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.HISTORY_SWAP_NAV+user.getUserId();
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
//                RentalHeader rentalHeaderModel = gson.fromJson(response, RentalHeader.class);
                swapHeaderList.clear();
                swapHeaderList.addAll(Arrays.asList(gson.fromJson(response, SwapHeader[].class)));

                swapHeaderListComplete.clear();

                for(int init=0; init<swapHeaderList.size(); init++){
                    if(swapHeaderList.get(init).getStatus().equals("Complete")){
                        swapHeaderListComplete.add(swapHeaderList.get(init));
                    }
                }

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
            mListener.onHistorySwapOnClick(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHistorySwapInteractionListener) {
            mListener = (OnHistorySwapInteractionListener) context;
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

    public interface OnHistorySwapInteractionListener {
        void onHistorySwapOnClick(Uri uri);
    }
}
