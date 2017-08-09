package com.example.joane14.myapplication.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joane14 on 08/08/2017.
 */

public class LandingPageAdapter extends RecyclerView.Adapter<LandingPageAdapter.BookHolder>{

    private List<RentalDetail> bookList;
    private Activity context;

    private static MyClickListener myClickListener;

    @Override
    public LandingPageAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item_landing_page, parent, false);

        BookHolder dataObjectHolder = new BookHolder(view);
        return dataObjectHolder;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public LandingPageAdapter(List<RentalDetail> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(LandingPageAdapter.BookHolder holder, int position) {

        holder.mBookTitle.setText(bookList.get(position).getBookOwner().getBookObj().getBookTitle());
        holder.mBookPrice.setText(bookList.get(position).getBookOwner().getBookObj().getBookOriginalPrice().toString());
        String author = "";
        if(bookList.get(position).getBookOwner().getBookObj().getBookAuthor()!=null) {
            int size = bookList.get(position).getBookOwner().getBookObj().getBookAuthor().size();

            Log.d("inside", "author setTEXT");
            if (size > 1) {
                for (int i = 0; i < size; i++) {
                    author += bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(i).getAuthorFName();
                    if (size - 1 > i) {
                        author += ", ";
                    }
                }
            }
        }else{
            author = "No Author";
        }

        holder.mBookAuthor.setText(author);

        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getBookOwner().getBookObj().getBookFilename())).fit().into(holder.mBookFilename);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public static class BookHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView mBookTitle, mBookPrice, mBookAuthor;
        ImageView mBookFilename;

        public BookHolder(View itemView) {
            super(itemView);

            mBookTitle = (TextView) itemView.findViewById(R.id.lpBookTitle);
            mBookAuthor = (TextView) itemView.findViewById(R.id.lpAuthor);
            mBookPrice = (TextView) itemView.findViewById(R.id.lpBookPrice);
            mBookFilename = (ImageView) itemView.findViewById(R.id.displayBookPic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}

