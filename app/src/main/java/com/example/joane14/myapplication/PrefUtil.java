package com.example.joane14.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Joane14 on 23/07/2017.
 */

public class PrefUtil {

    private Activity activity;

    public PrefUtil(){

    }

    public PrefUtil(Activity activity){
        this.activity = activity;
    }

    public void saveAccessToken(String token){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor= prefs.edit();
        editor.putString("fb_access_token", token);
        editor.apply();
    }

    public String getToken(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("fb_access_tokent", null);
    }

    public void clearToken(){
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public void saveFacebookUserInfo(String firstName, String lastName, String email, String gender, String profileUrl){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fb_first_name", firstName);
        editor.putString("fb_last_name", lastName);
        editor.putString("fb_email", email);
        editor.putString("fb_gender", gender);
        editor.putString("fb_profileURL", profileUrl);
        editor.apply();
        Log.d("Koobym", "First Name:"+firstName+"\nLast Name:"+lastName+"\nEmail:"+email+"\nGender:"+gender+"\nProfile Url:"+profileUrl);

    }


    public void getFacebookUser(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Log.d("Koobym", "Name:"+prefs.getString("fb_name", null)+"\nEmail:"+prefs.getString("fb_email", null));

    }
}
