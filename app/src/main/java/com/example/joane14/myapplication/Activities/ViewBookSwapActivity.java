package com.example.joane14.myapplication.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Adapters.LandingPageAdapter;
import com.example.joane14.myapplication.Adapters.SwapCommentsAdapter;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.DisplaySwapComments;
import com.example.joane14.myapplication.Fragments.PreferencesFrag;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.joane14.myapplication.R.id.imageView;

public class ViewBookSwapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        DisplaySwapComments.OnSwapCommentInteractionListener{

    SwapDetail swapDetailObj;
    Bundle bundle;
    TextView mBookTitle, mAuthor, mPrice, mBookOwner;
    ImageView mBookImg, mBookOwnerImg;
    List<SwapComment> swapCommentList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book_swap);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarViewBookSwap);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book_swap);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ViewBookSwapActivity.this);


        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);


        View hView = navigationView.getHeaderView(0);
        TextView mName = (TextView) hView.findViewById(R.id.tvName);
        TextView mEmail = (TextView) hView.findViewById(R.id.tvEmail);
        ImageView profileImg = (ImageView) hView.findViewById(R.id.profPic);

        if(SPUtility.getSPUtil(this).contains("USER_OBJECT")){
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname()+" "+userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(ViewBookSwapActivity.this).load(String.format(Constants.IMAGE_URL, userModel.getImageFilename())).fit().into(profileImg);
        }


        mBookTitle = (TextView) findViewById(R.id.vbsBookTitle);
        mAuthor = (TextView) findViewById(R.id.vbsBookAuthor);
        mPrice = (TextView) findViewById(R.id.vbsBookPrice);

        mBookOwner = (TextView) findViewById(R.id.vbsBookOwner);

        mBookOwnerImg = (ImageView) findViewById(R.id.vbsBookOwnerIgm);
        mBookImg = (ImageView) findViewById(R.id.vbsBookImg);


        swapDetailObj = new SwapDetail();

        String author = "";
        if(getIntent().getExtras()!=null){
            bundle = getIntent().getExtras();
            swapDetailObj = (SwapDetail) bundle.getSerializable("ViewSwap" );

            if(swapDetailObj==null){
                Log.d("rentalDetail", "is empty");
            }else{
                Log.d("bundle", "is not empty");
                Log.d("SwapDetailBook", swapDetailObj.getSwapComments().get(0).getSwapComment());
                bundle.putSerializable("swapComment", swapDetailObj);
                fragmentManager = getSupportFragmentManager();
                DisplaySwapComments displaySwapComments = DisplaySwapComments.newInstance(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragement_display_comment, displaySwapComments, displaySwapComments.getTag());
                fragmentTransaction.commit();

                Log.d("RentalBookTitle", swapDetailObj.getBookOwner().getBookObj().getBookTitle());
                mBookTitle.setText(swapDetailObj.getBookOwner().getBookObj().getBookTitle());
                if(swapDetailObj.getBookOwner().getBookObj().getBookAuthor().get(0).getAuthorFName()==null){
                    if(swapDetailObj.getBookOwner().getBookObj().getBookAuthor().get(0).getAuthorLName()==null){
                        author="Unknown Author";
                    }

                    Log.d("SwapAdapter", swapDetailObj.toString());


                    Log.d("SwapComments", "inside");
                }else{
                    Log.d("RentalBookAuthor", String.valueOf(swapDetailObj.getBookOwner().getBookObj().getBookAuthor().get(0).getAuthorFName()));
                    author=swapDetailObj.getBookOwner().getBookObj().getBookAuthor().get(0).getAuthorFName();
                }
                Log.d("RentalAuthor", author);
                mAuthor.setText(author);
                mPrice.setText(String.valueOf(swapDetailObj.getSwapPrice()));
                Log.d("SwapDetail Id", String.valueOf(swapDetailObj.getSwapDetailId()));
                Glide.with(ViewBookSwapActivity.this).load(swapDetailObj.getBookOwner().getBookObj().getBookFilename()).fitCenter().into(mBookImg);
                Picasso.with(ViewBookSwapActivity.this).load(String.format(Constants.IMAGE_URL, swapDetailObj.getBookOwner().getUserObj().getImageFilename())).fit().into(mBookOwnerImg);
                mBookOwner.setText(swapDetailObj.getBookOwner().getUserObj().getUserFname()+" "+swapDetailObj.getBookOwner().getUserObj().getUserLname());
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
            Intent intent = new Intent(ViewBookSwapActivity.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(ViewBookSwapActivity.this, ProfileActivity.class);
//            User user = userModel;
//            Log.d("User Id", String.valueOf(userModel.getUserId()));
//            Log.d("User name", userModel.getUserFname()+" "+userModel.getUserLname());
            Bundle bundlePass = new Bundle();
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(ViewBookSwapActivity.this).getObject("USER_OBJECT", User.class);
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtra("user",bundlePass);
            startActivity(intent);
        } else if (id == R.id.shelf) {
            Intent intent = new Intent(ViewBookSwapActivity.this, MyShelf.class);
            startActivity(intent);
        } else if (id == R.id.history) {

        } else if (id == R.id.transaction) {
            Intent intent = new Intent(ViewBookSwapActivity.this, TransactionActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {
            Intent intent = new Intent(ViewBookSwapActivity.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(ViewBookSwapActivity.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ViewBookSwapActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book_swap);
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
    public void onSwapCommentOnClick(Uri uri) {

    }
}
