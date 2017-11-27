package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
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
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.DayModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.TimeModel;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
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

import static android.net.sip.SipErrorCode.TIME_OUT;
import static android.util.Log.d;

public class ViewOwnBookAct extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DisplaySwapComments.OnSwapCommentInteractionListener,
        DisplayBookReview.OnDisplayBookReviewInteractionListener{

    static final int MSG_DISMISS_DIALOG = 0;
    RentalDetail rentalDetailModel, rentToPost;
    SwapDetail swapDetail, swapToPost;
    BookOwnerModel bookOwnerModel;
    RatingBar mRating;
    TextView mRenters;
    private java.util.Calendar calendar;


    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_own_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarViewOwnBook);
        setSupportActionBar(toolbar);

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

        bookOwnerModel = new BookOwnerModel();

        rentalDetailModel = new RentalDetail();
        swapToPost = new SwapDetail();
        rentToPost = new RentalDetail();

        calendar = java.util.Calendar.getInstance();


        if(SPUtility.getSPUtil(this).contains("USER_OBJECT")){
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname()+" "+userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(ViewOwnBookAct.this).load( userModel.getImageFilename()).fit().into(profileImg);
        }

        mRenters = (TextView) findViewById(R.id.vbRenters);
        TextView mAuthor = (TextView) findViewById(R.id.vbAuthor);
        TextView mLPrice = (TextView) findViewById(R.id.vbLockInP);
        TextView mHeaderLPrice = (TextView) findViewById(R.id.headerLP);
        TextView mRPrice = (TextView) findViewById(R.id.vbRentalP);
        TextView mTitle = (TextView) findViewById(R.id.vbTitle);
        Button mViewBtn = (Button) findViewById(R.id.btnVbViewOwner);
        Button mRentBtn = (Button) findViewById(R.id.btnVbRent);
        TextView mContent = (TextView) findViewById(R.id.vbContent);
        TextView mCondition = (TextView) findViewById(R.id.vbCondition);
        ImageView mBookImg = (ImageView) findViewById(R.id.vbBookPic);
        LinearLayout buttonLinear = (LinearLayout) findViewById(R.id.button_ll);
        LinearLayout rentalLinear= (LinearLayout) findViewById(R.id.rentalLL);
        Spinner mSpinnerDay = (Spinner) findViewById(R.id.spinnerStatus);


        mRating = (RatingBar) findViewById(R.id.vbRating);

        if (getIntent().getExtras().getSerializable("viewBook")!=null){

            rentalDetailModel = (RentalDetail) getIntent().getExtras().getSerializable("viewBook");

            mTitle.setText(rentalDetailModel.getBookOwner().getBookObj().getBookTitle());

            mLPrice.setText(String.valueOf(rentalDetailModel.getCalculatedPrice()));
            mRPrice.setText(String.valueOf(rentalDetailModel.getCalculatedPrice()/2));

            final User user = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);

            if(user.getUserId()==rentalDetailModel.getBookOwner().getUserObj().getUserId()){
                buttonLinear.setVisibility(View.GONE);
            }

            getRatings(rentalDetailModel.getBookOwner().getBookOwnerId());
            List<String> statusBook = new ArrayList<String>();
            statusBook.add("Rent");
            statusBook.add("Swap");
            statusBook.add("Not Advertised");
            statusBook.add("Auction");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewOwnBookAct.this, android.R.layout.simple_dropdown_item_1line, statusBook);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mSpinnerDay.setAdapter(adapter);
            mSpinnerDay.setSelection(0);

            mSpinnerDay.setBackgroundColor(ContextCompat.getColor(ViewOwnBookAct.this, R.color.colorRent));

            mSpinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Log.d("RentBookOwner", String.valueOf(position));

                    if(position==0){
                        Toast.makeText(ViewOwnBookAct.this, "This book is already for Rent.", Toast.LENGTH_SHORT).show();
//                        BookOwnerModel bookOwnerModel = new BookOwnerModel();
//                        bookOwnerModel = rentalDetailModel.getBookOwner();
//                        bookOwnerModel.setStatus("Rent");
//                        updateBookOwner(bookOwnerModel);
                    }else if(position==1){
                        bookOwnerModel = rentalDetailModel.getBookOwner();
                        bookOwnerModel.setStatus("Swap");
                        updateBookOwner(bookOwnerModel, "Rent");
                    }else if(position==2){
                        bookOwnerModel = rentalDetailModel.getBookOwner();
                        bookOwnerModel.setStatus("none");
                        updateBookOwner(bookOwnerModel, "Rent");
                    }else{

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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

            Bundle bundle = new Bundle();
            bundle.putSerializable("rentalDetail", rentalDetailModel);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_review_container, DisplayBookReview.newInstance(bundle));
            ft.commit();

            mViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewOwnBookAct.this, ProfileActivity.class);
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


            List<String> statusBook = new ArrayList<String>();
            statusBook.add("Rent");
            statusBook.add("Swap");
            statusBook.add("Not Advertised");
            statusBook.add("Auction");

            getRatings(swapDetail.getBookOwner().getBookOwnerId());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewOwnBookAct.this, android.R.layout.simple_dropdown_item_1line, statusBook);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mSpinnerDay.setAdapter(adapter);

            mSpinnerDay.setSelection(1);
            mSpinnerDay.setBackgroundColor(ContextCompat.getColor(ViewOwnBookAct.this, R.color.colorSwap));

            mSpinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Log.d("SwapBookOwner", String.valueOf(position));
                    if(position==0){
                        bookOwnerModel = swapDetail.getBookOwner();
                        bookOwnerModel.setStatus("Rent");
                        customDialog("Swap");
                    }else if(position==1){
                        Toast.makeText(ViewOwnBookAct.this, "This book is already for Swap.", Toast.LENGTH_SHORT).show();
//                        BookOwnerModel bookOwnerModel = new BookOwnerModel();
//                        bookOwnerModel = rentalDetailModel.getBookOwner();
//                        bookOwnerModel.setStatus("Swap");
//                        updateBookOwner(bookOwnerModel);
                    }else if(position==2){
                        bookOwnerModel = swapDetail.getBookOwner();
                        bookOwnerModel.setStatus("none");
                        updateBookOwner(bookOwnerModel, "Swap");
                    }else{

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mTitle.setText(swapDetail.getBookOwner().getBookObj().getBookTitle());



//            mLPrice.setText(String.valueOf(swapDetail.getSwapPrice()));
//            mRPrice.setText(String.valueOf(swapDetail.getSwapPrice()/2));
//            mRPrice.setVisibility(View.GONE);

            final User user = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);

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

//            getRatings();
//            getCount();

//            Bundle bundle = new Bundle();
//            bundle.putSerializable("swapDetail", swapDetail);
//
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.fragment_review_container, DisplayBookReview.newInstance(bundle));
//            ft.commit();

            Log.d("SwapComment", "Display");
            Bundle bundle = new Bundle();
            bundle.putSerializable("swapComment", swapDetail);
            DisplaySwapComments displaySwapComments = DisplaySwapComments.newInstance(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_review_container, displaySwapComments, displaySwapComments.getTag());
            fragmentTransaction.commit();

            mRentBtn.setText("Request to Swap");

            mViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewOwnBookAct.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userModelPass", swapDetail.getBookOwner().getUserObj());
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
        }else if(getIntent().getExtras().getSerializable("auctionBook")!=null){


            swapDetail = new SwapDetail();
            swapDetail = (SwapDetail) getIntent().getExtras().getSerializable("swapBook");


            List<String> statusBook = new ArrayList<String>();
            statusBook.add("Rent");
            statusBook.add("Swap");
            statusBook.add("Not Advertised");
            statusBook.add("Auction");

            getRatings(swapDetail.getBookOwner().getBookOwnerId());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewOwnBookAct.this, android.R.layout.simple_dropdown_item_1line, statusBook);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mSpinnerDay.setAdapter(adapter);

            mSpinnerDay.setSelection(1);
            mSpinnerDay.setBackgroundColor(ContextCompat.getColor(ViewOwnBookAct.this, R.color.colorSwap));

            mSpinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Log.d("SwapBookOwner", String.valueOf(position));
                    if(position==0){
                        bookOwnerModel = swapDetail.getBookOwner();
                        bookOwnerModel.setStatus("Rent");
                        customDialog("Swap");
                    }else if(position==1){
                        Toast.makeText(ViewOwnBookAct.this, "This book is already for Swap.", Toast.LENGTH_SHORT).show();
//                        BookOwnerModel bookOwnerModel = new BookOwnerModel();
//                        bookOwnerModel = rentalDetailModel.getBookOwner();
//                        bookOwnerModel.setStatus("Swap");
//                        updateBookOwner(bookOwnerModel);
                    }else if(position==2){
                        bookOwnerModel = swapDetail.getBookOwner();
                        bookOwnerModel.setStatus("none");
                        updateBookOwner(bookOwnerModel, "Swap");
                    }else{

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mTitle.setText(swapDetail.getBookOwner().getBookObj().getBookTitle());



//            mLPrice.setText(String.valueOf(swapDetail.getSwapPrice()));
//            mRPrice.setText(String.valueOf(swapDetail.getSwapPrice()/2));
//            mRPrice.setVisibility(View.GONE);

            final User user = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);

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

//            getRatings();
//            getCount();

//            Bundle bundle = new Bundle();
//            bundle.putSerializable("swapDetail", swapDetail);
//
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.fragment_review_container, DisplayBookReview.newInstance(bundle));
//            ft.commit();

            Log.d("SwapComment", "Display");
            Bundle bundle = new Bundle();
            bundle.putSerializable("swapComment", swapDetail);
            DisplaySwapComments displaySwapComments = DisplaySwapComments.newInstance(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_review_container, displaySwapComments, displaySwapComments.getTag());
            fragmentTransaction.commit();

            mRentBtn.setText("Request to Swap");

            mViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewOwnBookAct.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userModelPass", swapDetail.getBookOwner().getUserObj());
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
        }else if(getIntent().getExtras().getSerializable("notAdBook")!=null){

            BookOwnerModel bookOwner = new BookOwnerModel();

            bookOwner = (BookOwnerModel) getIntent().getExtras().getSerializable("notAdBook");

            rentalLinear.setVisibility(View.GONE);
            List<String> statusBook = new ArrayList<String>();
            statusBook.add("Rent");
            statusBook.add("Swap");
            statusBook.add("Not Advertised");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewOwnBookAct.this, android.R.layout.simple_dropdown_item_1line, statusBook);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mSpinnerDay.setAdapter(adapter);
            mSpinnerDay.setSelection(2);

            mSpinnerDay.setBackgroundColor(ContextCompat.getColor(ViewOwnBookAct.this, R.color.colorGray));

            final BookOwnerModel finalBookOwner = bookOwner;
            mSpinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Log.d("NotAdBookOwner", String.valueOf(position));
                    if(position==0){
                        bookOwnerModel = finalBookOwner;
                        bookOwnerModel.setStatus("Rent");
                        customDialog("NotAdvertised");
                    }else if(position==1){
                        bookOwnerModel = finalBookOwner;
                        bookOwnerModel.setStatus("Swap");
                        updateBookOwner(bookOwnerModel, "NotAdvertised");
                    }else{
                        Toast.makeText(ViewOwnBookAct.this, "This book is already Not Advertised.", Toast.LENGTH_SHORT).show();
//                        BookOwnerModel bookOwnerModel = new BookOwnerModel();
//                        bookOwnerModel = swapDetail.getBookOwner();
//                        bookOwnerModel.setStatus("none");
//                        updateBookOwner(bookOwnerModel);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            BookOwnerModel bookOwnerModel = new BookOwnerModel();

            bookOwnerModel = (BookOwnerModel) getIntent().getExtras().getSerializable("notAdBook");

            mTitle.setText(bookOwnerModel.getBookObj().getBookTitle());

            mHeaderLPrice.setText("Original Price");
            mLPrice.setText(String.valueOf(bookOwnerModel.getBookObj().getBookOriginalPrice()));
//            mRPrice.setText(String.valueOf(swapDetail.getSwapPrice()/2));
            mRPrice.setVisibility(View.GONE);

            final User user = (User) SPUtility.getSPUtil(ViewOwnBookAct.this).getObject("USER_OBJECT", User.class);

            if(user.getUserId()==bookOwnerModel.getUserObj().getUserId()){
                buttonLinear.setVisibility(View.GONE);
            }

            String author = "";

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
            mAuthor.setText(author);
            Glide.with(this).load(bookOwnerModel.getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            mContent.setText(bookOwnerModel.getBookObj().getBookDescription());
            mCondition.setText(bookOwnerModel.getStatusDescription());


            mRentBtn.setText("Request to Swap");

            mViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewOwnBookAct.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userModelPass", swapDetail.getBookOwner().getUserObj());
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
        }
    }

    public void customDialog(final String status){
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
        if(adapter==null){
            Log.d("apater", "is null");
        }else{
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

    public void updateBookOwner(final BookOwnerModel bookOwnerModelToPost, final String fromWhere){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.UPDATE_BOOK_OWNER_1;

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookOwnerModelToPost);


        d("BookOwnerPostVolley", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("BookOwnerPutRes", response);

                if(bookOwnerModelToPost.getStatus().equals("Rent")){
                    if(fromWhere.equals("Swap")){
                        rentToPost.setBookOwner(bookOwnerModelToPost);
                        rentToPost.setCalculatedPrice(swapDetail.getSwapPrice());
                        rentToPost.setDaysForRent(5);
                    }else if(fromWhere.equals("NotAdvertised")){
                        rentToPost.setBookOwner(bookOwnerModelToPost);
                        rentToPost.setDaysForRent(5);
                        rentToPost.setCalculatedPrice(calculatePrice(bookOwnerModelToPost.getBookObj()));
                    }

                    addRentalDetail(rentToPost);
                }else if(bookOwnerModelToPost.getStatus().equals("Swap")){
                    if(fromWhere.equals("Rent")){
                        swapToPost.setBookOwner(bookOwnerModelToPost);
                        swapToPost.setSwapPrice(Float.parseFloat(String.valueOf(rentalDetailModel.getCalculatedPrice())));
                        swapToPost.setSwapDescription(rentalDetailModel.getBookOwner().getStatusDescription());
                    }else if(fromWhere.equals("NotAdvertised")){
                        swapToPost.setBookOwner(bookOwnerModelToPost);
                        swapToPost.setSwapPrice(Float.parseFloat(String.valueOf(calculatePrice(bookOwnerModel.getBookObj()))));
                        swapToPost.setSwapDescription(bookOwnerModel.getStatusDescription());
                    }
                    addSwapDetail(swapToPost);
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

    private void addSwapDetail(SwapDetail swapDetailModel) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_SWAP_DETAIL_1;


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

    public double calculatePrice(Book bookModel){
        double price = 0, depPrice;

        Date date = null;
        depPrice = bookModel.getBookOriginalPrice()/5;

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
        if(gap>=5){
            price = depPrice;
            Log.d("calculatedPric depPrice", String.valueOf(price));
        }else if(gap==0){
            price = bookModel.getBookOriginalPrice();
            Log.d("calculatedPric OrgPri", String.valueOf(price));
        }else{
            price = bookModel.getBookOriginalPrice() - (depPrice*gap);
            Log.d("calculatedPrice dep*gap", String.valueOf(price));
        }

        Log.d("calculatedPrice", String.valueOf(price));
        return price;
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
                            Toast.makeText(ViewOwnBookAct.this, "You disagreed to the terms and condition.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{

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
        String URL = Constants.GET_RATINGS+bookOwnerId;
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
        } else if (id == R.id.shelf) {
            Intent intent = new Intent(ViewOwnBookAct.this, MyShelf.class);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(ViewOwnBookAct.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            Intent intent = new Intent(ViewOwnBookAct.this, TransactionActivity.class);
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
    public void onDisplayBookReviewOnClick(Uri uri) {

    }

    @Override
    public void onSwapCommentOnClick(Uri uri) {

    }
}
