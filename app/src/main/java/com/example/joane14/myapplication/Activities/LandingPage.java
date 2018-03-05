package com.example.joane14.myapplication.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Class.BadgeDrawable;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.MapLandingPage;
import com.example.joane14.myapplication.Fragments.MostRentedBookFrag;
import com.example.joane14.myapplication.Fragments.PreferencesFrag;
import com.example.joane14.myapplication.Fragments.SearcgByCategoryFrag;
import com.example.joane14.myapplication.Fragments.SwapLandingPageFrag;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.PlacesUtility;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.squareup.picasso.Picasso;

public class LandingPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MostRentedBookFrag.OnFragmentInteractionListener,
        PreferencesFrag.OnFragmentInteractionListener,
        SwapLandingPageFrag.OnSwapLPInteractionListener,
        MapLandingPage.OnMapInteractionListener,
        SearcgByCategoryFrag.OnFragmentInteractionListener {
    private String name, userId, email, gender;
    Bundle mBundle, mBundleLogin, bundlePass;
    User userModel;
    ImageView profileImg;
    FragmentManager fragmentManager;
    int countBadge;
    LayerDrawable icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Home");


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
//        View hView = navigationView1.findViewById(R.id.navHeader)
        TextView mName = (TextView) hView.findViewById(R.id.tvName);
        TextView mEmail = (TextView) hView.findViewById(R.id.tvEmail);
        profileImg = (ImageView) hView.findViewById(R.id.profPic);

        final TextView mTitle = (TextView) findViewById(R.id.lpTitle);

        Log.d("Inside", "landing page");


        bundlePass = new Bundle();

        Intent intent = getIntent();
        User userMod = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        Log.d("toInitializePusher", "");
        initializePusherNotification(Integer.toString(userMod.getUserId()));

        if (null != intent.getBundleExtra("ProfileBundle")) {
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
        } else if (null != intent.getBundleExtra("user")) {
            Log.d("User Login", "inside");


            mBundleLogin = intent.getBundleExtra("user");
            this.userModel = (User) mBundleLogin.getSerializable("userModel");

            this.userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            if (userModel == null) {
                Log.d("UserModel", "is null");
            } else {
                Log.d("UserModel", "is not null");
            }

            Log.d("userModel", userModel.toString());


            Log.d("User filename", userModel.getImageFilename());
            Log.d("User Id", String.valueOf(userModel.getUserId()));

            Log.d("User Login", userModel.getUserFname());
            mName.setText(userModel.getUserFname() + " " + userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Log.d("moaatay", userModel.getImageFilename());
            Glide.with(LandingPage.this).load(userModel.getImageFilename()).into(profileImg);
            Picasso.with(LandingPage.this).load(userModel.getImageFilename()).fit().into(profileImg);


            if (mBundleLogin.getBoolean("fromRegister") == true) {
                Log.d("inside", "TRUEfromRegister");
                mTitle.setText("Books you might like");
                fragmentManager = getSupportFragmentManager();
                MostRentedBookFrag mrbf = MostRentedBookFrag.newInstance();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing_container, MostRentedBookFrag.newInstance(), mrbf.getTag());
                fragmentTransaction.commit();
            } else {
                Log.d("PrefFrag", "else inside");
                mTitle.setText("Books you might like");
                bundlePass.putSerializable("userModelPass", userModel);
                Log.d("userModelPass1st", userModel.toString());
                fragmentManager = getSupportFragmentManager();
                TextView title = (TextView) findViewById(R.id.lpTitle);
                title.setVisibility(View.GONE);
//                PreferencesFrag prefFrag = PreferencesFrag.newInstance(bundlePass);
                MapLandingPage prefFrag = MapLandingPage.newInstance();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing_container, prefFrag, prefFrag.getTag());
                fragmentTransaction.commit();

            }
        } else if (null != intent.getBundleExtra("SPBundle")) {
            mBundleLogin = intent.getBundleExtra("SPBundle");
            this.userModel = (User) mBundleLogin.getSerializable("userModel");

            Log.d("User filename", userModel.getImageFilename());
            Log.d("User Id", String.valueOf(userModel.getUserId()));

            Log.d("User Login", userModel.getUserFname());
            mName.setText(userModel.getUserFname() + " " + userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(LandingPage.this).load(String.format(Constants.IMAGE_URL, userModel.getImageFilename())).fit().into(profileImg);


            if (mBundleLogin.getBoolean("fromRegister") == true) {
                Log.d("inside", "TRUEfromRegister");
                fragmentManager = getSupportFragmentManager();
                MostRentedBookFrag mrbf = MostRentedBookFrag.newInstance();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing_container, MostRentedBookFrag.newInstance(), mrbf.getTag());
                fragmentTransaction.commit();
            } else {
                bundlePass.putSerializable("userModelPass", userModel);
                Log.d("userModelPass1st", userModel.toString());
                fragmentManager = getSupportFragmentManager();
                PreferencesFrag prefFrag = PreferencesFrag.newInstance(bundlePass);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing_container, prefFrag, prefFrag.getTag());
                fragmentTransaction.commit();

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(notificationReceivedReceiver, new IntentFilter("NOTIFICATION_RECEIVED"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationReceivedReceiver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_landingPage);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d("Inside", "On back pressed");
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
            intent.putExtras(bundlePass);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(LandingPage.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            Intent intent = new Intent(LandingPage.this, BookActActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {
            Intent intent = new Intent(LandingPage.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(LandingPage.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(LandingPage.this, MainActivity.class);
            startActivity(intent);
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
        MenuItem item = menu.findItem(R.id.action_notifications);
        MenuItem mapView = menu.findItem(R.id.action_map_view);
        MenuItem gridView = menu.findItem(R.id.action_grid_view);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
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
                Intent intent = new Intent(LandingPage.this, SearchResult.class);
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
                Intent intent = new Intent(LandingPage.this, SearchActivity.class);
                startActivity(intent);
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_landing_container, SearcgByCategoryFrag.newInstance());
//                ft.commit();
            }
        });


        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(LandingPage.this, NotificationAct.class);
                startActivity(intent);


                return false;
            }
        });

        MenuItem itemCart = menu.findItem(R.id.action_notifications);
        icon = (LayerDrawable) itemCart.getIcon();
        getNotificationCount();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("overflow", "itemSelectInside");

        int id = item.getItemId();
        if (id == R.id.action_map) {
            Log.d("overflow", "mapView");
            fragmentManager = getSupportFragmentManager();
            TextView mtitle = (TextView) findViewById(R.id.lpTitle);
            mtitle.setVisibility(View.GONE);
            MapLandingPage fragMap = MapLandingPage.newInstance();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_landing_container, fragMap, fragMap.getTag());
            ft.commit();
        } else if (id == R.id.action_grid) {
            Log.d("overflow", "gridview");
            fragmentManager = getSupportFragmentManager();
            TextView title = (TextView) findViewById(R.id.lpTitle);
            title.setVisibility(View.VISIBLE);
            PreferencesFrag prefFrag = PreferencesFrag.newInstance(bundlePass);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_landing_container, prefFrag, prefFrag.getTag());
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
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
                setBadgeCount(LandingPage.this, count);

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
            // Reuse drawable if possible
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMostRentedListener(Uri uri) {

    }

    @Override
    public void onSwapLPInteraction(Uri uri) {

    }


    private void initializePusherNotification(String userId) {
        Log.d("USER ID BAI", userId);
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");
        Pusher pusher = new Pusher("0aa2ef5ad16d9caba80a", options);

        Channel channel = pusher.subscribe(userId);
        Log.d("USER ID BAI", userId);

        channel.bind("notification-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                Log.d("RECEIVED SOMETHING", data);
                Intent i = new Intent();
                i.putExtra("data", data);
                i.setAction("NOTIFICATION_RECEIVED");
                getApplicationContext().sendBroadcast(i);
            }
        });

        pusher.connect();
    }


    BroadcastReceiver notificationReceivedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(getApplicationContext(), "push notification", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onMapInteraction(Uri uri) {

    }
}