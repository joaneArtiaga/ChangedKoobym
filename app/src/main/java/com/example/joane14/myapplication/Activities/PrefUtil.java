package com.example.joane14.myapplication.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.joane14.myapplication.Model.FbUSer;

/**
 * Created by Joane14 on 23/07/2017.
 */

public class PrefUtil {

    private Activity activity;
    private static PrefUtil instance;

    private PrefUtil(){

    }

    public static PrefUtil getPrefUtilInstance(Activity activity){
        if(instance == null){
            instance = new PrefUtil(activity);
        }
        return instance;
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

    public void saveFacebookUserInfo(String name, String email, String gender, String userId){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fb_name", name);
        editor.putString("fb_email", email);
        editor.putString("fb_gender", gender);
        editor.putString("fb_userId", userId);
        editor.apply();
    }


    public FbUSer getLoggedInFacebookUserDetails(){
        FbUSer flag = new FbUSer();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        flag.setName(prefs.getString("fb_name",null));
        flag.setEmail(prefs.getString("fb_email",null));
        flag.setGender(prefs.getString("fb_gender", null));
        flag.setUserId(prefs.getString("fb_userId", null));
        return flag;
    }


    public void getFacebookUser(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Log.d("Koobym", "Name:"+prefs.getString("fb_name", null)+"\nEmail:"+prefs.getString("fb_email", null));

    }
}
