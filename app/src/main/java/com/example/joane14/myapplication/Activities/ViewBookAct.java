package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Adapters.SwapBookChooserAdapter;
import com.example.joane14.myapplication.Class.BadgeDrawable;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.DisplayBookReview;
import com.example.joane14.myapplication.Fragments.DisplaySwapComments;
import com.example.joane14.myapplication.Fragments.MapLandingPage;
import com.example.joane14.myapplication.Fragments.VolleyUtil;
import com.example.joane14.myapplication.Model.AuctionComment;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.SwapHeaderDetail;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserNotification;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.PlacesUtility;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.LogRecord;

import static android.net.sip.SipErrorCode.TIME_OUT;
import static android.util.Log.d;

public class ViewBookAct extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        DisplayBookReview.OnDisplayBookReviewInteractionListener,
        DisplaySwapComments.OnSwapCommentInteractionListener, AdapterView.OnItemClickListener {

    static final int MSG_DISMISS_DIALOG = 0;
    RentalDetail rentalDetailModel;
    SwapDetail swapDetail;
    AuctionDetailModel auctionDetailModel;
    RatingBar mRating;
    TextView mRenters;
    List<SwapDetail> sdList;
    LayerDrawable icon;
    int countBadge;

    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book2);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarViewBook);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Book");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ViewBookAct.this);

        View hView = navigationView.getHeaderView(0);
        TextView mName = (TextView) hView.findViewById(R.id.tvName);
        TextView mEmail = (TextView) hView.findViewById(R.id.tvEmail);
        ImageView profileImg = (ImageView) hView.findViewById(R.id.profPic);

        rentalDetailModel = new RentalDetail();


        if (SPUtility.getSPUtil(this).contains("USER_OBJECT")) {
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname() + " " + userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(ViewBookAct.this).load(userModel.getImageFilename()).fit().into(profileImg);
        }
        getNotificationCount();

        mRenters = (TextView) findViewById(R.id.vbRenters);
        TextView mAuthor = (TextView) findViewById(R.id.vbAuthor);
        TextView mHeaderLP = (TextView) findViewById(R.id.headerLP);
        TextView mHeaderRP = (TextView) findViewById(R.id.heaedrRP);
        TextView mLPrice = (TextView) findViewById(R.id.vbLockInP);
        TextView mRPrice = (TextView) findViewById(R.id.vbRentalP);
        TextView mTitle = (TextView) findViewById(R.id.vbTitle);
        Button mViewBtn = (Button) findViewById(R.id.btnVbViewOwner);
        Button mRentBtn = (Button) findViewById(R.id.btnVbRent);
        TextView mContent = (TextView) findViewById(R.id.vbContent);
        TextView mCondition = (TextView) findViewById(R.id.vbCondition);
        TextView mGenre = (TextView) findViewById(R.id.vbGenre);
        ImageView mBookImg = (ImageView) findViewById(R.id.vbBookPic);
        LinearLayout buttonLinear = (LinearLayout) findViewById(R.id.button_ll);
        LinearLayout lockInLinear = (LinearLayout) findViewById(R.id.lockInLinear);
        LinearLayout priceLinear = (LinearLayout) findViewById(R.id.priceLL);

        mRating = (RatingBar) findViewById(R.id.vbRating);

        if (getIntent().getExtras().getSerializable("viewBook") != null) {

            rentalDetailModel = (RentalDetail) getIntent().getExtras().getSerializable("viewBook");

//            getLatestRenter(rentalDetailModel.getRental_detailId());
            mRenters.setVisibility(View.GONE);
            mTitle.setText(rentalDetailModel.getBookOwner().getBookObj().getBookTitle());

            mLPrice.setText("₱ " + String.format("%.2f", rentalDetailModel.getCalculatedPrice()));
            mRPrice.setText("₱ " + String.format("%.2f", rentalDetailModel.getCalculatedPrice() / 2));

            final User user = (User) SPUtility.getSPUtil(ViewBookAct.this).getObject("USER_OBJECT", User.class);

            if (user.getUserId() == rentalDetailModel.getBookOwner().getUserObj().getUserId()) {
                buttonLinear.setVisibility(View.GONE);
            }

            String genreStr = "";
            int genreSize = rentalDetailModel.getBookOwner().getBookObj().getBookGenre().size();
            if (genreSize > 1) {
                for (int init = 0; init < genreSize; init++) {
                    genreStr = genreStr + rentalDetailModel.getBookOwner().getBookObj().getBookGenre().get(init).getGenreName();
                    if (genreSize - 1 > init) {
                        genreStr = genreStr + ", ";
                    }
                }
            } else {
                genreStr = rentalDetailModel.getBookOwner().getBookObj().getBookGenre().get(0).getGenreName();
            }

            mGenre.setText(genreStr);
            String author = "";

            if (rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().size() != 0) {
                for (int init = 0; init < rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().size(); init++) {
                    if (!(rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))) {
                        author += rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName() + " ";
                        if (!(rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))) {
                            author += rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if (init + 1 < rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().size()) {
                                author += ", ";
                            }
                        }
                    }
                }
            } else {
                author = "Unknown Author";
            }
            mAuthor.setText(author);
            Glide.with(this).load(rentalDetailModel.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            mContent.setText(rentalDetailModel.getBookOwner().getBookObj().getBookDescription());
            mCondition.setText(rentalDetailModel.getBookOwner().getStatusDescription());
            makeTextViewResizable(mContent, 5, "View More", true);

            mRating.setRating(Float.parseFloat(String.valueOf(rentalDetailModel.getBookOwner().getRate())));

            getRatings(rentalDetailModel.getBookOwner().getBookOwnerId());

            Bundle bundle = new Bundle();
            bundle.putSerializable("rentalDetail", rentalDetailModel);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_review_container, DisplayBookReview.newInstance(bundle));
            ft.commit();

            mViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewBookAct.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userModelPass", rentalDetailModel.getBookOwner().getUserObj());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mRentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateRent(user.getUserId(), rentalDetailModel);
                }
            });
        } else if (getIntent().getExtras().getSerializable("swapBook") != null) {
            swapDetail = new SwapDetail();
            swapDetail = (SwapDetail) getIntent().getExtras().getSerializable("swapBook");

            mTitle.setText(swapDetail.getBookOwner().getBookObj().getBookTitle());

            mRating.setRating(Float.parseFloat(String.valueOf(swapDetail.getBookOwner().getRate())));

            mHeaderRP.setText("Swap Price");
            mLPrice.setText(String.valueOf(swapDetail.getSwapPrice()));
            mRPrice.setText(String.valueOf(swapDetail.getSwapPrice() / 2));
            mRPrice.setVisibility(View.GONE);
            mHeaderRP.setVisibility(View.GONE);
            priceLinear.setVisibility(View.GONE);

            final User user = (User) SPUtility.getSPUtil(ViewBookAct.this).getObject("USER_OBJECT", User.class);

            if (user.getUserId() == swapDetail.getBookOwner().getUserObj().getUserId()) {
                buttonLinear.setVisibility(View.GONE);
            }

            String genreStr = "";
            int genreSize = swapDetail.getBookOwner().getBookObj().getBookGenre().size();
            if (genreSize > 1) {
                for (int init = 0; init < genreSize; init++) {
                    genreStr = genreStr + swapDetail.getBookOwner().getBookObj().getBookGenre().get(init).getGenreName();
                    if (genreSize - 1 > init) {
                        genreStr = genreStr + ", ";
                    }
                }
            } else {
                genreStr = swapDetail.getBookOwner().getBookObj().getBookGenre().get(0).getGenreName();
            }

            mGenre.setText(genreStr);
            String author = "";

            if (swapDetail.getBookOwner().getBookObj().getBookAuthor().size() != 0) {
                for (int init = 0; init < swapDetail.getBookOwner().getBookObj().getBookAuthor().size(); init++) {
                    if (!(swapDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))) {
                        author += swapDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName() + " ";
                        if (!(swapDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))) {
                            author += swapDetail.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if (init + 1 < swapDetail.getBookOwner().getBookObj().getBookAuthor().size()) {
                                author += ", ";
                            }
                        }
                    }
                }
            } else {
                author = "Unknown Author";
            }
            mAuthor.setText(author);
            Glide.with(this).load(swapDetail.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            mContent.setText(swapDetail.getBookOwner().getBookObj().getBookDescription());
            mCondition.setText(swapDetail.getBookOwner().getStatusDescription());
            makeTextViewResizable(mContent, 5, "View More", true);

            getRatings(swapDetail.getBookOwner().getBookOwnerId());
            Log.d("SwapComment", "Display");

            mRentBtn.setText("Request to Swap");

            mViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewBookAct.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userModelPass", swapDetail.getBookOwner().getUserObj());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mRentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkExist();
                }
            });
        } else if (getIntent().getExtras().getSerializable("auctionBook") != null) {
            auctionDetailModel = new AuctionDetailModel();
            auctionDetailModel = (AuctionDetailModel) getIntent().getExtras().getSerializable("auctionBook");


            mTitle.setText(auctionDetailModel.getBookOwner().getBookObj().getBookTitle());

            mRating.setRating(Float.parseFloat(String.valueOf(auctionDetailModel.getBookOwner().getRate())));

            lockInLinear.setVisibility(View.GONE);
            mHeaderRP.setText("Starting Price");
            mLPrice.setText(String.valueOf(auctionDetailModel.getStartingPrice()));

            final User user = (User) SPUtility.getSPUtil(ViewBookAct.this).getObject("USER_OBJECT", User.class);

            if (user.getUserId() == auctionDetailModel.getBookOwner().getUserObj().getUserId()) {
                buttonLinear.setVisibility(View.GONE);
            }

            String genreStr = "";
            int genreSize = auctionDetailModel.getBookOwner().getBookObj().getBookGenre().size();
            if (genreSize > 1) {
                for (int init = 0; init < genreSize; init++) {
                    genreStr = genreStr + auctionDetailModel.getBookOwner().getBookObj().getBookGenre().get(init).getGenreName();
                    if (genreSize - 1 > init) {
                        genreStr = genreStr + ", ";
                    }
                }
            } else {
                genreStr = auctionDetailModel.getBookOwner().getBookObj().getBookGenre().get(0).getGenreName();
            }

            mGenre.setText(genreStr);

            String author = "";

            if (auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().size() != 0) {
                for (int init = 0; init < auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().size(); init++) {
                    if (!(auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))) {
                        author += auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName() + " ";
                        if (!(auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))) {
                            author += auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if (init + 1 < auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().size()) {
                                author += ", ";
                            }
                        }
                    }
                }
            } else {
                author = "Unknown Author";
            }
            mAuthor.setText(author);
            Glide.with(this).load(auctionDetailModel.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            mContent.setText(auctionDetailModel.getBookOwner().getBookObj().getBookDescription());
            mCondition.setText(auctionDetailModel.getBookOwner().getStatusDescription());
            makeTextViewResizable(mContent, 5, "View More", true);

            getRatings(auctionDetailModel.getBookOwner().getBookOwnerId());

            Log.d("SwapComment", "Display");

            mRentBtn.setText("Bid");

            mViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewBookAct.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userModelPass", auctionDetailModel.getBookOwner().getUserObj());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }
    }

    public void checkExist() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final User user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        String URL = Constants.CHECK_SWAP_REQUEST + user.getUserId() + "/" + swapDetail.getSwapDetailId();
        d("SwapURL", URL);
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapDetail);

        d("SwapDetail", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("SwapDetailResponse", response);
                if (response.length() == 0) {
                    getMySwapDetail();
                } else {
                    AlertDialog ad = new AlertDialog.Builder(ViewBookAct.this).create();
                    ad.setTitle("Alert!");
                    ad.setMessage("You already sent a request for this book. You can't send a request again.");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public void getMySwapDetail() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final User user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        String URL = Constants.GET_ALL_MY_SWAP + user.getUserId();
        d("SwapURL", URL);
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapDetail);

        d("SwapDetail", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("suggestedSwap", response);
                final List<SwapDetail> swapDetailList = new ArrayList<SwapDetail>();

                swapDetailList.addAll(Arrays.asList(gson.fromJson(response, SwapDetail[].class)));

                if(swapDetailList.isEmpty()){
                    Log.d("suggestedSwap", "empty");
                }else{
                    Log.d("suggestedSwap", "not");
                }

                if (swapDetailList.isEmpty()) {
                    AlertDialog ad = new AlertDialog.Builder(ViewBookAct.this).create();
                    ad.setTitle("Alert!");
                    ad.setMessage("You don't have any books that are available for swap.");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                } else {

                    final Dialog dialog = new Dialog(ViewBookAct.this);
                    View view = getLayoutInflater().inflate(R.layout.swap_book_chooser, null);

                    final ListView ly = (ListView) view.findViewById(R.id.listSwap);
                    final SwapBookChooserAdapter adapter = new SwapBookChooserAdapter(ViewBookAct.this, swapDetailList);

                    sdList = swapDetailList;
                    ly.setAdapter(adapter);
                    Log.d("lySelectedNum", ly.getCheckedItemCount() + "");
                    Button mOkay = (Button) view.findViewById(R.id.btnReq);
                    Button mCancel = (Button) view.findViewById(R.id.btnCancel);

                    mOkay.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View v) {
                            List<SwapDetail> requesteeSwapDetail = adapter.getSelectedSwap();
                            if (requesteeSwapDetail.size() != 0) {
                                List<SwapHeaderDetail> listSHD = new ArrayList<SwapHeaderDetail>();

                                Calendar cal = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String currDate = sdf.format(cal.getTime());

                                SwapHeader swapHeader = new SwapHeader();

                                swapHeader.setDateRequest(currDate);

                                swapHeader.setStatus("Request");

                                swapHeader.setUser(user);

                                swapHeader.setSwapDetail(swapDetail);

                                swapHeader.setRequestedSwapDetail(requesteeSwapDetail.get(0));

                                swapHeader.setDateTimeStamp(currDate);

                                for (int init = 0; init < requesteeSwapDetail.size(); init++) {
                                    SwapHeaderDetail shd = new SwapHeaderDetail();

                                    shd.setSwapDetail(requesteeSwapDetail.get(init));
                                    shd.setSwapType("Requestor");
                                    shd.getSwapDetail().setSwapStatus("Not Available");
                                    shd.getSwapDetail().getBookOwner().setBookStat("Not Available");
                                    shd.getSwapDetail().getBookOwner().getBookObj().setStatus("Not Available");
                                    listSHD.add(shd);
                                }

                                SwapHeaderDetail swapHeaderDetail = new SwapHeaderDetail();
                                swapHeaderDetail.setSwapDetail(swapDetail);
                                swapHeaderDetail.setSwapType("Requestee");

                                listSHD.add(swapHeaderDetail);
                                swapHeader.setSwapHeaderDetail(listSHD);

                                Log.d("This user ", user.getUserFname());

                                Log.d("SwapDetailRead:" + swapDetail.getBookOwner().getBookObj().getBookTitle(), "user:" + swapHeader.getSwapDetail().getBookOwner().getUserObj().getUserFname());
                                Log.d("ReqDetail:" + swapHeader.getRequestedSwapDetail().getBookOwner().getBookObj().getBookTitle(), "user:" + swapHeader.getRequestedSwapDetail().getBookOwner().getUserObj().getUserFname());


                                addSwapHeader(swapHeader);
                            } else {
                                AlertDialog ad = new AlertDialog.Builder(ViewBookAct.this).create();
                                ad.setTitle("Alert!");
                                ad.setMessage("You should choose a book/s for it to be swapped.");
                                ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                ad.show();
                            }
                        }
                    });

                    mCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setContentView(view);
                    dialog.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    private AdapterView.OnItemClickListener listviewOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(), "balalallala", Toast.LENGTH_SHORT).show();
        }
    };

//    private void getLatestRenter(int rentalDetailId) {
//        String URL = Constants.GET_LATEST_RENTER + rentalDetailId;
//        Log.d("LatestRenterURL", URL);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("LatestRenterResponse", response);
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
//                RentalHeader rh= gson.fromJson(response, RentalHeader.class);
//
//                String message = rh.getUserId().getUserFname()+" "+rh.getUserId().getUserLname()+" rented on "+rh.getDateDeliver();
//                mRenters.setText(message);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("LOG_VOLLEY", error.toString());
//            }
//        });
//        VolleyUtil.volleyRQInstance(ViewBookAct.this).add(stringRequest);
//    }

    private void getSwapDetail(int bookOwnerId) {
        String URL = Constants.GET_BOOK_OWNER_SWAP_DETAIL + bookOwnerId;
        Log.d("PreferenceURL", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SwapDetailResponse", response);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
                SwapDetail swapDetails = gson.fromJson(response, SwapDetail.class);
                Intent intent = new Intent(ViewBookAct.this, ViewBookAct.class);
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
        VolleyUtil.volleyRQInstance(ViewBookAct.this).add(stringRequest);
    }

    public void addSwapHeader(SwapHeader swapHeaderToPost) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final User user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        String URL = Constants.POST_SWAP_HEADER;
        d("SwapURL", URL);
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapHeaderToPost);

        d("a", mRequestBody);
        int maxLogSize = 2000;
        for (int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("addSwapHeader", mRequestBody.substring(start, end));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("AddSHRes", response);
                SwapHeader sh = gson.fromJson(response, SwapHeader.class);

                UserNotification un = new UserNotification();

                un.setActionId(sh.getSwapHeaderId());
                un.setBookActionPerformedOn(swapDetail.getBookOwner());
                un.setUser(swapDetail.getBookOwner().getUserObj());
                un.setUserPerformer(user);
                un.setActionName("swap");
                un.setActionStatus("Request");
                un.setProcessedBool(false);

                addUserNotif(un);

                for(int init=0; init<sh.getSwapHeaderDetail().size(); init++){
                    Log.d("swapHeaderDetailLoop", "yes");
                    if(sh.getSwapHeaderDetail().get(init).getSwapType().equals("Requestor")){
                        SwapDetail sd = new SwapDetail();
                        BookOwnerModel bo = new BookOwnerModel();
                        sd = sh.getSwapHeaderDetail().get(init).getSwapDetail();
                        bo = sd.getBookOwner();
                        bo.setBookStat("Not Available");
                        bo.getBookObj().setStatus("Not Available");
                        sd.setSwapStatus("Not Available");
                        sd.getBookOwner().setBookStat("Not Available");
                        updateSwapDetail(sd);
                        updateBookOwner(bo);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public void updateBookOwner(BookOwnerModel bookOwnerModel) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final User user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        String URL = Constants.PUT_BOOK_OWNER;
        d("SwapURL", URL);
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookOwnerModel);

        int maxLogSize = 2000;
        for (int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("updateBookOWner", mRequestBody.substring(start, end));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("updateBookOWner", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public void updateSwapDetail(SwapDetail swapDetailModel) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final User user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        String URL = Constants.PUT_SWAP_DETAIL;
        d("SwapURL", URL);
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapDetailModel);

        int maxLogSize = 2000;
        for (int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("updateSwapDetail", mRequestBody.substring(start, end));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("updateSwapDetail", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public void addUserNotif(UserNotification userNotification) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.POST_USER_NOTIFICATION;


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(userNotification);


        d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("userNotificationPost", response);

                AlertDialog ad = new AlertDialog.Builder(ViewBookAct.this).create();
                ad.setMessage("The owner will be notified of your request.");
                ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ViewBookAct.this, ViewBookAct.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("swapBook", swapDetail);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                ad.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public static void makeTextViewResizable(final TextView textView, final int maxLine, final String expandText, final boolean viewMore) {
        Log.d("makeTextViewResizable", "inside");
        if (textView.getTag() == null) {
            textView.setTag(textView.getText());
        }

        ViewTreeObserver treeObserver = textView.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndText;
                ViewTreeObserver observer = textView.getViewTreeObserver();

                observer.removeGlobalOnLayoutListener(this);

                if (maxLine == 0) {
                    lineEndText = textView.getLayout().getLineEnd(0);
                    text = textView.getText().subSequence(0, lineEndText - expandText.length() - 1) + " " + expandText;
                } else if (maxLine > 0 && textView.getLineCount() >= maxLine) {
                    lineEndText = textView.getLayout().getLineEnd(maxLine - 1);
                    text = textView.getText().subSequence(0, lineEndText - expandText.length() - 1) + " " + expandText;
                } else {
                    lineEndText = textView.getLayout().getLineEnd(textView.getLayout().getLineCount() - 1);
                    text = textView.getText().subSequence(0, lineEndText) + " " + expandText;
                }

                textView.setText(text);
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                textView.setText(addClickablePartTextViewResizable(Html.fromHtml(textView.getText().toString()), textView, lineEndText, expandText,
                        viewMore), TextView.BufferType.SPANNABLE);

            }
        });


    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    Log.d("spannable", "inside");
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        makeTextViewResizable(tv, 5, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public void addRentalHeader(RentalHeader rentalHeader) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = Constants.POST_RENTAL_HEADER;

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);

        d("RentalHeaderAdd", rentalHeader.toString());


        Log.v("LOG_VOLLEY", mRequestBody);
        d("RentalHeaderVolley", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("onResponse addRentalH", "inside");
                Log.i("AddRentalHeader", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    private void checkIfExist(final int userId, final int rentalDetailId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = Constants.CHECK_EXIST + "/" + userId + "/" + rentalDetailId;

        d("CheckURL", URL);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalDetailModel);

        d("CheckIfExist", "inside");

        d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                d("ResponseExist", response);

                if (response == null || response.isEmpty()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewBookAct.this);
                    alertDialogBuilder.setTitle("Terms and Condition");
                    alertDialogBuilder.setMessage("\n\n1.\tThis book must be returned on time.\n" +
                            "2.\tThis book should be returned in the same condition it was provided.\n" +
                            "3.\tThe renter will compensate for the damages that the book may incur during the duration of his/her usage.\n" +
                            "4.\tA fee of 50 pesos per day will be incurred to the renter if the book is not returned on or before the due date.");
                    alertDialogBuilder.setPositiveButton("Agree",
                            new DialogInterface.OnClickListener() {
                                @SuppressLint("NewApi")
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ViewBookAct.this);
                                    alertDialogBuilder.setMessage("The owner will be notified of your request.");
                                    alertDialogBuilder.setPositiveButton("Okay",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    RentalHeader rentalHeader = new RentalHeader();

                                                    User user = (User) SPUtility.getSPUtil(ViewBookAct.this).getObject("USER_OBJECT", User.class);
                                                    String nextDateStr = "";

                                                    @SuppressLint({"NewApi", "LocalSuppress"})
                                                    Calendar c = Calendar.getInstance();


                                                    @SuppressLint({"NewApi", "LocalSuppress"})
                                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                                    nextDateStr = format.format(c.getTime());

                                                    rentalHeader.setRentalDetail(rentalDetailModel);
                                                    rentalHeader.setRentalTimeStamp(nextDateStr);
                                                    rentalHeader.setUserId(user);
                                                    rentalHeader.setTotalPrice((float) rentalDetailModel.getCalculatedPrice());
                                                    rentalHeader.setStatus("Request");
                                                    addRentalHeader(rentalHeader);
                                                    alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss(DialogInterface dialog) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                            });

                                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                }
                            });

                    alertDialogBuilder.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ViewBookAct.this, "You disagreed to the terms and condition.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {

                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ViewBookAct.this);
                    alertDialogBuilder.setTitle("Alert!");
                    alertDialogBuilder.setMessage("You already requested for this book. You can't request again.");
                    alertDialogBuilder.setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_DIALOG:
                    if (mAlertDialog != null && mAlertDialog.isShowing()) {
                        mAlertDialog.dismiss();
                    }
                    break;

                default:
                    break;
            }
        }
    };

//    private void timeOutDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Please wait for the approval of the owner.\n You will get notification once approved by the owner.");
//        builder.setPositiveButton("OK", null)
//                .setNegativeButton("cacel", null);
//        mAlertDialog = builder.create();
//        mAlertDialog.show();
//        mHandler.sendEmptyMessageDelayed(MSG_DISMISS_DIALOG, TIME_OUT);
//    }

    public void getCount() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_COUNT + rentalDetailModel.getBookOwner().getBookOwnerId();

        d("CountURL", URL);

        final RentalHeader rentalHeader = new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CountBookResponse", response);
                mRenters.setText("Rented by " + response + " people.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public void validateSwap(int userId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.VALIDATE_SWAP + "/" + userId;


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(userId);


        d("LOG_VOLLEY", mRequestBody);
        final User finalUser = user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("validate", response);
                if (response.equals("true")) {
                    checkExist();
                } else {
                    AlertDialog ad = new AlertDialog.Builder(getApplicationContext()).create();
                    ad.setMessage("Maximum Swap Books is 3 only. You cannot request this book.");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }


    public void validateRent(final int userId, final RentalDetail rentalDetail) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.VALIDATE_RENT + "/" + userId;

        final RentalHeader rentalHeader = new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        d("LOG_VOLLEY", mRequestBody);
        final User finalUser = user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("validate", response);
                if (response.equals("true")) {
                    checkIfExist(userId, rentalDetail.getRental_detailId());
                }else {
                    AlertDialog ad = new AlertDialog.Builder(ViewBookAct.this).create();
                    ad.setMessage("Maximum Rent Books is 3 only. You cannot request this book.");
                    ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    ad.show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    public void getRatings(int bookOwnerId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_RATINGS + "/" + bookOwnerId;

        final RentalHeader rentalHeader = new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ViewBookResponse", response);

                Float fl = Float.parseFloat(response);

                mRating.setRating(fl);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        d("onNavigationItem", "inside");

        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(ViewBookAct.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(ViewBookAct.this, ProfileActivity.class);
            Bundle bundlePass = new Bundle();
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(ViewBookAct.this).getObject("USER_OBJECT", User.class);
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtras(bundlePass);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(ViewBookAct.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            Intent intent = new Intent(ViewBookAct.this, BookActActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {
            Intent intent = new Intent(ViewBookAct.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(ViewBookAct.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ViewBookAct.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_main, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        MenuItem item = menu.findItem(R.id.action_notifications);
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                FragmentManager fragmentManager;
                fragmentManager = getSupportFragmentManager();
                MapLandingPage prefFrag = MapLandingPage.newInstance();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_landing_container, prefFrag, prefFrag.getTag());
                fragmentTransaction.commit();

                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SearchKeyword", query);
                Intent intent = new Intent(ViewBookAct.this, SearchResult.class);
                Bundle bundle = new Bundle();
                bundle.putString("SearchKeyword", query);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SearchView", "onclick");
                Intent intent = new Intent(ViewBookAct.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(ViewBookAct.this, NotificationAct.class);
                startActivity(intent);


                return false;
            }
        });

        MenuItem itemCart = menu.findItem(R.id.action_notifications);
        icon = (LayerDrawable) itemCart.getIcon();
        return super.onCreateOptionsMenu(menu);
    }

    private int getNotificationCount() {
        User user = (User) SPUtility.getSPUtil(getApplicationContext()).getObject("USER_OBJECT", User.class);
        String query = Constants.GET_COUNT_NOTIF + user.getUserId();

        this.countBadge = 0;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int count = Integer.parseInt(response);
                countBadge = count;
                setBadgeCount(ViewBookAct.this, count);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        PlacesUtility.getInstance(this).add(stringRequest);
        return countBadge;
    }

    public void setBadgeCount(Context context, int count) {
        if (icon != null) {
            String countString = Integer.toString(count);
            BadgeDrawable badge;
            Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
            if (reuse != null && reuse instanceof BadgeDrawable) {
                badge = (BadgeDrawable) reuse;
            } else {
                badge = new BadgeDrawable(context);
            }

            badge.setCount(countString);
            icon.mutate();
            icon.setDrawableByLayerId(R.id.ic_badge, badge);
        }
    }

    @Override
    public void onDisplayBookReviewOnClick(Uri uri) {

    }

    @Override
    public void onSwapCommentOnClick(Uri uri) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("SelectNaSwap", sdList.get(position).getBookOwner().getBookObj().getBookTitle());
    }
}
