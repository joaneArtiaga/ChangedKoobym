package com.example.joane14.myapplication.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.joane14.myapplication.Fragments.SearchByAuthor;
import com.example.joane14.myapplication.Fragments.SearchByGenre;
import com.example.joane14.myapplication.Fragments.SearchByOwner;
import com.example.joane14.myapplication.Fragments.SearchFragmentResult;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchByOwner.OnFragmentInteractionListener,
        SearchFragmentResult.OnSearchListener,
        SearchByAuthor.OnFragmentInteractionListener,
        SearchByGenre.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_search);
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
            Picasso.with(SearchActivity.this).load(userModel.getImageFilename()).fit().into(profileImg);
        }

        final EditText mSearch = (EditText) findViewById(R.id.searchET);
        final Spinner spinCat = (Spinner) findViewById(R.id.catSpin);
        Button mSearchBtn = (Button) findViewById(R.id.btnSearch);
        final TextView mResult = (TextView) findViewById(R.id.resultTv);
        String selectedCat = "";


        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSearch.getText().length()!=0&&spinCat.getSelectedItem()!=null){
                    mResult.setText("'"+mSearch.getText().toString()+"'");

                    if(spinCat.getSelectedItem().equals("Book Title")){
                        Log.d("searchCat", spinCat.getSelectedItem()+"");
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.result_search_container, SearchFragmentResult.newInstance(mSearch.getText().toString()));
                        ft.commit();
                    }else if(spinCat.getSelectedItem().equals("Book Genre")){
                        Log.d("searchCat", spinCat.getSelectedItem()+"");
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.result_search_container, SearchByGenre.newInstance(mSearch.getText().toString()));
                        ft.commit();
                    }else if(spinCat.getSelectedItem().equals("Book Author")){
                        Log.d("searchCat", spinCat.getSelectedItem()+"");
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.result_search_container, SearchByAuthor.newInstance(mSearch.getText().toString()));
                        ft.commit();
                    }else if(spinCat.getSelectedItem().equals("Book Owner")){
                        Log.d("searchCat", spinCat.getSelectedItem()+"");
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.result_search_container, SearchByOwner.newInstance(mSearch.getText().toString()));
                        ft.commit();
                    }

                }else if(mSearch.getText().length()==0||spinCat.getSelectedItem()==null){
                    if(mSearch.getText().length()==0){
                        mSearch.setError("Fill this field");
                    }
                    if(spinCat.getSelectedItem()==null){
                        TextView errorText = (TextView) spinCat.getSelectedView();
                        errorText.setError("Select category");
                    }

                }
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(SearchActivity.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
            Bundle bundlePass = new Bundle();
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtras(bundlePass);
            startActivity(intent);
        } else if (id == R.id.history) {

        } else if (id == R.id.transaction) {
            Intent intent = new Intent(SearchActivity.this, BookActActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {
            Intent intent = new Intent(SearchActivity.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(SearchActivity.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_search);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        MenuItem item = menu.findItem(R.id.action_notifications);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSearchOnClick(Uri uri) {

    }
}
