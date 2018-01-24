package com.example.joane14.myapplication.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.joane14.myapplication.Adapters.LandingPageAdapter;
import com.example.joane14.myapplication.Adapters.SwapCommentsAdapter;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.DisplaySwapComments;
import com.example.joane14.myapplication.Fragments.PreferencesFrag;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapCommentDetail;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;
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

import static com.example.joane14.myapplication.R.id.imageView;

public class ViewBookSwapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        DisplaySwapComments.OnSwapCommentInteractionListener{

    SwapDetail swapDetailObj;
    SwapCommentDetail swapCommentDetail;
    Bundle bundle;
    TextView mBookTitle, mAuthor, mPrice, mBookOwner, mDes;
    Button mBookSwap;
    ImageView mBookImg, mBookOwnerImg;
    List<SwapComment> swapCommentList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FragmentManager fragmentManager;
    SwapComment swapComment;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book_swap);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarViewBookSwap);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book_swap);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ViewBookSwapActivity.this);


        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);


        View hView = navigationView.getHeaderView(0);
        TextView mName = (TextView) hView.findViewById(R.id.tvName);
        TextView mEmail = (TextView) hView.findViewById(R.id.tvEmail);
        ImageView profileImg = (ImageView) hView.findViewById(R.id.profPic);

        if(SPUtility.getSPUtil(this).contains("USER_OBJECT")){
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
            mName.setText(userModel.getUserFname()+" "+userModel.getUserLname());
            mEmail.setText(userModel.getEmail());
            Picasso.with(ViewBookSwapActivity.this).load(userModel.getImageFilename()).fit().into(profileImg);
        }


        mBookTitle = (TextView) findViewById(R.id.vbsBookTitle);
        mAuthor = (TextView) findViewById(R.id.vbsBookAuthor);
        mPrice = (TextView) findViewById(R.id.vbsBookPrice);
        mDes = (TextView) findViewById(R.id.swapBookDesc);

        mBookSwap = (Button) findViewById(R.id.btnSwap);

        mBookOwner = (TextView) findViewById(R.id.vbsBookOwner);

        mBookOwnerImg = (ImageView) findViewById(R.id.vbsBookOwnerIgm);
        mBookImg = (ImageView) findViewById(R.id.vbsBookImg);


        mBookSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = new User();
                user = (User) SPUtility.getSPUtil(ViewBookSwapActivity.this).getObject("USER_OBJECT", User.class);

                if(user.getUserId()==swapDetailObj.getBookOwner().getUserObj().getUserId()){
                    showWarning();
                }else{
                    getRecommendSwap();
                }
            }
        });

        String author = "";
        if(getIntent().getExtras()!=null){
            bundle = getIntent().getExtras();
            swapDetailObj = (SwapDetail) bundle.getSerializable("ViewSwap");

            if(swapDetailObj==null){
                Log.d("rentalDetail", "is empty");
            }else{
                Log.d("bundle", "is not empty");
                bundle.putSerializable("swapComment", swapDetailObj);
                fragmentManager = getSupportFragmentManager();
                DisplaySwapComments displaySwapComments = DisplaySwapComments.newInstance(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragement_display_comment, displaySwapComments, displaySwapComments.getTag());
                fragmentTransaction.commit();

                Log.d("RentalBookTitle", swapDetailObj.getBookOwner().getBookObj().getBookTitle());
                mBookTitle.setText(swapDetailObj.getBookOwner().getBookObj().getBookTitle());
                if(swapDetailObj.getBookOwner().getBookObj().getBookAuthor().size()!=0){
                    for(int init=0; init<swapDetailObj.getBookOwner().getBookObj().getBookAuthor().size(); init++){
                        if(!(swapDetailObj.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                            author+=swapDetailObj.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                            if(!(swapDetailObj.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                                author+=swapDetailObj.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                                if(init+1<swapDetailObj.getBookOwner().getBookObj().getBookAuthor().size()){
                                    author+=", ";
                                }
                            }
                        }
                    }
                }else{
                    author="Unknown Author";
                }
                mDes.setText(swapDetailObj.getSwapDescription());
                Log.d("RentalAuthor", author);
                mAuthor.setText(author);
                mPrice.setText(swapDetailObj.getBookOwner().getBookObj().getBookOriginalPrice().toString());
                Log.d("SwapDetail Id", String.valueOf(swapDetailObj.getSwapDetailId()));
                Glide.with(ViewBookSwapActivity.this).load(swapDetailObj.getBookOwner().getBookObj().getBookFilename()).fitCenter().into(mBookImg);
                Picasso.with(ViewBookSwapActivity.this).load(swapDetailObj.getBookOwner().getUserObj().getImageFilename()).fit().into(mBookOwnerImg);
                mBookOwner.setText(swapDetailObj.getBookOwner().getUserObj().getUserFname()+" "+swapDetailObj.getBookOwner().getUserObj().getUserLname());
                makeTextViewResizable(mDes, 5, "See More", true);
            }

            Log.d("bundle", "is not empty");
        }else{
            Log.d("bundle", "is empty");
        }
    }

    public static void makeTextViewResizable(final TextView textView, final int maxLine, final String expandText, final boolean viewMore){
        Log.d("makeTextViewResizable","inside");
        if(textView.getTag() == null){
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

                if(maxLine == 0){
                    lineEndText = textView.getLayout().getLineEnd(0);
                    text = textView.getText().subSequence(0, lineEndText - expandText.length()-1)+" "+ expandText;
                }else if(maxLine > 0 && textView.getLineCount() >= maxLine){
                    lineEndText = textView.getLayout().getLineEnd(maxLine - 1);
                    text = textView.getText().subSequence(0, lineEndText - expandText.length()-1)+" "+ expandText;
                }else{
                    lineEndText = textView.getLayout().getLineEnd(textView.getLayout().getLineCount()-1);
                    text = textView.getText().subSequence(0, lineEndText)+" "+ expandText;
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

    public void showWarning(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ViewBookSwapActivity.this);
        alertDialogBuilder.setTitle("!!!");
        alertDialogBuilder.setMessage("You can't swap your own book.");
        alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showInputDialog() {
        swapCommentDetail = new SwapCommentDetail();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_input_swap, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.disSwapDescription);
        final TextView tv = (TextView) dialogView.findViewById(R.id.disBookTitle);

        tv.setText(swapDetailObj.getBookOwner().getBookObj().getBookTitle());

        dialogBuilder.setTitle("Leave Message");
        dialogBuilder.setPositiveButton("Swap", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("InsideSwap", "dialog");
                user = new User();
                user = (User) SPUtility.getSPUtil(ViewBookSwapActivity.this).getObject("USER_OBJECT", User.class);
                swapComment = new SwapComment();
                swapComment.setUser(user);
                swapComment.setSwapComment(edt.getText().toString());

                swapCommentDetail.setSwapDetail(swapDetailObj);
                swapCommentDetail.setSwapComment(swapComment);
                addSwapComment();
                if(swapComment==null){
                    Log.d("SwapComment", "is null");
                }else{
                    Log.d("SwapComment", swapComment.toString());
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void getRecommendSwap(){
        RequestQueue requestQueue = Volley.newRequestQueue(ViewBookSwapActivity.this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        User user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        String URL = Constants.RECOMMEND_SWAP_BOOK+swapDetailObj.getBookOwner().getUserObj().getUserId()+"/"+swapDetailObj.getBookOwner().getBookObj().getBookOriginalPrice();

//        String URL = Constants.RECOMMEND_SWAP_BOOK+"/"+swapComment.getUser().getUserId()+"/"+swapDetailObj.getSwapPrice();

        Log.d("URLprice", URL);
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapDetailObj);


        Log.d("LOG_VOLLEY", mRequestBody);
        Log.d("LOG_VOLLEY rentalHeader", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("SwapDetailResponse", response);
                if(response.equals("")){
                    Log.i("SwapDetailResponse", "null");
                }else{
                    Log.i("SwapDetailResponse", "not null");
                }
                if(response==null){
                    android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ViewBookSwapActivity.this);
                    alertDialogBuilder.setTitle("!!!");
                    alertDialogBuilder.setMessage("You have no books available for swap that has the same or is around the price of this book.");
                    alertDialogBuilder.setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{
                    showInputDialog();
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

    public void addSwapComment(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://192.168.1.6:8080/Koobym/swapHeader/add";
        String URL = Constants.POST_SWAP_COMMENT_DETAIL;


        SwapComment swapCommentToPost = new SwapComment();
        swapCommentToPost.setSwapComment(swapComment.getSwapComment());
        swapCommentToPost.setUser(swapComment.getUser());

        SwapCommentDetail swapCommentDetailToPost = new SwapCommentDetail();
        swapCommentDetailToPost.setSwapComment(swapCommentToPost);
        swapCommentDetailToPost.setSwapDetail(swapDetailObj);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapCommentDetailToPost);

        Log.d("swapHeader_VOLLEY", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse addSwapC", "inside");
                Log.i("addSwapComment", response);
                Intent intent = new Intent(ViewBookSwapActivity.this, MyShelf.class);
                startActivity(intent);

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

    public void showWarning(final int position){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(ViewBookSwapActivity.this);
        alertDialogBuilder.setTitle("!!!");
        alertDialogBuilder.setMessage("You can't swap your own book.");
        alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d("onNavigationItem", "inside");

        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(ViewBookSwapActivity.this, LandingPage.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromRegister", false);
            intent.putExtra("user", bundle);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(ViewBookSwapActivity.this, ProfileActivity.class);
//            User user = userModel;
//            Log.d("User Id", String.valueOf(userModel.getUserId()));
//            Log.d("User name", userModel.getUserFname()+" "+userModel.getUserLname());
            Bundle bundlePass = new Bundle();
            User userModel = new User();
            userModel = (User) SPUtility.getSPUtil(ViewBookSwapActivity.this).getObject("USER_OBJECT", User.class);
            bundlePass.putSerializable("userModelPass", userModel);
            intent.putExtras(bundlePass);
            startActivity(intent);
        } else if (id == R.id.shelf) {
            Intent intent = new Intent(ViewBookSwapActivity.this, MyShelf.class);
            startActivity(intent);
        } else if (id == R.id.history) {
            Intent intent = new Intent(ViewBookSwapActivity.this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.transaction) {
            Intent intent = new Intent(ViewBookSwapActivity.this, TransactionActivity.class);
            startActivity(intent);
        } else if (id == R.id.request) {
            Intent intent = new Intent(ViewBookSwapActivity.this, RequestActivity.class);
            startActivity(intent);
        } else if (id == R.id.signOut) {
            SPUtility.getSPUtil(ViewBookSwapActivity.this).clear();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(ViewBookSwapActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_view_book_swap);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        MenuItem item = menu.findItem(R.id.action_notifications);
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSwapCommentOnClick(Uri uri) {

    }
}
