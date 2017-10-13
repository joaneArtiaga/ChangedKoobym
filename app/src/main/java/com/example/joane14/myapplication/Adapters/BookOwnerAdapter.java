package com.example.joane14.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.ViewBookActivity;
import com.example.joane14.myapplication.Model.BookOwnerModel;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.R;

import java.util.List;

/**
 * Created by Joane14 on 12/10/2017.
 */

public class BookOwnerAdapter extends RecyclerView.Adapter<BookOwnerAdapter.BookHolder>{

    public List<BookOwnerModel> bookList;
    public Activity context;


    @Override
    public BookOwnerAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_book_item, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        BookOwnerAdapter.BookHolder dataObjectHolder = new BookOwnerAdapter.BookHolder(this.context, view);
        return dataObjectHolder;
    }



    public BookOwnerAdapter(List<BookOwnerModel> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(BookOwnerAdapter.BookHolder holder, int position) {

        holder.mBookTitle.setText(bookList.get(position).getBookObj().getBookTitle());
        holder.mBookDescription.setText(bookList.get(position).getBookObj().getBookDescription());
        Log.d("libroShet",bookList.get(position).getBookObj().getBookOriginalPrice().toString());
        String author = " ";
        if(bookList.get(position).getBookObj().getBookAuthor().size()!=0){
            for(int init=0; init<bookList.get(position).getBookObj().getBookAuthor().size(); init++){
                if(!(bookList.get(position).getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                    author+=bookList.get(position).getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                    if(!(bookList.get(position).getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                        author+=bookList.get(position).getBookObj().getBookAuthor().get(init).getAuthorLName();
                        if(init+1<bookList.get(position).getBookObj().getBookAuthor().size()){
                            author+=", ";
                        }
                    }
                }
            }
        }else{
            author="Unknown Author";
        }
        holder.mBookAuthor.setText(author);
        Log.d("libroPanga",bookList.get(position).getBookObj().getBookFilename());
        Glide.with(context).load(bookList.get(position).getBookObj().getBookFilename()).centerCrop().into(holder.mBookFilename);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mBookTitle, mBookDescription, mBookAuthor;
        ImageView mBookFilename;
        BookOwnerModel bookOwnerModel;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBookTitle = (TextView) itemView.findViewById(R.id.bTitleBook);
            mBookDescription = (TextView) itemView.findViewById(R.id.bDescriptionBook);
            mBookAuthor = (TextView) itemView.findViewById(R.id.bAuthorBook);
            mBookFilename = (ImageView) itemView.findViewById(R.id.bBookPic);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookOwnerModel = new BookOwnerModel();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));

                }
            });
        }

    }


}

