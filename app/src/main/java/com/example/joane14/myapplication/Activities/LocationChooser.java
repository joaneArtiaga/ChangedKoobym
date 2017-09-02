package com.example.joane14.myapplication.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.joane14.myapplication.Adapters.LocationAdapter;
import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.facebook.GraphRequest.TAG;

public class LocationChooser extends FragmentActivity implements
        View.OnClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnMapLongClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener{

    GoogleMap googleMap;
    private int cntMarker;
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;
    Button mNext;
    RecyclerView mRecycler;
    RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager recyclerLayout;
    ArrayList<LocationModel> locationList = new ArrayList<LocationModel>();
    LocationModel locationObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_chooser);

        cntMarker=0;
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationList = new ArrayList<LocationModel>();

        locationObj = new LocationModel();
//        createMapView();


        mNext = (Button) findViewById(R.id.btnNext);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Next Button", "Triggered");
                Intent intent = new Intent(LocationChooser.this, SignUp.class);
                intent.putExtra("Genre", "genreChooser");
                startActivity(intent);
            }
        });


        mRecycler = (RecyclerView) findViewById(R.id.location_recycler_view);
        mRecycler.setHasFixedSize(true);
        recyclerLayout = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(recyclerLayout);
        recyclerAdapter = new LocationAdapter(locationList);
        mRecycler.setAdapter(recyclerAdapter);

    }

    private void createMapView() {
        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.map)).getMap();

                if(null != googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Mapa carregado!", Toast.LENGTH_SHORT).show();
                }

                if (null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Erro ao criar mapa", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        ((LocationAdapter) recyclerAdapter).setOnItemClickListener(new LocationAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i("Location", " Clicked on Item " + position);
            }
        });
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG,"view click event");
        Toast.makeText(this, "Click Event", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap mMap) {

        googleMap = mMap;



        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double latLat, latLong;

                if(cntMarker==5){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(LocationChooser.this);
                    builder1.setMessage("Only 5 locations are available. You cannot choose more than 5 or less than 5.");
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
                    cntMarker++;

                    MarkerOptions marker = new MarkerOptions().position(
                            latLng)
                            .title("Hello Maps ");
                    marker.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    googleMap.addMarker(marker);

                    latLat = latLng.latitude;
                    latLong = latLng.longitude;

                    locationObj.setLatitude(latLat);
                    locationObj.setLongitude(latLong);
                    locationObj.setLocationName("Temporary name");
                    locationList.add(locationObj);

                    Log.d("Latlng",latLng.toString());


                    Log.d("Count Marker", Integer.toString(cntMarker));
                    recyclerAdapter.notifyDataSetChanged();
                }

            }

        });


    }

    public void getCurrentLocation(){
        googleMap.clear();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Turn on Location", Toast.LENGTH_SHORT).show();
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if(location!=null){
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            moveMap();
        }
    }

    private void moveMap() {
        LatLng latlng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .draggable(true)
                    .title("Current Location"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("Count Marker", Integer.toString(cntMarker));
        return false;
    }
}
