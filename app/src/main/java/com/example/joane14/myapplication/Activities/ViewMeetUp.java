package com.example.joane14.myapplication.Activities;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ViewMeetUp extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<LocationModel> locationModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meet_up);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationModel locationModel = new LocationModel();
        locationModelList = new ArrayList<LocationModel>();

        locationModel.setLocationName("Jumalon Butterfly Sanctuary And Art Gallery");
        locationModel.setLatitude("10.290995");
        locationModel.setLongitude("123.864788");
        locationModelList.add(locationModel);

        locationModel = new LocationModel();
        locationModel.setLocationName("Siege Paintball Cebu");
        locationModel.setLatitude("10.292319");
        locationModel.setLongitude("123.864006");
        locationModelList.add(locationModel);

        locationModel = new LocationModel();
        locationModel.setLocationName("Eddelyn Enterprises");
        locationModel.setLatitude("10.292820");
        locationModel.setLongitude("123.865345");
        locationModelList.add(locationModel);

        locationModel = new LocationModel();
        locationModel.setLocationName("Santa Cruz-San Roque Chapel");
        locationModel.setLatitude("10.292615");
        locationModel.setLongitude("123.865299");
        locationModelList.add(locationModel);

        locationModel = new LocationModel();
        locationModel.setLocationName("New Found Burger");
        locationModel.setLatitude("10.292855");
        locationModel.setLongitude("123.866499");
        locationModelList.add(locationModel);


        for(int init=0; init<locationModelList.size();init++){
            Log.d("ViewMeetUp", locationModelList.get(0).toString());
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker);


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        LatLng location = null;

        for (int init=0; init<locationModelList.size(); init++){

            String lati, longi;
            Double latitude, longitude;

            lati = locationModelList.get(init).getLatitude();
            longi = locationModelList.get(init).getLongitude();
            latitude = Double.parseDouble(lati);
            longitude = Double.parseDouble(longi);
            Log.d("Latitude "+latitude.toString(), "Longitude "+longitude.toString());
            location = new LatLng(latitude,longitude);
            mMap.addMarker(new MarkerOptions().position(location).title(locationModelList.get(init).getLocationName()));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
