package com.example.joane14.myapplication.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.Genre;
import com.example.joane14.myapplication.Fragments.ProfileFragment;
import com.example.joane14.myapplication.Fragments.ShowBooksFrag;
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ProfileFragment.OnFragmentInteractionListener{

    FloatingActionButton mBtnAdd;
    Bundle mBundle, fragmentBundle;
    User userObj;
    ImageView profileImg;
    String titleKeyword;
    private FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView1.getHeaderView(0);

        profileImg = (ImageView) hView.findViewById(R.id.profPic);

        Intent intent = getIntent();


            if(null!=intent.getBundleExtra("user")){
                Log.d("User Login", "inside");


                mBundle = intent.getBundleExtra("user");
                this.userObj = (User) mBundle.getSerializable("userModelPass");

                Log.d("User filename", userObj.getImageFilename());
                Log.d("User Id", String.valueOf(userObj.getUserId()));

//                Log.d("User Login", userObj.getUserFname());
//                mName.setText(userObj.getUserFname()+" "+ userObj.getUserLname());
//                mEmail.setText(userObj.getEmail());
                Picasso.with(ProfileActivity.this).load(String.format(Constants.IMAGE_URL, userObj.getImageFilename())).fit().into(profileImg);
                Log.d("Fragment", "inside");
                mBundle.putSerializable("userDetails", userObj);
                Log.d("userDetails", userObj.toString());

                fragmentManager = getSupportFragmentManager();
                ProfileFragment profileModel = new ProfileFragment();
                mBundle.putSerializable("userDetails", userObj);
                profileModel.setArguments(mBundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_books, profileModel, "profile fragment");
                fragmentTransaction.commit();
            }





    }

    public void searchISBN(String booktitle){

        String query = booktitle;
        try {
            query = URLEncoder.encode(booktitle, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = String.format(Constants.ISBN_SEARCH_URL, query);

        Log.d("BOOK URL", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ISBNresponse", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray items = obj.getJSONArray("data");
                    for(int init = 0; init< items.length(); init++){
                        JSONObject arrayObject = items.getJSONObject(init);
                        Log.d("ISBN",arrayObject.getString("isbn13"));

                        searchGoogleBook(arrayObject.getString("isbn13"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GOOGLE BOOK", error.toString());
            }
        });

        requestQueue.add(stringRequest);


    }


    public void searchGoogleBook(String booktitle) {
        String query = booktitle;
        try {
            query = URLEncoder.encode(booktitle, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = String.format(Constants.GOOGLE_API_SEARCH_URL_ISBN, query);

        Log.d("BOOK URL", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SEARCHGOOGLEBOOK RES", response);
                try {
                    JSONObject obj = new JSONObject(response);
                        Log.d("VolumeInfo",obj.getString("totalItems"));

                    if(obj.getString("totalItems")=="0"){
                        Log.d("totalItemsNumber", "is 0");
                    }else{
                        Log.d("totalItemsNumber", "is not 0");
                    }

                    Log.d("ImageGoogle", obj.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail"));

                    if(obj.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail")==null){
                        Log.d("ImageGoogle", "is empty");
                    }else{
                        Log.d("ImageGoogle", obj.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail"));
                    }


//                    fragmentManager = getSupportFragmentManager();
//                    ShowBooksFrag bookModel = new ShowBooksFrag();
//                    changeFragment(bookModel, response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GOOGLE BOOK", error.toString());
            }
        });

        requestQueue.add(stringRequest);

    }


    public void searchBook(String booktitle) {
        String query = booktitle;
        try {
            query = URLEncoder.encode(booktitle, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = String.format(Constants.GOOGLE_API_SEARCH_URL, query);

        Log.d("BOOK URL", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SEARCH BOOK RES", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray items = obj.getJSONArray("items");
                    for(int init = 0; init< items.length(); init++){
                        JSONObject arrayObject = items.getJSONObject(init);
                        Log.d("Title",arrayObject.getJSONObject("volumeInfo").getString("title"));
                        Log.d("VolumeInfo",arrayObject.getJSONObject("volumeInfo").toString());

                    }


                        fragmentManager = getSupportFragmentManager();
                        ShowBooksFrag bookModel = new ShowBooksFrag();
                        changeFragment(bookModel, response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GOOGLE BOOK", error.toString());
            }
        });

        requestQueue.add(stringRequest);

    }
    private void changeFragment(Fragment fragment, String result) {
        Log.d("Change Fragment", "inside");
        if(result.length()==0){
            Log.d("nullResponse", "is null");
        }else{
            Log.d("notNullResponse", "is not null");
        }
        fragmentBundle = new Bundle();
        fragmentBundle.putString("searchResult", result);
        fragmentBundle.putSerializable("userProfile", userObj);
        fragment.setArguments(fragmentBundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_books, fragment, fragment.getTag());
        fragmentTransaction.commit();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        boolean flag=true;

        if (id == R.id.home) {
            Intent intent = new Intent(ProfileActivity.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {

        } else if (id == R.id.rent) {

        } else if (id == R.id.myBook) {
            Intent intent = new Intent(ProfileActivity.this, MyShelf.class);
            startActivity(intent);
        } else if (id == R.id.history) {

        } else if (id == R.id.transaction) {

        } else if (id == R.id.request) {

        } else if (id == R.id.signOut) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.shelf) {
        }

        if(flag == false){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile);
            drawer.closeDrawer(GravityCompat.START);
        }else{
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile);
            drawer.closeDrawer(GravityCompat.START);
        }

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d("Inside","On back pressed");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
        }
    }

    @Override
    public void onAddBookSelected(String keyword) {
        searchBook(keyword);
        searchISBN(keyword);
    }
}
