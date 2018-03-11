package com.example.joane14.myapplication.Activities;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.joane14.myapplication.Fragments.SearchFragmentResult;
import com.example.joane14.myapplication.Fragments.ShowBooksFrag;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;

public class SearchResult extends AppCompatActivity implements SearchFragmentResult.OnSearchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        TextView mTitle = (TextView) findViewById(R.id.searchTitle);
        if(getIntent().getStringExtra("bookResult")!=null){
            if(getIntent().getStringExtra("bookTitle")!=null){
                mTitle.setText("'"+getIntent().getStringExtra("bookTitle")+"'");
            }

            User userObj = new User();
            userObj = (User) SPUtility.getSPUtil(getApplicationContext()).getObject("USER_OBJECT", User.class);
            Fragment frag = new ShowBooksFrag();
            Bundle bundle = new Bundle();
            bundle.putString("searchResult", getIntent().getStringExtra("bookResult"));
            bundle.putSerializable("userProfile", userObj);
            frag.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container_search, frag);
            ft.commit();
        }
    }

    @Override
    public void onSearchOnClick(Uri uri) {

    }
}
