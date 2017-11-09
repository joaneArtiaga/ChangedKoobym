package com.example.joane14.myapplication.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.MeetUp;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

public class MeetUpChooser extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();
    List<LocationModel> locationModelList;
    RentalHeader rentalHeader;
    MeetUp meetUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_up_chooser);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rentalHeader = new RentalHeader();
        meetUp = new MeetUp();

        if(getIntent().getExtras().getSerializable("rentalHeader")!=null){
            rentalHeader = (RentalHeader) getIntent().getExtras().getSerializable("rentalHeader");
            Log.d("MeetUpChooser", String.valueOf(rentalHeader.getRentalDetail().getBookOwner().getUserObj().getUserId()));
            locationModelList = rentalHeader.getRentalDetail().getBookOwner().getUserObj().getLocationArray();
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MeetUpChooser.this);
                alertDialogBuilder.setTitle("Selected Location");
                alertDialogBuilder.setMessage(marker.getTitle());
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(MeetUpChooser.this, "You agreed to the terms and condition.", Toast.LENGTH_SHORT).show();

                                int position = mHashMap.get(marker);
                                Log.d("MarkerPosition", String.valueOf(position));
                                Intent intent = new Intent(MeetUpChooser.this,TimeDateChooser.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("rentHeader", rentalHeader);
                                mBundle.putBoolean("fromRent", true);
                                mBundle.putSerializable("locationChose", locationModelList.get(position));
                                intent.putExtra("confirm", mBundle);
                                meetUp.setLocation(locationModelList.get(position));
                                mBundle.putSerializable("meetUp", meetUp);
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
