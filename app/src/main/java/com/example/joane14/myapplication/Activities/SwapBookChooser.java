package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Adapters.BookChooserAdapter;
import com.example.joane14.myapplication.Adapters.LandingPageAdapter;
import com.example.joane14.myapplication.Adapters.SwapChooserAdapter;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserDayTime;
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

public class SwapBookChooser extends AppCompatActivity {

    SwapComment swapCommentModel;
    SwapDetail swapDetailModel;
    SwapHeader swapHeader;
    List<SwapDetail> suggested;
    private GridView mGridView;
    private BookChooserAdapter mAdapter;
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
            d("SwapDetailDisplay", String.valueOf(swapDetailModel.getSwapPrice()));
            d("SwapDetailDisplay", String.valueOf(swapDetailModel.getSwapDetailId()));
            d("SwapDetailDisplay", swapDetailModel.getSwapDescription());

        }
        if(getIntent().getExtras().getSerializable("swapHeader")!=null){
            swapHeader = (SwapHeader) getIntent().getSerializableExtra("swapHeader");
            swapHeader.setUser(swapDetailModel.getBookOwner().getUserObj());
            d("SwapHeaderDisplay", swapHeader.getSwapDetail().toString());
            d("SwapHeaderDisplay", swapHeader.getUser().toString());
        }

        mGridView = (GridView) findViewById(R.id.book_chooser_gridview);
        mAdapter = new BookChooserAdapter(this, suggested);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showConfirmation(position);
            }
        });
        getRecommendSwap();
    }

    public void getRecommendSwap(){
        RequestQueue requestQueue = Volley.newRequestQueue(SwapBookChooser.this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.RECOMMEND_SWAP_BOOK+swapCommentModel.getUser().getUserId()+"/"+swapDetailModel.getBookOwner().getBookObj().getBookOriginalPrice();

        User user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);


        String URL = Constants.GET_MY_SWAP_RECOMMENDED+user.getUserId()+"/"+swapDetailModel.getSwapPrice();
        d("SwapURL", URL);
        d("SwapPrice", String.valueOf(swapDetailModel.getSwapPrice()));

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapDetailModel);


        d("LOG_VOLLEY", mRequestBody);
        d("LOG_VOLLEY rentalHeader", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("SwapDetailResponse", response);
                if(response.isEmpty()){
                    d("Available", "No books for swap");
                    showWarningBookChooser();
                }else{
                    d("Available","books for swap");
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                    suggested.clear();
                    suggested.addAll(Arrays.asList(gson.fromJson(response, SwapDetail[].class)));
                    mAdapter.notifyDataSetChanged();
                }
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

    public void showWarningBookChooser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Swap Book");
        alertDialogBuilder.setMessage("You don't have an available book for swap that matches with "+swapDetailModel.getBookOwner().getBookObj().getBookTitle()+"'s price.");
        alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void showConfirmation(final int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirmation");
        alertDialogBuilder.setMessage("Are you sure that you will choose to swap your book, "+suggested.get(position).getBookOwner().getBookObj().getBookTitle()+
                " with "+swapDetailModel.getBookOwner().getUserObj().getUserFname()+" "+swapDetailModel.getBookOwner().getUserObj().getUserLname()+"'s book, "+swapDetailModel.getBookOwner().getBookObj().getBookTitle()+"?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(SwapBookChooser.this, "Please wait for the owner's Approval.", Toast.LENGTH_SHORT).show();

                        SwapHeader swapHeaderToPost = new SwapHeader();

                        User user = (User) SPUtility.getSPUtil(SwapBookChooser.this).getObject("USER_OBJECT", User.class);
                        String nextDateStr="";

                        @SuppressLint({"NewApi", "LocalSuppress"})
                        Calendar c = Calendar.getInstance();


                        @SuppressLint({"NewApi", "LocalSuppress"})
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        nextDateStr = format.format(c.getTime());

                        swapHeaderToPost.setDateTimeStamp(nextDateStr);
                        swapHeaderToPost.setUser(user);
                        swapHeaderToPost.setSwapDetail(swapDetailModel);
                        swapHeaderToPost.setRequestedSwapDetail(suggested.get(position));
                        swapHeaderToPost.setDateRequest(nextDateStr);
                        swapHeaderToPost.setStatus("Request");

                        addSwapHeader(swapHeaderToPost);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(SwapBookChooser.this, "You disagreed to the terms and condition.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void d(String TAG, String message) {
        int maxLogSize = 2000;
        if(message!=null) {
            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                android.util.Log.d(TAG, message.substring(start, end));
            }
        }
    }

    public void addSwapHeader(SwapHeader swapToPost){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.POST_SWAP_HEADER;

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapToPost);

        d("swapHeader_VOLLEY", mRequestBody);
        int maxLogSize = 2000;
        for(int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("addSwapHeader", mRequestBody.substring(start, end));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("onResponse addSwapH", "inside");
                Log.i("AddSwapHeader", response);
                SwapHeader swapHeaderModelMod = gson.fromJson(response, SwapHeader.class);
//                updateSwap("Request", swapHeaderModelMod);
                Intent intent = new Intent(SwapBookChooser.this, NotificationAct.class);
                startActivity(intent);


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

    @SuppressLint("NewApi")
    public void updateSwap(final String status, SwapHeader swapHeaderMod){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";
        String nextDateStr = "";

        java.util.Calendar c = java.util.Calendar.getInstance();
        @SuppressLint({"NewApi", "LocalSuppress"})
        DateFormat format = new android.icu.text.SimpleDateFormat("yyyy-MM-dd");
        nextDateStr = format.format(c.getTime());

        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        String URL = Constants.UPDATE_SWAP_HEADER+"/"+status+"/"+swapHeaderMod.getSwapHeaderId()+"/"+nextDateStr;

        Log.d("UpdateSwapHeaderURL", URL);


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeader);


        Log.d("LOG_VOLLEY_swapHeaderUD", mRequestBody);
        Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
        final String finalNextDateStr = nextDateStr;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("UpdateSwapHeader", response);
//                SwapHeader swapHeaderMod = gson.fromJson(response, SwapHeader.class);
//
//                Log.d("swapHeaderStatus", swapHeaderMod.getStatus());
//                swapHeaderMod.setMeetUp(meetUp);
//                swapHeaderMod.setStatus(status);
////                swapHeaderMod.setDateReceived(finalNextDateStr);
//                updateSwapHeader(swapHeaderMod);
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
