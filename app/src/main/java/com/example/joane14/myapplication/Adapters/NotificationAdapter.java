package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.joane14.myapplication.Activities.AuctionMeetUpChooser;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.LandingPage;
import com.example.joane14.myapplication.Activities.MeetUpChooser;
import com.example.joane14.myapplication.Activities.NotificationAct;
import com.example.joane14.myapplication.Activities.ProfileActivity;
import com.example.joane14.myapplication.Activities.SwapMeetUpChooser;
import com.example.joane14.myapplication.Activities.ViewAuctionBook;
import com.example.joane14.myapplication.Activities.ViewBookAct;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.AuctionHeader;
import com.example.joane14.myapplication.Model.BookOwnerRating;
import com.example.joane14.myapplication.Model.BookOwnerReview;
import com.example.joane14.myapplication.Model.RentalHeader;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.util.Log.d;

/**
 * Created by Joane14 on 12/10/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.BookHolder> {

    public List<UserNotification> userNotificationList;
    BookOwnerReview bookOwnerReview;
    SwapHeader swapHeaderModelOut;
    AuctionHeader auctionHeaderModel;
    RentalHeader rentalHeaderObj;
    AuctionDetailModel auctionDetailModel;
    String message = "";

    public Activity context;

    @Override
    public NotificationAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_notification, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter", "inside");
        NotificationAdapter.BookHolder dataObjectHolder = new NotificationAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(userNotificationList.size()));
        return dataObjectHolder;
    }

    public NotificationAdapter(List<UserNotification> myDataset) {
        userNotificationList = myDataset;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(NotificationAdapter.BookHolder holder, final int position) {

        UserNotification userNotification = userNotificationList.get(position);

        String dateStr = "";
        Calendar c = Calendar.getInstance();
        @SuppressLint({"NewApi", "LocalSuppress"})
        DateFormat format = new android.icu.text.SimpleDateFormat("yyyy-MM-dd");
        dateStr = format.format(c.getTime());

        swapHeaderModelOut = new SwapHeader();
        auctionDetailModel = new AuctionDetailModel();
        auctionHeaderModel = new AuctionHeader();
        rentalHeaderObj = new RentalHeader();


        if (userNotification.getActionName().equals("rental")) {
            Log.d("notifRent", "inside");
            if (userNotificationList.get(position).getActionStatus().equals("request") || userNotificationList.get(position).getActionStatus().equals("Request")) {
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + "ed your book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be rented.";
            } else if (userNotification.getActionStatus().equals("Approved")) {
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + " your request of the book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be rented.";
            } else if (userNotification.getActionStatus().equals("Confirm")) {
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + "ed the meet up location and time with the book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be rented.";
            }else if(userNotification.getActionStatus().equals("Due")){
                message = "The book you rented "+userNotification.getBookActionPerformedOn().getBookObj().getBookTitle()+" is already due, " +
                        "your deposit will be deducted.";
            }else if(userNotification.getActionStatus().equals("return")){
                message = userNotification.getUserPerformer().getUserFname()+" "+userNotification.getUserPerformer().getUserLname()+" wants to return the book earlier.";
            }else if(userNotification.getActionStatus().equalsIgnoreCase("rejected")){
                message = userNotification.getUserPerformer().getUserFname()+" "+userNotification.getUserPerformer().getUserLname()+" rejected your request.";
            }else if(userNotification.getActionStatus().equals("return-confirmed")){
                message = userNotification.getUserPerformer().getUserFname()+" "+userNotification.getUserPerformer().getUserLname()+" accepted your request for the book, "+userNotification.getBookActionPerformedOn().getBookObj().getBookTitle()+", to be returned early.";
            }else if(userNotification.getActionStatus().equals("return-rejected")){
                message = userNotification.getUserPerformer().getUserFname()+" "+userNotification.getUserPerformer().getUserLname()+" rejected your request for the book, "+userNotification.getBookActionPerformedOn().getBookObj().getBookTitle()+", to be returned early.";
            }else if(userNotification.getActionStatus().equals("Complete")){
                message = userNotification.getUserPerformer().getUserFname()+" "+userNotification.getUserPerformer().getUserLname()+" received the book,"+userNotification.getBookActionPerformedOn().getBookObj().getBookTitle()+", and rated you.";
            }else if(userNotification.getActionStatus().equals("delivered")){
                message = userNotification.getUserPerformer().getUserFname()+" "+userNotification.getUserPerformer().getUserLname()+" delivered the book,"+userNotification.getBookActionPerformedOn().getBookObj().getBookTitle()+".";
            }else if(userNotification.getActionStatus().equals("returned")){
                message = userNotification.getUserPerformer().getUserFname()+" "+userNotification.getUserPerformer().getUserLname()+" returned the book,"+userNotification.getBookActionPerformedOn().getBookObj().getBookTitle()+" and rated the book.";
            }
        } else if (userNotification.getActionName().equals("swap")) {
            Log.d("notifSwap", "inside");
            if (userNotificationList.get(position).getActionStatus().equals("request") || userNotificationList.get(position).getActionStatus().equals("Request")) {
//                getSwapHeader(position, userNotification);
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + "ed your book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be swapped.";
            } else if (userNotification.getActionStatus().equals("Approved")) {
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + " your request of the book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be swapped.";
            } else if (userNotification.getActionStatus().equals("Confirm")) {
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + "ed the meet up location and time with the book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be swapped.";
            } else if (userNotification.getActionStatus().equals("ToGive")) {
                message = "The book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " which is to be swapped should be delivered today, " + dateStr + " to " + userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname();
            } else if (userNotification.getActionStatus().equals("ToReceive")) {
                message = "The book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " was delivered  by " + userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname();
            } else if (userNotification.getActionStatus().equals("Rejected")) {
                message = "The book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " was Rejected by " + userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname();
            } else if (userNotification.getActionStatus().equals("Completed")) {
                message = "The transaction of the book that you want to " + userNotification.getActionName() + " is completed";
            } else if(userNotification.getActionStatus().equals("delivered")){
                message = userNotification.getUserPerformer().getUserFname()+" "+userNotification.getUserPerformer().getUserLname()+" delivered the book, "+userNotification.getBookActionPerformedOn().getBookObj().getBookTitle()+".";
            }
        } else if(userNotification.getActionName().equals("auction")){
            User winner = new User();
            Log.d("notifAuc", "inside");
            if(userNotificationList.get(position).getActionStatus().equals("win")){
                Log.d("notifAuc", "win");
                message = "The book, "+userNotification.getBookActionPerformedOn().getBookObj().getBookTitle()+", you tried bidding just ended. Your bid fortunately won which is "+userNotification.getExtraMessage();
            }else if(userNotificationList.get(position).getActionStatus().equals("lose")){
                Log.d("notifAuc", "lose");
                message = "The book, "+userNotification.getBookActionPerformedOn().getBookObj().getBookTitle()+", you tried bidding just ended. Your bid unfortunately lost.";
            }else if(userNotificationList.get(position).getActionStatus().equals("own")){
                message = "The book, "+userNotification.getBookActionPerformedOn().getBookObj().getBookTitle()+", you auctioned just ended. The highest bid is "+userNotification.getExtraMessage()+" by "+userNotification.getUserPerformer().getUserFname()+" "+userNotification.getUserPerformer().getUserLname();
            }else if(userNotification.getActionStatus().equals("delivered")){
                message = userNotification.getUserPerformer().getUserFname()+" "+userNotification.getUserPerformer().getUserLname()+" delivered the book, "+userNotification.getBookActionPerformedOn().getBookObj().getBookTitle()+".";
            }else if(userNotification.getActionStatus().equals("Complete")){
                message = "The transaction of the book that you want to " + userNotification.getActionName() + " is completed";
            }
        }


        holder.mMessage.setText(message);
        Picasso.with(context).load(userNotificationList.get(position).getUserPerformer().getImageFilename()).fit().into(holder.mPerformer);
        Picasso.with(context).load(userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookFilename()).fit().into(holder.mBookPerformed);

        if (userNotification.getRead() == true) {
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            Log.d("cardRead", "true");
        } else if (userNotification.getRead() == false) {
            holder.mCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorMint));
            Log.d("cardRead", "true");
        }
    }
    public void getSwapHeader(final int position, final UserNotification userNotification) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

        String URL = Constants.GET_SWAP_DETAIL + userNotificationList.get(position).getActionId();

        d("getSwapHeader", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        final SwapHeader swapHeader = new SwapHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeader);


        d("LOG_VOLLEY", mRequestBody);
        final User finalUser = user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("swapHeaderResponseId", response);
                SwapHeader swapHeaderMod = gson.fromJson(response, SwapHeader.class);
                swapHeaderModelOut = swapHeaderMod;
                message = userNotification.getUserPerformer().getUserFname() + " " + userNotification.getUserPerformer().getUserLname() + " " + userNotification.getActionStatus() + "ed your book " + userNotification.getBookActionPerformedOn().getBookObj().getBookTitle() + " to be swapped with your book "+swapHeaderModelOut.getSwapDetail().getBookOwner().getBookObj().getBookTitle()+".";
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

    @Override
    public int getItemCount() {
        return userNotificationList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mMessage;
        ImageView mPerformer, mBookPerformed;
        CardView mCardView;
        float rateNumber;
        Context context;


        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
//            final String rateNumber="";

            rateNumber = 0f;
            mMessage = (TextView) itemView.findViewById(R.id.notifMessage);
            mPerformer = (ImageView) itemView.findViewById(R.id.notifPerformer);
            mBookPerformed = (ImageView) itemView.findViewById(R.id.notifBookPerformer);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    rentalHeaderObj = new RentalHeader();
//                    Bundle bundle = new Bundle();
//                    int position = getAdapterPosition();
//                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
//                    intent.putExtra("ViewBook", "fromAdapter");
//                    bundle.putSerializable("View", rentalDetailObj);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
                    final int position = getAdapterPosition();
                    Log.d("AdapterNotif", userNotificationList.get(position).getUserPerformer().getUserFname());

                    User user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
                    if (user.getUserId() == userNotificationList.get(position).getUser().getUserId()) {

                        if (userNotificationList.get(position).getActionName().equals("rental")) {
                            Log.d("RentalAdapter", "Inside");

                            if (userNotificationList.get(position).getActionStatus().equals("Request")) {
                                createDialog(position);
                            } else if (userNotificationList.get(position).getActionStatus().equals("Approved")) {
                                Log.d("ApprovedNotif", "Inside");
                                getRentalHeader(position, "meetUp");
                                updateReceive(position, "Confirm");
                                getRead(position);
                            } else if (userNotificationList.get(position).getActionStatus().equals("Confirm")) {
                                Log.d("Confirm", "Inside");
                                getRentalHeader(position, "summary");
                                getRead(position);
                            }else if(userNotificationList.get(position).getActionStatus().equals("return")){
                                getRead(position);
                                getRentalHeader(position, "return");
                            }else if(userNotificationList.get(position).getActionStatus().equals("return-confirmed")){
                                getRead(position);
                                getRentalHeader(position, "return-confirmed");
                            }else if(userNotificationList.get(position).getActionStatus().equals("return-rejected")){
                                getRead(position);
                                getRentalHeader(position, "return-rejected");
                            }else if(userNotificationList.get(position).getActionStatus().equals("returned")){
                                getRead(position);
                                getRentalHeader(position, "returned");
                            }else if(userNotificationList.get(position).getActionStatus().equals("Complete")){
                                getUserRating(position);
                            }else if(userNotificationList.get(position).getActionStatus().equals("delivered")){
                                getRead(position);
                                getRentalHeader(position, "delivered");
                            }

                        } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                            Log.d("SwapAdapter", "Inside");
                            if (userNotificationList.get(position).getActionName().equals("swap")) {

                                if (userNotificationList.get(position).getActionStatus().equals("Request")) {
                                    acceptSwap(position);
                                } else if (userNotificationList.get(position).getActionStatus().equals("Approved")) {
                                    Log.d("ApprovedNotif", "Inside");
                                    getSwapHeader(position, "meetUp");
                                    updateReceive(position, "Confirm");
                                    getRead(position);
                                } else if (userNotificationList.get(position).getActionStatus().equals("Confirm")) {
                                    Log.d("ConfirmNotif", "Inside");
                                    getRead(position);
//                                    getSwapHeader(position, "summ");
                                    toDeliverDialog(position);
                                } else if (userNotificationList.get(position).getActionStatus().equals("ToReceive")) {
                                    Log.d("ToReceiveNotif", "Inside");
                                    toGiveDialog(position);
                                    getRead(position);
                                    getSwapHeader(position, "change");
                                } else if (userNotificationList.get(position).getActionStatus().equals("Completed")) {
                                    Log.d("CompletedNotif", "Inside");
                                    getRead(position);
                                    getSwapHeader(position, "Completed");
//                                    getSwapHeader(position, "change");
                                }else if(userNotificationList.get(position).getActionStatus().equals("delivered")){
                                    getRead(position);
                                    getSwapHeader(position, "delivered");
                                }
                            }
                        }else if(userNotificationList.get(position).getActionName().equals("auction")){
                            if(userNotificationList.get(position).getActionStatus().equals("win")){
                                getRead(position);
                                getAuctionHeader(position, userNotificationList.get(position).getActionStatus());
                            }else if(userNotificationList.get(position).getActionStatus().equals("lose")){
                                getRead(position);
                                getAuctionHeader(position, userNotificationList.get(position).getActionStatus());
                            }else if(userNotificationList.get(position).getActionStatus().equals("own")){
                                getRead(position);
                                getAuctionHeader(position, userNotificationList.get(position).getActionStatus());
                            }else if(userNotificationList.get(position).getActionStatus().equals("delivered")){
                                getRead(position);
                                getAuctionHeader(position, "delivered");
                            }else if(userNotificationList.get(position).getActionStatus().equals("Complete")){
                                getRead(position);
                                getAuctionHeader(position, "Completed");
                            }
                        }
                    } else {

                        if (userNotificationList.get(position).getActionName().equals("rental")) {
                            getRentalHeader(position, "view");
                        } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                            getSwapHeader(position, "view");
                        }else if(userNotificationList.get(position).getActionName().equals("auction")){
                            getAuctionHeader(position, "view");
                        }
                    }

                }
            });
        }

        public void getUserRating(final int position) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
            String URL = Constants.GET_USER_RATING_BY_ID+userNotificationList.get(position).getExtraMessage();
            String nextDateStr = "";

            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

            Log.d("getUserRatingURL", URL);


            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(userNotificationList);


            Log.d("LOG_VOLLEY getUserRate", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("getUserRatingResponse", response);
                    final UserRating userRating = gson.fromJson(response, UserRating.class);

                    AlertDialog ad = new AlertDialog.Builder(context).create();
                    ad.setTitle("Complete Transaction");
                    ad.setMessage("The book, "+userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookTitle()+" was received and "+userNotificationList.get(position).getUserPerformer().getUserFname()+" "+userNotificationList.get(position).getUserPerformer().getUserLname()+" rated you.");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "See Rating", new DialogInterface.OnClickListener() {
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

                            mRateNow.setText("Okay");
                            etReviewMessage.setHint(userRating.getComment());
                            etReviewMessage.setFocusable(false);

                            mTitle.setText(userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookTitle());

                            Glide.with(context).load(userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                            String author = "";

                            if(userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().size()!=0){
                                for(int init=0; init<userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().size(); init++){
                                    if(!(userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                                        author+=userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                                        if(!(userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                                            author+=userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().get(init).getAuthorLName();
                                            if(init+1<userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().size()){
                                                author+=", ";
                                            }
                                        }
                                    }
                                }
                            }else{
                                author="Unknown Author";
                            }
                            mAuthor.setText(author);

                            mRateBar.setRating(userRating.getRate().getRateNumber());

                            mRateNow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, ProfileActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("userModelPass", userRating.getUser());
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }
                            });

                            dialogCustom.show();
                        }
                    });
                    ad.show();

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

                    setToComplete(position, ur.getUserRatingId());

                    Intent intent = new Intent(context, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userModelPass", userRating.getUser());
                    intent.putExtras(bundle);
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

        public void updateBookOwner(int position, final boolean bool, SwapHeader swapHeader) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
            String status = "", URL = "";

            if (bool == true) {
                URL = Constants.UPDATE_BOOK_OWNER + "/" + swapHeader.getSwapDetail().getBookOwner().getBookOwnerId() + "/" + swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getUserId();
            } else {
                URL = Constants.UPDATE_BOOK_OWNER + "/" + swapHeader.getRequestedSwapDetail().getBookOwner().getBookOwnerId() + "/" + swapHeader.getSwapDetail().getBookOwner().getUserObj().getUserId();
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

        public void meetUpSumm(final int position, SwapHeader swapHeaderSumm) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("MeetUp Summary");
            alertDialogBuilder.setMessage("Date:\t" +swapHeaderSumm.getDateDelivered()+
                    "\n\nDay:\t"+swapHeaderSumm.getMeetUp().getUserDayTime().getDay().getStrDay()+
                    "\n\nTime:\t"+swapHeaderSumm.getMeetUp().getUserDayTime().getTime().getStrTime()+
                    "\n\nLocation:\t"+swapHeaderSumm.getMeetUp().getLocation().getLocationName());
            alertDialogBuilder.setPositiveButton("Okay",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            getRead(position);
                            if (userNotificationList.get(position).getActionName().equals("rental")) {
                                updateReceive(position, "Approved");
                            } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                                updateSwap(position, "ToReceive");
                            }else if(userNotificationList.get(position).getActionName().equals("auction")){

                            }
                            Intent intent = new Intent(context, NotificationAct.class);
                            context.startActivity(intent);
                        }
                    });
//            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    getRead(position);
//                    updateReceive(position, "Rejected");
//                    Intent intent = new Intent(context, NotificationAct.class);
//                    context.startActivity(intent);
//                }
//            });

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        public void meetUpAuctionSumm(final int position, AuctionHeader auctionHeader) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("MeetUp Summary");
            alertDialogBuilder.setMessage("Date:\t" +auctionHeader.getDateDelivered()+
                    "\n\nDay:\t"+auctionHeader.getMeetUp().getUserDayTime().getDay().getStrDay()+
                    "\n\nTime:\t"+auctionHeader.getMeetUp().getUserDayTime().getTime().getStrTime()+
                    "\n\nLocation:\t"+auctionHeader.getMeetUp().getLocation().getLocationName());
            alertDialogBuilder.setPositiveButton("Okay",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            getRead(position);
                            if (userNotificationList.get(position).getActionName().equals("rental")) {
                                updateReceive(position, "Approved");
                            } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                                updateSwap(position, "ToReceive");
                            }else if(userNotificationList.get(position).getActionName().equals("auction")){

                            }
                            Intent intent = new Intent(context, NotificationAct.class);
                            context.startActivity(intent);
                        }
                    });
//            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    getRead(position);
//                    updateReceive(position, "Rejected");
//                    Intent intent = new Intent(context, NotificationAct.class);
//                    context.startActivity(intent);
//                }
//            });

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        public void toDeliverDialog(final int position) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Did you deliver the book " + userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookTitle() + " to " + userNotificationList.get(position).getUserPerformer().getUserFname() + " " + userNotificationList.get(position).getUserPerformer().getUserLname() + " on time?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            getRead(position);
                            if (userNotificationList.get(position).getActionName().equals("rental")) {
                                updateReceive(position, "Approved");
                            } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                                updateSwap(position, "ToReceive");
                            }
                            Intent intent = new Intent(context, NotificationAct.class);
                            context.startActivity(intent);
                        }
                    });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getRead(position);
                    updateReceive(position, "Rejected");
                    Intent intent = new Intent(context, NotificationAct.class);
                    context.startActivity(intent);
                }
            });

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        public void toGiveDialog(final int position) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setMessage("Did you receive the book " + userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookTitle() + " that was delivered by " + userNotificationList.get(position).getUserPerformer().getUserFname() + " " + userNotificationList.get(position).getUserPerformer().getUserLname() + "?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            getRead(position);
                            if (userNotificationList.get(position).getActionName().equals("rental")) {
                                Log.d("InsideGiveDialog","rental");
                                updateReceive(position, "Approved");
                            } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                                updateSwap(position, "Complete");
                                getSwapHeader(position, "Complete");
                                Log.d("InsideGiveDialog","swap");
                            }
                        }
                    });
            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getRead(position);
                    updateReceive(position, "Rejected");
                    Intent intent = new Intent(context, LandingPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromRegister", false);
                    intent.putExtra("user", bundle);
                    context.startActivity(intent);
                }
            });

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        public void createDialog(final int position) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Request");
            alertDialogBuilder.setMessage("Do you want to approve " + userNotificationList.get(position).getUserPerformer().getUserFname() + " " + userNotificationList.get(position).getUserPerformer().getUserLname() + "'s " + userNotificationList.get(position).getActionStatus() + "?");
            alertDialogBuilder.setPositiveButton("Approve",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            getRead(position);
                            if (userNotificationList.get(position).getActionName().equals("rental")) {
                                updateReceive(position, "Approved");
                                AlertDialog ad = new AlertDialog.Builder(context).create();
                                ad.setTitle("Approval");
                                ad.setMessage("You just approved "+userNotificationList.get(position).getUserPerformer().getUserFname()+" "+userNotificationList.get(position).getUserPerformer().getUserLname() + "'s " + userNotificationList.get(position).getActionStatus());
                                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(context, LandingPage.class);
                                        Bundle bond = new Bundle();
                                        bond.putBoolean("fromRegister", false);
                                        intent.putExtra("user", bond);
                                        context.startActivity(intent);
                                    }
                                });
                                ad.show();
                            } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                                updateSwap(position, "Approved");
                            }
                        }
                    });
            alertDialogBuilder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getRead(position);
                    updateReceive(position, "Rejected");
                    Intent intent = new Intent(context, NotificationAct.class);
                    context.startActivity(intent);
                }
            });

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        public void acceptSwap(final int position) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Swap Request");
            alertDialogBuilder.setMessage("Do you want to accept " + userNotificationList.get(position).getUserPerformer().getUserFname() + " " + userNotificationList.get(position).getUserPerformer().getUserLname() + "'s " + userNotificationList.get(position).getActionStatus() + "?");
            alertDialogBuilder.setPositiveButton("Accept",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            getRead(position);
                            if (userNotificationList.get(position).getActionName().equals("rental")) {
                                updateReceive(position, "Approved");
                            } else if (userNotificationList.get(position).getActionName().equals("swap")) {
                                updateSwap(position, "Approved");
                            }
                            Intent intent = new Intent(context, LandingPage.class);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("fromRegister", false);
                            intent.putExtra("user", bundle);
                            context.startActivity(intent);
                        }
                    });
            alertDialogBuilder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getRead(position);
                    updateReceive(position, "Rejected");
                    Intent intent = new Intent(context, NotificationAct.class);
                    context.startActivity(intent);
                }
            });

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        public void swapOwners(SwapHeader swapHeaderModule) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.SWAP_OWNER+swapHeaderModule.getSwapHeaderId();
            String nextDateStr = "";

            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

            Log.d("SwapOwnerURL", URL);


            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(userNotificationList);


            Log.d("LOG_VOLLEY_swapHeaderUD", mRequestBody);
            Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("UpdateSwapOwner", response);
                    SwapHeader swapHeaderMod = gson.fromJson(response, SwapHeader.class);

//                    getSwapHeader(position, "view");
                    Intent intent = new Intent(context, LandingPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromRegister", false);
                    intent.putExtra("user", bundle);
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

        @SuppressLint("NewApi")
        public void updateSwap(final int position, final String status) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";
            String nextDateStr = "";

            Calendar c = Calendar.getInstance();
            @SuppressLint({"NewApi", "LocalSuppress"})
            DateFormat format = new android.icu.text.SimpleDateFormat("yyyy-MM-dd");
            nextDateStr = format.format(c.getTime());

            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
            String URL = Constants.UPDATE_SWAP_HEADER + "/" + status + "/" + userNotificationList.get(position).getActionId() + "/" + nextDateStr;

            Log.d("UpdateSwapHeaderURL", URL);


            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(userNotificationList);


            Log.d("LOG_VOLLEY_swapHeaderUD", mRequestBody);
            Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("UpdateSwapHeader", response);
                    SwapHeader swapHeaderMod = gson.fromJson(response, SwapHeader.class);

//                    getSwapHeader(position, "view");

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
        public void updateReceive(final int position, String status) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";
            String nextDateStr = "";

            Calendar c = Calendar.getInstance();
            @SuppressLint({"NewApi", "LocalSuppress"})
            DateFormat format = new android.icu.text.SimpleDateFormat("yyyy-MM-dd");
            nextDateStr = format.format(c.getTime());

            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
            String URL = Constants.UPDATE_RENTAL_HEADER + "/" + userNotificationList.get(position).getActionId() + "/" + status + "/" + nextDateStr;

            Log.d("UpdateRentalHeaderURL", URL);


            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(userNotificationList);


            Log.d("LOG_VOLLEY_swapHeaderUD", mRequestBody);
            Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("UpdateRentalHeader", response);
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

        public void getRead(int position) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
            d("UserIdReceive", String.valueOf(user.getUserId()));
            String URL = Constants.PUT_USER_READ + userNotificationList.get(position).getUserNotificationId();

            d("CountURL", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

            final RentalHeader rentalHeader = new RentalHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(rentalHeader);


            d("LOG_VOLLEY", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("userNotificationRead", response);
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

        public void getRentalHeader(final int position, final String status) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

            String URL = Constants.GET_RENTAL_DETAIL + userNotificationList.get(position).getActionId();

            d("getRentalHeader", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

            final RentalHeader rentalHeader = new RentalHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(rentalHeader);


            d("LOG_VOLLEY", mRequestBody);
            final User finalUser = user;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("rentalHeaderResponseId", response);
                    final RentalHeader rentalHeaderMod = gson.fromJson(response, RentalHeader.class);

                    if (status.equals("view")) {
                        Intent intent = new Intent(context, ViewBookAct.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("viewBook", rentalHeaderMod.getRentalDetail());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (status.equals("meetUp")) {

                        if (finalUser.getUserId() == userNotificationList.get(position).getUser().getUserId()) {
                            Intent intent = new Intent(context, MeetUpChooser.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("rentalHeader", rentalHeaderMod);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }else if(status.equals("return")){
//                        rentalHeaderObj = rentalHeaderMod;
                        AlertDialog ad = new AlertDialog.Builder(context).create();
                        ad.setTitle("Confirmation");
                        ad.setMessage("Location:\t"+rentalHeaderMod.getReturnMeetUp().getLocation().getLocationName()+
                                "Date:\t" +rentalHeaderMod.getReturnMeetUp().getUserDayTime().getDay().getStrDay()+
                                "\n\nTime:\t"+rentalHeaderMod.getReturnMeetUp().getUserDayTime().getTime().getStrTime());
                        ad.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                confirmEarlyNotif(rentalHeaderMod);
                                dialog.dismiss();
                            }
                        });
                        ad.setButton(AlertDialog.BUTTON_NEGATIVE, "Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {

                                final Dialog dialogCustom = new Dialog(context);
                                dialogCustom.setContentView(R.layout.reject_custom_dialog);
                                final EditText etReason = (EditText) dialogCustom.findViewById(R.id.etReason);
                                Button mSubmitReason = (Button) dialogCustom.findViewById(R.id.submitReject);

                                mSubmitReason.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(etReason.getText().length()==0){
                                            etReason.setError("Field should not be empty.");
                                        }else{
                                            UserNotification un = new UserNotification();
                                            un.setUser(userNotificationList.get(position).getUserPerformer());
                                            un.setUserPerformer(userNotificationList.get(position).getUser());
                                            un.setActionStatus("return-denied");
                                            un.setActionId(userNotificationList.get(position).getActionId());
                                            un.setActionName("rental");
                                            un.setBookActionPerformedOn(userNotificationList.get(position).getBookActionPerformedOn());
                                            un.setExtraMessage(etReason.getText().toString());

                                            addUserNotif(un);
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                dialogCustom.show();
//                                denyEarlyNotif(rentalHeaderMod);
                            }
                        });
                        ad.show();
                    }else if(status.equals("summary")){
                        AlertDialog ad = new AlertDialog.Builder(context).create();
                        ad.setTitle("Meet Up Summary");
                        ad.setMessage("Date:\t" +rentalHeaderMod.getDateDeliver()+
                                "\n\nDay:\t"+rentalHeaderMod.getMeetUp().getUserDayTime().getDay().getStrDay()+
                                "\n\nTime:\t"+rentalHeaderMod.getMeetUp().getUserDayTime().getTime().getStrTime());
                        ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context, LandingPage.class);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("fromRegister", false);
                                intent.putExtra("user", bundle);
                                context.startActivity(intent);
                            }
                        });
                        ad.show();

                    }else if(status.equals("return-rejected")){
                        AlertDialog ad = new AlertDialog.Builder(context).create();
                        ad.setTitle("Request Rejected");
                        ad.setMessage(userNotificationList.get(position).getUser().getUserFname()+" "+userNotificationList.get(position).getUser().getUserLname()+" rejected your request because of her reason: '"+userNotificationList.get(position).getExtraMessage()+"'.");
                        ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context, NotificationAct.class);
                                context.startActivity(intent);
                            }
                        });
                        ad.show();
                    }else if(status.equals("return-confirmed")){
                        AlertDialog ad = new AlertDialog.Builder(context).create();
                        ad.setTitle("Meet Up Summary");
                        ad.setMessage("Location:\t\t"+rentalHeaderMod.getReturnMeetUp().getLocation().getLocationName()+
                                "\n\nDate:\t\t" +rentalHeaderMod.getReturnMeetUp().getUserDayTime().getDay().getStrDay()+
                                "\n\nTime:\t\t"+rentalHeaderMod.getReturnMeetUp().getUserDayTime().getTime().getStrTime());
                        ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(context, NotificationAct.class);
                                context.startActivity(intent);
                            }
                        });
                        ad.show();
                    }else if(status.equals("returned")){
                        final Dialog dialogCustom = new Dialog(context);
                        dialogCustom.setContentView(R.layout.rent_delivered_book_custom_dialog);
                        TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleDelivery);
                        TextView mOwner = (TextView) dialogCustom.findViewById(R.id.deliveredBy);
                        TextView mLocation = (TextView) dialogCustom.findViewById(R.id.locationDelivery);
                        TextView mDate = (TextView) dialogCustom.findViewById(R.id.dateDelivery);
                        TextView mTime = (TextView) dialogCustom.findViewById(R.id.timeDelivery);
                        ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookDelivery);
                        Button btnOkay = (Button) dialogCustom.findViewById(R.id.btnDeliveryOkay);
                        Button btnSeeRating = (Button) dialogCustom.findViewById(R.id.btnSeeRating);


                        Glide.with(context).load(rentalHeaderMod.getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                        mTitle.setText(rentalHeaderMod.getRentalDetail().getBookOwner().getBookObj().getBookTitle());
                        mOwner.setText(rentalHeaderMod.getRentalDetail().getBookOwner().getUserObj().getUserFname()+" "+rentalHeaderMod.getRentalDetail().getBookOwner().getUserObj().getUserLname());
                        mLocation.setText(rentalHeaderMod.getReturnMeetUp().getLocation().getLocationName());
                        mDate.setText(rentalHeaderMod.getRentalReturnDate().toString());
                        mTime.setText(rentalHeaderMod.getReturnMeetUp().getUserDayTime().getTime().getStrTime());

                        btnOkay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, NotificationAct.class);
                                context.startActivity(intent);
                            }
                        });

                        btnSeeRating.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getBookReview(position, rentalHeaderMod);
                            }
                        });

                        dialogCustom.show();
                    }else if(status.equals("delivered")){
                        final Dialog dialogCustom = new Dialog(context);
                        dialogCustom.setContentView(R.layout.delivered_book_custom_dialog);
                        TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleDelivery);
                        TextView mOwner = (TextView) dialogCustom.findViewById(R.id.deliveredBy);
                        TextView mLocation = (TextView) dialogCustom.findViewById(R.id.locationDelivery);
                        TextView mDate = (TextView) dialogCustom.findViewById(R.id.dateDelivery);
                        TextView mTime = (TextView) dialogCustom.findViewById(R.id.timeDelivery);
                        ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookDelivery);
                        Button btnOkay = (Button) dialogCustom.findViewById(R.id.btnDeliveryOkay);

                        Glide.with(context).load(rentalHeaderMod.getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                        mTitle.setText(rentalHeaderMod.getRentalDetail().getBookOwner().getBookObj().getBookTitle());
                        mOwner.setText(rentalHeaderMod.getRentalDetail().getBookOwner().getUserObj().getUserFname()+" "+rentalHeaderMod.getRentalDetail().getBookOwner().getUserObj().getUserLname());
                        mLocation.setText(rentalHeaderMod.getMeetUp().getLocation().getLocationName());
                        mDate.setText(rentalHeaderMod.getDateDeliver().toString());
                        mTime.setText(rentalHeaderMod.getMeetUp().getUserDayTime().getTime().getStrTime());

                        btnOkay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, NotificationAct.class);
                                context.startActivity(intent);
                            }
                        });

                        dialogCustom.show();
                    }
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

        public void getBookReview(final int position, final RentalHeader rentalHeader1) {
            Log.d("setToComplete", "inside");
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

            List<String> rateStr = Arrays.asList(userNotificationList.get(position).getExtraMessage().split(","));

            d("UserIdReceive", String.valueOf(user.getUserId()));
            String URL = Constants.GET_BOOK_REVIEW+rateStr.get(1).replaceAll("\\s+","");
            Log.d("getBookReview", URL);

            final RentalHeader rentalHeader = new RentalHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(rentalHeader);


            d("setToComplete", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("setToCompleteResponse", response);

                    BookOwnerReview bookOwnerReview = gson.fromJson(response, BookOwnerReview.class);

                    getBookRating(position, bookOwnerReview, rentalHeader1);

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

        public void getBookRating(final int position, final BookOwnerReview bookOwnerReviewMod, RentalHeader rentalHeader1) {
            Log.d("setToComplete", "inside");
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

            List<String> rateStr = Arrays.asList(userNotificationList.get(position).getExtraMessage().split(","));

            d("UserIdReceive", String.valueOf(user.getUserId()));
            String URL = Constants.GET_BOOK_RATINGS+rateStr.get(0).replaceAll("\\s+","");
            Log.d("getBookRating", URL);

            final RentalHeader rentalHeader = new RentalHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(rentalHeader);


            d("setToComplete", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("setToCompleteResponse", response);

                    final BookOwnerRating bookOwnerRating = gson.fromJson(response, BookOwnerRating.class);

                    final Dialog dialogCustom = new Dialog(context);
                    dialogCustom.setContentView(R.layout.review_custom_dialog);
                    TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleReview);
                    TextView mAuthor = (TextView) dialogCustom.findViewById(R.id.bookAuthorReview);
                    ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookReview);
                    final EditText etReviewMessage = (EditText) dialogCustom.findViewById(R.id.etReviewReview);
                    final RatingBar mRateBar = (RatingBar) dialogCustom.findViewById(R.id.ratingReview);
                    Button mRateNow = (Button) dialogCustom.findViewById(R.id.btnRateReview);

                    mRateNow.setText("Okay");
                    etReviewMessage.setHint(bookOwnerReviewMod.getComment());
                    etReviewMessage.setFocusable(false);

                    mTitle.setText(userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookTitle());

                    Glide.with(context).load(userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                    String author = "";

                    if(userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().size()!=0){
                        for(int init=0; init<userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().size(); init++){
                            if(!(userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                                author+=userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                                if(!(userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                                    author+=userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().get(init).getAuthorLName();
                                    if(init+1<userNotificationList.get(position).getBookActionPerformedOn().getBookObj().getBookAuthor().size()){
                                        author+=", ";
                                    }
                                }
                            }
                        }
                    }else{
                        author="Unknown Author";
                    }
                    mAuthor.setText(author);

                    mRateBar.setRating(bookOwnerRating.getRate().getRateNumber());

                    mRateNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("userModelPass", bookOwnerRating.getUser());
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    });

                    dialogCustom.show();


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

        public void setToComplete(int position, int userRatingId) {
            Log.d("setToComplete", "inside");
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
            d("UserIdReceive", String.valueOf(user.getUserId()));
            String URL = Constants.SET_TO_COMPLETE+userNotificationList.get(position).getActionId()+"/"+userRatingId;
            Log.d("setToComplete", URL);

            final RentalHeader rentalHeader = new RentalHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(rentalHeader);


            d("setToComplete", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("setToCompleteResponse", response);
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

        public void confirmEarlyNotif(RentalHeader rentalHeaderMod) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
            d("UserIdReceive", String.valueOf(user.getUserId()));
            String URL = Constants.CONFIRM_RETURN+rentalHeaderMod.getRentalHeaderId();

            final RentalHeader rentalHeader = new RentalHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(rentalHeaderMod);


            d("LOG_VOLLEY", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("confirmEarlyNotif", response);
                    Intent intent = new Intent(context, LandingPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromRegister", false);
                    intent.putExtra("user", bundle);
                    context.startActivity(intent);

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

        public void denyEarlyNotif(RentalHeader rentalHeaderMod) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
            d("UserIdReceive", String.valueOf(user.getUserId()));
            String URL = Constants.DENY_RETURN+rentalHeaderMod.getRentalHeaderId();

            final RentalHeader rentalHeader = new RentalHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(rentalHeaderMod);


            d("LOG_VOLLEY", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("denyEarlyNotif", response);
                    Intent intent = new Intent(context, LandingPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromRegister", false);
                    intent.putExtra("user", bundle);
                    context.startActivity(intent);

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

        public void addUserNotif(UserNotification userNotification) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
            d("UserIdReceive", String.valueOf(user.getUserId()));
            String URL = Constants.POST_USER_NOTIFICATION;

            final RentalHeader rentalHeader = new RentalHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(userNotification);


            d("LOG_VOLLEY", mRequestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("userNotificationPost", response);
                    Intent intent = new Intent(context, LandingPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromRegister", false);
                    intent.putExtra("user", bundle);
                    context.startActivity(intent);
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

        public void getAuctionHeader(final int position, final String status) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

            String URL = Constants.GET_AUCTION_HEADER + userNotificationList.get(position).getActionId();

            d("getAuctionHeader", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

            final AuctionHeader auctionHeader = new AuctionHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(auctionHeader);


            d("LOG_VOLLEY", mRequestBody);
            final User finalUser = user;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("auctionHeaderResponseId", response);
                    AuctionHeader auctionHeaderMod = gson.fromJson(response, AuctionHeader.class);

                    if (status.equals("win")) {

                        if (finalUser.getUserId() == userNotificationList.get(position).getUser().getUserId()) {
                            Intent intent = new Intent(context, AuctionMeetUpChooser.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("auctionHeader", auctionHeaderMod);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    } else if (status.equals("own")) {

                        Intent intent = new Intent(context, ProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userModelPass", userNotificationList.get(position).getUserPerformer());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
//                        updateBookOwner(position, true, swapHeaderMod);
//                        updateBookOwner(position, false, swapHeaderMod);
                    }else if(status.equals("lose")){
                        Intent intent = new Intent(context, ViewAuctionBook.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("auctionBook",auctionHeaderMod.getAuctionDetail());
                        bundle.putBoolean("notif", true);
                        intent.putExtras(bundle);

//                        if(auctionDetailModel==null){
//                            Log.d("NotifAuc", "null");
//                        }else{
//                            Log.d("NotifAuc", auctionDetailModel.toString());
//                        }
                        context.startActivity(intent);
//                        meetUpSumm(position, swapHeaderMod);
                    }else if (status.equals("view")) {
                        Intent intent = new Intent(context, ViewBookAct.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("auctionBook", auctionHeader.getAuctionDetail());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }else if(status.equals("delivered")){
                        final Dialog dialogCustom = new Dialog(context);
                        dialogCustom.setContentView(R.layout.delivered_book_custom_dialog);
                        TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleDelivery);
                        TextView mOwner = (TextView) dialogCustom.findViewById(R.id.deliveredBy);
                        TextView mLocation = (TextView) dialogCustom.findViewById(R.id.locationDelivery);
                        TextView mDate = (TextView) dialogCustom.findViewById(R.id.dateDelivery);
                        TextView mTime = (TextView) dialogCustom.findViewById(R.id.timeDelivery);
                        ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookDelivery);
                        Button btnOkay = (Button) dialogCustom.findViewById(R.id.btnDeliveryOkay);

                        Glide.with(context).load(auctionHeaderMod.getAuctionDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                        mTitle.setText(auctionHeaderMod.getAuctionDetail().getBookOwner().getBookObj().getBookTitle());
                        mOwner.setText(auctionHeaderMod.getAuctionDetail().getBookOwner().getUserObj().getUserFname()+" "+auctionHeaderMod.getAuctionDetail().getBookOwner().getUserObj().getUserLname());
                        mLocation.setText(auctionHeaderMod.getMeetUp().getLocation().getLocationName());
                        mDate.setText(auctionHeaderMod.getDateDelivered());
                        mTime.setText(auctionHeaderMod.getMeetUp().getUserDayTime().getTime().getStrTime());

                        btnOkay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, NotificationAct.class);
                                context.startActivity(intent);
                            }
                        });

                        dialogCustom.show();

                    }else if(status.equals("Completed")){
                        final Dialog dialogCustom = new Dialog(context);
                        dialogCustom.setContentView(R.layout.auction_complete_book_custom_dialog);
                        TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleDelivery);
                        TextView mOwner = (TextView) dialogCustom.findViewById(R.id.deliveredBy);
                        ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookDelivery);
                        Button btnOkay = (Button) dialogCustom.findViewById(R.id.btnDeliveryOkay);

                        Glide.with(context).load(auctionHeaderMod.getAuctionDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                        mTitle.setText(auctionHeaderMod.getAuctionDetail().getBookOwner().getBookObj().getBookTitle());
                        mOwner.setText(auctionHeaderMod.getAuctionDetail().getBookOwner().getUserObj().getUserFname()+" "+auctionHeaderMod.getAuctionDetail().getBookOwner().getUserObj().getUserLname());

                        btnOkay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, NotificationAct.class);
                                context.startActivity(intent);
                            }
                        });

                        dialogCustom.show();
                    }
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

        public void getSwapHeader(final int position, final String status) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
            User user = new User();
            user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

            String URL = Constants.GET_SWAP_DETAIL + userNotificationList.get(position).getActionId();

            d("getSwapHeader", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

            final SwapHeader swapHeader = new SwapHeader();

            final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
            final String mRequestBody = gson.toJson(swapHeader);


            d("LOG_VOLLEY", mRequestBody);
            final User finalUser = user;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("swapHeaderResponseId", response);
                    SwapHeader swapHeaderMod = gson.fromJson(response, SwapHeader.class);

                    if (status.equals("view")) {
                        Intent intent = new Intent(context, ViewBookAct.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("swapBook", swapHeaderMod.getSwapDetail());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (status.equals("meetUp")) {
                        if (finalUser.getUserId() == userNotificationList.get(position).getUser().getUserId()) {
                            Intent intent = new Intent(context, SwapMeetUpChooser.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("swapHeader", swapHeaderMod);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    } else if (status.equals("change")) {
                        swapOwners(swapHeaderMod);
//                        updateBookOwner(position, true, swapHeaderMod);
//                        updateBookOwner(position, false, swapHeaderMod);
                    }else if(status.equals("summ")){
                        meetUpSumm(position, swapHeaderMod);
                    }else if(status.equals("delivered")){
                        final Dialog dialogCustom = new Dialog(context);
                        dialogCustom.setContentView(R.layout.delivered_book_custom_dialog);
                        TextView mTitle = (TextView) dialogCustom.findViewById(R.id.bookTitleDelivery);
                        TextView mOwner = (TextView) dialogCustom.findViewById(R.id.deliveredBy);
                        TextView mLocation = (TextView) dialogCustom.findViewById(R.id.locationDelivery);
                        TextView mDate = (TextView) dialogCustom.findViewById(R.id.dateDelivery);
                        TextView mTime = (TextView) dialogCustom.findViewById(R.id.timeDelivery);
                        ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.ivBookDelivery);
                        Button btnOkay = (Button) dialogCustom.findViewById(R.id.btnDeliveryOkay);

                        Glide.with(context).load(swapHeaderMod.getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                        mTitle.setText(swapHeaderMod.getSwapDetail().getBookOwner().getBookObj().getBookTitle());
                        mOwner.setText(swapHeaderMod.getSwapDetail().getBookOwner().getUserObj().getUserFname()+" "+swapHeaderMod.getSwapDetail().getBookOwner().getUserObj().getUserLname());
                        mLocation.setText(swapHeaderMod.getMeetUp().getLocation().getLocationName());
                        mDate.setText(swapHeaderMod.getDateDelivered());
                        mTime.setText(swapHeaderMod.getMeetUp().getUserDayTime().getTime().getStrTime());

                        btnOkay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogCustom.dismiss();
                            }
                        });

                        dialogCustom.show();
                    }else if(status.equals("Completed")){
                        final Dialog dialogCustom = new Dialog(context);
                        dialogCustom.setContentView(R.layout.success_swap_custom_dialog);
                        TextView mTitle = (TextView) dialogCustom.findViewById(R.id.successBookTitle);
                        TextView mTitle2 = (TextView) dialogCustom.findViewById(R.id.successBookTitle2);
                        TextView mOwner = (TextView) dialogCustom.findViewById(R.id.successBookOwner);
                        TextView mOwner2 = (TextView) dialogCustom.findViewById(R.id.successBookOwner2);
                        ImageView ivBook = (ImageView) dialogCustom.findViewById(R.id.successBook);
                        ImageView ivBook2 = (ImageView) dialogCustom.findViewById(R.id.successBook2);
                        Button btnOkay = (Button) dialogCustom.findViewById(R.id.btnOkaySuccess);

                        Glide.with(context).load(swapHeaderMod.getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);
                        Glide.with(context).load(swapHeaderMod.getRequestedSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook2);

                        mTitle.setText(swapHeaderMod.getSwapDetail().getBookOwner().getBookObj().getBookTitle());
                        mOwner.setText(swapHeaderMod.getRequestedSwapDetail().getBookOwner().getUserObj().getUserFname()+" "+swapHeaderMod.getRequestedSwapDetail().getBookOwner().getUserObj().getUserLname());
                        mTitle2.setText(swapHeaderMod.getRequestedSwapDetail().getBookOwner().getBookObj().getBookTitle());
                        mOwner2.setText(swapHeaderMod.getSwapDetail().getBookOwner().getUserObj().getUserFname()+" "+swapHeaderMod.getSwapDetail().getBookOwner().getUserObj().getUserLname());

                        btnOkay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogCustom.dismiss();
                            }
                        });

                        dialogCustom.show();
                    }

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

}

