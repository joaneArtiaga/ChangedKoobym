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
import com.example.joane14.myapplication.Activities.ViewBookSwapActivity;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.R;

import java.util.List;

/**
 * Created by Joane14 on 06/10/2017.
 */

public class SwapLPAdapter extends RecyclerView.Adapter<SwapLPAdapter.BookHolder> {

    public List<SwapDetail> bookList;
    public Activity context;


    @Override
    public SwapLPAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        SwapLPAdapter.BookHolder dataObjectHolder = new SwapLPAdapter.BookHolder(this.context, view);
        return dataObjectHolder;
    }



    public SwapLPAdapter(List<SwapDetail> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(SwapLPAdapter.BookHolder holder, int position) {

        holder.mBookTitle.setText(bookList.get(position).getBookOwner().getBookObj().getBookTitle());
        holder.mBookPrice.setText(bookList.get(position).getBookOwner().getBookObj().getBookOriginalPrice().toString());
        Log.d("libroShet",bookList.get(position).getBookOwner().getBookObj().getBookOriginalPrice().toString());
        String author = " ";
        if(bookList.get(position).getBookOwner().getBookObj().getBookAuthor()!=null) {
            int size = bookList.get(position).getBookOwner().getBookObj().getBookAuthor().size();

            Log.d("inside", "author setTEXT");
            if (size > 1) {
                for (int i = 0; i < size; i++) {
                    author += bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(i).getAuthorFName();
                    Log.d("authorLoop", bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(i).getAuthorFName());
                    if (size - 1 > i) {
                        author += ", ";
                    }
                }
            }
            Log.d("authorName if", "inside");
            Log.d("authorName if", author);
        }
        holder.mBookAuthor.setText(author);
        Log.d("libroPanga",bookList.get(position).getBookOwner().getBookObj().getBookFilename());
        Glide.with(context).load(bookList.get(position).getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mBookFilename);
//        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getBookOwner().getBookObj().getBookFilename())).fit().into(holder.mBookFilename);
//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

        if(bookList.get(position).getBookOwner().getBookObj().getBookFilename()==null){
            Log.d("displayImage", "is null");
        }else{
            Log.d("displayImage", "is not null");
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mBookTitle, mBookPrice, mBookAuthor;
        ImageView mBookFilename;
        SwapDetail swapDetailObj;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBookTitle = (TextView) itemView.findViewById(R.id.tvTitleBook);
            mBookAuthor = (TextView) itemView.findViewById(R.id.tvAuthorBook);
            mBookPrice = (TextView) itemView.findViewById(R.id.tvDescriptionBook);
            mBookFilename = (ImageView) itemView.findViewById(R.id.ivBookPic);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swapDetailObj = new SwapDetail();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
                    Intent intent = new Intent(SwapLPAdapter.this.context, ViewBookSwapActivity.class);
                    swapDetailObj = SwapLPAdapter.this.bookList.get(position);
                    if(swapDetailObj==null){
                        Log.d("rentalDetailAdapter", "is null");
                    }else{
                        Log.d("rentalDetailAdapter", "is not null");
                    }
                    intent.putExtra("ViewBookSwap", "fromAdapter");
                    bundle.putSerializable("ViewSwap", swapDetailObj);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

//        @Override
//        public void onClick(View v) {
//            int position = getAdapterPosition();
//
//
//            Log.d("AdapterPosition", "inside "+Integer.toString(position));
//
//        }
    }

    public String getDetails(int position){
        String result = "";

        result = bookList.get(position).getBookOwner().toString();



        return result;

    }
}
