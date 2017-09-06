package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.LocationChooser;
import com.example.joane14.myapplication.Adapters.GenreAdapter;
import com.example.joane14.myapplication.Model.GenreModel;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Genre extends Fragment implements AdapterView.OnItemClickListener{

    GenreModel modelGenre;
    ArrayList<GenreModel> genreArray;
    ImageView mBtnNext;
    TextView mScifi, mAction, mAdventure, mRomace, mHorror, mHealth, mTravel, mReligion, mCook, mDrama, mMystery, mSelfHelp, mGuide, mChildren, mComics, mBiography;
    private GenreAdapter genreAdapter;
    private GridView mGridViewGenres;
    private int genreCnt;
    private OnFragmentInteractionListener mListener;

    public Genre() {
    }

    public static Genre newInstance() {
        Genre fragment = new Genre();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre, container, false);

        modelGenre = new GenreModel();

        genreArray = new ArrayList<GenreModel>();

        mBtnNext = (ImageView) view.findViewById(R.id.btnNext);

        genreCnt=0;

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(genreCnt==0){
                    Log.d("genreArray empty", Integer.toString(genreCnt));
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("You must select one or more genre to be able to go the next stage.");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    Log.d("genreArray not empty", Integer.toString(genreCnt));
                    List<GenreModel> selectedGenres = new ArrayList<GenreModel>();
                    for(GenreModel mod : genreArray){
                        if(mod.isSelected()){
                            selectedGenres.add(mod);
                        }
                    }
                    mListener.onGenreSelected(selectedGenres);
                }
            }
        });

        mGridViewGenres = (GridView) view.findViewById(R.id.gridView_genres);
        genreAdapter = new GenreAdapter(getContext(), genreArray);
        mGridViewGenres.setAdapter(genreAdapter);
        mGridViewGenres.setOnItemClickListener(this);
        getGenres();

        Log.d("Oncreate", "inside");
        return view;
    }

    public void getGenres() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        String URL = "http://192.168.42.39:8080/Koobym/genre/all";
        String URL = Constants.WEB_SERVICE_URL+"genre/all";
        final Gson gson = new Gson();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOG_VOLLEY", response);
                GenreModel[] mcArray = gson.fromJson(response, GenreModel[].class);
                genreArray.addAll(Arrays.asList(mcArray));
                genreAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        });

        requestQueue.add(stringRequest);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        GenreModel model = genreArray.get(i);
        if(model.isSelected()){
            model.setSelected(false);
            this.genreCnt--;
        }else{
            model.setSelected(true);
            this.genreCnt++;
        }
        genreAdapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        void onGenreSelected(List<GenreModel> genres);
    }
}
