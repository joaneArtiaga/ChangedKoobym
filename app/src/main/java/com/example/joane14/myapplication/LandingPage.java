package com.example.joane14.myapplication;

import android.content.Intent;
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

import com.facebook.login.LoginManager;

public class LandingPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String name, userId, email, gender, pictureFile;
    Bundle mBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        mBundle = intent.getExtras();
        if(null!=intent){
            Log.d("Inside", "null!=intent");
//            name = mBundle.getString("name");
//            email = mBundle.getString("email");
//            gender = mBundle.getString("gender");
//            userId = mBundle.getString("userId");
//            Log.d("Name", name);
//            Log.d("Email", email);
//            Log.d("Gender", gender);
//            Log.d("UserId", userId);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.profile) {

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
