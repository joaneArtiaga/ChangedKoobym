package com.example.joane14.myapplication.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.joane14.myapplication.Adapters.LandingPageAdapter;
import com.example.joane14.myapplication.Adapters.RentersAdapter;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.DisplayBookReview;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ViewBookActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        DisplayBookReview.OnDisplayBookReviewInteractionListener {

    Bundle bundle;
    RentalDetail rentalDetail;
    List<RentalHeader> rentalHeaderList;
    TextView mBookTitle, mOwnerName;
    TextView mBookAuthor;
    TextView mBookDescription;
    ImageView imageView, mViewMeetUp, mRatings, mGenres, mRenters, mOwnerImg;
    ImageButton mBtnRequest;
    Dialog listDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarViewBook);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ViewBookActivity.this);


        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);

        rentalHeaderList = new ArrayList<RentalHeader>();

        View hView = navigationView.getHeaderView(0);
        TextView mName = (TextView) hView.findViewById(R.id.tvName);
        TextView mEmail = (TextView) hView.findViewById(R.id.tvEmail);
        ImageView profileImg = (ImageView) hView.findViewById(R.id.profPic);

        mViewMeetUp = (ImageView) findViewById(R.id.ivVbViewMap);

        if(SPUtility.getSPUtil(this).contains("USER_OBJECT")){
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname()+" "+userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(ViewBookActivity.this).load( userModel.getImageFilename()).fit().into(profileImg);
        }


        mOwnerImg = (ImageView) findViewById(R.id.ownerImg);
        mOwnerName = (TextView) findViewById(R.id.ownerName);
        imageView = (ImageView) findViewById(R.id.vbBookImg);
        mRatings = (ImageView) findViewById(R.id.ivVbViewRating);
        mGenres = (ImageView) findViewById(R.id.ivVbViewGenre);
        mRenters = (ImageView) findViewById(R.id.ivVbViewRenters);

        mBookAuthor = (TextView) findViewById(R.id.vbBookAuthor);
        mBookDescription = (TextView) findViewById(R.id.vbBookDescription);

        mBtnRequest = (ImageButton) findViewById(R.id.vbBtnRequest);


        mRenters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        mRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBookRating();
            }
        });



        if (mBookAuthor==null){
            Log.d("mBookAuthor", "is null");
        }else{
            Log.d("mBookAuthor", "is not null");
        }

        if (mBookDescription==null){
            Log.d("mBookDescription", "is null");
        }else{
            Log.d("mBookDescription", "is not null");
        }
        rentalDetail = new RentalDetail();
        String author = "";

        mBookTitle = (TextView) findViewById(R.id.vbBookTitle);
        if(getIntent().getExtras().getSerializable("View")!=null){
            bundle = getIntent().getExtras();
            rentalDetail = (RentalDetail) getIntent().getExtras().getSerializable("View" );

            if(rentalDetail==null){
                Log.d("rentalDetail", "is empty");

            }else{

                Bundle bundle = new Bundle();
                bundle.putSerializable("rentalDetail", rentalDetail);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container_reviews, DisplayBookReview.newInstance(bundle));
                ft.commit();
                Log.d("bundle", "is not empty");
                Log.d("RentalBookTitle", rentalDetail.getBookOwner().getBookObj().getBookTitle());
                mBookTitle.setText(rentalDetail.getBookOwner().getBookObj().getBookTitle());
                mOwnerName.setText(rentalDetail.getBookOwner().getUserObj().getUserFname()+" "+rentalDetail.getBookOwner().getUserObj().getUserLname());
                Glide.with(ViewBookActivity.this).load(rentalDetail.getBookOwner().getUserObj().getImageFilename()).fitCenter().into(mOwnerImg);
                if(rentalDetail.getBookOwner().getBookObj().getBookAuthor().size()!=0){
                        for(int init=0; init<rentalDetail.getBookOwner().getBookObj().getBookAuthor().size(); init++){
                            if(!(rentalDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                                author+=rentalDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                                if(!(rentalDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                                    author+=rentalDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                                    if(init+1<rentalDetail.getBookOwner().getBookObj().getBookAuthor().size()){
                                        author+=", ";
                                    }
                                }
                            }
                        }
                }else{
                    author="Unknown Author";
                }
                Log.d("RentalAuthor", author);
                mBookAuthor.setText(author);
                Log.d("RentalBookDescription", rentalDetail.getBookOwner().getBookObj().getBookDescription().toString());
                mBookDescription.setText((rentalDetail.getBookOwner().getBookObj().getBookDescription().toString()));
                Glide.with(ViewBookActivity.this).load(rentalDetail.getBookOwner().getBookObj().getBookFilename()).fitCenter().into(imageView);
            }
            Log.d("bundle", "is not empty");
        }else{
            Log.d("bundle", "is empty");
        }

        mBtnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user = (User) SPUtility.getSPUtil(ViewBookActivity.this).getObject("USER_OBJECT", User.class);

                if(user.getUserId()==rentalDetail.getBookOwner().getUserObj().getUserId()){
                    showWarning();
                }else{
                    checkIfExist(user.getUserId(), rentalDetail.getRental_detailId());
                }
            }
        });

        mGenres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";

                if(rentalDetail.getBookOwner().getBookObj().getBookGenre().size()>1){
                    for(int init=0; init<rentalDetail.getBookOwner().getBookObj().getBookGenre().size()-1; init++){
                        message+=rentalDetail.getBookOwner().getBookObj().getBookGenre().get(init).getGenreName() + " ";
                    }
                }else{
                    message = rentalDetail.getBookOwner().getBookObj().getBookGenre().get(0).getGenreName();
                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewBookActivity.this);
                alertDialogBuilder.setTitle("Genre");
                alertDialogBuilder.setMessage("This book "+rentalDetail.getBookOwner().getBookObj().getBookTitle()
                        +" is a "+message+" book.");
                alertDialogBuilder.setPositiveButton("Okay",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        mViewMeetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookActivity.this, ViewMeetUp.class);
                Bundle mbundle = new Bundle();
                mbundle.putSerializable("rentalDetail", rentalDetail);
                intent.putExtras(mbundle);
                startActivity(intent);
            }
        });
    }

    private void showDialog(){
        listDialog = new Dialog(this);
        listDialog.setTitle("Renters:");
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.dialog_layout_renters, null, false);
        listDialog.setContentView(view);
        listDialog.setCancelable(true);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_renters);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RentersAdapter(rentalHeaderList);
        mRecyclerView.setAdapter(mAdapter);

        getRenters(rentalDetail.getRental_detailId());

        listDialog.show();
    }

    private void getRenters(int rentalDetailId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.CHECK_EXIST+"/"+rentalDetailId;


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalDetail);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                rentalHeaderList.clear();
                rentalHeaderList.addAll(Arrays.asList(gson.fromJson(response, RentalHeader[].class)));
                mAdapter.notifyDataSetChanged();
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

    private void checkIfExist(int userId, int rentalDetailId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.CHECK_EXIST+"/"+userId+"/"+rentalDetailId;


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalDetail);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                if(response==null){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewBookActivity.this);
                    alertDialogBuilder.setTitle("Terms and Condition");
                    alertDialogBuilder.setMessage("\n\n1.\tThis book must be returned on time.\n" +
                            "2.\tThis book should be returned in the same condition it was provided.\n" +
                            "3.\tThe renter will compensate for the damages that the book may incur during the duration of his/her usage.\n" +
                            "4.\tA fee of 50 pesos per day will be incurred to the renter if the book is not returned on or before the due date.");
                    alertDialogBuilder.setPositiveButton("Agree",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Toast.makeText(ViewBookActivity.this, "You agreed to the terms and condition.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ViewBookActivity.this, MeetUpChooser.class);
                                    Bundle mbundle = new Bundle();
                                    mbundle.putSerializable("rentalDetail", rentalDetail);
                                    intent.putExtras(mbundle);
                                    startActivity(intent);
                                }
                            });

                    alertDialogBuilder.setNegativeButton("Disagree",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ViewBookActivity.this, "You disagreed to the terms and condition.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ViewBookActivity.this);
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

    private void getBookRating() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.GET_BOOK_RATING+"/"+rentalDetail.getBookOwner().getBookOwnerId();


//        user.setDayTimeModel();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalDetail);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewBookActivity.this);
                alertDialogBuilder.setTitle("Rating");
                alertDialogBuilder.setMessage("This book "+rentalDetail.getBookOwner().getBookObj().getBookTitle()
                        +" has an average rating of "+response);
                alertDialogBuilder.setPositiveButton("Okay",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

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

    public void showWarning(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ViewBookActivity.this);
        alertDialogBuilder.setTitle("!!!");
        alertDialogBuilder.setMessage("You can't rent your own book.");
        alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("onNavigationItem", "inside");

        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(ViewBookActivity.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(ViewBookActivity.this, ProfileActivity.class);
//            User user = userModel;
//            Log.d("User Id", String.valueOf(userModel.getUserId()));
//            Log.d("User name", userModel.getUserFname()+" "+userModel.getUserLname());
            Bundle bundlePass = new Bundle();
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(ViewBookActivity.this).getObject("USER_OBJECT", User.class);
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtras(bundlePass);
            startActivity(intent);
        } else if (id == R.id.shelf) {
            Intent intent = new Intent(ViewBookActivity.this, MyShelf.class);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(ViewBookActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            Intent intent = new Intent(ViewBookActivity.this, TransactionActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {
            Intent intent = new Intent(ViewBookActivity.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(ViewBookActivity.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ViewBookActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        MenuItem item = menu.findItem(R.id.action_notifications);
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDisplayBookReviewOnClick(Uri uri) {

    }
}
