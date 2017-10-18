package com.example.joane14.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Adapters.LandingPageAdapter;
import com.example.joane14.myapplication.Adapters.SwapChooserAdapter;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SwapBookChooser extends AppCompatActivity {

    SwapComment swapCommentModel;
    SwapDetail swapDetailModel;
    SwapHeader swapHeader;
    List<SwapDetail> suggested;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_book_chooser);
        Toast.makeText(SwapBookChooser.this, "Inside Swap Book Chooser", Toast.LENGTH_SHORT).show();


        swapCommentModel = new SwapComment();
        swapDetailModel = new SwapDetail();

        suggested = new ArrayList<SwapDetail>();

        if(getIntent().getExtras().getSerializable("swapComment")!=null){
            swapCommentModel = (SwapComment) getIntent().getExtras().getSerializable("swapComment");
        }
        if(getIntent().getExtras().getSerializable("swapDetail")!=null){
            swapDetailModel = (SwapDetail) getIntent().getExtras().getSerializable("swapDetail");
            Log.d("SwapDetailDisplay", String.valueOf(swapDetailModel.getSwapPrice()));
            Log.d("SwapDetailDisplay", String.valueOf(swapDetailModel.getSwapDetailId()));
            Log.d("SwapDetailDisplay", swapDetailModel.getSwapDescription());

        }
        if(getIntent().getExtras().getSerializable("swapHeader")!=null){
            swapHeader = (SwapHeader) getIntent().getSerializableExtra("swapHeader");
            swapHeader.setUser(swapDetailModel.getBookOwner().getUserObj());
            Log.d("SwapHeaderDisplay", swapHeader.getSwapDetail().toString());
            Log.d("SwapHeaderDisplay", swapHeader.getUser().toString());
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_swap_chooser);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(SwapBookChooser.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SwapChooserAdapter(suggested, swapHeader);
        mRecyclerView.setAdapter(mAdapter);
        getRecommendSwap();
    }

    public void getRecommendSwap(){
        RequestQueue requestQueue = Volley.newRequestQueue(SwapBookChooser.this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.RECOMMEND_SWAP_BOOK+swapCommentModel.getUser().getUserId()+"/"+swapDetailModel.getBookOwner().getBookObj().getBookOriginalPrice();


        String URL = Constants.GET_MY_SWAP+"/"+swapCommentModel.getUser().getUserId();

        Log.d("SwapURL", URL);
        Log.d("SwapPrice", String.valueOf(swapDetailModel.getSwapPrice()));

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapDetailModel);


        Log.d("LOG_VOLLEY", mRequestBody);
        Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("SwapDetailResponse", response);
                if(response.isEmpty()){
                    Log.d("Available", "No books for swap");
                }else{
                    Log.d("Available","books for swap");
                }
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                suggested.clear();
                suggested.addAll(Arrays.asList(gson.fromJson(response, SwapDetail[].class)));
                mAdapter.notifyDataSetChanged();
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

}
