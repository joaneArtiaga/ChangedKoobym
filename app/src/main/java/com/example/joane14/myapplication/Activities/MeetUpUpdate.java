package com.example.joane14.myapplication.Activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.MeetUp;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeetUpUpdate extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    List<LocationModel> locationModelList;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_up_update);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        user = new User();
        locationModelList = new ArrayList<LocationModel>();

        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        locationModelList = user.getLocationArray();



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Marker mapMarker;

        LatLng location = null;


        for(int i = 0; i<locationModelList.size(); i++){
            String lati, longi;
            Double latitude, longitude;

            lati = locationModelList.get(i).getLatitude();
            longi = locationModelList.get(i).getLongitude();
            latitude = Double.parseDouble(lati);
            longitude = Double.parseDouble(longi);
            Log.d("Latitude "+latitude.toString(), "Longitude "+longitude.toString());
            Log.d("LocationName "+locationModelList.get(i).getLocationName(), "ArrayPosition "+i);
            location = new LatLng(latitude,longitude);
            mapMarker = mMap.addMarker(new MarkerOptions().position(location).title(locationModelList.get(i).getLocationName()));

            mapMarker.showInfoWindow();

            mHashMap.put(mapMarker, i);

        }

    }
}
