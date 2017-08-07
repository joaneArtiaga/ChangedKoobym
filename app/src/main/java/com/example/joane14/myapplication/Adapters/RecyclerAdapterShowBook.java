package com.example.joane14.myapplication.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joane14.myapplication.Activities.LandingPage;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Fragments.ShowBooksFrag;
import com.example.joane14.myapplication.Model.Book;
import com.example.joane14.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Joane14 on 04/08/2017.
 */

public class RecyclerAdapterShowBook extends RecyclerView.Adapter<RecyclerAdapterShowBook.DataObjectHolder> {

    private ArrayList<Book> mBookList;
    private static MyClickListener myClickListener;
    Context mContext;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView mBookTitle, mBookAuthor, mBookDescription;
        ImageView mBookImg;


        public DataObjectHolder(View itemView) {
            super(itemView);
            mBookTitle = (TextView) itemView.findViewById(R.id.tvTitleBook);
            mBookAuthor = (TextView) itemView.findViewById(R.id.tvAuthorBook);
            mBookDescription = (TextView) itemView.findViewById(R.id.tvDescriptionBook);
            mBookImg = (ImageView) itemView.findViewById(R.id.ivBookPic);


            Log.i("inside", "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public RecyclerAdapterShowBook(ArrayList<Book> myDataset) {
        mBookList = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item, parent, false);

        this.mContext = parent.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        String author = "";
        holder.mBookTitle.setText(mBookList.get(position).getBookTitle());
        holder.mBookDescription.setText(mBookList.get(position).getBookDescription());
        if(!(mBookList.get(position).getBookFilename().equals(""))){
            Picasso.with(mContext).load(mBookList.get(position).getBookFilename()).fit().into(holder.mBookImg);
            Log.d("filename", mBookList.get(position).getBookFilename());
        }else{
            holder.mBookImg.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
        }

        if(mBookList.get(position).getBookAuthor()!=null) {
            int size = mBookList.get(position).getBookAuthor().size();

            if (size > 1) {
                for (int i = 0; i < size; i++) {
                    author += mBookList.get(position).getBookAuthor().get(i).getAuthorFName();
                    if (size - 1 > i) {
                        author += ", ";
                    }
                }
            }
        }else{
            author = "No Author";
        }

        holder.mBookAuthor.setText(author);

    }

    public void addItem(Book bookObj, int index) {
        mBookList.add(index, bookObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mBookList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
