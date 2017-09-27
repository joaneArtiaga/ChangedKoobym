package com.example.joane14.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;

public class ViewBookActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Bundle bundle;
    RentalDetail rentalDetail;
    TextView mBookTitle;
    TextView mBookAuthor;
    TextView mBookDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_profile);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ViewBookActivity.this);


        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView1.getHeaderView(0);


        mBookAuthor = (TextView) hView.findViewById(R.id.vbBookAuthor);
        mBookDescription = (TextView) hView.findViewById(R.id.vbBookDescription);

        rentalDetail = new RentalDetail();
        String author = "";

        mBookTitle = (TextView) findViewById(R.id.vbBookTitle);
        if(getIntent().getExtras()!=null){
            bundle = getIntent().getExtras();
            rentalDetail = (RentalDetail) bundle.getSerializable("View" );

            if(rentalDetail==null){
                Log.d("rentalDetail", "is empty");

            }else{
                Log.d("bundle", "is not empty");
                Log.d("RentalBookTitle", rentalDetail.getBookOwner().getBookObj().getBookTitle());
                mBookTitle.setText(rentalDetail.getBookOwner().getBookObj().getBookTitle());
                Log.d("RentalBookAuthor", String.valueOf(rentalDetail.getBookOwner().getBookObj().getBookAuthor().get(0)));
                author+=rentalDetail.getBookOwner().getBookObj().getBookAuthor().get(0).getAuthorFName();
                mBookAuthor.setText(author);
                Log.d("RentalBookDescription", rentalDetail.getBookOwner().getBookObj().getBookDescription().toString());
                mBookDescription.setText((rentalDetail.getBookOwner().getBookObj().getBookDescription().toString()));
            }
            Log.d("bundle", "is not empty");
        }else{
            Log.d("bundle", "is empty");
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
            Intent intent = new Intent(ViewBookActivity.this, ProfileActivity.class);
//            User user = userModel;
//            Log.d("User Id", String.valueOf(userModel.getUserId()));
//            Log.d("User name", userModel.getUserFname()+" "+userModel.getUserLname());
            Bundle bundlePass = new Bundle();
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(ViewBookActivity.this).getObject("USER_OBJECT", User.class);
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtra("user",bundlePass);
            startActivity(intent);
        } else if (id == R.id.rent) {

        } else if (id == R.id.myBook) {

        } else if (id == R.id.history) {

        } else if (id == R.id.transaction) {

        } else if (id == R.id.request) {

        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(ViewBookActivity.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ViewBookActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.shelf) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_landingPage);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
