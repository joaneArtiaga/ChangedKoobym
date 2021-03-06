package com.example.joane14.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.example.joane14.myapplication.Activities.ViewAuctionBook;
import com.example.joane14.myapplication.Activities.ViewBookAct;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.VolleyUtil;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by Joane14 on 20/10/2017.
 */

public class DisplayBooksAdapter extends BaseAdapter {

    private Context context;
    List<BookOwnerModel> bookOwnerModelList;
    AuctionDetailModel auctionDetailModel;
    SwapDetail swapDetailModel;
    RentalDetail rentalDetailModel;
    BookOwnerModel bookOwnerModel;
    private LayoutInflater mInflater;
    RatingBar mRating;
    Float retFloat;
    TextView statusBook;
    LinearLayout statusLinear;


    public DisplayBooksAdapter(Context context, List<BookOwnerModel> bookOwnerModelList){
        this.context = context;
        this.bookOwnerModelList = bookOwnerModelList;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount(){
        return bookOwnerModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_shelf, null);
        }

        auctionDetailModel = new AuctionDetailModel();
        swapDetailModel = new SwapDetail();
        rentalDetailModel = new RentalDetail();
        bookOwnerModel = bookOwnerModelList.get(position);

        ImageView bookPic = (ImageView) convertView.findViewById(R.id.displayBookPic);
        TextView bookTitle = (TextView) convertView.findViewById(R.id.lpBookTitle);
        TextView bookAuthor = (TextView) convertView.findViewById(R.id.lpAuthor);
        statusBook = (TextView) convertView.findViewById(R.id.ratingStatusBook);
        TextView bookPrice = (TextView) convertView.findViewById(R.id.lpSwapPrice);
        statusLinear = (LinearLayout) convertView.findViewById(R.id.status_ll);

        mRating = (RatingBar) convertView.findViewById(R.id.rating_bookRating);
        Log.d("inside", "PrefferedAdapter");
        bookTitle.setText(bookOwnerModel.getBookObj().getBookTitle());

        statusBook.setText(bookOwnerModel.getStatus());
        if(bookOwnerModel.getStatus().equals("Rent")){

            if(bookOwnerModel.getBookStat().equals("Available")){
                statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRent));
            }else{
                statusBook.setText("RENTED");
                statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightRent));
            }
        }else if(bookOwnerModel.getStatus().equals("Swap")){
//           getSwapDetail(bookOwnerModel.getBookOwnerId());

            if(bookOwnerModel.getBookStat().equals("Available")){
                statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSwap));
            }else{
                statusBook.setText("SWAPPED");
                statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightSwap));
            }
//            if(swapDetailModel==null){
//                Log.d("NullMan", "ang pisti");
//            }else{
//                Log.d("NullMan", "joke dili diay");
//            }
        }else if(bookOwnerModel.getStatus().equals("Auction")){
//            getAuctionDetail(bookOwnerModel.getBookOwnerId());
            if(bookOwnerModel.getBookStat().equals("Available")){
                statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAuction));
            }else{
                statusBook.setText("SOLD");
                statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightAuction));
            }
//            if(auctionDetailModel==null){
//                Log.d("NullMan", "ang pisti");
//            }else{
//                Log.d("NullMan", "joke dili diay");
//            }
        }else{
            statusBook.setText("Not Advertised");
            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGray));
        }
        bookPrice.setVisibility(View.GONE);

        String author = " ";
        if(bookOwnerModel.getBookObj().getBookAuthor().size()!=0){
            for(int init=0; init<bookOwnerModel.getBookObj().getBookAuthor().size(); init++){
                if(!(bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                    author+=bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                    if(!(bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                        author+=bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorLName();
                        if(init+1<bookOwnerModel.getBookObj().getBookAuthor().size()){
                            author+=", ";
                        }
                    }
                }
            }
        }else{
            author="Unknown Author";
        }
        bookAuthor.setText(author);

        Glide.with(context).load(bookOwnerModel.getBookObj().getBookFilename()).centerCrop().into(bookPic);

//        getRatings();

        mRating.setRating(Float.parseFloat(String.valueOf(bookOwnerModel.getRate())));
        Log.d("getRateDisplayAdap", String.valueOf(bookOwnerModel.getRate()));

        return convertView;
    }

    private void getAuctionDetail(int bookOwnerId){
//        String URL = "http://104.198.152.85/Koobym/rentalDetail/suggested/%d";
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/suggested/%d";
        String URL = Constants.GET_BOOK_OWNER_AUCTION_DETAIL+bookOwnerId;
//        URL = String.format(URL, userId);
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AuctionDetailResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                auctionDetailModel = gson.fromJson(response, AuctionDetailModel.class);

                if(auctionDetailModel.getStatus().equals("Available")){
                    statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAuction));
                }else if(auctionDetailModel.getStatus()==null){
                }else{
                    statusBook.setText("SOLD");
                    statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightAuction));
                }
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
//                BookOwnerModel[] bookOwnerModels = gson.fromJson(response, BookOwnerModel[].class);
//                bookOwnerModelList.addAll(Arrays.asList(bookOwnerModels));
//                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(context).add(stringRequest);

    }

    private void getRentalDetail(int bookOwnerId){
//        String URL = "http://104.198.152.85/Koobym/rentalDetail/suggested/%d";
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/suggested/%d";
        String URL = Constants.GET_BOOK_OWNER_RENTAL_DETAIL+bookOwnerId;
//        URL = String.format(URL, userId);
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RentalDetailResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                rentalDetailModel = gson.fromJson(response, RentalDetail.class);

                if(rentalDetailModel.getRentalStatus().equals("Available")){
                    statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRent));
                }else if(rentalDetailModel.getRentalStatus()==null){
                }else{
                    statusBook.setText("RENTED");
                    statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightRent));
                }

//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
//                BookOwnerModel[] bookOwnerModels = gson.fromJson(response, BookOwnerModel[].class);
//                bookOwnerModelList.addAll(Arrays.asList(bookOwnerModels));
//                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(context).add(stringRequest);

    }

    private void getSwapDetail(int bookOwnerId){
//        String URL = "http://104.198.152.85/Koobym/rentalDetail/suggested/%d";
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/suggested/%d";
//        String URL = Constants.GET_BOOK_OWNER_SWAP_DETAIL+bookOwnerId;
        String URL = Constants.GET_BOOK_OWNER_SWAP_DETAIL+bookOwnerId;
//        URL = String.format(URL, userId);
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("swappy", "inside");
                Log.d("SwapDetailResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                swapDetailModel = gson.fromJson(response, SwapDetail.class);

                if(swapDetailModel.getSwapStatus().equalsIgnoreCase("Available")){
                    statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSwap));
                }else if(swapDetailModel.getSwapStatus()==null){

                }else{
                    statusBook.setText("SWAPPED");
                    statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightSwap));
                }
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
//                BookOwnerModel[] bookOwnerModels = gson.fromJson(response, BookOwnerModel[].class);
//                bookOwnerModelList.addAll(Arrays.asList(bookOwnerModels));
//                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(context).add(stringRequest);
    }

    public void getRatings(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
        Log.d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_RATINGS+bookOwnerModel.getBookOwnerId();

        final RentalHeader rentalHeader =new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ResponseRequestReceived", response);

                Float fl = Float.parseFloat(response);

                mRating.setRating(fl);
                Log.d("RatingAdapter: "+bookOwnerModel.getBookObj().getBookTitle(), String.valueOf(fl));
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

}