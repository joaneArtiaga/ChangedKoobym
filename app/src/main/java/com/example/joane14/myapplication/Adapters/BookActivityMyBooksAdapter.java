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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Model.BookActivity;
import com.example.joane14.myapplication.R;

import java.util.List;

/**
 * Created by Joane14 on 20/10/2017.
 */

public class BookActivityMyBooksAdapter extends BaseAdapter {

    private Context context;
    List<BookActivity> bookActivityList;
    BookActivity bookActivityModel;
    private LayoutInflater mInflater;


    public BookActivityMyBooksAdapter(Context context, List<BookActivity> bookActivityList){
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

        statusBook.setText(bookActivityModel.getStatus());
        if(bookActivityModel.getBookStatus().equals("rent")){
            typeBook.setText("Rent");
            statusLinear.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRent));
        }else if(bookActivityModel.getBookStatus().equals("swap")){
            typeBook.setText("Swap");
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