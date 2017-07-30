package com.example.joane14.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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


        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<GenreModel> selectedGenres = new ArrayList<GenreModel>();
                for(GenreModel mod : genreArray){
                    if(mod.isSelected()){
                        selectedGenres.add(mod);
                    }
                }
                mListener.onGenreSelected(selectedGenres);
            }
        });

        mGridViewGenres = (GridView) view.findViewById(R.id.gridView_genres);
        genreAdapter = new GenreAdapter(getContext(), genreArray);
        mGridViewGenres.setAdapter(genreAdapter);
        mGridViewGenres.setOnItemClickListener(this);
        getGenres();

/*

        mScifi = (TextView) view.findViewById(R.id.genre_sciFi);
        mAction = (TextView) view.findViewById(R.id.genre_action);
        mAdventure= (TextView) view.findViewById(R.id.genre_adventure);
        mRomace= (TextView) view.findViewById(R.id.genre_romance);
        mHorror = (TextView) view.findViewById(R.id.genre_horror);
        mHealth= (TextView) view.findViewById(R.id.genre_health);
        mTravel = (TextView) view.findViewById(R.id.genre_travel);
        mReligion = (TextView) view.findViewById(R.id.genre_religion);
        mChildren= (TextView) view.findViewById(R.id.genre_children);
        mComics= (TextView) view.findViewById(R.id.genre_comics);
        mCook= (TextView) view.findViewById(R.id.genre_cook);
        mDrama = (TextView) view.findViewById(R.id.genre_drama);
        mMystery = (TextView) view.findViewById(R.id.genre_mystery);
        mSelfHelp = (TextView) view.findViewById(R.id.genre_self_help);
        mGuide = (TextView) view.findViewById(R.id.genre_guide);
        mBiography = (TextView) view.findViewById(R.id.genre_biography);



        mScifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Sci-Fi");
                genreArray.add(modelGenre);
                Log.d("Genre", "Sci-Fi");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Action");
                genreArray.add(modelGenre);
                Log.d("Genre", "Action");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Adventure");
                genreArray.add(modelGenre);
                Log.d("Genre", "Adventure");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mRomace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Romance");
                genreArray.add(modelGenre);
                Log.d("Genre", "Romance");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mHorror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Horror");
                genreArray.add(modelGenre);
                Log.d("Genre", "Horror");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Health");
                genreArray.add(modelGenre);
                Log.d("Genre", "Health");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Travel");
                genreArray.add(modelGenre);
                Log.d("Genre", "Travel");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mReligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Religion");
                genreArray.add(modelGenre);
                Log.d("Genre", "Religion");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });
        mChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Children");
                genreArray.add(modelGenre);
                Log.d("Genre", "Children");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mComics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Comics");
                genreArray.add(modelGenre);
                Log.d("Genre", "Comics");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Cooks");
                genreArray.add(modelGenre);
                Log.d("Genre", "Cooks");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mDrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Drama");
                genreArray.add(modelGenre);
                Log.d("Genre", "Drama");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mMystery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Drama");
                genreArray.add(modelGenre);
                Log.d("Genre", "Mystery");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mSelfHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Self-Help");
                genreArray.add(modelGenre);
                Log.d("Genre", "Self-Help");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mBiography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Biography");
                genreArray.add(modelGenre);
                Log.d("Genre", "Biography");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });
        mGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelGenre.setGenreName("Guide");
                genreArray.add(modelGenre);
                Log.d("Genre", "Guide");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });
*/
        Log.d("Oncreate", "inside");
        return view;
    }

    public void getGenres() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String URL = "http://192.168.1.4:8080/Mexaco/genre/all";
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
        }else{
            model.setSelected(true);
        }
        genreAdapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        void onGenreSelected(List<GenreModel> genres);
    }
}
