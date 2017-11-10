package com.example.joane14.myapplication.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.BookActivity;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by Joane14 on 20/10/2017.
 */

public class BookActivityRequestAdapter extends BaseAdapter {

    private Context context;
    List<BookActivity> bookActivityList;
    BookActivity bookActivityModel;
    private LayoutInflater mInflater;


    public BookActivityRequestAdapter(Context context, List<BookActivity> bookActivityList){
        this.context = context;
        this.bookActivityList = bookActivityList;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount(){
        return bookActivityList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_book_activity, null);
        }

        bookActivityModel = bookActivityList.get(position);

        ImageView bookPic = (ImageView) convertView.findViewById(R.id.displayBookPic);
        TextView bookTitle = (TextView) convertView.findViewById(R.id.baBookTitle);
        TextView bookAuthor = (TextView) convertView.findViewById(R.id.baAuthor);
        TextView typeBook = (TextView) convertView.findViewById(R.id.bookType);
        TextView statusBook = (TextView) convertView.findViewById(R.id.bookStatus);
        LinearLayout statusLinear = (LinearLayout) convertView.findViewById(R.id.status_ll);

        Log.d("inside", "PrefferedAdapter");
        bookTitle.setText(bookActivityModel.getBookOwner().getBookObj().getBookTitle());

        Log.d("STATA", bookActivityModel.getBookStatus());

        if(bookActivityModel.getBookStatus().equals("swap")){
            if(bookActivityModel.getStatus().equals("Request")){
                statusBook.setText("Request, "+bookActivityModel.getBookStatus());
            }else if(bookActivityModel.getStatus().equals("Approved")){
                statusBook.setText("Waiting for Confirmation, "+bookActivityModel.getBookStatus());
            }else if(bookActivityModel.getStatus().equals("Confirm")){
                statusBook.setText("To Deliver, "+bookActivityModel.getBookStatus());
            }else if(bookActivityModel.getStatus().equals("ToReceive")){
                statusBook.setText("Complete, "+bookActivityModel.getBookStatus());
            }else if(bookActivityModel.getStatus().equals("Complete")){
                statusBook.setText("Complete, "+bookActivityModel.getBookStatus());
            }
        }else if(bookActivityModel.getBookStatus().equals("rent")){
            if(bookActivityModel.getStatus().equals("Request")){
                statusBook.setText("Request Sent, "+bookActivityModel.getBookStatus());
            }else if(bookActivityModel.getStatus().equals("Approved")){
                statusBook.setText("Waiting for Confirmation, "+bookActivityModel.getBookStatus());
            }else if(bookActivityModel.getStatus().equals("Confirm")){
                statusBook.setText("To Receive, "+bookActivityModel.getBookStatus());
            }else if(bookActivityModel.getStatus().equals("ToReceive")){
                statusBook.setText("Complete, "+bookActivityModel.getBookStatus());
            }else if(bookActivityModel.getStatus().equals("Complete")){
                statusBook.setText("Complete, "+bookActivityModel.getBookStatus());
            }
        }


        Log.d("BookStata", bookActivityModel.getBookOwner().getBookObj().getBookTitle()+" : "+bookActivityModel.getBookStatus());
        if(bookActivityModel.getBookStatus().equals("rent")){
            typeBook.setText("Rent");
            if(bookActivityModel.getStatus().equals("Request")){
                statusBook.setText("Request Sent");
            }else if(bookActivityModel.getStatus().equals("Approved")){
                statusBook.setText("Waiting for Confirmation");
            }else if(bookActivityModel.getStatus().equals("Confirm")){
                statusBook.setText("To Receive");
            }else if(bookActivityModel.getStatus().equals("ToReceive")){
                statusBook.setText("Complete");
            }else if(bookActivityModel.getStatus().equals("Complete")){
                statusBook.setText("Complete");
            }else if(bookActivityModel.getStatus().equals("Rejected")){
                statusBook.setText("Rejected");
            }
            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRent));
        }else if(bookActivityModel.getBookStatus().equals("swap")){
            typeBook.setText("Swap");
            if(bookActivityModel.getStatus().equals("Request")){
                statusBook.setText("Request");
            }else if(bookActivityModel.getStatus().equals("Approved")){
                statusBook.setText("Waiting for Confirmation");
            }else if(bookActivityModel.getStatus().equals("Confirm")){
                statusBook.setText("To Deliver");
            }else if(bookActivityModel.getStatus().equals("ToReceive")){
                statusBook.setText("Complete");
            }else if(bookActivityModel.getStatus().equals("Complete")){
                statusBook.setText("Complete");
            }else if(bookActivityModel.getStatus().equals("Rejected")){
                statusBook.setText("Rejected");
            }
            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSwap));
        }else{
            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGray));
        }

        String author = " ";
        if(bookActivityModel.getBookOwner().getBookObj().getBookAuthor().size()!=0){
            for(int init=0; init<bookActivityModel.getBookOwner().getBookObj().getBookAuthor().size(); init++){
                if(!(bookActivityModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                    author+=bookActivityModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                    if(!(bookActivityModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                        author+=bookActivityModel.getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                        if(init+1<bookActivityModel.getBookOwner().getBookObj().getBookAuthor().size()){
                            author+=", ";
                        }
                    }
                }
            }
        }else{
            author="Unknown Author";
        }
        bookAuthor.setText(author);

        Glide.with(context).load(bookActivityModel.getBookOwner().getBookObj().getBookFilename()).centerCrop().into(bookPic);

//        getRatings();


        return convertView;
    }

}