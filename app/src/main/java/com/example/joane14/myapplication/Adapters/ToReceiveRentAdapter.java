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
import com.example.joane14.myapplication.Fragments.VolleyUtil;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.util.Log.d;

/**
 * Created by Joane14 on 05/10/2017.
 */

public class ToReceiveRentAdapter extends RecyclerView.Adapter<ToReceiveRentAdapter.BookHolder> {

    public List<RentalHeader> bookList;
    RentalHeader rentalHeader;
    Float rateNumber;
    public Activity context;

    @Override
    public ToReceiveRentAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_activity_item, parent, false);

        this.context = (Activity) parent.getContext();
        rentalHeader = new RentalHeader();
        Log.d("ToRecevieAdapter","inside");
        ToReceiveRentAdapter.BookHolder dataObjectHolder = new ToReceiveRentAdapter.BookHolder(this.context, view);

        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ToReceiveRentAdapter(List<RentalHeader> myDataset) {
        bookList = myDataset;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ToReceiveRentAdapter.BookHolder holder, final int position) {

        if(bookList.get(position).getRentalExtraMessage()==null||bookList.get(position).getRentalExtraMessage().equals("Return")){
            Log.d("sulodSa", "gray");
            holder.mBtnRate.setImageResource(R.drawable.notrate);
        }else{
            Log.d("sulodSa", "green");
            holder.mBtnRate.setImageResource(R.drawable.checkbookact);
        }
        holder.mBtnMail.setVisibility(View.GONE);

        Log.d("mPrice", holder.mPrice.getText().toString()+"   "+String.valueOf(bookList.get(position).getRentalDetail().getCalculatedPrice()));

        if(bookList.get(position).getStatus().equals("Confirm")||bookList.get(position).getStatus().equals("Delivered")){
            Log.d("sulodSa","if");
            holder.mBookDate.setText(bookList.get(position).getRentalTimeStamp());
            holder.mPrice.setText(holder.mPrice.getText().toString()+"   "+String.valueOf(bookList.get(position).getRentalDetail().getCalculatedPrice()));
            holder.mDate.setText(bookList.get(position).getDateDeliver());
            holder.mTime.setText(bookList.get(position).getMeetUp().getUserDayTime().getTime().getStrTime());
            holder.mLocation.setText(bookList.get(position).getMeetUp().getLocation().getLocationName());
        }else{
            Log.d("sulodSa","else");
            Calendar calendar = Calendar.getInstance();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            final String currDAte = sdf.format(c);
            Date dateReturn = null;
            try {
                dateReturn = sdf.parse(bookList.get(position).getRentalReturnDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar thatDay = Calendar.getInstance();
            thatDay.setTime(dateReturn);
            long diff = calendar.getTimeInMillis() - thatDay.getTimeInMillis();
            long daysDiff = diff / (24*60*60*1000);

            float lockin = 0f;

            Log.d("diffDays", daysDiff+"");


            lockin = daysDiff*50;

            float lockinPrice=0f;

            lockinPrice = bookList.get(position).getTotalPrice()/2;

            float diffLp = 0f;

            diffLp = lockinPrice - lockin;

            if(diffLp<0){
                holder.mPrice.setText("(- ₱ "+String.format("%.2f", Math.abs(diffLp))+")");
            }else{
                holder.mPrice.setText("₱  "+String.format("%.2f", diffLp));
            }


            holder.mBookDate.setText(bookList.get(position).getRentalTimeStamp());
            holder.mDate.setText(bookList.get(position).getRentalReturnDate());
            holder.mTime.setText(bookList.get(position).getReturnMeetUp().getUserDayTime().getTime().getStrTime());
            holder.mLocation.setText(bookList.get(position).getReturnMeetUp().getLocation().getLocationName());
        }

        holder.mBookTitle.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mBookRenter.setText(bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getUserFname()+" "+bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getUserLname());


        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBook);

        holder.mBtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userModelPass", bookList.get(position).getRentalDetail().getBookOwner().getUserObj());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.mBtnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookList.get(position).getRentalExtraMessage()==null){
                    if(bookList.get(position).getRentalExtraMessage()==null){
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Alert!");
                        alertDialog.setMessage("The owner has not yet confirmed the book delivery.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }else{
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Alert!");
                        alertDialog.setMessage("The renter has not yet confirmed the book returned.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                }else if(bookList.get(position).getRentalExtraMessage().equals("Delivered")){
                    AlertDialog ad = new AlertDialog.Builder(context).create();
                    ad.setTitle("Alert!");
                    ad.setMessage("Will notify the owner that you received the book.");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            received(bookList.get(position));
                        }
                    });
                    ad.show();
                }else if(bookList.get(position).getRentalExtraMessage().equals("Returned")){
                    AlertDialog ad = new AlertDialog.Builder(context).create();
                    ad.setTitle("Alert!");
                    ad.setMessage("Will notify the renter that you received the book.");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
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

                            etReviewMessage.setHint("Review renter");
                            mTitle.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());

                            Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(ivBook);

                            String author = "";

                            if(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookAuthor().size()!=0){
                                for(int init=0; init<bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookAuthor().size(); init++){
                                    if(!(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                                        author+=bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                                        if(!(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                                            author+=bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                                            if(init+1<bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookAuthor().size()){
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
                                        userRatingMod.setUserRater(bookList.get(position).getUserId());
                                        userRatingMod.setUser(bookList.get(position).getRentalDetail().getBookOwner().getUserObj());
                                        userRatingMod.setReview(reviewMod);
                                        userRatingMod.setRate(rateMod);

                                        userRate(userRatingMod, bookList.get(position));
                                    }

                                }
                            });

                            dialogCustom.show();
                        }
                    });
                    ad.setButton(AlertDialog.BUTTON_NEGATIVE, "Not Yet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }
            }
        });

    }

    private void received(final RentalHeader rentalHeader){
        String URL = Constants.RENT_RECEIVE+rentalHeader.getRentalHeaderId();
        Log.d("receiveToReceiveURL", URL);
        Log.d("receiveToReceive", rentalHeader.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("receiveToReceiveRes", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                final RentalHeader rentalHeaderMod = gson.fromJson(response, RentalHeader.class);

                Intent intent = new Intent(context, BookActActivity.class);
                context.startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(context).add(stringRequest);
    }

    private void complete(final RentalHeader rentalHeader, int userRateId){
        String URL = Constants.RENT_COMPLETE+rentalHeader.getRentalHeaderId()+"/"+userRateId;
        Log.d("completeToReceiveURL", URL);
        Log.d("completeToReceive", rentalHeader.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("completeToReceiveRes", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                final RentalHeader rentalHeaderMod = gson.fromJson(response, RentalHeader.class);

                Intent intent = new Intent(context, BookActActivity.class);
                context.startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(context).add(stringRequest);
    }

    public void userRate(final UserRating userRating, final RentalHeader rentalHeaderMod) {
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

                complete(rentalHeaderMod, ur.getUserRatingId());

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
        RentalHeader rentalHeaderObj;
        Context context;


        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBookTitle = (TextView) itemView.findViewById(R.id.bookTitleBA);
            mDate = (TextView) itemView.findViewById(R.id.dateBA);
            mBookRenter = (TextView) itemView.findViewById(R.id.renterNameBA);
//            mRenter = (TextView) itemView.findViewById(R.id.toReceiveRenter);
            mBookDate = (TextView) itemView.findViewById(R.id.bookDateBA);
            mLocation = (TextView) itemView.findViewById(R.id.locationBA);
            mPrice = (TextView) itemView.findViewById(R.id.bookPriceBA);
            mTime = (TextView) itemView.findViewById(R.id.timeBA);
//            mIvRenter = (ImageView) itemView.findViewById(R.id.toReceiveRenterImage);
            mIvBook = (ImageView) itemView.findViewById(R.id.ivBookBA);
            mBtnProfile = (ImageButton) itemView.findViewById(R.id.profileBA);
            mBtnMail = (ImageButton) itemView.findViewById(R.id.notifyBA);
            mBtnRate = (ImageButton) itemView.findViewById(R.id.rateButtonBA);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rentalHeaderObj = new RentalHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    rentalHeaderObj = ToReceiveRentAdapter.this.bookList.get(position);
                    if(rentalHeaderObj==null){
                        Log.d("reaceiveRentAdapter", "is null");
                    }else{
                        Log.d("reaceiveRentAdapter", "is not null");
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

    public void updateReceive(int position, Boolean bool){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        rentalHeader = bookList.get(position);
        String status="";
        if(rentalHeader.getStatus().equals("Confirmation")){
            if(bool==true){
                status = "Approved";
            }else{
                status = "Rejected";
            }
        }else if(rentalHeader.getStatus().equals("Approved")){
            if(bool==true){
                status = "Received";
            }else{
                status = "Rejected";
            }
        }else{
            if(bool==true){
                status = "Complete";

            }else{
                status = "Rejected";
            }

        }

        String URL = Constants.UPDATE_RENTAL_HEADER+"/"+rentalHeader.getRentalHeaderId()+"/"+status;


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        Log.d("LOG_VOLLEY", mRequestBody);
        Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
        final String finalStatus = status;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RequestReceivedStatus", response);
                RentalHeader rentalHeaderModel = new RentalHeader();
                if(finalStatus.equals("Complete")){
                    incrementRenters(rentalHeader.getRentalDetail().getBookOwner());
                }else if(finalStatus.equals("Received")){
                    Intent intent = new Intent(context, BookReviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("rentalHeader", rentalHeader);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else if(finalStatus.equals("Approved")){
                    Intent intent = new Intent(context, TransactionActivity.class);
                    context.startActivity(intent);
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

    public void incrementRenters(BookOwnerModel bookOwnerModel){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        String URL = Constants.INCREMENT_BOOK_OWNER+"/"+bookOwnerModel.getBookOwnerId();


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookOwnerModel);


        Log.d("LOG_VOLLEY", mRequestBody);
        Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("RequestReceivedStatus", response);
                Intent intent = new Intent(context, UserReviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("rentalHeader", rentalHeader);
                intent.putExtras(bundle);
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


}
