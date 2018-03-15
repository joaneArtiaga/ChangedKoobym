package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.example.joane14.myapplication.Activities.MeetUpChooser;
import com.example.joane14.myapplication.Activities.ProfileActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.VolleyUtil;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.BookOwnerRating;
import com.example.joane14.myapplication.Model.BookOwnerReview;
import com.example.joane14.myapplication.Model.Rate;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.Review;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserNotification;
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

/**
 * Created by Joane14 on 05/10/2017.
 */

public class ToReturnAdapter extends RecyclerView.Adapter<ToReturnAdapter.BookHolder>{

    public List<RentalHeader> bookList;
    public Activity context;
    float rateNumber;

    @Override
    public ToReturnAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_activity_item, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        ToReturnAdapter.BookHolder dataObjectHolder = new ToReturnAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ToReturnAdapter(List<RentalHeader> myDataset) {
        bookList = myDataset;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final ToReturnAdapter.BookHolder holder, final int position) {

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

        holder.mBookTitle.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mRenter.setText("Available "+bookList.get(position).getRentalDetail().getDaysForRent()+" days for rent.");
        holder.mBookDate.setText(bookList.get(position).getRentalTimeStamp());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar cal = Calendar.getInstance();

        Date dateToCompare = new Date();
        try {
            dateToCompare = df.parse(bookList.get(position).getRentalReturnDate());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newDate = "";
        newDate = df.format(cal.getTime());

        Log.d("toReturn\t CurrentDate: "+currDAte, "ReturnDate: "+bookList.get(position).getRentalReturnDate());
        if(dateToCompare.after(cal.getTime())|| newDate.equals(bookList.get(position).getDateDeliver())){
            holder.mRate.setImageResource(R.drawable.checkbookact);
        }else{
            holder.mRate.setImageResource(R.drawable.notrate);
        }

        if(bookList.get(position).getRentalEndDate()==null){
            Log.d("EndDateReturnRent", "walay sulod");
        }else{
            holder.mDate.setText(bookList.get(position).getRentalReturnDate());
        }


        if(bookList.get(position).getReturnMeetUp()==null){

        }else{
            holder.mLocation.setText(bookList.get(position).getReturnMeetUp().getLocation().getLocationName());
            holder.mTime.setText(bookList.get(position).getReturnMeetUp().getUserDayTime().getTime().getStrTime());
        }

        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

        holder.mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userModelPass", bookList.get(position).getRentalDetail().getBookOwner().getUserObj());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.mNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog ad = new AlertDialog.Builder(context).create();
                ad.setTitle("Alert!");
                ad.setMessage("Are you sure you can return the book earlier?");
                ad.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        sendMailNotif(bookList.get(position));
                        Intent intent = new Intent(context, MeetUpChooser.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("rentalHeader", bookList.get(position));
                        bundle.putBoolean("return", true);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
                ad.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        final Date finalDateToCompare = dateToCompare;
        final String finalNewDate = newDate;
        holder.mRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(finalDateToCompare.after(cal.getTime())|| finalNewDate.equals(bookList.get(position).getDateDeliver())){
                    AlertDialog ad = new AlertDialog.Builder(context).create();
                    ad.setTitle("Confirmation");
                    ad.setMessage("Did you receive the book?");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
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

                            etReviewMessage.setHint("Book review");
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

                                        BookOwnerReview bookOwnerReview = new BookOwnerReview();

                                        bookOwnerReview.setReview(reviewMod);
                                        bookOwnerReview.setComment(etReviewMessage.getText().toString());
                                        bookOwnerReview.setBookOwner(bookList.get(position).getRentalDetail().getBookOwner());
                                        bookOwnerReview.setUser(bookList.get(position).getUserId());

                                        BookOwnerRating bookOwnerRating = new BookOwnerRating();
                                        bookOwnerRating.setRate(rateMod);
                                        bookOwnerRating.setUser(bookList.get(position).getUserId());
                                        bookOwnerRating.setBookOwner(bookList.get(position).getRentalDetail().getBookOwner());

                                        userRate(bookOwnerReview, bookOwnerRating, bookList.get(position));
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
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Alert!");
                    alertDialog.setMessage("This is not yet done.");
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

    public void userRate(final BookOwnerReview bookReview, final BookOwnerRating bookRating, final RentalHeader rentalHeaderMod) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_BOOK_OWNER_REVIEW;


        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);

        Log.d("addBookRatingURL", URL);
        Log.d("addBookRating", bookReview.toString());


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookReview);


        Log.d("addBookRating", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("addBookRatingResponse", response);

                BookOwnerReview ur = new BookOwnerReview();
                ur = gson.fromJson(response, BookOwnerReview.class);

                bookRate(bookRating, rentalHeaderMod, ur);


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

    public void bookRate(final BookOwnerRating bookRating, final RentalHeader rentalHeaderMod, final BookOwnerReview bookOwnerReview) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_BOOK_OWNER_RATE;

        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
        Log.d("addBookRatingURL", URL);
        Log.d("addBookRating", bookRating.toString());


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookRating);


        Log.d("addBookRating", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("addBookRatingResponse", response);

                BookOwnerRating ur = new BookOwnerRating();
                ur = gson.fromJson(response, BookOwnerRating.class);

                returnToReceive(rentalHeaderMod, ur, bookOwnerReview);
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

    private void returnToReceive(final RentalHeader rentalHeader, BookOwnerRating bookRating, BookOwnerReview bookReview){
//        String URL = "http://104.198.152.85/Koobym/rentalDetail/suggested/%d";
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/suggested/%d";
        String URL = Constants.RETURN_TO_RECEIVE+rentalHeader.getRentalHeaderId()+"/"+bookRating.getBookOwnerRatingId()+"/"+bookReview.getBookOwnerReviewId();
//        URL = String.format(URL, userId);
        Log.d("returnToReceiveURL", URL);
        Log.d("returnToReceive", rentalHeader.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("returnToReceiveResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                RentalHeader rentalHeaderMod = gson.fromJson(response, RentalHeader.class);

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

    private void sendMailNotif(final RentalHeader rentalHeader){
//        String URL = "http://104.198.152.85/Koobym/rentalDetail/suggested/%d";
//        String URL = Constants.WEB_SERVICE_URL+"rentalDetail/suggested/%d";
        String URL = Constants.SEND_NOTIF_MAIL+rentalHeader.getRentalHeaderId();
//        URL = String.format(URL, userId);
        Log.d("MailNotif", URL);
        Log.d("MailNotif", rentalHeader.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("EarlyNotif", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                RentalHeader rentalHeaderMod = gson.fromJson(response, RentalHeader.class);

//                rentalHeaderMod.setRentalDetail(rentalHeader.getRentalDetail());

                Intent intent = new Intent(context, MeetUpChooser.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("rentalHeader", rentalHeader);
                bundle.putBoolean("return", true);
                intent.putExtras(bundle);
                context.startActivity(intent);

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

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mBookTitle, mRenter, mBookDate, mPrice, mLocation, mTime, mDate;
        ImageView mIvBookImg;
        ImageButton mProfile, mNotify, mRate;
        RentalHeader rentalHeaderObj;

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
                    rentalHeaderObj = new RentalHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    rentalHeaderObj = ToReturnAdapter.this.bookList.get(position);
                    if(rentalHeaderObj==null){
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

    public String getDetails(int position){
        String result = "";

        result = bookList.get(position).toString();



        return result;

    }
}
