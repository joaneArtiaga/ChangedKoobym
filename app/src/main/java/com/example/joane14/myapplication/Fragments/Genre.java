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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Genre extends Fragment{

    ArrayList<String> genreArray;
    ImageView mBtnNext;
    TextView mScifi, mAction, mAdventure, mRomace, mHorror, mHealth, mTravel, mReligion, mCook, mDrama, mMystery, mSelfHelp, mGuide, mChildren, mComics, mBiography;

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

        genreArray = new ArrayList<String>();

        mBtnNext = (ImageView) view.findViewById(R.id.btnNext);

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

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onGenreSelected(genreArray);
            }
        });

        mScifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Sci-Fi");
                Log.d("Genre", "Sci-Fi");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Action");
                Log.d("Genre", "Action");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Adventure");
                Log.d("Genre", "Adventure");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mRomace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Romance");
                Log.d("Genre", "Romance");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mHorror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Horror");
                Log.d("Genre", "Horror");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Health");
                Log.d("Genre", "Health");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Travel");
                Log.d("Genre", "Travel");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mReligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Religion");
                Log.d("Genre", "Religion");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });
        mChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Children");
                Log.d("Genre", "Children");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mComics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Comics");
                Log.d("Genre", "Comics");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Cooks");
                Log.d("Genre", "Cooks");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mDrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Drama");
                Log.d("Genre", "Drama");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mMystery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Mystery");
                Log.d("Genre", "Mystery");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mSelfHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Self-Help");
                Log.d("Genre", "Self-Help");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });

        mBiography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Biography");
                Log.d("Genre", "Biography");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });
        mGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genreArray.add("Guide");
                Log.d("Genre", "Guide");

                if(genreArray!=null){
                    Log.d("Genre Array", genreArray.toString());
                }
            }
        });




        Log.d("Oncreate", "inside");




        return view;
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

    public interface OnFragmentInteractionListener {
        void onGenreSelected(List<String> genres);
    }
}
