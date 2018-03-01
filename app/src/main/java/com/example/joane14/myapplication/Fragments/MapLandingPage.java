package com.example.joane14.myapplication.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapLandingPage extends Fragment implements
        View.OnClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnMapLongClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener{

    MapView mMapView;
    private GoogleMap googleMap;

    List<Marker> mMarker;
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;
    private Marker thisMarker;
    private OnMapInteractionListener mListener;
    List<BookOwnerModel> bookOwnerModelList;
    private LocationManager locManager;

    public MapLandingPage() {
    }


    public static MapLandingPage newInstance() {
        MapLandingPage fragment = new MapLandingPage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_landing_page, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();


        double latitude = 17.385044;
        double longitude = 78.486671;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Hello Maps");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(17.385044, 78.486671)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        bookOwnerModelList = new ArrayList<BookOwnerModel>();
        User user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        getSuggested(user.getUserId());


        mMarker = new ArrayList<Marker>();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public String getCompleteAddress(double latitude, double longitude){

        Log.d("inside", "getCompleteAddress");

        String address="", city="", state="", country = "", postalCode ="", knownName = "";
        String addressComplete="";


        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;


        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                Log.d("Location address", address);
                addressComplete = address;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressComplete;
    }

    private void getSuggested(int userId){
        String URL = Constants.GET_RECOMMENDATION+userId;
        URL = String.format(URL, userId);
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Inside get suggested", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                BookOwnerModel[] bookOwnerModels = gson.fromJson(response, BookOwnerModel[].class);
                bookOwnerModelList.addAll(Arrays.asList(bookOwnerModels));

                double latLat, latLong;

                for(int init=0; init<bookOwnerModelList.size(); init++){
                    Log.d("Inside", "BookOwner");

                    for(int num=0; num<bookOwnerModelList.get(init).getUserObj().getLocationArray().size(); num++){
                        Log.d("Inside", "LocationArray");
                        if(bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getStatus().equals("Address")){
                            Log.d("Inside", bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getLocationName());

                            latLat = Double.parseDouble(bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getLatitude());
                            latLong = Double.parseDouble(bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getLongitude());

                            String title = bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getLocationName();

                            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latLat, latLong)).title(title);
                            markerOptions.icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            googleMap.addMarker(markerOptions);
                        }

                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) ;
        VolleyUtil.volleyRQInstance(getContext()).add(stringRequest);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMapInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMapInteractionListener) {
            mListener = (OnMapInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        Log.d("onMapReady","inside");
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double latLat, latLong;

                for(int init=0; init<bookOwnerModelList.size(); init++){
                    for(int num=0; num<bookOwnerModelList.get(init).getUserObj().getLocationArray().size(); num++){
                        if(bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getStatus().equals("Address")){
                            latLat = Double.parseDouble(bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getLatitude());
                            latLong = Double.parseDouble(bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getLongitude());
                            String title = bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getLocationName();
                            MarkerOptions marker = new MarkerOptions().position(new LatLng(latLat, latLong)).title(title);
                            marker.icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            thisMarker = googleMap.addMarker(marker);
                            mMarker.add(thisMarker);
                        }

                    }

                }
//
//                MarkerOptions marker = new MarkerOptions().position(
//                        latLng)
//                        .title(getCompleteAddress(latLat, latLong));
//                marker.icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//
//                thisMarker = googleMap.addMarker(marker);
//                mMarker.add(thisMarker);
//                int position = mMarker.indexOf(thisMarker);
//
//                Log.d("position Marker", Integer.toString(position));
            }
        });
    }

    public void getCurrentLocation(){
        googleMap.clear();

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getContext(), "Location not granted", Toast.LENGTH_SHORT).show();
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

    public interface OnMapInteractionListener {
        void onMapInteraction(Uri uri);
    }


}
