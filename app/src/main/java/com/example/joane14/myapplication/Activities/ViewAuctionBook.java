package com.example.joane14.myapplication.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.util.Log.d;

public class ViewAuctionBook extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AuctionBidFragment.OnAuctionBidInteractionListener,
        CountdownFrag.OnCountdownInteractionListener {

    AuctionDetailModel auctionDetailModel;
    RatingBar mRating;
    TextView mRenters;
    Float priceCompare;
    List<AuctionComment> auctionHeaderModelMod;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_auction_book);

        Log.d("ViewAuction", "inside");

        auctionHeaderModelMod = new ArrayList<AuctionComment>();

        priceCompare = Float.valueOf(0);

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
        TextView mTitle = (TextView) findViewById(R.id.vbTitle);
        Button mViewBtn = (Button) findViewById(R.id.btnVbViewOwner);
        Button mRentBtn = (Button) findViewById(R.id.btnVbRent);
        TextView mContent = (TextView) findViewById(R.id.vbContent);
        TextView mCondition = (TextView) findViewById(R.id.vbCondition);
        ImageView mBookImg = (ImageView) findViewById(R.id.vbBookPic);
        LinearLayout buttonLinear = (LinearLayout) findViewById(R.id.button_ll);
        LinearLayout priceLinear = (LinearLayout) findViewById(R.id.linearPriceLI);

        mRating = (RatingBar) findViewById(R.id.vbRating);

        if (getIntent().getExtras().getSerializable("auctionBook") != null) {
            auctionDetailModel = new AuctionDetailModel();
            auctionDetailModel = (AuctionDetailModel) getIntent().getExtras().getSerializable("auctionBook");

            priceCompare = auctionDetailModel.getStartingPrice();

            FrameLayout containerForCounter = (FrameLayout) findViewById(R.id.fragment_bid_container);

            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle bundle = new Bundle();
            FragmentTransaction ft = fragmentManager.beginTransaction();

                bundle.putSerializable("auctionBook", auctionDetailModel);
                CountdownFrag cdf = CountdownFrag.newInstance(bundle);
                ft.replace(R.id.countdown_container, cdf);


            bundle.putSerializable("auctionComment", auctionDetailModel);
            AuctionBidFragment abf = AuctionBidFragment.newInstance(bundle);
            ft.replace(R.id.fragment_bid_container, abf);
            ft.commit();


            mTitle.setText(auctionDetailModel.getBookOwner().getBookObj().getBookTitle());

            mRating.setRating(Float.parseFloat(String.valueOf(auctionDetailModel.getBookOwner().getRate())));
//
            priceLinear.setVisibility(View.GONE);
//            mHeaderRP.setText("Starting Price");
            mRPrice.setText(String.valueOf(auctionDetailModel.getStartingPrice()));
//            mRPrice.setVisibility(View.GONE);

            final User user = (User) SPUtility.getSPUtil(ViewAuctionBook.this).getObject("USER_OBJECT", User.class);

            if (user.getUserId() == auctionDetailModel.getBookOwner().getUserObj().getUserId()) {
                buttonLinear.setVisibility(View.GONE);
            }

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
                    getMaximumBid(auctionDetailModel);
                    if (auctionDetailModel.getAuctionStatus().equals("start")) {

                        final Dialog dialogCustom = new Dialog(ViewAuctionBook.this);
                        LayoutInflater inflater = (LayoutInflater) ViewAuctionBook.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.seekbar_custom_dialog, (ViewGroup) findViewById(R.id.seekbar_layout));
                        dialogCustom.setContentView(layout);

                        Button mBtnOkay = (Button) layout.findViewById(R.id.btnOkay);
                        Button mBtnCancel = (Button) layout.findViewById(R.id.btnCancel);

                        final TextView mPriceBar = (TextView) layout.findViewById(R.id.tvPriceBar);
                        final ProgressBar mProgBar = (ProgressBar) layout.findViewById(R.id.progBar);
                        final SeekBar mSeekBar = (SeekBar) layout.findViewById(R.id.seekbarPrice);

                        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                getMaximumBid(auctionDetailModel);

                                if (auctionHeaderModelMod.isEmpty()) {
                                    progress += Math.round(auctionDetailModel.getStartingPrice());
                                } else {
                                    progress += auctionHeaderModelMod.get(0).getAuctionComment();
                                }
                                mProgBar.setProgress(progress);
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


                                int price = mSeekBar.getProgress() + Math.round(auctionDetailModel.getStartingPrice());
                                Log.d("SeekBarPrice", String.valueOf(price));

                                AuctionComment auctionComment = new AuctionComment();
                                auctionComment.setUser(user);
                                auctionComment.setAuctionComment(price);

                                AuctionCommentDetail auctionCommentDetail = new AuctionCommentDetail();
                                auctionCommentDetail.setAuctionDetail(auctionDetailModel);
                                auctionCommentDetail.setAuctionComment(auctionComment);

                                AuctionHeader auctionHeaderPost = new AuctionHeader();
                                auctionHeaderPost.setAuctionDetail(auctionDetailModel);
                                auctionHeaderPost.setUser(user);
                                auctionHeaderPost.setAuctionHeaderDateStamp(currDate);
                                addAuctionHeader(auctionHeaderPost, auctionComment, auctionCommentDetail);

                                dialogCustom.dismiss();
                            }
                        });

                        mBtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogCustom.dismiss();
                            }
                        });

                        dialogCustom.show();

                    }else if(auctionDetailModel.getAuctionStatus().equals("stop")){
                        AlertDialog ad = new AlertDialog.Builder(ViewAuctionBook.this).create();
                        ad.setTitle("ALERT!");
                        ad.setMessage("You can't bid because the Aution already ended.");
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

    public void getMaximumBid(AuctionDetailModel auctionDetailModel) {
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
        } else if (id == R.id.shelf) {
            Intent intent = new Intent(ViewAuctionBook.this, MyShelf.class);
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
    public void onAuctionBidClick(Uri uri) {

    }

    @Override
    public void onCountdownOnClick(Uri uri) {

    }
}
