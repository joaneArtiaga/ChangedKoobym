package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.BookActActivity;
import com.example.joane14.myapplication.Activities.BookReviewActivity;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.HistoryActivity;
import com.example.joane14.myapplication.Activities.ProfileActivity;
import com.example.joane14.myapplication.Activities.TransactionActivity;
import com.example.joane14.myapplication.Activities.UserReviewActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.AuctionComment;
import com.example.joane14.myapplication.Model.AuctionCommentDetail;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.AuctionHeader;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.Rate;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.Review;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserRating;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.util.Log.d;

/**
 * Created by Joane14 on 05/10/2017.
 */

public class ToReceiveAuctionAdapter extends RecyclerView.Adapter<ToReceiveAuctionAdapter.BookHolder> {

    public List<AuctionHeader> bookList;
    public List<AuctionComment> auctionHeaderList;
    AuctionHeader auctionHeader;
    float rateNumber;
    public Activity context;

    @Override
    public ToReceiveAuctionAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_activity_item, parent, false);

        this.context = (Activity) parent.getContext();
        auctionHeader = new AuctionHeader();
        Log.d("ToRecevieAuctionAdapter","inside");
        ToReceiveAuctionAdapter.BookHolder dataObjectHolder = new ToReceiveAuctionAdapter.BookHolder(this.context, view);

        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ToReceiveAuctionAdapter(List<AuctionHeader> myDataset) {
        bookList = myDataset;
        auctionHeaderList = new ArrayList<AuctionComment>();
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ToReceiveAuctionAdapter.BookHolder holder, final int position) {

        getMaximumBid(bookList.get(position).getAuctionDetail(), holder);
        if(bookList.get(position).getAuctionExtraMessage()!=null){
            holder.mBtnRate.setImageResource(R.drawable.checkbookact);
        }else{
            holder.mBtnRate.setImageResource(R.drawable.notrate);
        }


        holder.mBtnMail.setVisibility(View.GONE);

        holder.mBookTitle.setText(bookList.get(position).getAuctionDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mBookDate.setText(bookList.get(position).getAuctionHeaderDateStamp());
        holder.mBookRenter.setText(bookList.get(position).getAuctionDetail().getBookOwner().getUserObj().getUserFname()+" "+bookList.get(position).getAuctionDetail().getBookOwner().getUserObj().getUserLname());
        if(bookList.get(position).getDateDelivered()==null){
            Log.d("EndDateReceiveAuction", "walay sulod");
        }else{
            holder.mDate.setText(bookList.get(position).getDateDelivered());
        }

        if(bookList.get(position).getMeetUp()==null){

        }else{
            holder.mTime.setText(bookList.get(position).getMeetUp().getUserDayTime().getTime().getStrTime());
            holder.mLocation.setText(bookList.get(position).getMeetUp().getLocation().getLocationName());
        }

        Glide.with(context).load(bookList.get(position).getAuctionDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBook);

        holder.mBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userModelPass", bookList.get(position).getAuctionDetail().getBookOwner().getUserObj());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.mBtnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookList.get(position).getAuctionExtraMessage()!=null){
                    AuctionHeader auctionHeaderModel = new AuctionHeader();
                    auctionHeaderModel = bookList.get(position);
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setMessage("Will notify the owner that you received the book.");
                    final AuctionHeader finalAuctionHeaderModel = auctionHeaderModel;
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final Dialog dialogCustom = new Dialog(context);
                            dialogCustom.setContentView(R.layout.review_custom_dialog);
                            TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleReview);
                            TextView mAuthor = (TextView) dialogCustom.findViewById(R.id.bookAuthorReview);
                            ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookReview);
                            final EditText etReviewMessage = (EditText) dialogCustom.findViewById(R.id.etReviewReview);
                            final RatingBar mRateBar = (RatingBar) dialogCustom.findViewById(R.id.ratingReview);
                            Button mRateNow = (Button) dialogCustom.findViewById(R.id.btnRateReview);

                            etReviewMessage.setHint("Review owner");
                            mTitle.setText(finalAuctionHeaderModel.getAuctionDetail().getBookOwner().getBookObj().getBookTitle());

                            Glide.with(context).load(finalAuctionHeaderModel.getAuctionDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                            String author = "";

                            if(finalAuctionHeaderModel.getAuctionDetail().getBookOwner().getBookObj().getBookAuthor().size()!=0){
                                for(int init = 0; init< finalAuctionHeaderModel.getAuctionDetail().getBookOwner().getBookObj().getBookAuthor().size(); init++){
                                    if(!(finalAuctionHeaderModel.getAuctionDetail().getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                                        author+= finalAuctionHeaderModel.getAuctionDetail().getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                                        if(!(finalAuctionHeaderModel.getAuctionDetail().getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                                            author+= finalAuctionHeaderModel.getAuctionDetail().getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                                            if(init+1< finalAuctionHeaderModel.getAuctionDetail().getBookOwner().getBookObj().getBookAuthor().size()){
                                                author+=", ";
                                            }
                                        }
                                    }
                                }
                            }else{
                                author="Unknown Author";
                            }
                            mAuthor.setText(author);

                            mRateBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                    rateNumber = rating;
                                }
                            });

                            mRateNow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(etReviewMessage.getText().length()==0){
                                        etReviewMessage.setError("Fill necessary fields.");
                                    }
                                    if(rateNumber==0){
                                        Toast.makeText(context, "Should rate.", Toast.LENGTH_LONG);
                                    }

                                    if(etReviewMessage.getText().length()>0&&rateNumber>0){
                                        java.util.Calendar c = java.util.Calendar.getInstance();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                        Rate rateMod = new Rate();
                                        rateMod.setRateNumber(rateNumber);
                                        rateMod.setRateTimeStamp(sdf.format(c.getTime()));

                                        Review reviewMod = new Review();
                                        reviewMod.setReviewTimeStamp(sdf.format(c.getTime()));

                                        UserRating userRatingMod = new UserRating();

                                        userRatingMod.setComment(etReviewMessage.getText().toString());
                                        userRatingMod.setUserRater(finalAuctionHeaderModel.getUser());
                                        userRatingMod.setUser(finalAuctionHeaderModel.getAuctionDetail().getBookOwner().getUserObj());
                                        userRatingMod.setReview(reviewMod);
                                        userRatingMod.setRate(rateMod);

                                        userRate(userRatingMod, position);
                                    }

                                }
                            });

                            dialogCustom.show();
                        }
                    });
                    alertDialog.show();
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Alert!");
                    alertDialog.setMessage("The owner hasn't yet confirmed the delivery.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

    }

    public void getMaximumBid(final AuctionDetailModel auctionDetailModel, final ToReceiveAuctionAdapter.BookHolder holder) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.GET_MAXIMUM_BID + auctionDetailModel.getAuctionDetailId();

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

                auctionHeaderList.clear();
                auctionHeaderList.addAll(Arrays.asList(gson.fromJson(response, AuctionComment[].class)));

                holder.mPrice.setText("₱  "+auctionHeaderList.get(0).getAuctionComment()+".00");


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

    public void completed(AuctionHeader auctionHeader, UserRating ur) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.AUCTION_COMPLETED + auctionHeader.getAuctionHeaderId()+"/"+ur.getUserRatingId();

        rateNumber=0f;
        Log.d("auctionCompletedURL", URL);
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(auctionHeader);

        d("auctionCompleted", mRequestBody);
        int maxLogSize = 2000;
        for (int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("auctionCompleted", mRequestBody.substring(start, end));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("auctionCompletedRes", response);
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

    public void userRate(final UserRating userRating, final int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = Constants.POST_USER_RATE;


        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

        Log.d("addUserRating", URL);
        Log.d("addUserRatingModel", userRating.toString());


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(userRating);


        Log.d("LOG_VOLLEYuserRating", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("UserRatingResponse", response);

                UserRating ur = new UserRating();
                ur = gson.fromJson(response, UserRating.class);

                completed(bookList.get(position), ur);

                Intent intent = new Intent(context, BookActActivity.class);
                context.startActivity(intent);
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
        TextView mBookTitle, mBookRenter, mBookDate, mPrice, mLocation, mTime, mDate;
        ImageView mIvBook;
        ImageButton mBtnProfile, mBtnMail, mBtnRate;
        AuctionHeader auctionHeaderObj;
        Context context;


        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBookTitle = (TextView) itemView.findViewById(R.id.bookTitleBA);
            mDate = (TextView) itemView.findViewById(R.id.dateBA);
            mBookRenter = (TextView) itemView.findViewById(R.id.renterNameBA);
            mBookDate = (TextView) itemView.findViewById(R.id.bookDateBA);
            mLocation = (TextView) itemView.findViewById(R.id.locationBA);
            mPrice = (TextView) itemView.findViewById(R.id.bookPriceBA);
            mTime = (TextView) itemView.findViewById(R.id.timeBA);
            mIvBook = (ImageView) itemView.findViewById(R.id.ivBookBA);
            mBtnProfile = (ImageButton) itemView.findViewById(R.id.profileBA);
            mBtnMail = (ImageButton) itemView.findViewById(R.id.notifyBA);
            mBtnRate = (ImageButton) itemView.findViewById(R.id.rateButtonBA);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auctionHeaderObj = new AuctionHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside " + Integer.toString(position));
                    auctionHeaderObj = ToReceiveAuctionAdapter.this.bookList.get(position);
                    if (auctionHeaderObj == null) {
                        Log.d("reaceiveRentAdapter", "is null");
                    } else {
                        Log.d("reaceiveRentAdapter", "is not null");
                    }
                }
            });
        }

    }

}
