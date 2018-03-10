package com.example.joane14.myapplication.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Fragments.AboutProfFrag;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.DisplayBookReview;
import com.example.joane14.myapplication.Fragments.DisplayMyBooks;
import com.example.joane14.myapplication.Fragments.DisplayUserReview;
import com.example.joane14.myapplication.Fragments.Genre;
import com.example.joane14.myapplication.Fragments.ProfileFrag;
import com.example.joane14.myapplication.Fragments.ProfileFragment;
import com.example.joane14.myapplication.Fragments.RentTransaction;
import com.example.joane14.myapplication.Fragments.ShowBooksFrag;
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ProfileFragment.OnFragmentInteractionListener, ProfileFrag.OnProfileFragInteractionListener,
        AboutProfFrag.OnAboutProfInteractionListener,
        DisplayUserReview.OnUserReviewInteractionListener,
        DisplayMyBooks.OnDisplayMyBooksInteractionListener{

    FloatingActionButton mBtnAdd;
    Book book;
    BookOwnerModel bookOwner;
    Bundle mBundle, fragmentBundle;
    User userObj;
    ImageView profileImg;
    String titleKeyword;
    private FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        TextView mName = (TextView) findViewById(R.id.tvName);
        TextView mBtnEdit = (TextView) findViewById(R.id.tvEditProfile);
        TextView mEmail = (TextView) findViewById(R.id.tvEmailProfile);
        ImageView mBackBtn = (ImageView) findViewById(R.id.backBtn);
        mBtnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        TabLayout tab = (TabLayout) findViewById(R.id.result_tabs);

        tab.setupWithViewPager(viewPager);

        book = new Book();
        bookOwner = new BookOwnerModel();
        mBundle = new Bundle();


            if(intent.getExtras().getSerializable("userModelPass")!=null){
                Log.d("User Login", "inside");


                this.userObj = (User) getIntent().getExtras().getSerializable("userModelPass");
                User userMod = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);


                Log.d("User filename", userObj.getImageFilename());
                Log.d("User Id", String.valueOf(userObj.getUserId()));

                Log.d("User Login", userObj.getUserFname());
                mBundle.putSerializable("userDetails", userObj);

                mEmail.setText(userObj.getEmail());
                ImageView profileImg = (ImageView) findViewById(R.id.profIvProf);
                mName.setText(userObj.getUserFname()+" "+ userObj.getUserLname());
                Picasso.with(getApplicationContext()).load(userObj.getImageFilename()).fit().into(profileImg);

                mBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                        startActivity(intent);
                    }
                });

            }else{
                Log.d("null oi", "mao white screen");
            }

            mBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        onBackPressed();
                    }
                }
            });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Inside", "Floating Action listener");

                final AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());

                final EditText edittext = new EditText(getApplicationContext());
                edittext.setHint("Book Title");
                alert.setTitle("Enter Your Title of the Book");

                alert.setView(edittext);

                alert.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d("Inside", "onClickPositiveButton");
                        titleKeyword = edittext.getText().toString();
                        Log.d("Title Keyword", titleKeyword);

                        searchBook(titleKeyword);
                    }
                });

                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d("Inside", "onClickNegativeButton");
                        dialog.dismiss();
                    }
                });

                alert.show();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        userObj = (User) SPUtility.getSPUtil(getApplicationContext()).getObject("USER_OBJECT", User.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", userObj);
        RentTransaction.Adapter adapter = new RentTransaction.Adapter(getSupportFragmentManager());
        adapter.addFragment(AboutProfFrag.newInstance(bundle), "User Profile");
        adapter.addFragment(DisplayMyBooks.newInstance(bundle), "Shelf");
        viewPager.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    public void searchISBNPrice(String booktitle){

        String query = booktitle;
        try {
            query = URLEncoder.encode(booktitle, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = String.format(Constants.ISBN_SEARCH_PRICES, query);

        Log.d("BOOK URL", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ISBNPriceresponse", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray items = obj.getJSONArray("data");
                    if(items.length()!=0){
                        Log.d("PriceSize", "not Empty");
                    }else{
                        Log.d("PriceSize", "Empty");
                    }
                    for(int init = 0; init< items.length(); init++){
                        JSONObject arrayObject = items.getJSONObject(init);
                        Log.d("PRICE",arrayObject.getString("price"));

                        book.setBookOriginalPrice(Float.parseFloat(String.valueOf(arrayObject.get("price"))));
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


    public void searchGoogleBook(final String booktitle) {
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

        String URL1 = URL+"&maxResults=40";
        Log.d("BOOKURL", URL1);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GOOGLEBOOK", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray items = obj.getJSONArray("items");
                    for(int init = 0; init< items.length(); init++){
                        JSONObject arrayObject = items.getJSONObject(init);
                        Log.d("Title",arrayObject.getJSONObject("volumeInfo").getString("title"));
                        Log.d("VolumeInfo",arrayObject.getJSONObject("volumeInfo").toString());

                        if(arrayObject.getJSONObject("saleInfo").getString("saleability").equals("NOT_FOR_SALE")){
                            Log.d("googleBookPrice", "not for sale");
                        }else if(arrayObject.getJSONObject("saleInfo").getString("saleability").equals("FOR_SALE")){
                                String price = arrayObject.getJSONObject("saleInfo").getJSONObject("listPrice").getString("amount");
                                Log.d("priceGoogle", price);
                                Log.d("googleBookPrice", "not null");
                        }
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
            Log.d("HomeFrom", "Profile");
            Intent intent = new Intent(ProfileActivity.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {

        } else if (id == R.id.history) {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            Intent intent = new Intent(ProfileActivity.this, BookActActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {
            Intent intent = new Intent(ProfileActivity.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(ProfileActivity.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

                finish();
    }

    @Override
    public void onAddBookSelected(String keyword) {
        searchBook(keyword);
    }

    @Override
    public void onAboutProfOnClick(Uri uri) {

    }

    @Override
    public void onDisplayMyBooksOnClick(Uri uri) {

    }

    @Override
    public void onProfileFragOnClick(Uri uri) {

    }

    @Override
    public void onUserReviewOnClick(Uri uri) {

    }
}
