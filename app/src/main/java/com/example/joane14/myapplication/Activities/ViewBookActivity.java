package com.example.joane14.myapplication.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    ImageView imageView, mViewMeetUp;
    ImageButton mBtnRequest;


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


        View hView = navigationView.getHeaderView(0);
        TextView mName = (TextView) hView.findViewById(R.id.tvName);
        TextView mEmail = (TextView) hView.findViewById(R.id.tvEmail);
        ImageView profileImg = (ImageView) hView.findViewById(R.id.profPic);

        mViewMeetUp = (ImageView) findViewById(R.id.ivVbViewMap);

        mViewMeetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewBookActivity.this, ViewMeetUp.class);
                startActivity(intent);
            }
        });
        if(SPUtility.getSPUtil(this).contains("USER_OBJECT")){
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname()+" "+userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
        }



        imageView = (ImageView) findViewById(R.id.vbBookImg);
        mBookAuthor = (TextView) findViewById(R.id.vbBookAuthor);
        mBookDescription = (TextView) findViewById(R.id.vbBookDescription);

        mBtnRequest = (ImageButton) findViewById(R.id.vbBtnRequest);



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
        String author = "Author not found.";

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
                if(rentalDetail.getBookOwner().getBookObj().getBookAuthor().isEmpty()){
                        author="Unknown Author";
                }else{
                    Log.d("RentalBookAuthor", String.valueOf(rentalDetail.getBookOwner().getBookObj().getBookAuthor().get(0).getAuthorFName()));
                    author=rentalDetail.getBookOwner().getBookObj().getBookAuthor().get(0).getAuthorFName();
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
            intent.putExtra("user",bundlePass);
            startActivity(intent);
        } else if (id == R.id.shelf) {
            Intent intent = new Intent(ViewBookActivity.this, MyShelf.class);
            startActivity(intent);
        } else if (id == R.id.history) {

        } else if (id == R.id.transaction) {

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

}
