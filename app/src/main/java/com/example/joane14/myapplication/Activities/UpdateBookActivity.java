package com.example.joane14.myapplication.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.DayModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.TimeModel;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.util.Log.d;

/**
 * Created by Kimberly Cañedo on 06/10/2017.
 */

public class UpdateBookActivity extends AppCompatActivity {

    RentalDetail rentalDetailModel, rentToPost;
    SwapDetail swapDetailModel, swapToPost;
    AuctionDetailModel auctionDetailModel, auctionToPost;
    boolean boolStartDate, boolEndDate, boolStartTime, boolEndTime;
    TextView mStartTime, mEndTime;
    BookOwnerModel bookOwnerModel;
    ArrayAdapter<String> adapterA;
    User user;
    EditText mDatePub;
    Button mStartDate;
    List<String> mEndDate;
    private Calendar aucDate;
    private java.util.Calendar calendar;
    int chosen;
    DatePickerDialog.OnDateSetListener date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        calendar = java.util.Calendar.getInstance();

        rentalDetailModel = new RentalDetail();
        swapDetailModel = new SwapDetail();
        auctionDetailModel = new AuctionDetailModel();
        rentToPost = new RentalDetail();
        swapToPost = new SwapDetail();
        auctionToPost = new AuctionDetailModel();
        chosen = 0;
        bookOwnerModel = new BookOwnerModel();
        user = new User();
        String author = "";

        boolStartDate = false;
        boolEndDate = false;

        boolStartTime = false;
        boolEndTime = false;

        user = (User) SPUtility.getSPUtil(this).getObject("USER_OBJECT", User.class);

        TextView mBookTitle = (TextView) findViewById(R.id.tvBookTitleUB);
        TextView mAuthor = (TextView) findViewById(R.id.tvAuthorUB);
        TextView mBookDesc = (TextView) findViewById(R.id.tvBookDescUB);
        TextView mBookPrice = (TextView) findViewById(R.id.tvBookPriceUB);
        final EditText mBookCond = (EditText) findViewById(R.id.tvBookCondUB);
        mDatePub = (EditText) findViewById(R.id.tvDatePublishedUB);
        Spinner mBookStat = (Spinner) findViewById(R.id.spinnerStatusUB);
        ImageView mBookImg = (ImageView) findViewById(R.id.ivBookImgUB);
        Button mUpdateBtn = (Button) findViewById(R.id.btnUpdateUB);

        final List<String> statusBook = new ArrayList<String>();

        if(getIntent().getExtras().getSerializable("viewBook")!=null){
            statusBook.add("Swap");
            statusBook.add("Not Advertised");
            statusBook.add("Auction");
            rentalDetailModel = (RentalDetail) getIntent().getExtras().getSerializable("viewBook");

            mBookTitle.setText(rentalDetailModel.getBookOwner().getBookObj().getBookTitle());
            mBookDesc.setText(rentalDetailModel.getBookOwner().getBookObj().getBookDescription());
            mBookPrice.setText(String.valueOf(rentalDetailModel.getBookOwner().getBookObj().getBookOriginalPrice()));
            mBookCond.setText(rentalDetailModel.getBookOwner().getStatusDescription());
            mDatePub.setText(rentalDetailModel.getBookOwner().getDateBought());

            if(rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().size()!=0){
                for(int init=0; init<rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().size(); init++){
                    if(!(rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                        author+=rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                        if(!(rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                            author+=rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if(init+1<rentalDetailModel.getBookOwner().getBookObj().getBookAuthor().size()){
                                author+=", ";
                            }
                        }
                    }
                }
            }else{
                author="Unknown Author";
            }
            mAuthor.setText(author);

            statusBook.remove(0);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateBookActivity.this, android.R.layout.simple_dropdown_item_1line, statusBook);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mBookStat.setAdapter(adapter);
            mBookStat.setSelection(0);

            mBookStat.setBackgroundColor(ContextCompat.getColor(UpdateBookActivity.this, R.color.colorRent));

            mBookStat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    chosen = position;
                    Log.d("chosen-"+position, statusBook.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Glide.with(this).load(rentalDetailModel.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            makeTextViewResizable(mBookDesc, 5, "See More", true);

            mDatePub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(UpdateBookActivity.this, date, calendar
                            .get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH),
                            calendar.get(java.util.Calendar.DAY_OF_MONTH)).show();
                }
            });

            date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    calendar.set(java.util.Calendar.YEAR, year);
                    calendar.set(java.util.Calendar.MONTH, monthOfYear);
                    calendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };

            mUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(statusBook.get(chosen).equals("Swap")){
                        bookOwnerModel = rentalDetailModel.getBookOwner();
                        bookOwnerModel.getBookObj().setStatus("Available");
                        bookOwnerModel.setBookStat("Available");
                        bookOwnerModel.setStatus("Swap");
                        bookOwnerModel.setStatusDescription(mBookCond.getText().toString());
                        bookOwnerModel.setDateBought(mDatePub.getText().toString());
                        updateBookOwner(bookOwnerModel, "Rent");
                    }else if(statusBook.get(chosen).equals("Not Advertised")){
                        bookOwnerModel = rentalDetailModel.getBookOwner();
                        bookOwnerModel.getBookObj().setStatus("Available");
                        bookOwnerModel.setBookStat("Available");
                        bookOwnerModel.setStatus("NotAdvertised");
                        bookOwnerModel.setStatusDescription(mBookCond.getText().toString());
                        bookOwnerModel.setDateBought(mDatePub.getText().toString());
                        updateBookOwner(bookOwnerModel, "Rent");
                    }else if(statusBook.get(chosen).equals("Auction")){
                        bookOwnerModel = rentalDetailModel.getBookOwner();
                        bookOwnerModel.getBookObj().setStatus("Available");
                        bookOwnerModel.setBookStat("Available");
                        bookOwnerModel.setStatus("Auction");
                        customAuctionDialog("Rent");
                    }
                }
            });
        }else if(getIntent().getExtras().getSerializable("swapBook")!=null){
            statusBook.add("Rent");
            statusBook.add("Not Advertised");
            statusBook.add("Auction");

            swapDetailModel = (SwapDetail) getIntent().getExtras().getSerializable("swapBook");

            mBookTitle.setText(swapDetailModel.getBookOwner().getBookObj().getBookTitle());
            mBookDesc.setText(swapDetailModel.getBookOwner().getBookObj().getBookDescription());
            mBookPrice.setText(String.valueOf(swapDetailModel.getBookOwner().getBookObj().getBookOriginalPrice()));
            mBookCond.setText(swapDetailModel.getBookOwner().getStatusDescription());
            mDatePub.setText(swapDetailModel.getBookOwner().getDateBought());

            if(swapDetailModel.getBookOwner().getBookObj().getBookAuthor().size()!=0){
                for(int init=0; init<swapDetailModel.getBookOwner().getBookObj().getBookAuthor().size(); init++){
                    if(!(swapDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                        author+=swapDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                        if(!(swapDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                            author+=swapDetailModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if(init+1<swapDetailModel.getBookOwner().getBookObj().getBookAuthor().size()){
                                author+=", ";
                            }
                        }
                    }
                }
            }else{
                author="Unknown Author";
            }
            mAuthor.setText(author);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateBookActivity.this, android.R.layout.simple_dropdown_item_1line, statusBook);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mBookStat.setAdapter(adapter);
            mBookStat.setSelection(1);

            mBookStat.setBackgroundColor(ContextCompat.getColor(UpdateBookActivity.this, R.color.colorSwap));

            mBookStat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    chosen = position;
                    Log.d("chosen-"+position, statusBook.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Glide.with(this).load(swapDetailModel.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            makeTextViewResizable(mBookDesc, 5, "See More", true);

            mUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(statusBook.get(chosen).equals("Rent")){
                        bookOwnerModel = swapDetailModel.getBookOwner();
                        bookOwnerModel.getBookObj().setStatus("Available");
                        bookOwnerModel.setBookStat("Available");
                        bookOwnerModel.setStatus("Rent");
                        bookOwnerModel.setStatusDescription(mBookCond.getText().toString());
                        bookOwnerModel.setDateBought(mDatePub.getText().toString());
                        customDialog("Swap");
                    }else if(statusBook.get(chosen).equals("Not Advertised")){
                        bookOwnerModel = swapDetailModel.getBookOwner();
                        bookOwnerModel.getBookObj().setStatus("Available");
                        bookOwnerModel.setBookStat("Available");
                        bookOwnerModel.setStatus("none");
                        bookOwnerModel.setStatusDescription(mBookCond.getText().toString());
                        bookOwnerModel.setDateBought(mDatePub.getText().toString());
                        updateBookOwner(bookOwnerModel, "Swap");
                    }else if(statusBook.get(chosen).equals("Auction")){
                        bookOwnerModel = swapDetailModel.getBookOwner();
                        bookOwnerModel.getBookObj().setStatus("Available");
                        bookOwnerModel.setBookStat("Available");
                        bookOwnerModel.setStatus("Auction");
                        bookOwnerModel.setStatusDescription(mBookCond.getText().toString());
                        bookOwnerModel.setDateBought(mDatePub.getText().toString());
                        customAuctionDialog("Swap");
                    }
                }
            });
        }else if(getIntent().getExtras().getSerializable("auctionBook")!=null){
            statusBook.add("Rent");
            statusBook.add("Swap");
            statusBook.add("Not Advertised");

            auctionDetailModel = (AuctionDetailModel) getIntent().getExtras().getSerializable("auctionBook");

            mBookTitle.setText(auctionDetailModel.getBookOwner().getBookObj().getBookTitle());
            mBookDesc.setText(auctionDetailModel.getBookOwner().getBookObj().getBookDescription());
            mBookPrice.setText(String.valueOf(auctionDetailModel.getBookOwner().getBookObj().getBookOriginalPrice()));
            mBookCond.setText(auctionDetailModel.getBookOwner().getStatusDescription());
            mDatePub.setText(auctionDetailModel.getBookOwner().getDateBought());

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

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateBookActivity.this, android.R.layout.simple_dropdown_item_1line, statusBook);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mBookStat.setAdapter(adapter);
            mBookStat.setSelection(3);

            mBookStat.setBackgroundColor(ContextCompat.getColor(UpdateBookActivity.this, R.color.colorAuction));

            mBookStat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    chosen = position;
                    Log.d("chosen-"+position, statusBook.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Glide.with(this).load(auctionDetailModel.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            makeTextViewResizable(mBookDesc, 5, "See More", true);

            mUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(statusBook.get(chosen).equals("Rent")){
                        bookOwnerModel = auctionDetailModel.getBookOwner();
                        bookOwnerModel.getBookObj().setStatus("Available");
                        bookOwnerModel.setBookStat("Available");
                        bookOwnerModel.setStatus("Rent");
                        bookOwnerModel.setStatusDescription(mBookCond.getText().toString());
                        bookOwnerModel.setDateBought(mDatePub.getText().toString());
                        customDialog("Auction");
                    }else if(statusBook.get(chosen).equals("Swap")){
                        BookOwnerModel bookOwnerModel = new BookOwnerModel();
                        bookOwnerModel = auctionDetailModel.getBookOwner();
                        bookOwnerModel.getBookObj().setStatus("Available");
                        bookOwnerModel.setBookStat("Available");
                        bookOwnerModel.setStatus("Swap");
                        bookOwnerModel.setStatusDescription(mBookCond.getText().toString());
                        bookOwnerModel.setDateBought(mDatePub.getText().toString());
                        updateBookOwner(bookOwnerModel, "Auction");
                    }else if(statusBook.get(chosen).equals("Not Advertised")){
                        bookOwnerModel = auctionDetailModel.getBookOwner();
                        bookOwnerModel.getBookObj().setStatus("Available");
                        bookOwnerModel.setBookStat("Available");
                        bookOwnerModel.setStatus("none");
                        bookOwnerModel.setStatusDescription(mBookCond.getText().toString());
                        bookOwnerModel.setDateBought(mDatePub.getText().toString());
                        updateBookOwner(bookOwnerModel, "Auction");
                    }
                }
            });
        }else if(getIntent().getExtras().getSerializable("notAdBook")!=null){
            statusBook.add("Rent");
            statusBook.add("Swap");
            statusBook.add("Auction");

            BookOwnerModel bookOwner = new BookOwnerModel();

            bookOwner = (BookOwnerModel) getIntent().getExtras().getSerializable("notAdBook");

            mBookTitle.setText(bookOwner.getBookObj().getBookTitle());
            mBookDesc.setText(bookOwner.getBookObj().getBookDescription());
            mBookPrice.setText(String.valueOf(bookOwner.getBookObj().getBookOriginalPrice()));
            mBookCond.setText(bookOwner.getStatusDescription());
            mDatePub.setText(bookOwner.getDateBought());

            if(bookOwner.getBookObj().getBookAuthor().size()!=0){
                for(int init=0; init<bookOwner.getBookObj().getBookAuthor().size(); init++){
                    if(!(bookOwner.getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                        author+=bookOwner.getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                        if(!(bookOwner.getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                            author+=bookOwner.getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if(init+1<bookOwner.getBookObj().getBookAuthor().size()){
                                author+=", ";
                            }
                        }
                    }
                }
            }else{
                author="Unknown Author";
            }
            mAuthor.setText(author);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateBookActivity.this, android.R.layout.simple_dropdown_item_1line, statusBook);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mBookStat.setAdapter(adapter);
            mBookStat.setSelection(2);

            mBookStat.setBackgroundColor(ContextCompat.getColor(UpdateBookActivity.this, R.color.colorGray));

            mBookStat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    chosen = position;
                    Log.d("chosen", statusBook.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            final BookOwnerModel finalBookOwner = bookOwner;

            Glide.with(this).load(bookOwner.getBookObj().getBookFilename()).centerCrop().into(mBookImg);
            makeTextViewResizable(mBookDesc, 5, "See More", true);

            mUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(statusBook.get(chosen).equals("Rent")){
                        bookOwnerModel = finalBookOwner;
                        bookOwnerModel.getBookObj().setStatus("Available");
                        bookOwnerModel.setBookStat("Available");
                        bookOwnerModel.setStatus("Rent");
                        bookOwnerModel.setStatusDescription(mBookCond.getText().toString());
                        bookOwnerModel.setDateBought(mDatePub.getText().toString());
                        customDialog("NotAdvertised");
                    }else if(statusBook.get(chosen).equals("Swap")){
                        bookOwnerModel = finalBookOwner;
                        bookOwnerModel.setBookStat("Available");
                        bookOwnerModel.getBookObj().setStatus("Available");
                        bookOwnerModel.setStatus("Swap");
                        bookOwnerModel.setStatusDescription(mBookCond.getText().toString());
                        bookOwnerModel.setDateBought(mDatePub.getText().toString());
                        updateBookOwner(bookOwnerModel, "NotAdvertised");
                    }else if(statusBook.get(chosen).equals("Auction")){
                        bookOwnerModel=finalBookOwner;
                        bookOwnerModel.setBookStat("Available");
                        bookOwnerModel.getBookObj().setStatus("Available");
                        bookOwnerModel.setStatus("Auction");
                        bookOwnerModel.setStatusDescription(mBookCond.getText().toString());
                        bookOwnerModel.setDateBought(mDatePub.getText().toString());
                        customAuctionDialog("NotAdvertised");
                    }
                }
            });
        }
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        bookOwnerModel.setDateBought(sdf.format(calendar.getTime()));
        mDatePub.setText(sdf.format(calendar.getTime()));
        Log.d("sdf", sdf.format(calendar.getTime()).toString());

//        this.dateBought = sdf.format(calendar.getTime());
    }

    public void customDialog(final String status){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.spinner);
        dialog.setTitle("Choose day range:");
        final DayModel day;
        final TimeModel time;


        day = new DayModel();
        time = new TimeModel();

        final List<String> rangeDays = new ArrayList<String>();

        rangeDays.add("1");
        rangeDays.add("2");
        rangeDays.add("3");
        rangeDays.add("4");
        rangeDays.add("5");

        Button mBtnOkay = (Button) dialog.findViewById(R.id.btnOkay);
        Button mBtnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        Spinner mSpinnerDay = (Spinner) dialog.findViewById(R.id.spinnerDay);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, rangeDays);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        if(adapter==null){
            Log.d("apater", "is null");
        }else{
            Log.d("apater", "is not null");
        }
        mSpinnerDay.setAdapter(adapter);

        mSpinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rentToPost.setDaysForRent(Integer.parseInt(rangeDays.get(position)));
                rentToPost.setRentalStatus("Available");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mBtnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBookOwner(bookOwnerModel, status);
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @SuppressLint("NewApi")
    public void customAuctionDialog(final String status){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.auction_custom_dialog);
        final DayModel day;
        final TimeModel time;
        final DatePickerDialog.OnDateSetListener mDatePicker;

        aucDate = Calendar.getInstance();


        day = new DayModel();
        time = new TimeModel();

        mEndDate = new ArrayList<String>();
        Log.d("mEndDate", "labay lang");
        final List<String> dateEnd = new ArrayList<String>();
        final List<String> rangeDays = new ArrayList<String>();


        auctionToPost = new AuctionDetailModel();


        Button mBtnOkay = (Button) dialog.findViewById(R.id.btnOkay);
        Button mBtnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        mStartTime = (TextView) dialog.findViewById(R.id.tvAucStartTime);
        mEndTime = (TextView) dialog.findViewById(R.id.tvAucEndTime);

        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolStartTime = true;
                CreateTimePicker(mStartTime, "start");
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolEndTime = true;
                CreateTimePicker(mEndTime, "end");
            }
        });

        mDatePicker = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                aucDate.set(java.util.Calendar.YEAR, year);
                aucDate.set(java.util.Calendar.MONTH, monthOfYear);
                aucDate.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(myFormat, Locale.US);
                mStartDate.setText(sdf.format(aucDate.getTime()));
                Log.d("startDate", sdf.format(aucDate.getTime()));

                auctionToPost.setStartDate(sdf.format(aucDate.getTime()));


                aucDate.add(java.util.Calendar.DAY_OF_MONTH, 1);
                dateEnd.add(sdf.format(aucDate.getTime()));
                adapterA.notifyDataSetChanged();
                Log.d("endDate 1", sdf.format(aucDate.getTime()));


                aucDate.add(java.util.Calendar.DAY_OF_MONTH, 1);
                dateEnd.add(sdf.format(aucDate.getTime()));
                adapterA.notifyDataSetChanged();
                Log.d("endDate 2", sdf.format(aucDate.getTime()));

                aucDate.add(java.util.Calendar.DAY_OF_MONTH, 1);
                dateEnd.add(sdf.format(aucDate.getTime()));
                adapterA.notifyDataSetChanged();
                Log.d("endDate 3", sdf.format(aucDate.getTime()));
            }
        };

        mStartDate = (Button) dialog.findViewById(R.id.tvAucStartDate);
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolStartDate = true;
                new DatePickerDialog(UpdateBookActivity.this, mDatePicker, aucDate.get(java.util.Calendar.YEAR),
                        aucDate.get(java.util.Calendar.MONTH), aucDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if(dateEnd.size()==0||dateEnd.isEmpty()){
            Log.d("dateEnd", "empty");
        }else{
            Log.d("dateEnd", "not empty");
        }

        adapterA = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dateEnd);
        adapterA.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        Spinner mSpinner = (Spinner) dialog.findViewById(R.id.aucSpinner);
        if(adapterA==null){
            Log.d("apater", "is null");
        }else{
            Log.d("apater", "is not null");
        }
        mSpinner.setAdapter(adapterA);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("AuctionItemSelected", dateEnd.get(position));
                auctionToPost.setEndDate(dateEnd.get(position));
                auctionToPost.setAuctionStatus("pending");
                boolEndDate = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("AuctionItemSelected", "walay selceted");

            }
        });
        mBtnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(boolStartTime==false||boolEndTime==false||boolEndDate==false||boolStartDate==false){
                    AlertDialog ad = new AlertDialog.Builder(UpdateBookActivity.this).create();
                    ad.setTitle("Alert!");
                    ad.setMessage("Fill up all data.");
                    ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }else{
                    updateBookOwner(bookOwnerModel, status);
                }


            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    public double calculatePrice(Book bookModel){
        double price = 0, depPrice;

        Date date = null;
        depPrice = bookModel.getBookOriginalPrice()/5;

        java.text.SimpleDateFormat formate = new java.text.SimpleDateFormat("yyyy");

        try {
            date = (formate).parse(bookModel.getBookPublishedDate());
            Log.d("datePila", String.valueOf(date));
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int gap = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) - calendar.get(java.util.Calendar.YEAR);

        Log.d("gapPila", String.valueOf(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)));
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

    public void CreateTimePicker(final TextView tvSet, final String stat){


        final java.util.Calendar c = java.util.Calendar.getInstance();
        int mHour = c.get(java.util.Calendar.HOUR_OF_DAY);
        int mMinute = c.get(java.util.Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        int hour = hourOfDay;
                        int minutes = minute;
                        String timeSet = "";
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12){
                            timeSet = "PM";
                        }else{
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes ;
                        else
                            min = String.valueOf(minutes);

                        String hours = "";

                        if(hour<10)
                            hours = "0" + hour;
                        else
                            hours = String.valueOf(hour);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(hours).append(':')
                                .append(min ).append(" ").append(timeSet).toString();

                        String timeGiven = "";
                        hourOfDay = Integer.parseInt(String.format("%02d", hourOfDay));
                        minute = Integer.parseInt(String.format("%02d", minute));
                        timeGiven = hourOfDay + ":" + minute;
                        Log.d("time selected Auction", timeGiven);
//
//                        if(pos==0){
//                            mStartTime.setText(aTime);
//                        }else{
//                            mEndTime.setText(aTime);
//                        }
//
                        tvSet.setText(aTime);

                        if(stat.equals("start")){
                            auctionToPost.setStartTime(aTime);
                        }else if(stat.equals("end")) {
                            auctionToPost.setEndTime(aTime);
                        }

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void updateBookOwner(final BookOwnerModel bookOwnerModelToPost, final String fromWhere){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.UPDATE_BOOK_OWNER_1;

        final User user = (User) SPUtility.getSPUtil(UpdateBookActivity.this).getObject("USER_OBJECT", User.class);

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(bookOwnerModelToPost);

        int maxLogSize = 2000;
        for (int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("BookOwnerPostVolley", mRequestBody.substring(start, end));
        }
        d("BookOwnerPostVolley", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                Log.i("BookOwnerPutRes", response);

                if(bookOwnerModelToPost.getStatus().equals("Rent")){
                    if(fromWhere.equals("Swap")){
                        rentToPost.setBookOwner(bookOwnerModelToPost);
                        rentToPost.setCalculatedPrice(swapDetailModel.getSwapPrice());
//                        rentToPost.setDaysForRent(5);

                        addRentalDetail(rentToPost);
                    }else if(fromWhere.equals("NotAdvertised")){
                        rentToPost.setBookOwner(bookOwnerModelToPost);
//                        rentToPost.setDaysForRent(5);
                        rentToPost.setCalculatedPrice(calculatePrice(bookOwnerModelToPost.getBookObj()));

                        addRentalDetail(rentToPost);
                    }else if(fromWhere.equals("Auction")){
                        rentToPost.setBookOwner(bookOwnerModelToPost);
//                        rentToPost.setDaysForRent(5);
                        rentToPost.setCalculatedPrice(calculatePrice(bookOwnerModelToPost.getBookObj()));

                        addRentalDetail(rentToPost);
                    }else if(fromWhere.equals("none")){
                        Log.d("SuccesUpdate", response);

                        AlertDialog ad = new AlertDialog.Builder(UpdateBookActivity.this).create();
                        ad.setTitle("Update");
                        ad.setMessage("Updated Successfully!");
                        ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(UpdateBookActivity.this, ProfileActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("userModelPass", user);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        ad.show();
                    }

                }else if(bookOwnerModelToPost.getStatus().equals("Swap")){
                    if(fromWhere.equals("Rent")){
                        swapToPost.setBookOwner(bookOwnerModelToPost);
                        swapToPost.setSwapPrice(Float.parseFloat(String.valueOf(rentalDetailModel.getCalculatedPrice())));
                        swapToPost.setSwapDescription(rentalDetailModel.getBookOwner().getStatusDescription());

                        addSwapDetail(swapToPost);
                    }else if(fromWhere.equals("NotAdvertised")){
                        swapToPost.setBookOwner(bookOwnerModelToPost);
                        swapToPost.setSwapPrice(Float.parseFloat(String.valueOf(calculatePrice(bookOwnerModelToPost.getBookObj()))));
                        swapToPost.setSwapDescription(bookOwnerModelToPost.getStatusDescription());

                        addSwapDetail(swapToPost);
                    }else if(fromWhere.equals("Auction")){
                        swapToPost.setBookOwner(bookOwnerModelToPost);
                        swapToPost.setSwapPrice(Float.parseFloat(String.valueOf(calculatePrice(bookOwnerModelToPost.getBookObj()))));
                        swapToPost.setSwapDescription(bookOwnerModelToPost.getStatusDescription());

                        addSwapDetail(swapToPost);
                    }else if(fromWhere.equals("none")){
                        Log.d("SuccesUpdate", response);

                        AlertDialog ad = new AlertDialog.Builder(UpdateBookActivity.this).create();
                        ad.setTitle("Update");
                        ad.setMessage("Updated Successfully!");
                        ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(UpdateBookActivity.this, ProfileActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("userModelPass", user);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        ad.show();
                    }
                }else if(bookOwnerModelToPost.getStatus().equals("Auction")){
                    if(fromWhere.equals("NotAdvertised")){
                        auctionToPost.setBookOwner(bookOwnerModelToPost);
                        auctionToPost.setAuctionDescription(bookOwnerModelToPost.getStatusDescription());
                        auctionToPost.setStartingPrice(Float.parseFloat(String.valueOf(calculatePrice(bookOwnerModelToPost.getBookObj()))));
                        auctionToPost.setStartTime(mStartTime.getText().toString());
                        auctionToPost.setEndTime(mEndTime.getText().toString());

                        addAuctionDetail(auctionToPost);
                    }else if(fromWhere.equals("Swap")){
                        auctionToPost.setBookOwner(bookOwnerModelToPost);
                        auctionToPost.setAuctionDescription(bookOwnerModelToPost.getStatusDescription());
                        auctionToPost.setStartingPrice(Float.parseFloat(String.valueOf(calculatePrice(bookOwnerModelToPost.getBookObj()))));
                        auctionToPost.setStartTime(mStartTime.getText().toString());
                        auctionToPost.setEndTime(mEndTime.getText().toString());

                        addAuctionDetail(auctionToPost);
                    }else if(fromWhere.equals("Rent")){
                        auctionToPost.setBookOwner(bookOwnerModelToPost);
                        auctionToPost.setAuctionDescription(bookOwnerModelToPost.getStatusDescription());
                        auctionToPost.setStartingPrice(Float.parseFloat(String.valueOf(calculatePrice(bookOwnerModelToPost.getBookObj()))));
                        auctionToPost.setStartTime(mStartTime.getText().toString());
                        auctionToPost.setEndTime(mEndTime.getText().toString());

                        addAuctionDetail(auctionToPost);

                    }else if(fromWhere.equals("none")){
                        Log.d("SuccesUpdate", response);

                        AlertDialog ad = new AlertDialog.Builder(UpdateBookActivity.this).create();
                        ad.setTitle("Update");
                        ad.setMessage("Updated Successfully!");
                        ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(UpdateBookActivity.this, ProfileActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("userModelPass", user);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        ad.show();
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

    private void addAuctionDetail(AuctionDetailModel auctionDetailModel) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = Constants.POST_AUCTION_DETAIL_1;

        auctionDetailModel.setStatus("Available");

        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(auctionDetailModel);


        int maxLogSize = 2000;
        for(int i = 0; i <= mRequestBody.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > mRequestBody.length() ? mRequestBody.length() : end;
            Log.d("aucLogVolley", mRequestBody);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("auctionDetailAddLog", response);

                AlertDialog ad = new AlertDialog.Builder(UpdateBookActivity.this).create();
                ad.setTitle("Update");
                ad.setMessage("Updated Successfully!");
                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(UpdateBookActivity.this, ProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userModelPass", user);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addSwapDetail(SwapDetail swapDetailModel) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_SWAP_DETAIL_1;

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        swapDetailModel.setSwapTimeStamp(sdf.format(c.getTime()));

        swapDetailModel.setSwapStatus("Available");

//        user.setDayTimeModel();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(swapDetailModel);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("swapDetailAddLog", response);

                AlertDialog ad = new AlertDialog.Builder(UpdateBookActivity.this).create();
                ad.setTitle("Update");
                ad.setMessage("Updated Successfully!");
                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(UpdateBookActivity.this, ProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userModelPass", user);
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

    private void addRentalDetail(RentalDetail postRental) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String URL = "http://104.197.4.32:8080/Koobym/user/add";
        String URL = Constants.POST_RENTAL_DETAIL_1;

//        user.setDayTimeModel();
        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(Date.class, GsonDateDeserializer.getInstance()).create();
        final String mRequestBody = gson.toJson(postRental);


        Log.d("LOG_VOLLEY_RequestBody", mRequestBody);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("rentalDetailAddLog", response);

                AlertDialog ad = new AlertDialog.Builder(UpdateBookActivity.this).create();
                ad.setTitle("Update");
                ad.setMessage("Updated Successfully!");
                ad.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(UpdateBookActivity.this, ProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userModelPass", user);
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
}