package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.MeetUp;
import com.example.joane14.myapplication.Model.MeetUpModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SwapMeetUpChooser extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    List<LocationModel> locationModelList;
    SwapHeader swapHeader;
    SwapDetail swapDetail;
    MeetUp meetUpModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_meet_up_chooser);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        swapHeader = new SwapHeader();
        meetUpModel = new MeetUp();

        if(getIntent().getExtras().getSerializable("swapHeader")!=null){
            swapHeader = (SwapHeader) getIntent().getExtras().getSerializable("swapHeader");
            Log.d("MeetUpChooser", String.valueOf(swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getUserId()));
            locationModelList = swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getLocationArray();
            for(int init = 0; init<locationModelList.size(); init++){
                Log.d("MeetUpChooser Location", locationModelList.get(init).getLocationName());
            }
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Marker mapMarker;


        LatLng location = null;

        for (int init=0; init<locationModelList.size(); init++){

            String lati, longi;
            Double latitude, longitude;

            lati = locationModelList.get(init).getLatitude();
            longi = locationModelList.get(init).getLongitude();
            latitude = Double.parseDouble(lati);
            longitude = Double.parseDouble(longi);
            Log.d("Latitude "+latitude.toString(), "Longitude "+longitude.toString());
            Log.d("LocationName "+locationModelList.get(init).getLocationName(), "ArrayPosition "+init);
            location = new LatLng(latitude,longitude);
            mapMarker = mMap.addMarker(new MarkerOptions().position(location).title(locationModelList.get(init).getLocationName()));

            mapMarker.showInfoWindow();

            mHashMap.put(mapMarker, init);
            Log.d("MarkerPosition", String.valueOf(mapMarker.getPosition()));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SwapMeetUpChooser.this);
                alertDialogBuilder.setTitle("Selected Location");
                alertDialogBuilder.setMessage(marker.getTitle());
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                int position = mHashMap.get(marker);
                                Log.d("MarkerPosition", String.valueOf(position));
                                Intent intent = new Intent(SwapMeetUpChooser.this,TimeDateChooser.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("swapHeader", swapHeader);
                                mBundle.putBoolean("fromSwap", true);
                                mBundle.putSerializable("locationChose", locationModelList.get(position));
                                Log.d("LocationChose", locationModelList.get(position).getLocationName());
                                meetUpModel.setLocation(locationModelList.get(position));
                                mBundle.putSerializable("meetUp", meetUpModel);
                                intent.putExtra("confirm", mBundle);
                                intent.putExtras(mBundle);
                                startActivity(intent);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return false;
            }
        });
    }

}
