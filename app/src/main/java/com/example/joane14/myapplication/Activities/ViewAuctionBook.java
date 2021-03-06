package com.example.joane14.myapplication.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.joane14.myapplication.Class.BadgeDrawable;
import com.example.joane14.myapplication.Fragments.AuctionBidFragment;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.CountdownFrag;
import com.example.joane14.myapplication.Fragments.MapLandingPage;
import com.example.joane14.myapplication.Model.AuctionComment;
import com.example.joane14.myapplication.Model.AuctionCommentDetail;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.AuctionHeader;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.PlacesUtility;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.util.Log.d;

public class
ViewAuctionBook extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AuctionBidFragment.OnAuctionBidInteractionListener,
        CountdownFrag.OnCountdownInteractionListener {

    AuctionDetailModel auctionDetailModel;
    RatingBar mRating;
    TextView mRenters;
    TextView mPriceBar;
    Float priceCompare;
    boolean flag;
    int priceNiya;
    List<AuctionComment> auctionHeaderModelMod;
    LayerDrawable icon;
    int countBadge;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_auction_book);

        Log.d("ViewAuction", "inside");

        auctionHeaderModelMod = new ArrayList<AuctionComment>();

        priceCompare = Float.valueOf(0);
        priceNiya = 0;
        flag = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarViewBookAuction);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Book");


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

        getNotificationCount();

        if (SPUtility.getSPUtil(this).contains("USER_OBJECT")) {
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname() + " " + userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(ViewAuctionBook.this).load(userModel.getImageFilename()).fit().into(profileImg);
        }

        mRenters = (TextView) findViewById(R.id.vbRenters);
        TextView mAuthor = (TextView) findViewById(R.id.vbAuthor);
        TextView mHeaderLP = (TextView) findViewById(R.id.headerLP);
        TextView mHeaderRP = (TextView) findViewById(R.id.heaedrRP);
        TextView mLPrice = (TextView) findViewById(R.id.vbLockInP);
        TextView mRPrice = (TextView) findViewById(R.id.vbRentalP);
        TextView mGenre = (TextView) findViewById(R.id.vbGenre);
        TextView mTitle = (TextView) findViewById(R.id.vbTitle);
        Button mViewBtn = (Button) findViewById(R.id.btnVbViewOwner);
        Button mRentBtn = (Button) findViewById(R.id.btnVbRent);
        TextView mContent = (TextView) findViewById(R.id.vbContent);
        TextView mCondition = (TextView) findViewById(R.id.vbCondition);
        ImageView mBookImg = (ImageView) findViewById(R.id.vbBookPic);
        LinearLayout buttonLinear = (LinearLayout) findViewById(R.id.button_ll);
        LinearLayout priceLinear = (LinearLayout) findViewById(R.id.linearPriceLI);
        FrameLayout fl = (FrameLayout) findViewById(R.id.countdown_container);

        mRating = (RatingBar) findViewById(R.id.vbRating);

        if (getIntent().getExtras().getSerializable("auctionBook") != null) {

            auctionDetailModel = new AuctionDetailModel();
            auctionDetailModel = (AuctionDetailModel) getIntent().getExtras().getSerializable("auctionBook");

            priceCompare = auctionDetailModel.getStartingPrice();
            Bundle bundle = new Bundle();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            Log.d("stat", auctionDetailModel.getStatus());
            if(auctionDetailModel.getAuctionStatus().equals("start")){
                FragmentTransaction ft1 = fragmentManager.beginTransaction();
                bundle.putSerializable("auctionBook", auctionDetailModel);
                CountdownFrag cdf = CountdownFrag.newInstance(bundle);
                ft1.replace(R.id.countdown_container, cdf);
                ft1.commit();
            }else{
                fl.setVisibility(View.GONE);
                buttonLinear.setVisibility(View.GONE);
            }


            bundle.putSerializable("auctionComment", auctionDetailModel);
            AuctionBidFragment abf = AuctionBidFragment.newInstance(bundle);
            ft.replace(R.id.fragment_bid_container, abf);
            ft.commit();


            mTitle.setText(auctionDetailModel.getBookOwner().getBookObj().getBookTitle());

            mRating.setRating(Float.parseFloat(String.valueOf(auctionDetailModel.getBookOwner().getRate())));
            priceLinear.setVisibility(View.GONE);
            mRPrice.setText(String.valueOf(auctionDetailModel.getStartingPrice()));

            final User user = (User) SPUtility.getSPUtil(ViewAuctionBook.this).getObject("USER_OBJECT", User.class);

            if (user.getUserId() == auctionDetailModel.getBookOwner().getUserObj().getUserId()) {
                buttonLinear.setVisibility(View.GONE);
            }

            String genreStr = "";
            int genreSize = auctionDetailModel.getBookOwner().getBookObj().getBookGenre().size();
            if(genreSize>1){
                for(int init=0;init<genreSize; init++){
                    genreStr = genreStr + auctionDetailModel.getBookOwner().getBookObj().getBookGenre().get(init).getGenreName();
                    if(genreSize-1>init){
                        genreStr = genreStr + ", ";
                    }
                }
            }else{
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
            makeTextViewResizable(mContent, 5, "See More", true);

            getRatings(auctionDetailModel.getBookOwner().getBookOwnerId());
            Log.d("SwapComment", "Display");

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
                    Log.d("BidTriggered", auctionDetailModel.getAuctionStatus());

                    if (auctionDetailModel.getAuctionStatus().equals("start")) {
                        if(auctionDetailModel.getBookOwner().getUserObj().getUserId()==user.getUserId()){
                            AlertDialog ad = new AlertDialog.Builder(ViewAuctionBook.this).create();
                            ad.setTitle("Alert!");
                            ad.setMessage("You cannot bid your own book.");
                            ad.setButton(AlertDialog.BUTTON_POSITIVE, "Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            ad.show();
                        }else{

                            final Dialog dialogCustom = new Dialog(ViewAuctionBook.this);
                            LayoutInflater inflater = (LayoutInflater) ViewAuctionBook.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.seekbar_custom_dialog, (ViewGroup) findViewById(R.id.seekbar_layout));
                            dialogCustom.setContentView(layout);

                            Button mBtnOkay = (Button) layout.findViewById(R.id.btnOkay);
                            Button mBtnCancel = (Button) layout.findViewById(R.id.btnCancel);

                            mPriceBar = (TextView) layout.findViewById(R.id.tvPriceBar);
                            final SeekBar mSeekBar = (SeekBar) layout.findViewById(R.id.seekbarPrice);
                            getMaximumBid(auctionDetailModel);

                            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                    Log.d("startProgress", progress+"");

                                    progress = progress+1;
                                    if (flag==false) {
                                        Log.d("startProgress If", progress+Math.round(auctionDetailModel.getStartingPrice())+"");
                                        progress = progress + Math.round(auctionDetailModel.getStartingPrice());
                                    } else {
                                        progress = progress + auctionHeaderModelMod.get(0).getAuctionComment();
                                        Log.d("startProgress Else", progress+auctionHeaderModelMod.get(0).getAuctionComment()+"");
                                    }

                                    priceNiya = progress;

                                    mPriceBar.setText("" + progress);
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


                                    Log.d("SeekBarPrice", priceNiya+"");

                                    AuctionComment auctionComment = new AuctionComment();
                                    auctionComment.setUser(user);
                                    auctionComment.setAuctionComment(priceNiya);

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

                            mBtnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogCustom.dismiss();
                                }
                            });

                            dialogCustom.show();

                        }

                    }else if(auctionDetailModel.getAuctionStatus().equals("stop")){
                        AlertDialog ad = new AlertDialog.Builder(ViewAuctionBook.this).create();
                        ad.setTitle("Alert!");
                        ad.setMessage("You can't bid because the Auction has already ended.");
                        ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        ad.show();
                    }else if(auctionDetailModel.getAuctionStatus().equals("pending")){
                        AlertDialog ad = new AlertDialog.Builder(ViewAuctionBook.this).create();
                        ad.setTitle("Alert!");
                        ad.setMessage("You can't bid because the Auction has not yet started.");
                        ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        ad.show();
                    }

                }
            });
        }
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
                        makeTextViewResizable(tv, 3, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public void addAuctionHeader(AuctionHeader auctionToPost, AuctionComment auctionComment, final AuctionCommentDetail auctionCommentDetail) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.POST_AUCTION_HEADER;

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(auctionToPost);

        d("auctionHeader_VOLLEY", mRequestBody);
        int maxLogSize = 2000;
        for (int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
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

    public void getMaximumBid(final AuctionDetailModel auctionDetailModel) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.GET_MAXIMUM_BID + auctionDetailModel.getAuctionDetailId();

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(auctionDetailModel);

        d("maximumBid_VOLLEY", mRequestBody);
        int maxLogSize = 2000;
        for (int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("maximumBid", mRequestBody.substring(start, end));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                d("onResponse maxBid", "inside");
                Log.i("MaximumBid", response);

                auctionHeaderModelMod.clear();
                auctionHeaderModelMod.addAll(Arrays.asList(gson.fromJson(response, AuctionComment[].class)));


                if(auctionHeaderModelMod.size()==0){
                    Log.d("walaySulodAngHeader", "true");
                    mPriceBar.setText(Math.round(auctionDetailModel.getStartingPrice())+1+"");
                    flag = false;
                }else{
                    Log.d("walaySulodAngHeader", "false");
                    mPriceBar.setText(auctionHeaderModelMod.get(0).getAuctionComment()+1+"");
                    flag = true;
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

    public void addAuctionComment(AuctionCommentDetail auctionCommentDetail) {
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
                Intent intent = new Intent(ViewAuctionBook.this, ViewAuctionBook.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("auctionBook",auctionDetailModel);
                intent.putExtras(bundle);
                startActivity(intent);
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

    public void getRatings(int bookOwnerId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        d("UserIdReceive", String.valueOf(user.getUserId()));
        String URL = Constants.GET_RATINGS + "/" + bookOwnerId;
//        String URL = Constants.WEB_SERVICE_URL+"user/add";

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
        } else if (id == R.id.history) {
            Intent intent = new Intent(ViewAuctionBook.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            Intent intent = new Intent(ViewAuctionBook.this, BookActActivity.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book_auction);
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
                Intent intent = new Intent(ViewAuctionBook.this, SearchResult.class);
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
                Intent intent = new Intent(ViewAuctionBook.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(ViewAuctionBook.this, NotificationAct.class);
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
                setBadgeCount(ViewAuctionBook.this, count);

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
    public void onAuctionBidClick(Uri uri) {

    }

    @Override
    public void onCountdownOnClick(Uri uri) {

    }
}
