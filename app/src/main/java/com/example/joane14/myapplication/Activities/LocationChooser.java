package com.example.joane14.myapplication.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.joane14.myapplication.Adapters.LocationAdapter;
import com.example.joane14.myapplication.Model.DayTimeModel;
import com.example.joane14.myapplication.Model.GenreModel;
import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.MeetUpLocObj;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.facebook.GraphRequest.TAG;

public class LocationChooser extends FragmentActivity implements
        View.OnClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnMapLongClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener,
        LocationAdapter.onDeleteListener{

    GoogleMap googleMap;
    List<Marker> mMarker;
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;
    private Marker thisMarker;
    Button mNext;
    RecyclerView mRecycler;
    RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager recyclerLayout;
    ArrayList<LocationModel> locationList = new ArrayList<LocationModel>();
    LocationModel locationObj;
    AlertDialog alertDialog1;
    final List timeSelected = new ArrayList<String>();;
    final CharSequence[] values = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private int mHour, mMinute;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_chooser);


        final Dialog[] dialog = {new Dialog(this)};

        final ArrayList itemsSelected = new ArrayList();

        bundle = new Bundle();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mMarker = new ArrayList<Marker>();
        locationList = new ArrayList<LocationModel>();

        locationObj = new LocationModel();

        mNext = (Button) findViewById(R.id.btnNext);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Next Button", "Triggered");
                if(recyclerAdapter.getItemCount()<3){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(LocationChooser.this);
                    builder1.setMessage("Only 3 locations are available. You cannot choose less than 3.");
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
                }else if(recyclerAdapter.getItemCount()>3){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(LocationChooser.this);
                    builder1.setMessage("Only 3 locations are available. You cannot choose more than 3.");
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
                }else {
//                    CreateAlertDialogWithRadioButtonGroup();
//
                    AlertDialog.Builder builder = new AlertDialog.Builder(LocationChooser.this);
                    builder.setTitle("Choose available Day for Meet Up");

                    builder.setMultiChoiceItems(values, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int selectedItemId,
                                                    boolean isSelected) {
                                    Log.d("selectedItemId", Integer.toString(selectedItemId));

                                    if (isSelected) {
                                        itemsSelected.add(values[selectedItemId]);
                                    } else if (itemsSelected.contains(values[selectedItemId])) {
                                        Log.d("selectedItemId", Integer.toString(selectedItemId));
                                        itemsSelected.remove(values[selectedItemId]);
                                    }
                                }
                            })
                            .setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    for(int init=0; init<itemsSelected.size(); init++){
                                        Log.d("list checked", values[init].toString());
                                    }
                                    MeetUpLocObj meetUpLocObj = new MeetUpLocObj();
                                    meetUpLocObj.setLocationModelList(locationList);
                                    meetUpLocObj.setItemSelected(itemsSelected);
                                    Intent intent = new Intent(LocationChooser.this, SignUp.class);
                                    bundle.putSerializable("locationObj", meetUpLocObj);
                                    bundle.putBoolean("AddFrag", true);
                                    intent.putExtras(bundle);
                                    intent.putExtra("confirmation", bundle);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    dialog[0] = builder.create();
                    dialog[0].show();

                }
            }
        });


        mRecycler = (RecyclerView) findViewById(R.id.location_recycler_view);
        mRecycler.setHasFixedSize(true);
        recyclerLayout = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(recyclerLayout);
        recyclerAdapter = new LocationAdapter(locationList, this);
        mRecycler.setAdapter(recyclerAdapter);

    }

    public void CreateTimePicker(){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String timeGiven = "";
                        timeGiven = hourOfDay + ":" + minute;



                        timeSelected.add(timeGiven);
                        Log.d("time Selected", timeSelected.toString());


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    public String getCompleteAddress(double latitude, double longitude){

        Log.d("inside", "getCompleteAddress");

        String address="", city="", state="", country = "", postalCode ="", knownName = "";
        String addressComplete="";


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;


        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
//                city = addresses.get(0).getLocality();
//                state = addresses.get(0).getAdminArea();
//                country = addresses.get(0).getCountryName();
//                postalCode = addresses.get(0).getPostalCode();

                Log.d("Location address", address);
                addressComplete = address;
//                Log.d("Location city", city);
//                Log.d("Location state", state);
//                Log.d("Location country", country);
//                Log.d("Location postal code", postalCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressComplete;
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


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double latLat, latLong;

                if(recyclerAdapter.getItemCount()==5){
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

                    latLat = latLng.latitude;
                    latLong = latLng.longitude;

                    MarkerOptions marker = new MarkerOptions().position(
                            latLng)
                            .title(getCompleteAddress(latLat, latLong));
                    marker.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                    thisMarker = googleMap.addMarker(marker);
                    mMarker.add(thisMarker);
                    int position = mMarker.indexOf(thisMarker);

                    Log.d("position Marker", Integer.toString(position));


                    locationObj = new LocationModel();
                    locationObj.setLatitude(Double.toString(latLat));
                    locationObj.setLongitude(Double.toString(latLong));
                    Log.d("get Address", getCompleteAddress(latLat, latLong));
                    locationObj.setLocationName(getCompleteAddress(latLat, latLong));
                    locationList.add(locationObj);

                    Log.d("Latlng",latLng.toString());


                    recyclerAdapter.notifyDataSetChanged();
                }

            }

        });


    }

    public void getCurrentLocation(){
        googleMap.clear();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Location not granted", Toast.LENGTH_SHORT).show();
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
        return false;
    }

    @Override
    public void onDeleteClick(int position) {
        mMarker.get(position).remove();
        mMarker.remove(position);
    }

}
