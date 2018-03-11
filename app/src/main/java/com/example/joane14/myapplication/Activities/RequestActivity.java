package com.example.joane14.myapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Adapters.RequestRentAdapter;
import com.example.joane14.myapplication.Class.BadgeDrawable;
import com.example.joane14.myapplication.Fragments.ApprovedSwapFrag;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.MapLandingPage;
import com.example.joane14.myapplication.Fragments.MyRequestFrag;
import com.example.joane14.myapplication.Fragments.RequestFrag;
import com.example.joane14.myapplication.Fragments.RequestReceivedFrag;
import com.example.joane14.myapplication.Fragments.RequestRentFrag;
import com.example.joane14.myapplication.Fragments.RequestSwapFrag;
import com.example.joane14.myapplication.Fragments.SwapRequestFrag;
import com.example.joane14.myapplication.Fragments.ToApproveFrag;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.PlacesUtility;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MyRequestFrag.OnMyRequestInteractionListener,
        RequestReceivedFrag.OnRequestReceivedInteractionListener,
        SwapRequestFrag.OnSwapRequestInteractionListener,
        RequestFrag.OnRequestFragInteractionListener,
        ApprovedSwapFrag.OnApprovedSwapInteractionListener,
        ToApproveFrag.OnToApproveInteractionListener,
        RequestRentFrag.OnRequestRentInteractionListener,
        RequestSwapFrag.OnSwapRequestInteractionListener{


    ImageView profileImg;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    LayerDrawable icon;
    int countBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRequest);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request");


        Log.d("RequestActivity", "inside");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_request);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView mName = (TextView) hView.findViewById(R.id.tvName);
        TextView mEmail = (TextView) hView.findViewById(R.id.tvEmail);


        Log.d("Inside", "landing page");

        profileImg = (ImageView) hView.findViewById(R.id.profPic);

        if(SPUtility.getSPUtil(this).contains("USER_OBJECT")){
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname()+" "+userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(RequestActivity.this).load(userModel.getImageFilename()).fit().into(profileImg);
        }

        getNotificationCount();

        ImageButton mRent = (ImageButton) findViewById(R.id.rentRequest);
        ImageButton mSwap = (ImageButton) findViewById(R.id.swapRequest);
        final TextView tvRent = (TextView) findViewById(R.id.tvRentRequest);
        final TextView tvSwap = (TextView) findViewById(R.id.tvSwapRequest);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_request, RequestRentFrag.newInstance());
        ft.commit();


        tvRent.setTextColor(ContextCompat.getColor(RequestActivity.this, R.color.colorLightOrange));

        mRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvRent.setTextColor(ContextCompat.getColor(RequestActivity.this, R.color.colorLightOrange));
                tvSwap.setTextColor(ContextCompat.getColor(RequestActivity.this, R.color.colorDark));
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_request, RequestRentFrag.newInstance());
                fragmentTransaction.commit();
            }
        });

        mSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvRent.setTextColor(ContextCompat.getColor(RequestActivity.this, R.color.colorDark));
                tvSwap.setTextColor(ContextCompat.getColor(RequestActivity.this, R.color.colorLightOrange));
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_request, RequestSwapFrag.newInstance());
                fragmentTransaction.commit();
            }
        });
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("onNavigationItem", "inside");

        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(RequestActivity.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(RequestActivity.this, ProfileActivity.class);
//            User user = userModel;
//            Log.d("User Id", String.valueOf(userModel.getUserId()));
//            Log.d("User name", userModel.getUserFname()+" "+userModel.getUserLname());
            Bundle bundlePass = new Bundle();
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtras(bundlePass);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(RequestActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            Intent intent = new Intent(RequestActivity.this, BookActActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {

        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(RequestActivity.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(RequestActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_request);
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
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(RequestActivity.this, NotificationAct.class);
                startActivity(intent);


                return false;
            }
        });

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
                Intent intent = new Intent(RequestActivity.this, SearchResult.class);
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
                Intent intent = new Intent(RequestActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(RequestActivity.this, NotificationAct.class);
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
                setBadgeCount(RequestActivity.this, count);

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
    public void onMyRequestOnClick(Uri uri) {

    }

    @Override
    public void onRequestReceivedOnClick(Uri uri) {

    }

    @Override
    public void OnSwapRequestOnClick(Uri uri) {

    }

    @Override
    public void OnRequestFragOnClick(Uri uri) {

    }

    @Override
    public void OnApprovedSwapOnCllick(Uri uri) {

    }

    @Override
    public void ontToApproveOnClick(Uri uri) {

    }

    @Override
    public void onSwapRequestInteraction(Uri uri) {

    }

    @Override
    public void onRequestRentInteraction(Uri uri) {

    }
}
