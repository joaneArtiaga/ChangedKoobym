package com.example.joane14.myapplication.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Fragments.CompletedHistory;
import com.example.joane14.myapplication.Fragments.CompletedOwnerHistory;
import com.example.joane14.myapplication.Fragments.CompletedRenterHistory;
import com.example.joane14.myapplication.Fragments.CompletedSwapHistory;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.MyRequestFrag;
import com.example.joane14.myapplication.Fragments.RejectedHistory;
import com.example.joane14.myapplication.Fragments.RejectedOwnerHistory;
import com.example.joane14.myapplication.Fragments.RejectedRenterHistory;
import com.example.joane14.myapplication.Fragments.RejectedSwapHistory;
import com.example.joane14.myapplication.Fragments.RentHistoryFrag;
import com.example.joane14.myapplication.Fragments.RequestReceivedFrag;
import com.example.joane14.myapplication.Fragments.SwapHistory;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CompletedHistory.OnCompletedHistoryInteractionListener,
        RejectedHistory.OnRejectedHistoryInteractionListener,
        CompletedRenterHistory.OnCompletedRenterHistoryInteractionListener,
        CompletedOwnerHistory.OnCompletedOwnerHistoryInteractionListener,
        RejectedOwnerHistory.OnRejectedOwnerHistoryInteractionListener,
        RejectedRenterHistory.OnRejectedRenterHistoryInteractionListener,
        RentHistoryFrag.OnRentHistoryInteractionListener,
        SwapHistory.OnSwapHistoryInteractionListener,
        CompletedSwapHistory.OnCompletedSwapHistoryInteractionListener,
        RejectedSwapHistory.OnRejectedSwapHistoryInteractionListener{

    User userModel;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userModel = new User();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_history);
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
        ImageView profileImg = (ImageView) hView.findViewById(R.id.profPic);

        if(SPUtility.getSPUtil(this).contains("USER_OBJECT")){
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname()+" "+userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(HistoryActivity.this).load(userModel.getImageFilename()).fit().into(profileImg);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        HistoryActivity.ViewPagerAdapter adapter = new HistoryActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RentHistoryFrag(), "Rent");
        adapter.addFragment(new SwapHistory(), "Swap");
        viewPager.setAdapter(adapter);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("onNavigationItem", "inside");

        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(HistoryActivity.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(HistoryActivity.this, ProfileActivity.class);
//            User user = userModel;
//            Log.d("User Id", String.valueOf(userModel.getUserId()));
//            Log.d("User name", userModel.getUserFname()+" "+userModel.getUserLname());
            Bundle bundlePass = new Bundle();
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtras(bundlePass);
            startActivity(intent);
        } else if (id == R.id.shelf) {
            Intent intent = new Intent(HistoryActivity.this, MyShelf.class);
            startActivity(intent);
        } else if (id == R.id.history) {

        } else if (id == R.id.transaction) {
            Intent intent = new Intent(HistoryActivity.this, BookActActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {
            Intent intent = new Intent(HistoryActivity.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(HistoryActivity.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_history);
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
    public void OnCompletedHistoryOnClick(Uri uri) {

    }

    @Override
    public void OnRejectedHistoryOnClick(Uri uri) {

    }

    @Override
    public void OnCompletedRenterHistoryOnClick(Uri uri) {

    }

    @Override
    public void OnCompletedOwnerHistory(Uri uri) {

    }

    @Override
    public void OnRejectedOwnerHistoryOnClick(Uri uri) {

    }

    @Override
    public void OnRejectedRenterHistoryOnClick(Uri uri) {

    }

    @Override
    public void OnCompletedSwapHistoryOnClick(Uri uri) {

    }

    @Override
    public void OnSwapHistoryOnClick(Uri uri) {

    }

    @Override
    public void OnRentHistoryOnClick(Uri uri) {

    }

    @Override
    public void OnRejectedSwapHistoryOnClick(Uri uri) {

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
