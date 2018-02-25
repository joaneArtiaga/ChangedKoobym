package com.example.joane14.myapplication.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.HistoryActivity;
import com.example.joane14.myapplication.Activities.MyShelf;
import com.example.joane14.myapplication.Activities.ProfileActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.AuctionHeader;
import com.example.joane14.myapplication.Model.Rate;
import com.example.joane14.myapplication.Model.Review;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserNotification;
import com.example.joane14.myapplication.Model.UserRating;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.util.Log.d;

/**
 * Created by Joane14 on 09/10/2017.
 */

public class ToReceiveSwapAdapter extends RecyclerView.Adapter<ToReceiveSwapAdapter.BookHolder> {

    public List<SwapHeader> bookList;
    SwapHeader swapHeader;
    float rateNumber;
    public Activity context;

    @Override
    public ToReceiveSwapAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_activity_item, parent, false);

        this.context = (Activity) parent.getContext();
        swapHeader = new SwapHeader();
        Log.d("LandingPAgeAdapter","inside");
        ToReceiveSwapAdapter.BookHolder dataObjectHolder = new ToReceiveSwapAdapter.BookHolder(this.context, view);

        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ToReceiveSwapAdapter(List<SwapHeader> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(ToReceiveSwapAdapter.BookHolder holder, final int position) {

        if(bookList.get(position).getSwapExtraMessage()!=null){
            holder.mRate.setImageResource(R.drawable.checkbookact);
        }else{
            holder.mRate.setImageResource(R.drawable.notrate);
        }

        holder.mNotify.setVisibility(View.GONE);

        holder.mBookTitle.setText(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mRenter.setText(bookList.get(position).getSwapDetail().getBookOwner().getUserObj().getUserFname()+" "+bookList.get(position).getSwapDetail().getBookOwner().getUserObj().getUserLname());
        holder.mBookDate.setText(bookList.get(position).getDateTimeStamp());
        holder.mPrice.setVisibility(View.GONE);
        if(bookList.get(position).getDateDelivered()==null){
            Log.d("EndDateReceiveSwap", "walay sulod");
        }else{
            holder.mDate.setText(bookList.get(position).getDateDelivered());
        }

        Glide.with(context).load(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

        if(bookList.get(position).getMeetUp()==null){

        }else{
            holder.mLocation.setText(bookList.get(position).getMeetUp().getLocation().getLocationName());
            holder.mTime.setText(bookList.get(position).getMeetUp().getUserDayTime().getTime().getStrTime());
        }

        holder.mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userModelPass", bookList.get(position).getSwapDetail().getBookOwner().getUserObj());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.mRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookList.get(position).getSwapExtraMessage()!=null){
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Confirmation");
                    alertDialog.setMessage("Did you receive the book?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            completed(bookList.get(position));
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Not Yet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
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

    public void completed(SwapHeader swapHeader) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.SWAP_COMPLETED + swapHeader.getSwapHeaderId();

        rateNumber=0f;
        Log.d("swapCompletedURL", URL);
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeader);

        d("swapCompleted", mRequestBody);
        int maxLogSize = 2000;
        for (int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("swapCompleted", mRequestBody.substring(start, end));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("swapCompletedRes", response);
                final SwapHeader swapHeaderModel = gson.fromJson(response, SwapHeader.class);

                final Dialog dialogCustom = new Dialog(context);
                dialogCustom.setContentView(R.layout.review_custom_dialog);
                TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleReview);
                TextView mAuthor = (TextView) dialogCustom.findViewById(R.id.bookAuthorReview);
                ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookReview);
                final EditText etReviewMessage = (EditText) dialogCustom.findViewById(R.id.etReviewReview);
                final RatingBar mRateBar = (RatingBar) dialogCustom.findViewById(R.id.ratingReview);
                Button mRateNow = (Button) dialogCustom.findViewById(R.id.btnRateReview);

                etReviewMessage.setHint("Review user");
                mTitle.setText(swapHeaderModel.getSwapDetail().getBookOwner().getBookObj().getBookTitle());

                Glide.with(context).load(swapHeaderModel.getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                String author = "";

                if(swapHeaderModel.getSwapDetail().getBookOwner().getBookObj().getBookAuthor().size()!=0){
                    for(int init=0; init<swapHeaderModel.getSwapDetail().getBookOwner().getBookObj().getBookAuthor().size(); init++){
                        if(!(swapHeaderModel.getSwapDetail().getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                            author+=swapHeaderModel.getSwapDetail().getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                            if(!(swapHeaderModel.getSwapDetail().getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                                author+=swapHeaderModel.getSwapDetail().getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                                if(init+1<swapHeaderModel.getSwapDetail().getBookOwner().getBookObj().getBookAuthor().size()){
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
                            userRatingMod.setUserRater(swapHeaderModel.getUser());
                            userRatingMod.setUser(swapHeaderModel.getRequestedSwapDetail().getBookOwner().getUserObj());
                            userRatingMod.setReview(reviewMod);
                            userRatingMod.setRate(rateMod);

                            userRate(userRatingMod);
                        }

                    }
                });

                dialogCustom.show();

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

    public void userRate(final UserRating userRating) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
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

                Intent intent = new Intent(context, BookActActivity.class);
                context.startActivity(intent);
//                    if (bool == false) {
//                        Intent intent = new Intent(context, HistoryActivity.class);
//                        context.startActivity(intent);
//                    }
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
        TextView mBookTitle, mRenter, mBookDate, mPrice, mLocation, mTime, mDate;
        ImageView mIvBookImg;
        ImageButton mProfile, mNotify, mRate;
        SwapHeader swapHeaderObj;
        Context context;


        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBookTitle = (TextView) itemView.findViewById(R.id.bookTitleBA);
            mDate = (TextView) itemView.findViewById(R.id.dateBA);
            mRenter = (TextView) itemView.findViewById(R.id.renterNameBA);
//            mRenter = (TextView) itemView.findViewById(R.id.toReceiveRenter);
            mBookDate = (TextView) itemView.findViewById(R.id.bookDateBA);
            mLocation = (TextView) itemView.findViewById(R.id.locationBA);
            mPrice = (TextView) itemView.findViewById(R.id.bookPriceBA);
            mTime = (TextView) itemView.findViewById(R.id.timeBA);
//            mIvRenter = (ImageView) itemView.findViewById(R.id.toReceiveRenterImage);
            mIvBookImg = (ImageView) itemView.findViewById(R.id.ivBookBA);
            mProfile = (ImageButton) itemView.findViewById(R.id.profileBA);
            mNotify = (ImageButton) itemView.findViewById(R.id.notifyBA);
            mRate = (ImageButton) itemView.findViewById(R.id.rateButtonBA);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swapHeaderObj = new SwapHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    swapHeaderObj = ToReceiveSwapAdapter.this.bookList.get(position);
                    if(swapHeaderObj==null){
                        Log.d("rentalHeaderAdapter", "is null");
                    }else{
                        Log.d("rentalHeaderAdapter", "is not null");
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

    public void updateReceive(final int position, final Boolean bool){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
        swapHeader = bookList.get(position);
        String status="";
        if(swapHeader.getStatus().equals("Approved")){
            if(bool==true){
                status = "Complete";
            }else{
                status = "Rejected";
            }
        }

        String URL = Constants.UPDATE_SWAP_HEADER+"/"+status+"/"+swapHeader.getSwapHeaderId();

        Log.d("Update SwapHeader URL", URL);


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeader);


        Log.d("LOG_VOLLEY_swapHeaderUD", mRequestBody);
        Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RequestReceivedStatus", response);

                if(bool==true){
                    updateBookOwner(position, true);
                    updateBookOwner(position, false);
                }else{
                    Intent intent = new Intent(context, HistoryActivity.class);
                    context.startActivity(intent);
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



    public void updateBookOwner(int position, final boolean bool){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
        swapHeader = bookList.get(position);
        String status="", URL = "";

        if(bool==true){
            URL = Constants.UPDATE_BOOK_OWNER+"/"+swapHeader.getSwapDetail().getBookOwner().getBookOwnerId()+"/"+swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getUserId();
        }else{
            URL = Constants.UPDATE_BOOK_OWNER+"/"+swapHeader.getRequestedSwapDetail().getBookOwner().getBookOwnerId()+"/"+swapHeader.getSwapDetail().getBookOwner().getUserObj().getUserId();
        }

        Log.d("Update SwapHeader URL", URL);


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeader);


        Log.d("LOG_VOLLEY_swapHeaderUD", mRequestBody);
        Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RequestReceivedStatus", response);
                if(bool==false){
                    Intent intent = new Intent(context, MyShelf.class);
                    context.startActivity(intent);
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



}
