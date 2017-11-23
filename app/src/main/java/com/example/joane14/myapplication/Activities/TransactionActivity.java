package com.example.joane14.myapplication.Activities;

import android.content.Intent;
import android.net.Uri;
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
import com.example.joane14.myapplication.Fragments.BAbookRequest;
import com.example.joane14.myapplication.Fragments.BAmyBooks;
import com.example.joane14.myapplication.Fragments.CompleteFrag;
import com.example.joane14.myapplication.Fragments.CompleteSwapFrag;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.MyRequestFrag;
import com.example.joane14.myapplication.Fragments.RentTransaction;
import com.example.joane14.myapplication.Fragments.RequestReceivedFrag;
import com.example.joane14.myapplication.Fragments.SwapTransaction;
import com.example.joane14.myapplication.Fragments.ToApproveFrag;
import com.example.joane14.myapplication.Fragments.ToDeliverFrag;
import com.example.joane14.myapplication.Fragments.ToDeliverSwapFrag;
import com.example.joane14.myapplication.Fragments.ToReceiveFrag;
import com.example.joane14.myapplication.Fragments.ToReceiveSwapFrag;
import com.example.joane14.myapplication.Fragments.ToReturnFrag;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ToDeliverFrag.OnToDeliverInteractionListener,
        ToReturnFrag.OnToReturnInteractionListener,
        ToReceiveFrag.OnToReceiveInteractionListener,
        CompleteFrag.OnCompleteInteractionListener,
        ToReceiveSwapFrag.OnToSwapReceiveInteractionListener,
        RentTransaction.OnRentTransactionInteractionListener,
        SwapTransaction.OnSwapTransactionInteractionListener,
        ToDeliverSwapFrag.OnToDeliverSwapInteractionListener,
        CompleteSwapFrag.OnCompleteSwapInteractionListener,
        ToApproveFrag.OnToApproveInteractionListener,
        BAbookRequest.OnRequestInteractionListener,
        BAmyBooks.OnMyBookListener{

    ImageView profileImg;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("RequestActivity", "inside");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_transaction);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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
//            Glide.with(TransactionActivity.this).load(userModel.getImageFilename()).into(profileImg);
            Picasso.with(TransactionActivity.this).load(userModel.getImageFilename()).fit().into(profileImg);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        TransactionActivity.ViewPagerAdapter adapter = new TransactionActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BAmyBooks(), "My Books");
        adapter.addFragment(new BAbookRequest(), "Requests");
//        adapter.addFragment(new ToReturnFrag(), "To Return");
//        adapter.addFragment(new CompleteFrag(), "Complete");
        viewPager.setAdapter(adapter);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("onNavigationItem", "inside");

        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(TransactionActivity.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(TransactionActivity.this, ProfileActivity.class);
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
            Intent intent = new Intent(TransactionActivity.this, MyShelf.class);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(TransactionActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {

        } else if (id == R.id.request) {
            Intent intent = new Intent(TransactionActivity.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(TransactionActivity.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(TransactionActivity.this, MainActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_transaction);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SearchKeyword", query);
                Intent intent = new Intent(TransactionActivity.this, SearchResult.class);
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
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(TransactionActivity.this, NotificationAct.class);
                startActivity(intent);


                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onToDeliverOnClick(Uri uri) {

    }

    @Override
    public void onToReceiveOnClick(Uri uri) {

    }

    @Override
    public void onCompleteOnClick(Uri uri) {

    }

    @Override
    public void onToReturnOnClick(Uri uri) {

    }

    @Override
    public void onToSwapReceiveOnClick(Uri uri) {

    }

    @Override
    public void onRentTransactionOnClick(Uri uri) {

    }

    @Override
    public void onSwapTransasctionOnClick(Uri uri) {

    }

    @Override
    public void onCompleteSwapOnClick(Uri uri) {

    }

    @Override
    public void onToDeliverSwapOnClick(Uri uri) {

    }

    @Override
    public void ontToApproveOnClick(Uri uri) {

    }

    @Override
    public void onMyBookOnClick(Uri uri) {

    }

    @Override
    public void OnRequestOnClick(Uri uri) {

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
