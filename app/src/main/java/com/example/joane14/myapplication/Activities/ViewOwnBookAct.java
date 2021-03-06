package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.example.joane14.myapplication.Class.BadgeDrawable;
import com.example.joane14.myapplication.Fragments.AuctionBidFragment;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.CountdownFrag;
import com.example.joane14.myapplication.Fragments.DisplayBookReview;
import com.example.joane14.myapplication.Fragments.DisplaySwapComments;
import com.example.joane14.myapplication.Fragments.MapLandingPage;
import com.example.joane14.myapplication.Fragments.VolleyUtil;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.DayModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.TimeModel;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.PlacesUtility;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.net.sip.SipErrorCode.TIME_OUT;
import static android.util.Log.d;

public class ViewOwnBookAct extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DisplaySwapComments.OnSwapCommentInteractionListener,
        DisplayBookReview.OnDisplayBookReviewInteractionListener,
        CountdownFrag.OnCountdownInteractionListener,
        AuctionBidFragment.OnAuctionBidInteractionListener{

    static final int MSG_DISMISS_DIALOG = 0;
    RentalDetail rentalDetailModel, rentToPost;
    boolean boolStartDate, boolEndDate, boolStartTime, boolEndTime;
    SwapDetail swapDetail, swapToPost;
    AuctionDetailModel auctionDetail, auctionToPost;
    BookOwnerModel bookOwnerModel;
    RatingBar mRating;
    TextView mRenters, mStartTime, mEndTime;
    Button mStartDate;
    List<String> mEndDate;
    private java.util.Calendar calendar;
    private Calendar aucDate;
    ArrayAdapter<String> adapterA;
    ViewTreeObserver vto;
    LayerDrawable icon;
    int countBadge;


    private AlertDialog mAlertDialog;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_own_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarViewOwnBook);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Book");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ViewOwnBookAct.this);

        View hView = navigationView.getHeaderView(0);
        TextView mName = (TextView) hView.findViewById(R.id.tvName);
        TextView mEmail = (TextView) hView.findViewById(R.id.tvEmail);
        ImageView profileImg = (ImageView) hView.findViewById(R.id.profPic);

        FrameLayout containerForCounter = (FrameLayout) findViewById(R.id.countdown_container);

        bookOwnerModel = new BookOwnerModel();

        rentalDetailModel = new RentalDetail();
        swapToPost = new SwapDetail();
        rentToPost = new RentalDetail();

        calendar = java.util.Calendar.getInstance();


        if (SPUtility.getSPUtil(this).contains("USER_OBJECT")) {
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname() + " " + userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(ViewOwnBookAct.this).load(userModel.getImageFilename()).fit().into(profileImg);
        }

        mRenters = (TextView) findViewById(R.id.vbRenters);
        TextView mAuthor = (TextView) findViewById(R.id.vbAuthor);
        TextView mLPrice = (TextView) findViewById(R.id.vbLockInP);
        TextView mHeaderLPrice = (TextView) findViewById(R.id.headerLP);
        TextView mHeaderRPrice = (TextView) findViewById(R.id.headerRP);
        TextView mGenre = (TextView) findViewById(R.id.vbGenre);
        TextView mRPrice = (TextView) findViewById(R.id.vbRentalP);
        TextView mTitle = (TextView) findViewById(R.id.vbTitle);
        Button mUpdateBtn = (Button) findViewById(R.id.btnVbUpdate);
        Button mDeleteBtn = (Button) findViewById(R.id.btnVbDElete);
        final TextView mContent = (TextView) findViewById(R.id.vbContent);
        final TextView mCondition = (TextView) findViewById(R.id.vbCondition);
        ImageView mBookImg = (ImageView) findViewById(R.id.vbBookPic);
        LinearLayout buttonLinear = (LinearLayout) findViewById(R.id.button_ll);
        LinearLayout rentalLinear = (LinearLayout) findViewById(R.id.rentalLL);
        LinearLayout priceLinear = (LinearLayout) findViewById(R.id.llprice);
        Spinner mSpinnerDay = (Spinner) findViewById(R.id.spinnerStatus);

        getNotificationCount();
        mSpinnerDay.setVisibility(View.GONE);
        mRating = (RatingBar) findViewById(R.id.vbRating);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        swapToPost.setSwapTimeStamp(sdf.format(cal));
        swapToPost.setSwapStatus("Available");

        rentToPost.setRentalStatus("Available");

        if (getIntent().getExtras().getSerializable("viewBook") != null) {

            containerForCounter.setVisibility(View.GONE);

            rentalDetailModel = (RentalDetail) getIntent().getExtras().getSerializable("viewBook");

            if(rentalDetailModel.getBookOwner().getBookStat().equals("Not Available")){
                buttonLinear.setVisibility(View.GONE);
            }

            mTitle.setText(rentalDetailModel.getBookOwner().getBookObj().getBookTitle());

            mLPrice.setText("₱ " + String.format("%.2f", rentalDetailModel.getCalculatedPrice()));
            mRPrice.setText("₱ " + String.format("%.2f", rentalDetailModel.getCalculatedPrice() / 2));

            final User user = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);

            getRatings(rentalDetailModel.getBookOwner().getBookOwnerId());

            String genreStr = "";
            int genreSize = rentalDetailModel.getBookOwner().getBookObj().getBookGenre().size();
            if(genreSize>1){
                for(int init=0;init<genreSize; init++){
                    genreStr = genreStr + rentalDetailModel.getBookOwner().getBookObj().getBookGenre().get(init).getGenreName();
                    if(genreSize-1>init){
                        genreStr = genreStr + ", ";
                    }
                }
            }else{
                genreStr = rentalDetailModel.getBookOwner().getBookObj().getBookGenre().get(0).getGenreName();
            }

            mGenre.setText(genreStr);

            String author = "";

            if (rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().size() != 0) {
                for (int init = 0; init < rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().size(); init++) {
                    if (!(rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))) {
                        author += rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName() + " ";
                        if (!(rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))) {
                            author += rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if (init + 1 < rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().size()) {
                                author += ", ";
                            }
                        }
                    }
                }
            } else {
                author = "Unknown Author";
            }
            mAuthor.setText(author);
            Glide.with(this).load(rentalDetailModel.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            mContent.setText(rentalDetailModel.getBookOwner().getBookObj().getBookDescription());
            mCondition.setText(rentalDetailModel.getBookOwner().getStatusDescription());
            makeTextViewResizable(mContent, 5, "View More", true);

            mRating.setRating(Float.parseFloat(String.valueOf(rentalDetailModel.getBookOwner().getRate())));

            getLatestRenter(rentalDetailModel.getRental_detailId());

            Bundle bundle = new Bundle();
            bundle.putSerializable("rentalDetail", rentalDetailModel);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_review_container, DisplayBookReview.newInstance(bundle));
            ft.commit();

            if (user.getUserId() != rentalDetailModel.getBookOwner().getUserObj().getUserId()) {
                mUpdateBtn.setVisibility(View.GONE);
                mDeleteBtn.setVisibility(View.GONE);
            }else{
                Log.d("updateDelete", "equals ang user");
            }

            mUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewOwnBookAct.this, UpdateBookActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("viewBook", rentalDetailModel);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog ad = new AlertDialog.Builder(ViewOwnBookAct.this).create();
                    ad.setTitle("Delete");
                    ad.setMessage("Are you sure you want to delete this book?");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateBook(rentalDetailModel.getBookOwner().getBookObj());
                        }
                    });
                    ad.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    ad.show();
                }
            });

        } else if (getIntent().getExtras().getSerializable("swapBook") != null) {

            containerForCounter.setVisibility(View.GONE);
            swapDetail = new SwapDetail();
            swapDetail = (SwapDetail) getIntent().getExtras().getSerializable("swapBook");

            if(swapDetail.getBookOwner().getBookStat().equals("Not Available")){
                buttonLinear.setVisibility(View.GONE);
            }

            priceLinear.setVisibility(View.GONE);

            getRatings(swapDetail.getBookOwner().getBookOwnerId());


            mTitle.setText(swapDetail.getBookOwner().getBookObj().getBookTitle());

            final User user = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);

            String genreStr = "";
            int genreSize = swapDetail.getBookOwner().getBookObj().getBookGenre().size();
            if(genreSize>1){
                for(int init=0;init<genreSize; init++){
                    genreStr = genreStr + swapDetail.getBookOwner().getBookObj().getBookGenre().get(init).getGenreName();
                    if(genreSize-1>init){
                        genreStr = genreStr + ", ";
                    }
                }
            }else{
                genreStr = swapDetail.getBookOwner().getBookObj().getBookGenre().get(0).getGenreName();
            }

            mGenre.setText(genreStr);


            String author = "";

            if (swapDetail.getBookOwner().getBookObj().getBookAuthor().size() != 0) {
                for (int init = 0; init < swapDetail.getBookOwner().getBookObj().getBookAuthor().size(); init++) {
                    if (!(swapDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))) {
                        author += swapDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName() + " ";
                        if (!(swapDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))) {
                            author += swapDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if (init + 1 < swapDetail.getBookOwner().getBookObj().getBookAuthor().size()) {
                                author += ", ";
                            }
                        }
                    }
                }
            } else {
                author = "Unknown Author";
            }
            mAuthor.setText(author);
            Glide.with(this).load(swapDetail.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            mContent.setText(swapDetail.getBookOwner().getBookObj().getBookDescription());
            mCondition.setText(swapDetail.getBookOwner().getStatusDescription());
            makeTextViewResizable(mContent, 5, "View More", true);

            if (user.getUserId() != swapDetail.getBookOwner().getUserObj().getUserId()) {
                mUpdateBtn.setVisibility(View.GONE);
                mDeleteBtn.setVisibility(View.GONE);
            }else{
                Log.d("updateDelete", "equals ang user");
            }

            mUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewOwnBookAct.this, UpdateBookActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("swapBook", swapDetail);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog ad = new AlertDialog.Builder(ViewOwnBookAct.this).create();
                    ad.setTitle("Delete");
                    ad.setMessage("Are you sure you want to delete this book?");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateBook(swapDetail.getBookOwner().getBookObj());
                        }
                    });
                    ad.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    ad.show();
                }
            });

        } else if (getIntent().getExtras().getSerializable("auctionBook") != null) {
            Log.d("auctionBook", "inside");

            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putSerializable("auctionBook", getIntent().getExtras().getSerializable("auctionBook"));

            auctionDetail = new AuctionDetailModel();
            auctionDetail = (AuctionDetailModel) getIntent().getExtras().getSerializable("auctionBook");

            if(auctionDetail.getBookOwner().getBookStat().equals("Not Available")){
                buttonLinear.setVisibility(View.GONE);
            }
            mHeaderLPrice.setText("Starting Price:");
            mHeaderRPrice.setVisibility(View.GONE);
            mRPrice.setVisibility(View.GONE);
            mLPrice.setText("₱ " + String.format("%.2f", auctionDetail.getStartingPrice()));


            FragmentTransaction fragt = getSupportFragmentManager().beginTransaction();
            bundle.putSerializable("auctionComment", auctionDetail);
            AuctionBidFragment abf = AuctionBidFragment.newInstance(bundle);
            fragt.replace(R.id.fragment_review_container, abf);
            fragt.commit();

            if (auctionDetail.getAuctionStatus().equals("start")) {
                CountdownFrag cdf = CountdownFrag.newInstance(bundle);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.countdown_container, cdf);
                ft.commit();
                mUpdateBtn.setText("Edit");
                mUpdateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(ViewOwnBookAct.this);
                        dialog.setContentView(R.layout.auction_edit_custom_dialog);
                        final DayModel day;
                        final TimeModel time;
                        final DatePickerDialog.OnDateSetListener mDatePicker;

                        aucDate = Calendar.getInstance();

                        mEndDate = new ArrayList<String>();
                        Log.d("mEndDate", "labay lang");
                        final List<String> dateEnd = new ArrayList<String>();
                        auctionToPost = new AuctionDetailModel();


                        Button mBtnOkay = (Button) dialog.findViewById(R.id.btnOkay);
                        Button mBtnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                        mEndTime = (TextView) dialog.findViewById(R.id.tvAucEndTime);

                        final TextView mEnd = (TextView) dialog.findViewById(R.id.endDate);

                        mEndTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolEndTime = true;
                                CreateTimePicker(mEndTime, "end");
                            }
                        });

                        mEnd.setText(auctionDetail.getEndDate());
                        mDatePicker = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO Auto-generated method stub
                                aucDate.set(java.util.Calendar.YEAR, year);
                                aucDate.set(java.util.Calendar.MONTH, monthOfYear);
                                aucDate.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                                String myFormat = "yyyy-MM-dd";
                                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(myFormat, Locale.US);
                                mEnd.setText(sdf.format(aucDate.getTime()));
                                Log.d("startDate", sdf.format(aucDate.getTime()));
                            }
                        };

                        mEnd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(ViewOwnBookAct.this, mDatePicker, aucDate.get(java.util.Calendar.YEAR),
                                        aucDate.get(java.util.Calendar.MONTH), aucDate.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });

                        mBtnOkay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                auctionDetail.setEndDate(mEnd.getText().toString());
                                auctionDetail.setEndTime(mEndTime.getText().toString());

                                if (mEnd.getText().length()==0||boolEndTime==false) {
                                    AlertDialog ad = new AlertDialog.Builder(ViewOwnBookAct.this).create();
                                    ad.setTitle("Alert!");
                                    ad.setMessage("Fill up all data.");
                                    ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    ad.show();
                                } else {
                                    updateAuctionDetail(auctionDetail);
                                }


                            }
                        });

                        mBtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    }
                });
                mDeleteBtn.setVisibility(View.GONE);
            } else {
                containerForCounter.setVisibility(View.GONE);
                mUpdateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ViewOwnBookAct.this, UpdateBookActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("auctionBook", auctionDetail);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog ad = new AlertDialog.Builder(ViewOwnBookAct.this).create();
                        ad.setTitle("Delete");
                        ad.setMessage("Are you sure you want to delete this book?");
                        ad.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateBook(auctionDetail.getBookOwner().getBookObj());
                            }
                        });
                        ad.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        ad.show();
                    }
                });
            }
            getRatings(auctionDetail.getBookOwner().getBookOwnerId());

            final User user = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);

            mTitle.setText(auctionDetail.getBookOwner().getBookObj().getBookTitle());
            String genreStr = "";
            int genreSize = auctionDetail.getBookOwner().getBookObj().getBookGenre().size();
            if(genreSize>1){
                for(int init=0;init<genreSize; init++){
                    genreStr = genreStr + auctionDetail.getBookOwner().getBookObj().getBookGenre().get(init).getGenreName();
                    if(genreSize-1>init){
                        genreStr = genreStr + ", ";
                    }
                }
            }else{
                genreStr = auctionDetail.getBookOwner().getBookObj().getBookGenre().get(0).getGenreName();
            }

            mGenre.setText(genreStr);


            String author = "";

            if (auctionDetail.getBookOwner().getBookObj().getBookAuthor().size() != 0) {
                for (int init = 0; init < auctionDetail.getBookOwner().getBookObj().getBookAuthor().size(); init++) {
                    if (!(auctionDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))) {
                        author += auctionDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName() + " ";
                        if (!(auctionDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))) {
                            author += auctionDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if (init + 1 < auctionDetail.getBookOwner().getBookObj().getBookAuthor().size()) {
                                author += ", ";
                            }
                        }
                    }
                }
            } else {
                author = "Unknown Author";
            }
            mAuthor.setText(author);
            Glide.with(this).load(auctionDetail.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            mContent.setText(auctionDetail.getBookOwner().getBookObj().getBookDescription());
            mCondition.setText(auctionDetail.getBookOwner().getStatusDescription());

            makeTextViewResizable(mContent, 5, "View More", true);

            if (user.getUserId() != auctionDetail.getBookOwner().getUserObj().getUserId()) {
                mUpdateBtn.setVisibility(View.GONE);
                mDeleteBtn.setVisibility(View.GONE);
            }else{
                Log.d("updateDelete", "equals ang user");
            }

        } else if (getIntent().getExtras().getSerializable("notAdBook") != null) {

            containerForCounter.setVisibility(View.GONE);
            BookOwnerModel bookOwner = new BookOwnerModel();

            bookOwner = (BookOwnerModel) getIntent().getExtras().getSerializable("notAdBook");

            rentalLinear.setVisibility(View.GONE);
            BookOwnerModel bookOwnerModel = new BookOwnerModel();

            bookOwnerModel = (BookOwnerModel) getIntent().getExtras().getSerializable("notAdBook");

            mTitle.setText(bookOwnerModel.getBookObj().getBookTitle());

            mHeaderLPrice.setText("Original Price");
            mLPrice.setText(String.valueOf(bookOwnerModel.getBookObj().getBookOriginalPrice()));
            mRPrice.setVisibility(View.GONE);

            final User user = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);


            String author = "";

            String genreStr = "";
            int genreSize = bookOwnerModel.getBookObj().getBookGenre().size();
            if(genreSize>1){
                for(int init=0;init<genreSize; init++){
                    genreStr = genreStr + bookOwnerModel.getBookObj().getBookGenre().get(init).getGenreName();
                    if(genreSize-1>init){
                        genreStr = genreStr + ", ";
                    }
                }
            }else{
                genreStr = bookOwnerModel.getBookObj().getBookGenre().get(0).getGenreName();
            }

            mGenre.setText(genreStr);

            if (bookOwnerModel.getBookObj().getBookAuthor().size() != 0) {
                for (int init = 0; init < bookOwnerModel.getBookObj().getBookAuthor().size(); init++) {
                    if (!(bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))) {
                        author += bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorFName() + " ";
                        if (!(bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))) {
                            author += bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if (init + 1 < bookOwnerModel.getBookObj().getBookAuthor().size()) {
                                author += ", ";
                            }
                        }
                    }
                }
            } else {
                author = "Unknown Author";
            }
            mAuthor.setText(author);
            Glide.with(this).load(bookOwnerModel.getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            mContent.setText(bookOwnerModel.getBookObj().getBookDescription());
            mCondition.setText(bookOwnerModel.getStatusDescription());
            makeTextViewResizable(mContent, 5, "View More", true);

            if(bookOwner.getUserObj().getUserId()!=user.getUserId()){
                mUpdateBtn.setVisibility(View.GONE);
                mDeleteBtn.setVisibility(View.GONE);
            }else{
                Log.d("updateDelete", "equals ang user");
            }
            final BookOwnerModel finalBookOwner1 = bookOwner;
            mUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewOwnBookAct.this, UpdateBookActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notAdBook", finalBookOwner1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            final BookOwnerModel finalBookOwner2 = bookOwner;
            mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog ad = new AlertDialog.Builder(ViewOwnBookAct.this).create();
                    ad.setTitle("Delete");
                    ad.setMessage("Are you sure you want to delete this book?");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateBook(finalBookOwner2.getBookObj());
                        }
                    });
                    ad.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    ad.show();
                }
            });

        }
    }

    private void updateAuctionDetail(AuctionDetailModel auctionDetailModel) {
        String URL = Constants.PUT_AUCTION_DETAIL;
        Log.d("LatestRenterURL", URL);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(auctionDetailModel);

        int maxLogSize = 2000;
        for (int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("updateAuction", mRequestBody.substring(start, end));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("updateAucDetRes", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                AuctionDetailModel rh= gson.fromJson(response, AuctionDetailModel.class);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        });
        VolleyUtil.volleyRQInstance(ViewOwnBookAct.this).add(stringRequest);
    }

    private void getLatestRenter(int rentalDetailId) {
        String URL = Constants.GET_LATEST_RENTER + rentalDetailId;
        Log.d("LatestRenterURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LatestRenterResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                RentalHeader rh= gson.fromJson(response, RentalHeader.class);

                String message = "This book is rented by "+rh.getUserId().getUserFname()+" "+rh.getUserId().getUserLname()+" on "+rh.getDateDeliver();
                mRenters.setText(message);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        });
        VolleyUtil.volleyRQInstance(ViewOwnBookAct.this).add(stringRequest);
    }


    public static void makeTextViewResizable(final TextView textView, final int maxLine, final String expandText, final boolean viewMore) {
        Log.d("makeTextViewResizable", "inside");
        if (textView.getTag() == null) {
            textView.setTag(textView.getText());
        }

        ViewTreeObserver treeObserver = textView.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndText;
                ViewTreeObserver observer = textView.getViewTreeObserver();

                observer.removeGlobalOnLayoutListener(this);

                if (maxLine == 0) {
                    lineEndText = textView.getLayout().getLineEnd(0);
                    text = textView.getText().subSequence(0, lineEndText - expandText.length() - 1) + " " + expandText;
                } else if (maxLine > 0 && textView.getLineCount() >= maxLine) {
                    lineEndText = textView.getLayout().getLineEnd(maxLine - 1);
                    text = textView.getText().subSequence(0, lineEndText - expandText.length() - 1) + " " + expandText;
                } else {
                    lineEndText = textView.getLayout().getLineEnd(textView.getLayout().getLineCount() - 1);
                    text = textView.getText().subSequence(0, lineEndText) + " " + expandText;
                }

                textView.setText(text);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                textView.setText(addClickablePartTextViewResizable(Html.fromHtml(textView.getText().toString()), textView, lineEndText, expandText,
                        viewMore), TextView.BufferType.SPANNABLE);

            }
        });


    }

    public void updateBook(final Book bookModel) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        bookModel.setStatus("Deleted");
        String URL = Constants.UPDATE_BOOK;

        final User user = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookModel);


        d("BookOwnerPostVolley", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("BookPutRes", response);

                AlertDialog ad = new AlertDialog.Builder(ViewOwnBookAct.this).create();
                ad.setTitle("Delete");
                ad.setMessage("The book, " + bookModel.getBookTitle() + ", has been successfully deleted.");
                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ViewOwnBookAct.this, ProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userModelPass", user);
                        intent.putExtras(bundle);
                        startActivity(intent);
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

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    Log.d("spannable", "inside");
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        makeTextViewResizable(tv, 5, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    @SuppressLint("NewApi")
    public void customAuctionDialog(final String status) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.auction_custom_dialog);
        dialog.setTitle("Auction Settings");
        final DayModel day;
        final TimeModel time;
        final DatePickerDialog.OnDateSetListener mDatePicker;

        aucDate = Calendar.getInstance();


        day = new DayModel();
        time = new TimeModel();

        mEndDate = new ArrayList<String>();
        Log.d("mEndDate", "labay lang");
        final List<String> dateEnd = new ArrayList<String>();
        final List<String> rangeDays = new ArrayList<String>();


        auctionToPost = new AuctionDetailModel();


        Button mBtnOkay = (Button) dialog.findViewById(R.id.btnOkay);
        Button mBtnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        mStartTime = (TextView) dialog.findViewById(R.id.tvAucStartTime);
        mEndTime = (TextView) dialog.findViewById(R.id.tvAucEndTime);

        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolStartTime = true;
                CreateTimePicker(mStartTime, "start");
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolEndTime = true;
                CreateTimePicker(mEndTime, "end");
            }
        });

        mDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                aucDate.set(java.util.Calendar.YEAR, year);
                aucDate.set(java.util.Calendar.MONTH, monthOfYear);
                aucDate.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(myFormat, Locale.US);
                mStartDate.setText(sdf.format(aucDate.getTime()));
                Log.d("startDate", sdf.format(aucDate.getTime()));

                auctionToPost.setStartDate(sdf.format(aucDate.getTime()));
                auctionToPost.setAuctionStatus("start");
                auctionToPost.setStatus("Available");

                aucDate.add(java.util.Calendar.DAY_OF_MONTH, 1);
                dateEnd.add(sdf.format(aucDate.getTime()));
                adapterA.notifyDataSetChanged();
                Log.d("endDate 1", sdf.format(aucDate.getTime()));


                aucDate.add(java.util.Calendar.DAY_OF_MONTH, 1);
                dateEnd.add(sdf.format(aucDate.getTime()));
                adapterA.notifyDataSetChanged();
                Log.d("endDate 2", sdf.format(aucDate.getTime()));

                aucDate.add(java.util.Calendar.DAY_OF_MONTH, 1);
                dateEnd.add(sdf.format(aucDate.getTime()));
                adapterA.notifyDataSetChanged();
                Log.d("endDate 3", sdf.format(aucDate.getTime()));
            }
        };

        mStartDate = (Button) dialog.findViewById(R.id.tvAucStartDate);
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolStartDate = true;
                new DatePickerDialog(ViewOwnBookAct.this, mDatePicker, aucDate.get(java.util.Calendar.YEAR),
                        aucDate.get(java.util.Calendar.MONTH), aucDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if (dateEnd.size() == 0 || dateEnd.isEmpty()) {
            Log.d("dateEnd", "empty");
        } else {
            Log.d("dateEnd", "not empty");
        }

        adapterA = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dateEnd);
        adapterA.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        Spinner mSpinner = (Spinner) dialog.findViewById(R.id.aucSpinner);
        if (adapterA == null) {
            Log.d("apater", "is null");
        } else {
            Log.d("apater", "is not null");
        }
        mSpinner.setAdapter(adapterA);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("AuctionItemSelected", dateEnd.get(position));
                auctionToPost.setEndDate(dateEnd.get(position));
                boolEndDate = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("AuctionItemSelected", "walay selceted");

            }
        });
        mBtnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (boolStartTime == false || boolEndTime == false || boolEndDate == false || boolStartDate == false) {
                    AlertDialog ad = new AlertDialog.Builder(ViewOwnBookAct.this).create();
                    ad.setTitle("Alert!");
                    ad.setMessage("Fill up all data.");
                    ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                } else {
                    updateBookOwner(bookOwnerModel, status);
                }


            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    public void customDialog(final String status) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.spinner);
        dialog.setTitle("Choose day range:");
        final DayModel day;
        final TimeModel time;


        day = new DayModel();
        time = new TimeModel();

        final List<String> rangeDays = new ArrayList<String>();

        rangeDays.add("1");
        rangeDays.add("2");
        rangeDays.add("3");
        rangeDays.add("4");
        rangeDays.add("5");

        Button mBtnOkay = (Button) dialog.findViewById(R.id.btnOkay);
        Button mBtnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        Spinner mSpinnerDay = (Spinner) dialog.findViewById(R.id.spinnerDay);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, rangeDays);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        if (adapter == null) {
            Log.d("apater", "is null");
        } else {
            Log.d("apater", "is not null");
        }
        mSpinnerDay.setAdapter(adapter);

        mSpinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rentToPost.setDaysForRent(Integer.parseInt(rangeDays.get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBtnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBookOwner(bookOwnerModel, status);
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void updateBookOwner(final BookOwnerModel bookOwnerModelToPost, final String fromWhere) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.UPDATE_BOOK_OWNER_1;

        final User user = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookOwnerModelToPost);


        d("BookOwnerPostVolley", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("BookOwnerPutRes", response);

                if (bookOwnerModelToPost.getStatus().equals("Rent")) {
                    if (fromWhere.equals("Swap")) {
                        rentToPost.setBookOwner(bookOwnerModelToPost);
                        rentToPost.setCalculatedPrice(swapDetail.getSwapPrice());
                        rentToPost.setDaysForRent(5);
                    } else if (fromWhere.equals("NotAdvertised")) {
                        rentToPost.setBookOwner(bookOwnerModelToPost);
                        rentToPost.setDaysForRent(5);
                        rentToPost.setCalculatedPrice(calculatePrice(bookOwnerModelToPost.getBookObj()));
                    } else if (fromWhere.equals("Auction")) {
                        rentToPost.setBookOwner(bookOwnerModelToPost);
                        rentToPost.setDaysForRent(5);
                        rentToPost.setCalculatedPrice(calculatePrice(bookOwnerModelToPost.getBookObj()));
                    }

                    addRentalDetail(rentToPost);
                } else if (bookOwnerModelToPost.getStatus().equals("Swap")) {
                    if (fromWhere.equals("Rent")) {
                        swapToPost.setBookOwner(bookOwnerModelToPost);
                        swapToPost.setSwapPrice(Float.parseFloat(String.valueOf(rentalDetailModel.getCalculatedPrice())));
                        swapToPost.setSwapDescription(rentalDetailModel.getBookOwner().getStatusDescription());
                    } else if (fromWhere.equals("NotAdvertised")) {
                        swapToPost.setBookOwner(bookOwnerModelToPost);
                        swapToPost.setSwapPrice(Float.parseFloat(String.valueOf(calculatePrice(bookOwnerModelToPost.getBookObj()))));
                        swapToPost.setSwapDescription(bookOwnerModelToPost.getStatusDescription());
                    } else if (fromWhere.equals("Auction")) {
                        swapToPost.setBookOwner(bookOwnerModelToPost);
                        swapToPost.setSwapPrice(Float.parseFloat(String.valueOf(calculatePrice(bookOwnerModelToPost.getBookObj()))));
                        swapToPost.setSwapDescription(bookOwnerModelToPost.getStatusDescription());
                    }
                    addSwapDetail(swapToPost);
                } else if (bookOwnerModelToPost.getStatus().equals("Auction")) {
                    if (fromWhere.equals("NotAdvertised")) {
                        auctionToPost.setBookOwner(bookOwnerModelToPost);
                        auctionToPost.setAuctionStatus("start");
                        auctionToPost.setStatus("Available");
                        auctionToPost.setAuctionDescription(bookOwnerModelToPost.getStatusDescription());
                        auctionToPost.setStartingPrice(Float.parseFloat(String.valueOf(calculatePrice(bookOwnerModelToPost.getBookObj()))));
                        auctionToPost.setStartTime(mStartTime.getText().toString());
                        auctionToPost.setEndTime(mEndTime.getText().toString());
                    } else if (fromWhere.equals("Swap")) {
                        auctionToPost.setBookOwner(bookOwnerModelToPost);
                        auctionToPost.setAuctionStatus("start");
                        auctionToPost.setStatus("Available");
                        auctionToPost.setAuctionDescription(bookOwnerModelToPost.getStatusDescription());
                        auctionToPost.setStartingPrice(Float.parseFloat(String.valueOf(calculatePrice(bookOwnerModelToPost.getBookObj()))));
                        auctionToPost.setStartTime(mStartTime.getText().toString());
                        auctionToPost.setEndTime(mEndTime.getText().toString());
                    } else if (fromWhere.equals("Rent")) {
                        auctionToPost.setBookOwner(bookOwnerModelToPost);
                        auctionToPost.setAuctionStatus("start");
                        auctionToPost.setStatus("Available");
                        auctionToPost.setAuctionDescription(bookOwnerModelToPost.getStatusDescription());
                        auctionToPost.setStartingPrice(Float.parseFloat(String.valueOf(calculatePrice(bookOwnerModelToPost.getBookObj()))));
                        auctionToPost.setStartTime(mStartTime.getText().toString());
                        auctionToPost.setEndTime(mEndTime.getText().toString());
                    }

                    auctionToPost.setAuctionStatus("start");

                    addAuctionDetail(auctionToPost);
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

    private void addAuctionDetail(AuctionDetailModel auctionDetailModel) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_AUCTION_DETAIL_1;

        auctionDetailModel.setStatus("Available");
        auctionDetailModel.setAuctionStatus("start");


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(auctionDetailModel);


        int maxLogSize = 2000;
        for (int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("aucLogVolley", mRequestBody);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("auctionDetailAddLog", response);

                Intent intent = new Intent(ViewOwnBookAct.this, ProfileActivity.class);
                Bundle bundlePass = new Bundle();
                User userModel = new User();
                userModel = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);
                bundlePass.putSerializable("userModelPass", userModel);
                intent.putExtras(bundlePass);
                startActivity(intent);
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

    private void addSwapDetail(SwapDetail swapDetailModel) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_SWAP_DETAIL_1;

        swapDetailModel.setSwapStatus("Available");

//        user.setDayTimeModel();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapDetailModel);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("swapDetailAddLog", response);

                Intent intent = new Intent(ViewOwnBookAct.this, ProfileActivity.class);
                Bundle bundlePass = new Bundle();
                User userModel = new User();
                userModel = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);
                bundlePass.putSerializable("userModelPass", userModel);
                intent.putExtras(bundlePass);
                startActivity(intent);
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

    private void addRentalDetail(RentalDetail postRental) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_RENTAL_DETAIL_1;


        postRental.setRentalStatus("Available");

//        user.setDayTimeModel();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(postRental);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("rentalDetailAddLog", response);

                Intent intent = new Intent(ViewOwnBookAct.this, ProfileActivity.class);
                Bundle bundlePass = new Bundle();
                User userModel = new User();
                userModel = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);
                bundlePass.putSerializable("userModelPass", userModel);
                intent.putExtras(bundlePass);
                startActivity(intent);
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

    public double calculatePrice(Book bookModel) {
        double price = 0, depPrice;

        Date date = null;
        depPrice = bookModel.getBookOriginalPrice() / 5;

        java.text.SimpleDateFormat formate = new java.text.SimpleDateFormat("yyyy");

        try {
            date = (formate).parse(bookModel.getBookPublishedDate());
            Log.d("datePila", String.valueOf(date));
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int gap = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) - calendar.get(java.util.Calendar.YEAR);

        Log.d("gapPila", String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)));
        if (gap >= 5) {
            price = depPrice;
            Log.d("calculatedPric depPrice", String.valueOf(price));
        } else if (gap == 0) {
            price = bookModel.getBookOriginalPrice();
            Log.d("calculatedPric OrgPri", String.valueOf(price));
        } else {
            price = bookModel.getBookOriginalPrice() - (depPrice * gap);
            Log.d("calculatedPrice dep*gap", String.valueOf(price));
        }

        Log.d("calculatedPrice", String.valueOf(price));
        return price;
    }

    public void addRentalHeader(RentalHeader rentalHeader) {
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
        String URL = Constants.CHECK_EXIST + "/" + userId + "/" + rentalDetailId;

        d("CheckURL", URL);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalDetailModel);

        d("CheckIfExist", "inside");

        d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                d("ResponseExist", response);

                if (response.equals("")) {
                    d("ResponseExist", "is null");
                } else {
                    d("ResponseExist", "not null");
                }


                if (response == null || response.isEmpty()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewOwnBookAct.this);
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
                                    Toast.makeText(ViewOwnBookAct.this, "You agreed to the terms and condition.", Toast.LENGTH_SHORT).show();
                                    timeOutDialog();
                                    RentalHeader rentalHeader = new RentalHeader();

                                    User user = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);
                                    String nextDateStr = "";

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

                    alertDialogBuilder.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ViewOwnBookAct.this, "You disagreed to the terms and condition.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {

                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ViewOwnBookAct.this);
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

    public void CreateTimePicker(final TextView tvSet, final String stat) {


        final java.util.Calendar c = java.util.Calendar.getInstance();
        int mHour = c.get(java.util.Calendar.HOUR_OF_DAY);
        int mMinute = c.get(java.util.Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        int hour = hourOfDay;
                        int minutes = minute;
                        String timeSet = "";
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12) {
                            timeSet = "PM";
                        } else {
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes;
                        else
                            min = String.valueOf(minutes);

                        String hours = "";

                        if (hour < 10)
                            hours = "0" + hour;
                        else
                            hours = String.valueOf(hour);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(hours).append(':')
                                .append(min).append(" ").append(timeSet).toString();

                        String timeGiven = "";
                        hourOfDay = Integer.parseInt(String.format("%02d", hourOfDay));
                        minute = Integer.parseInt(String.format("%02d", minute));
                        timeGiven = hourOfDay + ":" + minute;
                        Log.d("time selected Auction", timeGiven);
//
//                        if(pos==0){
//                            mStartTime.setText(aTime);
//                        }else{
//                            mEndTime.setText(aTime);
//                        }
//
                        tvSet.setText(aTime);

                        if (stat.equals("start")) {
                            auctionToPost.setStartTime(aTime);
                        } else if (stat.equals("end")) {
                            auctionToPost.setEndTime(aTime);
                        }

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
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

    public void getCount() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_COUNT + rentalDetailModel.getBookOwner().getBookOwnerId();

        d("CountURL", URL);
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        final RentalHeader rentalHeader = new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CountBookResponse", response);
                mRenters.setText("Rented by " + response + " people.");
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

    public void getRatings(int bookOwnerId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_RATINGS + bookOwnerId;
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        final RentalHeader rentalHeader = new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ViewBookResponse", response);

                Float fl = Float.parseFloat(response);

                mRating.setRating(fl);

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

//    public void customDialog(){
//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.time_date_dialog);
//        dialog.setTitle("Choose Time and Day");
//        final DayModel day;
//        final TimeModel time;
//
//
//        day = new DayModel();
//        time = new TimeModel();
//
//        // set the custom dialog components - text, image and button
//        etTimeFrom = (EditText) dialog.findViewById(R.id.tcFrom);
//        etTimeTo = (EditText) dialog.findViewById(R.id.tcTo);
//        Button mBtnOkay = (Button) dialog.findViewById(R.id.btnOkay);
//        Button mBtnCancel = (Button) dialog.findViewById(R.id.btnCancel);
//        Spinner mSpinnerDay = (Spinner) dialog.findViewById(R.id.spinnerDay);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, selectedDays);
//        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        if(adapter==null){
//            Log.d("apater", "is null");
//        }else{
//            Log.d("apater", "is not null");
//        }
//        mSpinnerDay.setAdapter(adapter);
//
//        mSpinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                day.setStrDay(selectedDays.get(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        etTimeFrom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CreateTimePicker(0);
//            }
//        });
//        etTimeTo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CreateTimePicker(1);
//            }
//        });
//
//        mBtnOkay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                time.setStrTime(etTimeTo.getText().toString()+" - "+etTimeFrom.getText().toString());
//                userDayTime.setDay(day);
//                userDayTime.setTime(time);
//                userDayTimeList.add(userDayTime);
//                mAdapter.notifyDataSetChanged();
//                dialog.cancel();
//            }
//        });
//
//        mBtnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//            }
//        });
//        dialog.show();
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        d("onNavigationItem", "inside");

        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(ViewOwnBookAct.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(ViewOwnBookAct.this, ProfileActivity.class);
//            User user = userModel;
//            Log.d("User Id", String.valueOf(userModel.getUserId()));
//            Log.d("User name", userModel.getUserFname()+" "+userModel.getUserLname());
            Bundle bundlePass = new Bundle();
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtras(bundlePass);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(ViewOwnBookAct.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            Intent intent = new Intent(ViewOwnBookAct.this, BookActActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {
            Intent intent = new Intent(ViewOwnBookAct.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(ViewOwnBookAct.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ViewOwnBookAct.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_main, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        MenuItem item = menu.findItem(R.id.action_notifications);
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                FragmentManager fragmentManager;
                fragmentManager = getSupportFragmentManager();
                MapLandingPage prefFrag = MapLandingPage.newInstance();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing_container, prefFrag, prefFrag.getTag());
                fragmentTransaction.commit();

                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SearchKeyword", query);
                Intent intent = new Intent(ViewOwnBookAct.this, SearchResult.class);
                Bundle bundle = new Bundle();
                bundle.putString("SearchKeyword", query);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SearchView", "onclick");
                Intent intent = new Intent(ViewOwnBookAct.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(ViewOwnBookAct.this, NotificationAct.class);
                startActivity(intent);


                return false;
            }
        });

        MenuItem itemCart = menu.findItem(R.id.action_notifications);
        icon = (LayerDrawable) itemCart.getIcon();

        return super.onCreateOptionsMenu(menu);
    }

    private int getNotificationCount() {
        User user = (User) SPUtility.getSPUtil(getApplicationContext()).getObject("USER_OBJECT", User.class);
        String query = Constants.GET_COUNT_NOTIF + user.getUserId();

        this.countBadge = 0;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int count = Integer.parseInt(response);
                countBadge = count;
                setBadgeCount(ViewOwnBookAct.this, count);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        PlacesUtility.getInstance(this).add(stringRequest);
        return countBadge;
    }

    public void setBadgeCount(Context context, int count) {
        if (icon != null) {
            String countString = Integer.toString(count);
            BadgeDrawable badge;
            Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
            if (reuse != null && reuse instanceof BadgeDrawable) {
                badge = (BadgeDrawable) reuse;
            } else {
                badge = new BadgeDrawable(context);
            }

            badge.setCount(countString);
            icon.mutate();
            icon.setDrawableByLayerId(R.id.ic_badge, badge);
        }
    }

    @Override
    public void onDisplayBookReviewOnClick(Uri uri) {

    }

    @Override
    public void onSwapCommentOnClick(Uri uri) {

    }

    @Override
    public void onCountdownOnClick(Uri uri) {

    }

    @Override
    public void onAuctionBidClick(Uri uri) {

    }
}
