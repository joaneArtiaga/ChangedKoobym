package com.example.joane14.myapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
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
import com.example.joane14.myapplication.Class.BadgeDrawable;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.MapLandingPage;
import com.example.joane14.myapplication.Fragments.ToDeliver;
import com.example.joane14.myapplication.Fragments.ToDeliverAuctionFragment;
import com.example.joane14.myapplication.Fragments.ToDeliverRentFragment;
import com.example.joane14.myapplication.Fragments.ToDeliverSwapFrag;
import com.example.joane14.myapplication.Fragments.ToReceive;
import com.example.joane14.myapplication.Fragments.ToReceiveAuctionFragment;
import com.example.joane14.myapplication.Fragments.ToReceiveRentFragment;
import com.example.joane14.myapplication.Fragments.ToReceiveSwapFrag;
import com.example.joane14.myapplication.Fragments.ToReturnFrag;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.PlacesUtility;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

public class BookActActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ToDeliverSwapFrag.OnToDeliverSwapInteractionListener,
        ToDeliverRentFragment.OnToDeliverRentInteractionListener,
        ToDeliverAuctionFragment.OnToDeliverAuctionInteractionListener,
        ToReceiveSwapFrag.OnToSwapReceiveInteractionListener,
        ToReceiveAuctionFragment.OnToReceiveAuctionInteractionListener,
        ToReceiveRentFragment.OnToRecevieFragmentInteractionListener,
        ToReturnFrag.OnToReturnInteractionListener,
        ToReceive.OnToReceiveInteractionListener,
        ToDeliver.OnToDeliverInteractionListener {



    LayerDrawable icon;
    int countBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book Activity");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorDark)));



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_book_activity);
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
        ImageView profileImg = (ImageView) hView.findViewById(R.id.profPic);

        if(SPUtility.getSPUtil(this).contains("USER_OBJECT")){
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname()+" "+userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(BookActActivity.this).load(userModel.getImageFilename()).fit().into(profileImg);
        }
        getNotificationCount();

        final ImageButton mDeliver = (ImageButton) findViewById(R.id.toDeliverBA);
        final ImageButton mReceive = (ImageButton) findViewById(R.id.toReceiveBA);
        final ImageButton mReturnn = (ImageButton) findViewById(R.id.toReturnBA);
        final TextView tvDeliver = (TextView) findViewById(R.id.tvBAdeliver);
        final TextView tvReceive = (TextView) findViewById(R.id.tvBAreceive);
        final TextView tvReturn = (TextView) findViewById(R.id.tvBAreturn);

        FragmentTransaction fragT = getSupportFragmentManager().beginTransaction();
        tvDeliver.setTextColor(ContextCompat.getColor(BookActActivity.this, R.color.colorOrange));
        tvReceive.setTextColor(ContextCompat.getColor(BookActActivity.this, R.color.colorLightOrange));
        tvReturn.setTextColor(ContextCompat.getColor(BookActActivity.this, R.color.colorLightOrange));
        mReceive.setImageResource(R.drawable.lighttoreceive);
        mDeliver.setImageResource(R.drawable.newtodeliver);
        mReturnn.setImageResource(R.drawable.lighttoreceive);
        fragT.replace(R.id.fragment_container_book_activity, ToDeliver.newInstance());
        fragT.commit();

        mDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReceive.setImageResource(R.drawable.lighttoreceive);
                mDeliver.setImageResource(R.drawable.newtodeliver);
                mReturnn.setImageResource(R.drawable.lighttoreturn);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                tvDeliver.setTextColor(ContextCompat.getColor(BookActActivity.this, R.color.colorOrange));
                tvReceive.setTextColor(ContextCompat.getColor(BookActActivity.this, R.color.colorLightOrange));
                tvReturn.setTextColor(ContextCompat.getColor(BookActActivity.this, R.color.colorLightOrange));
                ft.replace(R.id.fragment_container_book_activity, ToDeliver.newInstance());
                ft.commit();
            }
        });

        mReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReceive.setImageResource(R.drawable.newtoreceive);
                mDeliver.setImageResource(R.drawable.lighttodeliver);
                mReturnn.setImageResource(R.drawable.lighttoreturn);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                tvDeliver.setTextColor(ContextCompat.getColor(BookActActivity.this, R.color.colorLightOrange));
                tvReceive.setTextColor(ContextCompat.getColor(BookActActivity.this, R.color.colorOrange));
                tvReturn.setTextColor(ContextCompat.getColor(BookActActivity.this, R.color.colorLightOrange));
                ft.replace(R.id.fragment_container_book_activity, ToReceive.newInstance());
                ft.commit();
            }
        });

        mReturnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReceive.setImageResource(R.drawable.lighttoreceive);
                mDeliver.setImageResource(R.drawable.lighttodeliver);
                mReturnn.setImageResource(R.drawable.newtoreturn);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                tvDeliver.setTextColor(ContextCompat.getColor(BookActActivity.this, R.color.colorLightOrange));
                tvReceive.setTextColor(ContextCompat.getColor(BookActActivity.this, R.color.colorLightOrange));
                tvReturn.setTextColor(ContextCompat.getColor(BookActActivity.this, R.color.colorOrange));
                ft.replace(R.id.fragment_container_book_activity, ToReturnFrag.newInstance());
                ft.commit();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("onNavigationItem", "inside");

        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(BookActActivity.this , LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(BookActActivity.this, ProfileActivity.class);
            Bundle bundlePass = new Bundle();
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtras(bundlePass);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(BookActActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            Intent intent = new Intent(BookActActivity.this, BookActActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {
            Intent intent = new Intent(BookActActivity.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(BookActActivity.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(BookActActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_book_activity);
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
                Intent intent = new Intent(BookActActivity.this, SearchResult.class);
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
                Intent intent = new Intent(BookActActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(BookActActivity.this, NotificationAct.class);
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
                setBadgeCount(BookActActivity.this, count);

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
    public void onToDeliverAuctionOnClick(Uri uri) {

    }

    @Override
    public void onToDeliverSwapOnClick(Uri uri) {

    }

    @Override
    public void onToDeliverRentOnClick(Uri uri) {

    }

    @Override
    public void onToReceiveAuctionOnClick(Uri uri) {

    }

    @Override
    public void onToReturnOnClick(Uri uri) {

    }

    @Override
    public void onToRecevieFragmentOnClick(Uri uri) {

    }

    @Override
    public void onToSwapReceiveOnClick(Uri uri) {

    }

    @Override
    public void oToDeliverOnClick(Uri uri) {

    }

    @Override
    public void onToReceiveOnClick(Uri uri) {

    }
}
