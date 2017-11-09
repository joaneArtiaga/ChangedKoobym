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
import com.example.joane14.myapplication.Model.Book;
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

public class ShowBooksAdapter extends BaseAdapter {

    private Context context;
    List<Book> bookList;
    Book bookModel;
    private LayoutInflater mInflater;
    RatingBar mRating;
    Float retFloat;


    public ShowBooksAdapter(Context context, List<Book> bookOwnerModelList){
        this.context = context;
        this.bookList = bookOwnerModelList;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount(){
        return bookList.size();
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
            convertView = mInflater.inflate(R.layout.item_add_book, null);
        }

        bookModel = bookList.get(position);

        ImageView bookPic = (ImageView) convertView.findViewById(R.id.displayBookPic);
        TextView bookTitle = (TextView) convertView.findViewById(R.id.lpBookTitle);
        TextView bookAuthor = (TextView) convertView.findViewById(R.id.lpAuthor);
        TextView bookPrice = (TextView) convertView.findViewById(R.id.lpPrice);

        Log.d("inside", "PrefferedAdapter");
        bookTitle.setText(bookModel.getBookTitle());

        bookPrice.setText(String.valueOf(bookModel.getBookOriginalPrice()));
        String author = " ";
        if(bookModel.getBookAuthor().size()!=0){
            for(int init=0; init<bookModel.getBookAuthor().size(); init++){
                if(!(bookModel.getBookAuthor().get(init).getAuthorFName().equals(""))){
                    author+=bookModel.getBookAuthor().get(init).getAuthorFName()+" ";
                    if(!(bookModel.getBookAuthor().get(init).getAuthorLName().equals(""))){
                        author+=bookModel.getBookAuthor().get(init).getAuthorLName();
                        if(init+1<bookModel.getBookAuthor().size()){
                            author+=", ";
                        }
                    }
                }
            }
        }else{
            author="Unknown Author";
        }
        bookAuthor.setText(author);

        Glide.with(context).load(bookModel.getBookFilename()).centerCrop().into(bookPic);

//        getRatings();




        return convertView;
    }


}