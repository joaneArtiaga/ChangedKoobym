package com.example.joane14.myapplication.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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
import com.example.joane14.myapplication.Class.BadgeDrawable;
import com.example.joane14.myapplication.Fragments.AboutProfFrag;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.DisplayBookReview;
import com.example.joane14.myapplication.Fragments.DisplayMyBooks;
import com.example.joane14.myapplication.Fragments.DisplayUserReview;
import com.example.joane14.myapplication.Fragments.Genre;
import com.example.joane14.myapplication.Fragments.MapLandingPage;
import com.example.joane14.myapplication.Fragments.ProfileFrag;
import com.example.joane14.myapplication.Fragments.ProfileFragment;
import com.example.joane14.myapplication.Fragments.RentTransaction;
import com.example.joane14.myapplication.Fragments.ShowBooksFrag;
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.PlacesUtility;
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
        DisplayMyBooks.OnDisplayMyBooksInteractionListener {

    FloatingActionButton mBtnAdd;
    Book book;
    BookOwnerModel bookOwner;
    Bundle mBundle, fragmentBundle;
    User userObj;
    ImageView profileImg;
    String titleKeyword;
    private FragmentManager fragmentManager;
    LayerDrawable icon;
    int countBadge;


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


        book = new Book();
        bookOwner = new BookOwnerModel();
        mBundle = new Bundle();


        if (intent.getExtras().getSerializable("userModelPass") != null) {
            Log.d("User Login", "inside");


            this.userObj = (User) getIntent().getExtras().getSerializable("userModelPass");

            getNotificationCount();

            Log.d("User Login", userObj.getUserFname());
            mBundle.putSerializable("userDetails", userObj);

            mEmail.setText(userObj.getEmail());
            ImageView profileImg = (ImageView) findViewById(R.id.profIvProf);
            mName.setText(userObj.getUserFname() + " " + userObj.getUserLname());
            Picasso.with(getApplicationContext()).load(userObj.getImageFilename()).fit().into(profileImg);

            mBtnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                    startActivity(intent);
                }
            });

        }

        setupViewPager(viewPager);
        TabLayout tab = (TabLayout) findViewById(R.id.result_tabs);

        tab.setupWithViewPager(viewPager);
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

                final Dialog dialogCustom = new Dialog(ProfileActivity.this);
                dialogCustom.setContentView(R.layout.add_book_custom_dialog);

                final EditText mTitle = (EditText) dialogCustom.findViewById(R.id.etTitle);
                Button mBtnSearch = (Button) dialogCustom.findViewById(R.id.searchBtn);
                Button mBtnCancel = (Button) dialogCustom.findViewById(R.id.cancelBtn);

                mBtnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTitle.getText().length() == 0) {
                            mTitle.setError("Field must not be empty.");
                        } else {
                            searchBook(mTitle.getText().toString());
                        }
                    }
                });

                mBtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogCustom.dismiss();
                    }
                });

                dialogCustom.show();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
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

    public void searchISBNPrice(String booktitle) {

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
                    if (items.length() != 0) {
                        Log.d("PriceSize", "not Empty");
                    } else {
                        Log.d("PriceSize", "Empty");
                    }
                    for (int init = 0; init < items.length(); init++) {
                        JSONObject arrayObject = items.getJSONObject(init);
                        Log.d("PRICE", arrayObject.getString("price"));

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

    public void searchISBN(String booktitle) {

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
                    for (int init = 0; init < items.length(); init++) {
                        JSONObject arrayObject = items.getJSONObject(init);
                        Log.d("ISBN", arrayObject.getString("isbn13"));

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
                    Log.d("VolumeInfo", obj.getString("totalItems"));

                    if (obj.getString("totalItems") == "0") {
                        Log.d("totalItemsNumber", "is 0");
                    } else {
                        Log.d("totalItemsNumber", "is not 0");
                    }

                    Log.d("ImageGoogle", obj.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail"));

                    if (obj.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail") == null) {
                        Log.d("ImageGoogle", "is empty");
                    } else {
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


    public void searchBook(final String booktitle) {
        String query = booktitle;
        try {
            query = URLEncoder.encode(booktitle, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = String.format(Constants.GOOGLE_API_SEARCH_URL, query);

        String URL1 = URL + "&maxResults=40";
        Log.d("BOOKURL", URL1);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GOOGLEBOOK", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray items = obj.getJSONArray("items");
                    for (int init = 0; init < items.length(); init++) {
                        JSONObject arrayObject = items.getJSONObject(init);
                        Log.d("Title", arrayObject.getJSONObject("volumeInfo").getString("title"));
                        Log.d("VolumeInfo", arrayObject.getJSONObject("volumeInfo").toString());

                        if (arrayObject.getJSONObject("saleInfo").getString("saleability").equals("NOT_FOR_SALE")) {
                            Log.d("googleBookPrice", "not for sale");
                        } else if (arrayObject.getJSONObject("saleInfo").getString("saleability").equals("FOR_SALE")) {
                            String price = arrayObject.getJSONObject("saleInfo").getJSONObject("listPrice").getString("amount");
                            Log.d("priceGoogle", price);
                            Log.d("googleBookPrice", "not null");
                        }
                    }
                    Intent intent = new Intent(ProfileActivity.this, SearchResult.class);
                    intent.putExtra("bookResult", response);
                    intent.putExtra("bookTitle", booktitle);
                    startActivity(intent);
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
        if (result.length() == 0) {
            Log.d("nullResponse", "is null");
        } else {
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
        boolean flag = true;

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

        if (flag == false) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_main, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        MenuItem item = menu.findItem(R.id.action_notifications);

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
                Intent intent = new Intent(ProfileActivity.this, SearchResult.class);
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
                Intent intent = new Intent(ProfileActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(ProfileActivity.this, NotificationAct.class);
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
                setBadgeCount(ProfileActivity.this, count);

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
