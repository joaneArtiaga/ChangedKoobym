package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.joane14.myapplication.Adapters.CategoryAdapter;
import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class SearcgByCategoryFrag extends Fragment {

    private OnFragmentInteractionListener mListener;

    static final String[] category = new String[]{"Book Title", "Book Author", "Book Genre", "Book Owner"};

    List<String> categoryArray;

    public SearcgByCategoryFrag() {
    }


    public static SearcgByCategoryFrag newInstance() {
        SearcgByCategoryFrag fragment = new SearcgByCategoryFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searcg_by_category, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.search_category);

        categoryArray = new ArrayList<String>();

        categoryArray.add("Book Title");
        categoryArray.add("Book Owner");
        categoryArray.add("Book Author");
        categoryArray.add("Book Genre");

        CategoryAdapter adapte = new CategoryAdapter(getContext(), categoryArray);
        gridView.setAdapter(adapte);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("CategorySelected", category[position]);
            }
        });



        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
