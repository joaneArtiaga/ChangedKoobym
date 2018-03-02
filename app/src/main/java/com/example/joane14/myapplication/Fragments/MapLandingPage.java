package com.example.joane14.myapplication.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Activities.ProfileActivity;
import com.example.joane14.myapplication.Activities.ViewAuctionBook;
import com.example.joane14.myapplication.Activities.ViewBookAct;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapDetail;
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
        GoogleMap.OnMarkerClickListener {

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
    FrameLayout fm;
    Bitmap bm;

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

        User user = (User) SPUtility.getSPUtil(getContext()).getObject("USER_OBJECT", User.class);
        Log.d("User", user.toString());

        double latitude = 0, longitude = 0;

        latitude = Double.parseDouble(user.getLocationArray().get(0).getLatitude());
        longitude = Double.parseDouble(user.getLocationArray().get(0).getLongitude());

        for (int init = 0; init < user.getLocationArray().size(); init++) {
            LocationModel location = user.getLocationArray().get(init);
            Log.d("UserLocation", user.getLocationArray().get(init).toString());
            if(location.getStatus().equals("Address")){
                latitude = Double.parseDouble(user.getLocationArray().get(init).getLatitude());
                longitude = Double.parseDouble(user.getLocationArray().get(init).getLongitude());
            }
        }

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Your Address");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        bookOwnerModelList = new ArrayList<BookOwnerModel>();
        getSuggested(user.getUserId());


        mMarker = new ArrayList<Marker>();


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final Dialog dialogCustom = new Dialog(getContext());
                dialogCustom.setContentView(R.layout.map_book_item);

                BookOwnerModel bookOwnerModel = new BookOwnerModel();

                int bookOwnerId = Integer.parseInt(marker.getTitle());

                for (int mark = 0; mark < bookOwnerModelList.size(); mark++) {
                    if (bookOwnerModelList.get(mark).getBookOwnerId() == bookOwnerId) {
                        bookOwnerModel = bookOwnerModelList.get(mark);
                    }
                }

                TextView mTitle = (TextView) dialogCustom.findViewById(R.id.lpBookTitle);
                TextView mAuthor = (TextView) dialogCustom.findViewById(R.id.lpAuthor);
                TextView mStatus = (TextView) dialogCustom.findViewById(R.id.ratingStatusBook);
                LinearLayout mStatLl = (LinearLayout) dialogCustom.findViewById(R.id.status_ll);
                RatingBar mRate = (RatingBar) dialogCustom.findViewById(R.id.rating_bookRating);
                ImageView ivBookSwap = (ImageView) dialogCustom.findViewById(R.id.displayBookPic);
                Button mViewOwner = (Button) dialogCustom.findViewById(R.id.btnOwner);
                Button mViewBook = (Button) dialogCustom.findViewById(R.id.btnBook);

                Glide.with(getContext()).load(bookOwnerModel.getBookObj().getBookFilename()).centerCrop().into(ivBookSwap);


                mTitle.setText(bookOwnerModel.getBookObj().getBookTitle());

                String author = " ";
                if (bookOwnerModel.getBookObj().getBookAuthor().size() != 0) {
                    for (int init = 0; init < bookOwnerModel.getBookObj().getBookAuthor().size(); init++) {
                        if (!(bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))) {
                            author += bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorFName() + " ";
                            if (!(bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))) {
                                author += bookOwnerModel.getBookObj().getBookAuthor().get(init).getAuthorLName();
                                if (init + 1 < bookOwnerModel.getBookObj().getBookAuthor().size()) {
                                    author += ", ";
                                }
                            }
                        }
                    }
                } else {
                    author = "Unknown Author";
                }

                mAuthor.setText(author);
                mStatus.setText(bookOwnerModel.getStatus());
                if (bookOwnerModel.getStatus().equals("Rent")) {
                    mStatLl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRent));
                } else if (bookOwnerModel.getStatus().equals("Swap")) {
                    mStatLl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSwap));
                } else if (bookOwnerModel.getStatus().equals("Auction")) {
                    mStatLl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAuction));
                } else {
                    mStatLl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGray));
                }

                mRate.setRating(Float.parseFloat(String.valueOf(bookOwnerModel.getRate())));

                final BookOwnerModel finalBookOwnerModel = bookOwnerModel;
                mViewOwner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userModelPass", finalBookOwnerModel.getUserObj());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                final BookOwnerModel finalBookOwnerModel1 = bookOwnerModel;
                mViewBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (finalBookOwnerModel1.getStatus().equals("Swap")) {
                            getSwapDetail(finalBookOwnerModel1.getBookOwnerId());
                        } else if (finalBookOwnerModel1.getStatus().equals("Rent")) {
                            getRentalDetail(finalBookOwnerModel1.getBookOwnerId());
                        } else {
                            getAuctionDetail(finalBookOwnerModel1.getBookOwnerId());
                        }
                    }
                });
                dialogCustom.show();

                return false;
            }
        });
        return view;
    }

    private void getRentalDetail(int bookOwnerId) {
        String URL = Constants.GET_BOOK_OWNER_RENTAL_DETAIL + bookOwnerId;
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RentalDetailResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                RentalDetail rentalDetails = gson.fromJson(response, RentalDetail.class);
                Intent intent = new Intent(getContext(), ViewBookAct.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("viewBook", rentalDetails);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        });
        VolleyUtil.volleyRQInstance(getContext()).add(stringRequest);
    }

    private void getSwapDetail(int bookOwnerId) {
        String URL = Constants.GET_BOOK_OWNER_SWAP_DETAIL + bookOwnerId;
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SwapDetailResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                SwapDetail swapDetails = gson.fromJson(response, SwapDetail.class);
                Intent intent = new Intent(getContext(), ViewBookAct.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("swapBook", swapDetails);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        });
        VolleyUtil.volleyRQInstance(getContext()).add(stringRequest);
    }

    private void getAuctionDetail(int bookOwnerId) {
        String URL = Constants.GET_BOOK_OWNER_AUCTION_DETAIL + bookOwnerId;

        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AuctionDetailResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                AuctionDetailModel auctionDetails = gson.fromJson(response, AuctionDetailModel.class);
                Intent intent = new Intent(getContext(), ViewAuctionBook.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("auctionBook", auctionDetails);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        });
        VolleyUtil.volleyRQInstance(getContext()).add(stringRequest);
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

    public String getCompleteAddress(double latitude, double longitude) {

        Log.d("inside", "getCompleteAddress");

        String address = "", city = "", state = "", country = "", postalCode = "", knownName = "";
        String addressComplete = "";


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

    private void getSuggested(int userId) {
        String URL = Constants.GET_RECOMMENDATION + userId;
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

                for (int init = 0; init < bookOwnerModelList.size(); init++) {
                    Log.d("Inside", "BookOwner");

                    for (int num = 0; num < bookOwnerModelList.get(init).getUserObj().getLocationArray().size(); num++) {
                        Log.d("Inside", "LocationArray");
                        if (bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getStatus().equals("Address")) {
                            Log.d("locationArr", bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).toString());

                            int height = 70;
                            int width = 60;
                            BitmapDrawable bitmapDrawable;

                            if(bookOwnerModelList.get(init).getStatus().equals("Rent")){
                                bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.markerrent);
                            }else if(bookOwnerModelList.get(init).getStatus().equals("Swap")){
                                bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.markerswap);
                            }else{
                                bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.markerauction);
                            }


                            Bitmap bitmap = bitmapDrawable.getBitmap();

                            Bitmap small = Bitmap.createScaledBitmap(bitmap, width, height, false);

                            latLat = Double.parseDouble(bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getLatitude());
                            latLong = Double.parseDouble(bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getLongitude());

                            String title = String.valueOf(bookOwnerModelList.get(init).getBookOwnerId());

                            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(latLat, latLong)).title(title);
                            markerOptions.icon(BitmapDescriptorFactory
                                    .fromBitmap(small));
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
        });
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

        Log.d("onMapReady", "inside");
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double latLat, latLong;

                for (int init = 0; init < bookOwnerModelList.size(); init++) {
                    for (int num = 0; num < bookOwnerModelList.get(init).getUserObj().getLocationArray().size(); num++) {
                        if (bookOwnerModelList.get(init).getUserObj().getLocationArray().get(num).getStatus().equals("Address")) {
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

    public void getCurrentLocation() {
        googleMap.clear();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Location not granted", Toast.LENGTH_SHORT).show();
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
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
