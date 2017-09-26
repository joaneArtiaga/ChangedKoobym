package com.example.joane14.myapplication.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.MostRentedBookFrag;
import com.example.joane14.myapplication.Fragments.PreferencesFrag;
import com.example.joane14.myapplication.Fragments.VolleyUtil;
import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LandingPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MostRentedBookFrag.OnFragmentInteractionListener,
        PreferencesFrag.OnFragmentInteractionListener{
    private String name, userId, email, gender;
    Bundle mBundle, mBundleLogin, bundlePass;
    User userModel;
    ImageView profileImg;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userModel = new User();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_landingPage);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView mName = (TextView) hView.findViewById(R.id.tvName);
        TextView mEmail = (TextView) hView.findViewById(R.id.tvEmail);

        Log.d("Inside", "landing page");

        profileImg = (ImageView) hView.findViewById(R.id.profPic);

        bundlePass = new Bundle();

        Intent intent = getIntent();

        if(null!=intent.getBundleExtra("ProfileBundle")){
            mBundle = intent.getBundleExtra("ProfileBundle");

            Log.d("Inside", mBundle.toString());
            name = mBundle.getString("name").toString();
            email = mBundle.getString("email").toString();
            gender = mBundle.getString("gender").toString();
            userId = mBundle.getString("userId").toString();


            Log.d("LandingName", name);
            Log.d("LandingEmail", email);
            Log.d("LandingGender", gender);
            Log.d("LandingUserId", userId);

            mName.setText(name);
            mEmail.setText(email);

//            ProfilePictureView mProfPic = (ProfilePictureView) hView.findViewById(R.id.profPic);
//            mProfPic.setProfileId(userId);
        }
        else if(null!=intent.getBundleExtra("user")){
            Log.d("User Login", "inside");


            mBundleLogin = intent.getBundleExtra("user");
            this.userModel = (User) mBundleLogin.getSerializable("userModel");

            Log.d("userModel", userModel.toString());


            Log.d("User filename", userModel.getImageFilename());
            Log.d("User Id", String.valueOf(userModel.getUserId()));

            Log.d("User Login", userModel.getUserFname());
            mName.setText(userModel.getUserFname()+" "+ userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(LandingPage.this).load(String.format(Constants.IMAGE_URL, userModel.getImageFilename())).fit().into(profileImg);


            if(mBundleLogin.getBoolean("fromRegister")==true){
                Log.d("inside", "TRUEfromRegister");
                fragmentManager = getSupportFragmentManager();
                MostRentedBookFrag mrbf = MostRentedBookFrag.newInstance();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing_container, MostRentedBookFrag.newInstance(), mrbf.getTag());
                fragmentTransaction.commit();
            }else{
                Log.d("PrefFrag","else inside");
                bundlePass.putSerializable("userModelPass", userModel);
                Log.d("userModelPass1st", userModel.toString());
                fragmentManager = getSupportFragmentManager();
                PreferencesFrag prefFrag = PreferencesFrag.newInstance(bundlePass);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing_container, prefFrag, prefFrag.getTag());
                fragmentTransaction.commit();

            }
        }
        else if(null!=intent.getBundleExtra("SPBundle")){
            mBundleLogin = intent.getBundleExtra("SPBundle");
            this.userModel = (User) mBundleLogin.getSerializable("userModel");

            Log.d("User filename", userModel.getImageFilename());
            Log.d("User Id", String.valueOf(userModel.getUserId()));

            Log.d("User Login", userModel.getUserFname());
            mName.setText(userModel.getUserFname()+" "+ userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(LandingPage.this).load(String.format(Constants.IMAGE_URL, userModel.getImageFilename())).fit().into(profileImg);


            if(mBundleLogin.getBoolean("fromRegister")==true){
                Log.d("inside", "TRUEfromRegister");
                fragmentManager = getSupportFragmentManager();
                MostRentedBookFrag mrbf = MostRentedBookFrag.newInstance();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing_container, MostRentedBookFrag.newInstance(), mrbf.getTag());
                fragmentTransaction.commit();
            }else{
                bundlePass.putSerializable("userModelPass", userModel);
                Log.d("userModelPass1st", userModel.toString());
                fragmentManager = getSupportFragmentManager();
                PreferencesFrag prefFrag = PreferencesFrag.newInstance(bundlePass);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing_container, prefFrag, prefFrag.getTag());
                fragmentTransaction.commit();

            }
        }
//
//        if(null!=intent.getBundleExtra("fromRegister")){
//            Log.d("User from Register","inside");
//
//            mBundle = intent.getBundleExtra("fromRegister");
//            if(mBundle.getBoolean("fromRegister")==true){
//                Log.d("inside", "TRUEfromRegister");
//                fragmentManager = getSupportFragmentManager();
//                MostRentedBookFrag mrbf = MostRentedBookFrag.newInstance();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_landing_container, MostRentedBookFrag.newInstance(), mrbf.getTag());
//                fragmentTransaction.commit();
//            }else{
//                bundlePass.putSerializable("userModelPass", userModel);
//                fragmentManager = getSupportFragmentManager();
//                PreferencesFrag pref = PreferencesFrag.newInstance();
//                pref.setArguments(bundlePass);
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_landing_container, PreferencesFrag.newInstance(), pref.getTag());
//                fragmentTransaction.commit();
//
//            }
//
//        }

    }







    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_landingPage);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d("Inside","On back pressed");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("onNavigationItem", "inside");

        int id = item.getItemId();

        if (id == R.id.home) {
        } else if (id == R.id.profile) {
            Intent intent = new Intent(LandingPage.this, ProfileActivity.class);
//            User user = userModel;
//            Log.d("User Id", String.valueOf(userModel.getUserId()));
//            Log.d("User name", userModel.getUserFname()+" "+userModel.getUserLname());
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtra("user",bundlePass);
            startActivity(intent);
        } else if (id == R.id.rent) {

        } else if (id == R.id.myBook) {

        } else if (id == R.id.history) {

        } else if (id == R.id.transaction) {

        } else if (id == R.id.request) {

        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(LandingPage.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(LandingPage.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.shelf) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_landingPage);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMostRentedListener(Uri uri) {

    }

}
