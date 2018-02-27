package com.example.joane14.myapplication.Utilities;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Model.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Joane14 on 27/02/2018.
 */

public class PlacesUtility {


    private static RequestQueue rq;

    public static RequestQueue getInstance(Activity activity) {
        if (rq == null) {
            rq = Volley.newRequestQueue(activity);
        }
        return rq;
    }


    public static void getPredictions(Activity activity, String key, LatLng latLng, Response.Listener<String> responseListener) {
        String apiQuery = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=%s&location=%f,%f&key=%s";
        key = key.replaceAll(" ", "%20");
        apiQuery = String.format(apiQuery, key, latLng.latitude, latLng.longitude, "AIzaSyAgopmfZJLKu9EpmyQoWE9sO_OwVzn7gHo");
        Log.d("query", apiQuery);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiQuery, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        PlacesUtility.getInstance(activity).add(stringRequest);
    }

    public static void getPlaceDetails(Activity activity, String id, Response.Listener<String> responseListener) {
        String apiQuery = "https://maps.googleapis.com/maps/api/place/details/json?placeid=%s&key=%s";

        apiQuery = String.format(apiQuery, id, "AIzaSyAgopmfZJLKu9EpmyQoWE9sO_OwVzn7gHo");
        Log.d("query", apiQuery);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiQuery, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        PlacesUtility.getInstance(activity).add(stringRequest);
    }


    public static List<Place> parsePredictionResult(String result) {
        List<Place> predictionString = new ArrayList<>();
        try {
            JSONObject jsonResult = new JSONObject(result);
            JSONArray jsonArrayRe = jsonResult.getJSONArray("predictions");
            JSONObject obj;
            for (int i = 0; i < jsonArrayRe.length(); i++) {
                obj = jsonArrayRe.getJSONObject(i);
                predictionString.add(new Place(obj.getString("description"), obj.getString("place_id")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return predictionString;
    }
}
