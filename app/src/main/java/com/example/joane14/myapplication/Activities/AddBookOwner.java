package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.*;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddBookOwner extends AppCompatActivity {

    Book bookModel;
    BookOwnerModel bookOwnerModel;
    TextView mBookTitle, mBookAuthor, mBookDescription, mPrice;
    ImageView mBookPic;
    EditText mBookCondition, mDate;
    Button mBtnAddBO;
    Spinner mSpinnerCat, mSpinnerDays;
    User user;
    RentalDetail rentalDetail;
    SwapDetail swapDetail;
    private Calendar calendar;
    DatePickerDialog.OnDateSetListener date;
    String category, dateBought;
    CheckBox mCb1, mCb2, mCb3, mCb4, mCb5, mCb6, mCb7;
    Boolean mCb1Bool, mCb2Bool, mCb3Bool, mCb4Bool, mCb5Bool, mCb6Bool, mCb7Bool;
    int catPos, daysForRent, year;
    List<String> bookDescList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_owner);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        bookDescList = new ArrayList<String>();
        mCb1Bool=false;
        mCb2Bool=false;
        mCb3Bool=false;
        mCb4Bool=false;
        mCb5Bool=false;
        mCb6Bool=false;
        mCb7Bool=false;

        catPos = 0;
        daysForRent = 0;
        year = 0;

        category = "";
        if(getIntent().getExtras().getSerializable("bookPass")!=null){
            bookModel = (Book) getIntent().getExtras().getSerializable("bookPass");
            bookModel.setStatus("Not Available");
        }

        dateBought = "";

        calendar = Calendar.getInstance();
        bookOwnerModel = new BookOwnerModel();
        rentalDetail = new RentalDetail();
        swapDetail = new SwapDetail();

        user = new User();
        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);
        mBookTitle = (TextView) findViewById(R.id.addBoTitle);
        mPrice = (TextView) findViewById(R.id.addBoPrice);
        mBookAuthor = (TextView) findViewById(R.id.addBoAuthor);
        mBookDescription = (TextView) findViewById(R.id.addBoDescription);

        mBookCondition = (EditText) findViewById(R.id.addBoCondition);
        mDate = (EditText) findViewById(R.id.addBoDatePublished);

        mBookPic = (ImageView) findViewById(R.id.addBOPic);

        mBtnAddBO = (Button) findViewById(R.id.addBoBookBtn);

        mBookCondition.setEnabled(false);
        mCb1 = (CheckBox) findViewById(R.id.checkDesc1);
        mCb2 = (CheckBox) findViewById(R.id.checkDesc2);
        mCb3 = (CheckBox) findViewById(R.id.checkDesc3);
        mCb4 = (CheckBox) findViewById(R.id.checkDesc4);
        mCb5 = (CheckBox) findViewById(R.id.checkDesc5);
        mCb6 = (CheckBox) findViewById(R.id.checkDesc6);
        mCb7 = (CheckBox) findViewById(R.id.checkDesc7);

        mBookTitle.setText(bookModel.getBookTitle());
        mBookDescription.setText(bookModel.getBookDescription());
        makeTextViewResizable(mBookDescription, 5, "View More", true);
        Picasso.with(this).load(bookModel.getBookFilename()).fit().into(mBookPic);

        mCb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CBDesc", "Good as new");
                if(isChecked==true){
                    bookDescList.add("Good as new");
                }else{
                    for(int init=0; init<bookDescList.size(); init++){
                        if(bookDescList.get(init).equals("Good as new")){
                            bookDescList.remove(init);
                            break;
                        }
                    }
                }
            }
        });

        mCb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CBDesc", "Fine");
                if(isChecked==true){
                    bookDescList.add("Fine");
                }else{
                    for(int init=0; init<bookDescList.size(); init++){
                        if(bookDescList.get(init).equals("Fine")){
                            bookDescList.remove(init);
                            break;
                        }
                    }
                }

            }
        });

        mCb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CBDesc", "Very Good");
                if(isChecked==true){
                    bookDescList.add("Very Good");
                }else{
                    for(int init=0; init<bookDescList.size(); init++){
                        if(bookDescList.get(init).equals("Very Good")){
                            bookDescList.remove(init);
                            break;
                        }
                    }
                }

            }
        });

        mCb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CBDesc", "Poor");
                if(isChecked==true){
                    bookDescList.add("Poor");
                }else{
                    for(int init=0; init<bookDescList.size(); init++){
                        if(bookDescList.get(init).equals("Poor")){
                            bookDescList.remove(init);
                            break;
                        }
                    }
                }

            }
        });

        mCb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CBDesc", "Fair");
                if(isChecked==true){
                    bookDescList.add("Fair");
                }else{
                    for(int init=0; init<bookDescList.size(); init++){
                        if(bookDescList.get(init).equals("Fair")){
                            bookDescList.remove(init);
                            break;
                        }
                    }
                }

            }
        });

        mCb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CBDesc", "Small flaws");
                if(isChecked==true){
                    bookDescList.add("Small flaws");
                }else{
                    for(int init=0; init<bookDescList.size(); init++){
                        if(bookDescList.get(init).equals("Small flaws")){
                            bookDescList.remove(init);
                            break;
                        }
                    }
                }

            }
        });

        mCb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CBDesc", "Others");
                if(isChecked==true){
                    mBookCondition.setEnabled(true);
                }else{
                    if(mBookCondition.getText().length()!=0){
                        for(int init=0;init<bookDescList.size(); init++){
                            if(bookDescList.get(init).equals(mBookCondition.getText().toString())){
                                bookDescList.remove(init);
                                break;
                            }
                        }
                    }
                    mBookCondition.setEnabled(false);
                }
            }
        });

        String author = " ";
        if(bookModel.getBookAuthor()!=null) {
            int size = bookModel.getBookAuthor().size();

            Log.d("inside", "author setTEXT");
            if (size > 1) {
                for (int i = 0; i < size; i++) {
                    author += bookModel.getBookAuthor().get(i).getAuthorFName();
                    Log.d("authorLoop", bookModel.getBookAuthor().get(i).getAuthorFName());
                    if (size - 1 > i) {
                        author += ", ";
                    }
                }
            }else{
                Log.d("authorName", "inside dododo");
                author = bookModel.getBookAuthor().get(0).getAuthorFName() + bookModel.getBookAuthor().get(0).getAuthorLName();
            }
        }
        mBookAuthor.setText(author);
        mPrice.setText("₱ "+String.format("%.2f",calculatePrice()));

        mBtnAddBO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("checkBoxSize", bookDescList.size()+"");
                String bookCond = "";
                if(mCb7.isChecked()==true){
                    if(mBookCondition.getText().length()==0){
                        mBookCondition.setError("Field must not be empty.");
                    }else{
                        bookDescList.add(mBookCondition.getText().toString());

                        int sizeCond = bookDescList.size();
                        Log.d("SizeBook", sizeCond+"");
                        if(sizeCond>1){
                            for(int init=0; init<bookDescList.size(); init++){
                                bookCond = bookCond+bookDescList.get(init);
                                if(sizeCond-1>init){
                                    bookCond = bookCond+"; ";
                                }
                            }
                        }else{
                            bookCond = bookDescList.get(0);
                        }
                        Log.d("bookCond", bookCond);

                        bookModel.setStatus("Not Available");
                        bookOwnerModel.setStatusDescription(bookCond);
                        bookOwnerModel.setBookObj(bookModel);
                        bookOwnerModel.setUserObj(user);
                        bookOwnerModel.setNoRenters(0);
                        addBook();
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        Bundle bundlePass = new Bundle();
                        User userModel = new User();
                        userModel = (User) SPUtility.getSPUtil(getApplicationContext()).getObject("USER_OBJECT", User.class);
                        bundlePass.putSerializable("userModelPass", userModel);
                        intent.putExtras(bundlePass);
                        startActivity(intent);
                    }
                }else if(mDate.getText().length()==0){
                    mDate.setError("Field must not be empty");
                }else{
                    int sizeCond = bookDescList.size();
                    Log.d("SizeBook", sizeCond+"");

                    if(sizeCond>1){
                        Log.d("validate", "If");
                        for(int init=0; init<bookDescList.size(); init++){
                            Log.d("validate", "for");
                            bookCond = bookCond+bookDescList.get(init);
                            if(sizeCond-1>init){
                                bookCond = bookCond + "; ";
                            }
                        }
                    }else if(sizeCond==1){
                        Log.d("validate", "elseIf");
                        bookCond = bookDescList.get(0);
                    }
                    Log.d("bookCondElse", bookCond);

                    bookOwnerModel.setStatusDescription(bookCond);
                    bookOwnerModel.setBookObj(bookModel);
                    bookOwnerModel.setUserObj(user);
                    bookOwnerModel.setNoRenters(0);
                    addBook();
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    Bundle bundlePass = new Bundle();
                    User userModel = new User();
                    userModel = (User) SPUtility.getSPUtil(getApplicationContext()).getObject("USER_OBJECT", User.class);
                    bundlePass.putSerializable("userModelPass", userModel);
                    intent.putExtras(bundlePass);
                    startActivity(intent);
                }
            }
        });

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddBookOwner.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


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
                        makeTextViewResizable(tv, 5, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        bookOwnerModel.setDateBought(sdf.format(calendar.getTime()));
        mDate.setText(sdf.format(calendar.getTime()));
        Log.d("sdf", sdf.format(calendar.getTime()).toString());

        this.dateBought = sdf.format(calendar.getTime());
    }

    public float calculatePrice(){
        float price = 0, depPrice;

        Date date = null;
        depPrice = bookModel.getBookOriginalPrice()/5;

        SimpleDateFormat formate = new SimpleDateFormat("yyyy");

        try {
            date = (formate).parse(bookModel.getBookPublishedDate());
            Log.d("datePila", String.valueOf(date));
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int gap = Calendar.getInstance().get(Calendar.YEAR) - calendar.get(Calendar.YEAR);

        Log.d("gapPila", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        if(gap>=5){
            price = depPrice;
            Log.d("calculatedPric depPrice", String.valueOf(price));
        }else if(gap==0){
            price = bookModel.getBookOriginalPrice();
            Log.d("calculatedPric OrgPri", String.valueOf(price));
        }else{
            price = bookModel.getBookOriginalPrice() - (depPrice*gap);
            Log.d("calculatedPrice dep*gap", String.valueOf(price));
        }

        Log.d("calculatedPrice", String.valueOf(price));
        return price;
    }

    private void addBook() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = Constants.POST_BOOK;

        Log.d("bookDate", bookOwnerModel.getDateBought());
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookOwnerModel.getBookObj());


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("addBook", response);

                Book book = gson.fromJson(response, Book.class);

                addBookOwner(book);

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


    private void addBookOwner(Book book) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = Constants.POST_BOOK_OWNER;


        bookOwnerModel.setBookObj(book);
        bookOwnerModel.setStatus("none");
        bookOwnerModel.setBookStat("Not Available");

        Log.d("bookDate", bookOwnerModel.getDateBought());
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookOwnerModel);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("addBookOwner", response);

                BookOwnerModel bookOwnerModel1 = gson.fromJson(response, BookOwnerModel.class);

                Intent intent = new Intent(AddBookOwner.this, ProfileActivity.class);
                Bundle bundlePass = new Bundle();
                User userModel = new User();
                userModel = (User) SPUtility.getSPUtil(getApplicationContext()).getObject("USER_OBJECT", User.class);
                bundlePass.putSerializable("userModelPass", userModel);
                intent.putExtras(bundlePass);
                startActivity(intent);

                Log.d("bookOwnerAdd", "");

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
}
