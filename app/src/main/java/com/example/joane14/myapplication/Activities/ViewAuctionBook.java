package com.example.joane14.myapplication.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Fragments.AuctionBidFragment;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.CountdownFrag;
import com.example.joane14.myapplication.Model.AuctionComment;
import com.example.joane14.myapplication.Model.AuctionCommentDetail;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.AuctionHeader;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapCommentDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.util.Log.d;

public class ViewAuctionBook extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AuctionBidFragment.OnAuctionBidInteractionListener,
        CountdownFrag.OnCountdownInteractionListener {

    AuctionDetailModel auctionDetailModel;
    RatingBar mRating;
    TextView mRenters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_auction_book);

        Log.d("ViewAuction", "inside");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarViewBookAuction);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book_auction);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ViewAuctionBook.this);

        View hView = navigationView.getHeaderView(0);
        TextView mName = (TextView) hView.findViewById(R.id.tvName);
        TextView mEmail = (TextView) hView.findViewById(R.id.tvEmail);
        ImageView profileImg = (ImageView) hView.findViewById(R.id.profPic);

        auctionDetailModel = new AuctionDetailModel();


        if(SPUtility.getSPUtil(this).contains("USER_OBJECT")){
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname()+" "+userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(ViewAuctionBook.this).load( userModel.getImageFilename()).fit().into(profileImg);
        }

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
        ImageView mBookImg = (ImageView) findViewById(R.id.vbBookPic);
        LinearLayout buttonLinear = (LinearLayout) findViewById(R.id.button_ll);
        LinearLayout priceLinear = (LinearLayout) findViewById(R.id.linearPriceLI);

        mRating = (RatingBar) findViewById(R.id.vbRating);

        if(getIntent().getExtras().getSerializable("auctionBook")!=null){
            auctionDetailModel = new AuctionDetailModel();
            auctionDetailModel = (AuctionDetailModel) getIntent().getExtras().getSerializable("auctionBook");


            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putSerializable("auctionComment", auctionDetailModel);
            AuctionBidFragment abf = AuctionBidFragment.newInstance(bundle);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_bid_container, abf);
//            ft.commit();

            bundle.putSerializable("auctionBook", auctionDetailModel);
            CountdownFrag cdf = CountdownFrag.newInstance(bundle);
            ft.replace(R.id.countdown_container, cdf);
            ft.commit();


            mTitle.setText(auctionDetailModel.getBookOwner().getBookObj().getBookTitle());

            mRating.setRating(Float.parseFloat(String.valueOf(auctionDetailModel.getBookOwner().getRate())));
//
            priceLinear.setVisibility(View.GONE);
//            mHeaderRP.setText("Starting Price");
            mRPrice.setText(String.valueOf(auctionDetailModel.getStartingPrice()));
//            mRPrice.setVisibility(View.GONE);

            final User user = (User) SPUtility.getSPUtil(ViewAuctionBook.this).getObject("USER_OBJECT", User.class);

            if(user.getUserId()==auctionDetailModel.getBookOwner().getUserObj().getUserId()){
                buttonLinear.setVisibility(View.GONE);
            }

            String author = "";

            if(auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().size()!=0){
                for(int init=0; init<auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().size(); init++){
                    if(!(auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                        author+=auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                        if(!(auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                            author+=auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if(init+1<auctionDetailModel.getBookOwner().getBookObj().getBookAuthor().size()){
                                author+=", ";
                            }
                        }
                    }
                }
            }else{
                author="Unknown Author";
            }
            mAuthor.setText(author);
            Glide.with(this).load(auctionDetailModel.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            mContent.setText(auctionDetailModel.getBookOwner().getBookObj().getBookDescription());
            mCondition.setText(auctionDetailModel.getBookOwner().getStatusDescription());

            getRatings(auctionDetailModel.getBookOwner().getBookOwnerId());
//            getCount();

//            Bundle bundle = new Bundle();
//            bundle.putSerializable("swapDetail", swapDetail);
//
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.fragment_review_container, DisplayBookReview.newInstance(bundle));
//            ft.commit();

            Log.d("SwapComment", "Display");
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("swapComment", swapDetail);
//            DisplaySwapComments displaySwapComments = DisplaySwapComments.newInstance(bundle);
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fragment_review_container, displaySwapComments, displaySwapComments.getTag());
//            fragmentTransaction.commit();

            mRentBtn.setText("Bid");

            mViewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewAuctionBook.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userModelPass", auctionDetailModel.getBookOwner().getUserObj());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            mRentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("BidTriggered", "YES");

                    Dialog dialogCustom = new Dialog(ViewAuctionBook.this);
                    LayoutInflater inflater = (LayoutInflater) ViewAuctionBook.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.seekbar_custom_dialog, (ViewGroup) findViewById(R.id.seekbar_layout));
                    dialogCustom.setContentView(layout);

                    Button mBtnOkay = (Button) layout.findViewById(R.id.btnOkay);
                    Button mBtnCancel = (Button) layout.findViewById(R.id.btnCancel);

                    final TextView mPriceBar = (TextView) layout.findViewById(R.id.tvPriceBar);
                    final ProgressBar mProgBar = (ProgressBar) layout.findViewById(R.id.progBar);
                    final SeekBar mSeekBar= (SeekBar) layout.findViewById(R.id.seekbarPrice);

                    mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            progress+=Math.round(auctionDetailModel.getStartingPrice());
                            mProgBar.setProgress(progress);
                            mPriceBar.setText(""+progress);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                    mBtnOkay.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View v) {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String currDate = sdf.format(calendar.getTime());


                            int price = mSeekBar.getProgress();
                            Log.d("SeekBarPrice", String.valueOf(price));

                            AuctionComment auctionComment = new AuctionComment();
                            auctionComment.setUser(user);
                            auctionComment.setAuctionComment(String.valueOf(price));

                            AuctionCommentDetail auctionCommentDetail = new AuctionCommentDetail();
                            auctionCommentDetail.setAuctionDetail(auctionDetailModel);
                            auctionCommentDetail.setAuctionComment(auctionComment);

                            AuctionHeader auctionHeaderPost = new AuctionHeader();
                            auctionHeaderPost.setAuctionDetail(auctionDetailModel);
                            auctionHeaderPost.setUser(user);
                            auctionHeaderPost.setAuctionHeaderDateStamp(currDate);
                            addAuctionHeader(auctionHeaderPost, auctionComment, auctionCommentDetail);
                        }
                    });

                    dialogCustom.show();
                }
            });
        }
    }

    public void addAuctionHeader(AuctionHeader auctionToPost, AuctionComment auctionComment, final AuctionCommentDetail auctionCommentDetail){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.POST_AUCTION_HEADER;

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(auctionToPost);

        d("auctionHeader_VOLLEY", mRequestBody);
        int maxLogSize = 2000;
        for(int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("addAuctionHeader", mRequestBody.substring(start, end));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("onResponse addSwapH", "inside");
                Log.i("AddSwapHeader", response);
                AuctionHeader auctionHeaderModelMod = gson.fromJson(response, AuctionHeader.class);

                addAuctionComment(auctionCommentDetail);
//                updateSwap("Request", swapHeaderModelMod);
//                Intent intent = new Intent(SwapBookChooser.this, NotificationAct.class);
//                startActivity(intent);


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

    public void addAuctionComment(AuctionCommentDetail auctionCommentDetail){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.POST_AUCTION_COMMENT_DETAIL;

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(auctionCommentDetail);

        Log.d("swapHeader_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse addSwapC", "inside");
                Log.i("addAuctionComment", response);
//                Intent intent = new Intent(ViewAuctionBook.this, MyShelf.class);
//                startActivity(intent);

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

    public void getRatings(int bookOwnerId){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_RATINGS+"/"+bookOwnerId;
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

        final RentalHeader rentalHeader =new RentalHeader();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalHeader);


        d("LOG_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("ViewBookResponse", response);

                Float fl = Float.parseFloat(response);

                mRating.setRating(fl);

//                d("RatingAdapter: "+rentalDetailModel.getBookOwner().getBookObj().getBookTitle(), String.valueOf(fl));
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
        // Handle navigation view item clicks here.
        d("onNavigationItem", "inside");

        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(ViewAuctionBook.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(ViewAuctionBook.this, ProfileActivity.class);
//            User user = userModel;
//            Log.d("User Id", String.valueOf(userModel.getUserId()));
//            Log.d("User name", userModel.getUserFname()+" "+userModel.getUserLname());
            Bundle bundlePass = new Bundle();
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(ViewAuctionBook.this).getObject("USER_OBJECT", User.class);
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtras(bundlePass);
            startActivity(intent);
        } else if (id == R.id.shelf) {
            Intent intent = new Intent(ViewAuctionBook.this, MyShelf.class);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(ViewAuctionBook.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            Intent intent = new Intent(ViewAuctionBook.this, TransactionActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {
            Intent intent = new Intent(ViewAuctionBook.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(ViewAuctionBook.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ViewAuctionBook.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book2);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAuctionBidClick(Uri uri) {

    }

    @Override
    public void onCountdownOnClick(Uri uri) {

    }
}
