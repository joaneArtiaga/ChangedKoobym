package com.example.joane14.myapplication.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class LandingPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String name, userId, email, gender;
    Bundle mBundle, mBundleLogin, bundlePass;
    User userModel;
    ImageView profileImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userModel = new User();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

            Log.d("User filename", userModel.getImageFilename());
            Log.d("User Id", String.valueOf(userModel.getUserId()));

            Log.d("User Login", userModel.getUserFname());
            mName.setText(userModel.getUserFname()+" "+ userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(LandingPage.this).load(String.format(Constants.IMAGE_URL, userModel.getImageFilename())).fit().into(profileImg);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            Log.d("User Id", String.valueOf(userModel.getUserId()));
            Log.d("User name", userModel.getUserFname()+" "+userModel.getUserLname());
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtra("user",bundlePass);
            startActivity(intent);
        } else if (id == R.id.rent) {

        } else if (id == R.id.myBook) {

        } else if (id == R.id.history) {

        } else if (id == R.id.transaction) {

        } else if (id == R.id.request) {

        } else if (id == R.id.signOut) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(LandingPage.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.shelf) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
