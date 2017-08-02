package com.example.joane14.myapplication.Fragments;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Joane14 on 01/08/2017.
 */

public class VolleyUtil {

    private static RequestQueue rq;

    public static RequestQueue volleyRQInstance(Context context){
        if(rq == null){
            rq = Volley.newRequestQueue(context);
        }
        return rq;
    }
}
