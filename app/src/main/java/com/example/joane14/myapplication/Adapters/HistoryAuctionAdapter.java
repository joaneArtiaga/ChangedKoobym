package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.HistoryAuction;
import com.example.joane14.myapplication.Model.AuctionComment;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.AuctionHeader;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.util.Log.d;

/**
 * Created by Joane14 on 05/10/2017.
 */

public class HistoryAuctionAdapter extends RecyclerView.Adapter<HistoryAuctionAdapter.BookHolder> {

    public List<AuctionHeader> bookList;
    public List<AuctionComment> bookCommentList;
    int price;
    public Activity context;

    @Override
    public HistoryAuctionAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_auction_item, parent, false);

        bookCommentList = new ArrayList<AuctionComment>();

        price = 0;
        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        HistoryAuctionAdapter.BookHolder dataObjectHolder = new HistoryAuctionAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public HistoryAuctionAdapter(List<AuctionHeader> myDataset) {
        bookList = myDataset;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(HistoryAuctionAdapter.BookHolder holder, int position) {

        getMaximumBid(bookList.get(position).getAuctionDetail(), holder);

        holder.mBookTitle.setText(bookList.get(position).getAuctionDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mDate.setText(bookList.get(position).getAuctionHeaderDateStamp());


//        Picasso.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getImageFilename()).fit().into(holder.mIvRenter);
        Glide.with(context).load(bookList.get(position).getAuctionDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

    }

    public void getMaximumBid(final AuctionDetailModel auctionDetailModel, final HistoryAuctionAdapter.BookHolder holder) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.GET_MAXIMUM_BID + auctionDetailModel.getAuctionDetailId();

        Log.d("URLmaximum", URL);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(auctionDetailModel);

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

                bookCommentList.clear();
                bookCommentList.addAll(Arrays.asList(gson.fromJson(response, AuctionComment[].class)));
//                holder.mPrice.setText(bookCommentList.get(0).getAuctionComment());

                if(bookCommentList.size()==0){
                    Log.d("emptyComment", "true");
                }else{
                    price = bookCommentList.get(0).getAuctionComment();
                    holder.mPrice.setText(price+"");
                    Log.d("emptyComment", "false");
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

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mBookTitle, mDate, mPrice;
        ImageView mIvBookImg;
        AuctionHeader auctionHeaderObj;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
//            mRenter = (TextView) itemView.findViewById(R.id.completeRenter);
//            mRenterText = (TextView) itemView.findViewById(R.id.textRenter);
            mBookTitle = (TextView) itemView.findViewById(R.id.bookTitleHA);
            mDate = (TextView) itemView.findViewById(R.id.bookDateHA);
            mPrice = (TextView) itemView.findViewById(R.id.bookPriceHA);
//            mIvRenter = (ImageView) itemView.findViewById(R.id.completeRenterImage);
            mIvBookImg = (ImageView) itemView.findViewById(R.id.bookImageHA);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auctionHeaderObj = new AuctionHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    auctionHeaderObj = HistoryAuctionAdapter.this.bookList.get(position);
                    if(auctionHeaderObj==null){
                        Log.d("auctionHeaderAdapter", "is null");
                    }else{
                        Log.d("auctionHeaderAdapter", "is not null");
                    }
//                    intent.putExtra("ViewBook", "fromAdapter");
//                    bundle.putSerializable("View", rentalDetailObj);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
                }
            });
        }

//        @Override
//        public void onClick(View v) {
//            int position = getAdapterPosition();
//
//
//            Log.d("AdapterPosition", "inside "+Integer.toString(position));
//
//        }
    }

    public String getDetails(int position){
        String result = "";

        result = bookList.get(position).toString();



        return result;

    }
}
