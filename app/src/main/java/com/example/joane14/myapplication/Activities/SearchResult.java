package com.example.joane14.myapplication.Activities;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.joane14.myapplication.Fragments.SearchFragmentResult;
import com.example.joane14.myapplication.R;

public class SearchResult extends AppCompatActivity implements SearchFragmentResult.OnSearchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

    }

    @Override
    public void onSearchOnClick(Uri uri) {

    }
}
