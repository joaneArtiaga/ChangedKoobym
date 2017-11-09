package com.example.joane14.myapplication.Adapters;

import android.content.Context;
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
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.BookOwnerModel;
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

public class BookChooserAdapter extends BaseAdapter {

    private Context context;
    List<SwapDetail> swapDetailList;
    SwapDetail swapDetailModel;
    private LayoutInflater mInflater;
    RatingBar mRating;
    Float retFloat;


    public static void d(String TAG, String message) {
        int maxLogSize = 2000;
        for(int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            android.util.Log.d(TAG, message.substring(start, end));
        }
    }
    public BookChooserAdapter(Context context, List<SwapDetail> swapDetailList){
        this.context = context;
        this.swapDetailList = swapDetailList;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount(){
        return swapDetailList.size();
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

        swapDetailModel = swapDetailList.get(position);

        ImageView bookPic = (ImageView) convertView.findViewById(R.id.displayBookPic);
        TextView bookTitle = (TextView) convertView.findViewById(R.id.lpBookTitle);
        TextView bookAuthor = (TextView) convertView.findViewById(R.id.lpAuthor);
        TextView statusBook = (TextView) convertView.findViewById(R.id.ratingStatusBook);
        LinearLayout statusLinear = (LinearLayout) convertView.findViewById(R.id.status_ll);

        mRating = (RatingBar) convertView.findViewById(R.id.rating_bookRating);
        Log.d("inside", "PrefferedAdapter");
        bookTitle.setText(swapDetailModel.getBookOwner().getBookObj().getBookTitle());

        statusBook.setText(swapDetailModel.getBookOwner().getStatus());
        if(swapDetailModel.getBookOwner().getStatus().equals("Rent")){
            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRent));
        }else if(swapDetailModel.getBookOwner().getStatus().equals("Swap")){
            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSwap));
        }else{
            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGray));
        }

        String author = " ";
        if(swapDetailModel.getBookOwner().getBookObj().getBookAuthor().size()!=0){
            for(int init=0; init<swapDetailModel.getBookOwner().getBookObj().getBookAuthor().size(); init++){
                if(!(swapDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                    author+=swapDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                    if(!(swapDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                        author+=swapDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                        if(init+1<swapDetailModel.getBookOwner().getBookObj().getBookAuthor().size()){
                            author+=", ";
                        }
                    }
                }
            }
        }else{
            author="Unknown Author";
        }
        bookAuthor.setText(author);

        Glide.with(context).load(swapDetailModel.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(bookPic);

        //getRatings();

        mRating.setRating(Float.parseFloat(String.valueOf(swapDetailModel.getBookOwner().getRate())));



        return convertView;
    }

    public void getRatings(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
        Log.d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_RATINGS+swapDetailModel.getBookOwner().getBookOwnerId();
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
                Log.d("RatingAdapter: "+swapDetailModel.getBookOwner().getBookObj().getBookTitle(), String.valueOf(fl));
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