package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    int catPos, daysForRent, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_owner);

        catPos = 0;
        daysForRent = 0;
        year = 0;

        category = "";
        if(getIntent().getExtras().getSerializable("bookPass")!=null){
            bookModel = (Book) getIntent().getExtras().getSerializable("bookPass");
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

//        mSpinnerCat = (Spinner) findViewById(R.id.spinnerCat);
//        mSpinnerDays = (Spinner) findViewById(R.id.spinnerDays);


        mBookTitle.setText(bookModel.getBookTitle());
        mBookDescription.setText(bookModel.getBookDescription());
        Picasso.with(this).load(bookModel.getBookFilename()).fit().into(mBookPic);


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
            }
            Log.d("authorName if", "inside");
            Log.d("authorName if", author);
        }
        mBookAuthor.setText(author);
        mPrice.setText(String.valueOf(calculatePrice()));

        mBtnAddBO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookOwnerModel.setStatusDescription(mBookCondition.getText().toString());
                bookOwnerModel.setBookObj(bookModel);
                bookOwnerModel.setUserObj(user);
                bookOwnerModel.setNoRenters(0);
//                if(catPos==0){
//                    rentalDetail.setBookOwner(bookOwnerModel);
//                    rentalDetail.setDaysForRent(daysForRent);
//                    rentalDetail.setCalculatedPrice(calculatePrice());
//                    addBook(true);
//                }else if(catPos==1){
//                    mSpinnerDays.setEnabled(false);
//                    swapDetail.setBookOwner(bookOwnerModel);
//                    swapDetail.setSwapDescription(mBookCondition.getText().toString());
//                    swapDetail.setSwapPrice(calculatePrice());
//                    @SuppressLint({"NewApi", "LocalSuppress"})
//                    String date = new android.icu.text.SimpleDateFormat("yyyy-MM-dd").format(new Date());
//                    swapDetail.setSwapTimeStamp(date);
//                    addBook(false);
//                }
                addBook();
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                Bundle bundlePass = new Bundle();
                User userModel = new User();
                userModel = (User) SPUtility.getSPUtil(getApplicationContext()).getObject("USER_OBJECT", User.class);
                bundlePass.putSerializable("userModelPass", userModel);
                intent.putExtras(bundlePass);
                startActivity(intent);
            }
        });

//        mSpinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("selectedCat", String.valueOf(position));
//                if(position==0){
//                    category = "Rent";
//                }else if(position==1){
//                    category="Swap";
//                }else{
//                    category = "Auction";
//                }
//                catPos = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//    mSpinnerDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        daysForRent = position+1;
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
//});
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
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


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
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_BOOK;


        Log.d("bookDate", bookOwnerModel.getDateBought());
//        user.setDayTimeModel();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookOwnerModel.getBookObj());


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("addBook", response);

                Book book = gson.fromJson(response, Book.class);

                addBookOwner(book);
//                if(bool==true){
//                    addRentalDetail(book);
//                }else{
//                    addBookOwner(book);
//                }


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


    private void addRentalDetail(Book bookId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_RENTAL_DETAIL;

        rentalDetail.getBookOwner().setBookObj(bookId);
//        user.setDayTimeModel();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(rentalDetail);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("rentalDetailAddLog", response);

                Intent intent = new Intent(AddBookOwner.this, MyShelf.class);
                startActivity(intent);
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
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_BOOK_OWNER;


        bookOwnerModel.setBookObj(book);
        bookOwnerModel.setStatus("none");

        Log.d("bookDate", bookOwnerModel.getDateBought());
//        user.setDayTimeModel();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookOwnerModel);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("addBookOwner", response);

                BookOwnerModel bookOwnerModel1 = gson.fromJson(response, BookOwnerModel.class);

//                swapDetail.setBookOwner(bookOwnerModel1);

                Log.d("bookOwnerAdd", "");

//                addSwapDetail(swapDetail);
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

    private void addSwapDetail(SwapDetail swapDetailModel) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_SWAP_DETAIL;


//        user.setDayTimeModel();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapDetailModel);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("swapDetailAddLog", response);
                Intent inten = new Intent(AddBookOwner.this, MyShelf.class);
                startActivity(inten);
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
