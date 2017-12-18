package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.DisplayBookReview;
import com.example.joane14.myapplication.Fragments.DisplaySwapComments;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.logging.LogRecord;

import static android.net.sip.SipErrorCode.TIME_OUT;
import static android.util.Log.d;

public class ViewBookAct extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        DisplayBookReview.OnDisplayBookReviewInteractionListener,
        DisplaySwapComments.OnSwapCommentInteractionListener{

    static final int MSG_DISMISS_DIALOG = 0;
    RentalDetail rentalDetailModel;
    SwapDetail swapDetail;
    AuctionDetailModel auctionDetailModel;
    RatingBar mRating;
    TextView mRenters;

    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book2);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarViewBook);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ViewBookAct.this);

        View hView = navigationView.getHeaderView(0);
        TextView mName = (TextView) hView.findViewById(R.id.tvName);
        TextView mEmail = (TextView) hView.findViewById(R.id.tvEmail);
        ImageView profileImg = (ImageView) hView.findViewById(R.id.profPic);

        rentalDetailModel = new RentalDetail();


        if(SPUtility.getSPUtil(this).contains("USER_OBJECT")){
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname()+" "+userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(ViewBookAct.this).load( userModel.getImageFilename()).fit().into(profileImg);
        }

        mRenters = (TextView) findViewById(R.id.vbRenters);
        TextView mAuthor = (TextView) findViewById(R.id.vbAuthor);
        TextView mHeaderLP = (TextView) findViewById(R.id.headerLP);
        TextView mHeaderRP = (TextView) findViewById(R.id.heaedrRP);
        TextView mLPrice = (TextView) findViewById(R.id.vbLockInP);
        TextView mRPrice = (TextView) findViewById(R.id.vbRentalP);
        TextView mTitle = (TextView) findViewById(R.id.vbTitle);
        Button mViewBtn = (Button) findViewById(R.id.btnVbViewOwner);
        Button mRentBtn = (Button) findViewById(R.id.btnVbRent);
        TextView mContent = (TextView) findViewById(R.id.vbContent);
        TextView mCondition = (TextView) findViewById(R.id.vbCondition);
        ImageView mBookImg = (ImageView) findViewById(R.id.vbBookPic);
        LinearLayout buttonLinear = (LinearLayout) findViewById(R.id.button_ll);
        LinearLayout lockInLinear = (LinearLayout) findViewById(R.id.lockInLinear);

        mRating = (RatingBar) findViewById(R.id.vbRating);

        if (getIntent().getExtras().getSerializable("viewBook")!=null){

            rentalDetailModel = (RentalDetail) getIntent().getExtras().getSerializable("viewBook");

            mTitle.setText(rentalDetailModel.getBookOwner().getBookObj().getBookTitle());

            mLPrice.setText(String.valueOf(rentalDetailModel.getCalculatedPrice()));
            mRPrice.setText(String.valueOf(rentalDetailModel.getCalculatedPrice()/2));

            final User user = (User) SPUtility.getSPUtil(ViewBookAct.this).getObject("USER_OBJECT", User.class);

            if(user.getUserId()==rentalDetailModel.getBookOwner().getUserObj().getUserId()){
                buttonLinear.setVisibility(View.GONE);
            }

            String author = "";

            if(rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().size()!=0){
                for(int init=0; init<rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().size(); init++){
                    if(!(rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                        author+=rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                        if(!(rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                            author+=rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if(init+1<rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().size()){
                                author+=", ";
                            }
                        }
                    }
                }
            }else{
                author="Unknown Author";
            }
            mAuthor.setText(author);
            Glide.with(this).load(rentalDetailModel.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            mContent.setText(rentalDetailModel.getBookOwner().getBookObj().getBookDescription());
            mCondition.setText(rentalDetailModel.getBookOwner().getStatusDescription());

            mRating.setRating(Float.parseFloat(String.valueOf(rentalDetailModel.getBookOwner().getRate())));

            getCount();
            getRatings(rentalDetailModel.getBookOwner().getBookOwnerId());

            Bundle bundle = new Bundle();
            bundle.putSerializable("rentalDetail", rentalDetailModel);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_review_container, DisplayBookReview.newInstance(bundle));
            ft.commit();

            mViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewBookAct.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userModelPass", rentalDetailModel.getBookOwner().getUserObj());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mRentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkIfExist(user.getUserId(), rentalDetailModel.getRental_detailId());
                }
            });
        }else if(getIntent().getExtras().getSerializable("swapBook")!=null){
            swapDetail = new SwapDetail();
            swapDetail = (SwapDetail) getIntent().getExtras().getSerializable("swapBook");

            mTitle.setText(swapDetail.getBookOwner().getBookObj().getBookTitle());

            mRating.setRating(Float.parseFloat(String.valueOf(swapDetail.getBookOwner().getRate())));

            lockInLinear.setVisibility(View.GONE);
            mHeaderRP.setText("Swap Price");
            mLPrice.setText(String.valueOf(swapDetail.getSwapPrice()));
            mRPrice.setText(String.valueOf(swapDetail.getSwapPrice()/2));
//            mRPrice.setVisibility(View.GONE);

            final User user = (User) SPUtility.getSPUtil(ViewBookAct.this).getObject("USER_OBJECT", User.class);

            if(user.getUserId()==swapDetail.getBookOwner().getUserObj().getUserId()){
                buttonLinear.setVisibility(View.GONE);
            }

            String author = "";

            if(swapDetail.getBookOwner().getBookObj().getBookAuthor().size()!=0){
                for(int init=0; init<swapDetail.getBookOwner().getBookObj().getBookAuthor().size(); init++){
                    if(!(swapDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                        author+=swapDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                        if(!(swapDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                            author+=swapDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if(init+1<swapDetail.getBookOwner().getBookObj().getBookAuthor().size()){
                                author+=", ";
                            }
                        }
                    }
                }
            }else{
                author="Unknown Author";
            }
            mAuthor.setText(author);
            Glide.with(this).load(swapDetail.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            mContent.setText(swapDetail.getBookOwner().getBookObj().getBookDescription());
            mCondition.setText(swapDetail.getBookOwner().getStatusDescription());

            getRatings(swapDetail.getBookOwner().getBookOwnerId());
//            getCount();

//            Bundle bundle = new Bundle();
//            bundle.putSerializable("swapDetail", swapDetail);
//
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.fragment_review_container, DisplayBookReview.newInstance(bundle));
//            ft.commit();

            Log.d("SwapComment", "Display");
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("swapComment", swapDetail);
//            DisplaySwapComments displaySwapComments = DisplaySwapComments.newInstance(bundle);
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_review_container, displaySwapComments, displaySwapComments.getTag());
//            fragmentTransaction.commit();

            mRentBtn.setText("Request to Swap");

            mViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewBookAct.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userModelPass", swapDetail.getBookOwner().getUserObj());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mRentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewBookAct.this, SwapBookChooser.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("swapDetail", swapDetail);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }else if(getIntent().getExtras().getSerializable("auctionBook")!=null){
            auctionDetailModel = new AuctionDetailModel();
            auctionDetailModel = (AuctionDetailModel) getIntent().getExtras().getSerializable("auctionBook");


            mTitle.setText(auctionDetailModel.getBookOwner().getBookObj().getBookTitle());

            mRating.setRating(Float.parseFloat(String.valueOf(auctionDetailModel.getBookOwner().getRate())));

            lockInLinear.setVisibility(View.GONE);
            mHeaderRP.setText("Starting Price");
            mLPrice.setText(String.valueOf(auctionDetailModel.getStartingPrice()));
//            mRPrice.setVisibility(View.GONE);

            final User user = (User) SPUtility.getSPUtil(ViewBookAct.this).getObject("USER_OBJECT", User.class);

            if(user.getUserId()==auctionDetailModel.getBookOwner().getUserObj().getUserId()){
                buttonLinear.setVisibility(View.GONE);
            }

            String author = "";

            if(auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().size()!=0){
                for(int init=0; init<auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().size(); init++){
                    if(!(auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                        author+=auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                        if(!(auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                            author+=auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if(init+1<auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().size()){
                                author+=", ";
                            }
                        }
                    }
                }
            }else{
                author="Unknown Author";
            }
            mAuthor.setText(author);
            Glide.with(this).load(auctionDetailModel.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            mContent.setText(auctionDetailModel.getBookOwner().getBookObj().getBookDescription());
            mCondition.setText(auctionDetailModel.getBookOwner().getStatusDescription());

            getRatings(auctionDetailModel.getBookOwner().getBookOwnerId());
//            getCount();

//            Bundle bundle = new Bundle();
//            bundle.putSerializable("swapDetail", swapDetail);
//
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.fragment_review_container, DisplayBookReview.newInstance(bundle));
//            ft.commit();

            Log.d("SwapComment", "Display");
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("swapComment", swapDetail);
//            DisplaySwapComments displaySwapComments = DisplaySwapComments.newInstance(bundle);
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_review_container, displaySwapComments, displaySwapComments.getTag());
//            fragmentTransaction.commit();

            mRentBtn.setText("Bid");

            mViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewBookAct.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userModelPass", auctionDetailModel.getBookOwner().getUserObj());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

//            mRentBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(ViewBookAct.this, SwapBookChooser.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("swapDetail", auctionDetailModel);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            });
        }
    }

    public void addRentalHeader(RentalHeader rentalHeader){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_RENTAL_HEADER;

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);

        d("RentalHeaderAdd", rentalHeader.toString());


        Log.v("LOG_VOLLEY", mRequestBody);
        d("RentalHeaderVolley", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("onResponse addRentalH", "inside");
                Log.i("AddRentalHeader", response);
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

    private void checkIfExist(int userId, int rentalDetailId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.CHECK_EXIST+"/"+userId+"/"+rentalDetailId;

        d("CheckURL", URL);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalDetailModel);

        d("CheckIfExist", "inside");

        d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                d("ResponseExist", response);

                if(response.equals("")){
                    d("ResponseExist", "is null");
                }else{
                    d("ResponseExist", "not null");
                }



                if(response==null||response.isEmpty()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewBookAct.this);
                    alertDialogBuilder.setTitle("Terms and Condition");
                    alertDialogBuilder.setMessage("\n\n1.\tThis book must be returned on time.\n" +
                            "2.\tThis book should be returned in the same condition it was provided.\n" +
                            "3.\tThe renter will compensate for the damages that the book may incur during the duration of his/her usage.\n" +
                            "4.\tA fee of 50 pesos per day will be incurred to the renter if the book is not returned on or before the due date.");
                    alertDialogBuilder.setPositiveButton("Agree",
                            new DialogInterface.OnClickListener() {
                                @SuppressLint("NewApi")
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Toast.makeText(ViewBookAct.this, "You agreed to the terms and condition.", Toast.LENGTH_SHORT).show();
                                    timeOutDialog();
                                    RentalHeader rentalHeader = new RentalHeader();

                                    User user = (User) SPUtility.getSPUtil(ViewBookAct.this).getObject("USER_OBJECT", User.class);
                                    String nextDateStr="";

                                    @SuppressLint({"NewApi", "LocalSuppress"})
                                    Calendar c = Calendar.getInstance();


                                    @SuppressLint({"NewApi", "LocalSuppress"})
                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    nextDateStr = format.format(c.getTime());

                                    rentalHeader.setRentalDetail(rentalDetailModel);
                                    rentalHeader.setRentalTimeStamp(nextDateStr);
                                    rentalHeader.setUserId(user);
                                    rentalHeader.setTotalPrice((float) rentalDetailModel.getCalculatedPrice());
                                    rentalHeader.setStatus("Request");
                                    addRentalHeader(rentalHeader);
                                }
                            });

                    alertDialogBuilder.setNegativeButton("Disagree",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ViewBookAct.this, "You disagreed to the terms and condition.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{

                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ViewBookAct.this);
                    alertDialogBuilder.setTitle("!!!");
                    alertDialogBuilder.setMessage("You already requested for this book. You can't request again.");
                    alertDialogBuilder.setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
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

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_DIALOG:
                    if (mAlertDialog != null && mAlertDialog.isShowing()) {
                        mAlertDialog.dismiss();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private void timeOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please wait for the approval of the owner.\n You will get notification once approved by the owner.");
        builder.setPositiveButton("OK", null)
                .setNegativeButton("cacel", null);
        mAlertDialog = builder.create();
        mAlertDialog.show();
        // dismiss dialog in TIME_OUT ms
        mHandler.sendEmptyMessageDelayed(MSG_DISMISS_DIALOG, TIME_OUT);
    }

    public void getCount(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_COUNT+rentalDetailModel.getBookOwner().getBookOwnerId();

        d("CountURL", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        final RentalHeader rentalHeader =new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CountBookResponse", response);
                mRenters.setText("Rented by "+response+" people.");
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

    public void getRatings(int bookOwnerId){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_RATINGS+"/"+bookOwnerId;
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        final RentalHeader rentalHeader =new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ViewBookResponse", response);

                Float fl = Float.parseFloat(response);

                mRating.setRating(fl);

//                d("RatingAdapter: "+rentalDetailModel.getBookOwner().getBookObj().getBookTitle(), String.valueOf(fl));
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        d("onNavigationItem", "inside");

        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(ViewBookAct.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(ViewBookAct.this, ProfileActivity.class);
//            User user = userModel;
//            Log.d("User Id", String.valueOf(userModel.getUserId()));
//            Log.d("User name", userModel.getUserFname()+" "+userModel.getUserLname());
            Bundle bundlePass = new Bundle();
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(ViewBookAct.this).getObject("USER_OBJECT", User.class);
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtras(bundlePass);
            startActivity(intent);
        } else if (id == R.id.shelf) {
            Intent intent = new Intent(ViewBookAct.this, MyShelf.class);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(ViewBookAct.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            Intent intent = new Intent(ViewBookAct.this, TransactionActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {
            Intent intent = new Intent(ViewBookAct.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(ViewBookAct.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ViewBookAct.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDisplayBookReviewOnClick(Uri uri) {

    }

    @Override
    public void onSwapCommentOnClick(Uri uri) {

    }
}
