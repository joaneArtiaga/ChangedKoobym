package com.example.joane14.myapplication.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.MostRentedBookFrag;
import com.example.joane14.myapplication.Fragments.VolleyUtil;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.GenreModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapHeader;
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

/**
 * Created by Joane14 on 20/10/2017.
 */

public class PrefferedAdapter extends BaseAdapter {

    private Context context;
    List<BookOwnerModel> bookOwnerModelList;
    BookOwnerModel bookOwnerModel;
    private LayoutInflater mInflater;
    RatingBar mRating;
    Float retFloat;


    public PrefferedAdapter(Context context, List<BookOwnerModel> bookOwnerModelList){
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

        bookOwnerModel = bookOwnerModelList.get(position);

        ImageView bookPic = (ImageView) convertView.findViewById(R.id.displayBookPic);
        TextView bookTitle = (TextView) convertView.findViewById(R.id.lpBookTitle);
        TextView bookAuthor = (TextView) convertView.findViewById(R.id.lpAuthor);
        TextView statusBook = (TextView) convertView.findViewById(R.id.ratingStatusBook);
        TextView bookPrice = (TextView) convertView.findViewById(R.id.lpSwapPrice);
        LinearLayout statusLinear = (LinearLayout) convertView.findViewById(R.id.status_ll);

        bookPrice.setVisibility(View.GONE);
        mRating = (RatingBar) convertView.findViewById(R.id.rating_bookRating);
        Log.d("inside", "PrefferedAdapter");
        bookTitle.setText(bookOwnerModel.getBookObj().getBookTitle());

        statusBook.setText(bookOwnerModel.getStatus());
        if(bookOwnerModel.getStatus().equals("Rent")){
            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRent));
        }else if(bookOwnerModel.getStatus().equals("Swap")){
            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSwap));
        }else{
            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGray));
        }

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

        //getRatings();

        mRating.setRating(Float.parseFloat(String.valueOf(bookOwnerModel.getRate())));



        return convertView;
    }

    public void getRatings(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
        Log.d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_RATINGS+bookOwnerModel.getBookOwnerId();
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

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