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
 * Created by Joane14 on 08/10/2017.
 */

public class SwapShelfAdapter extends RecyclerView.Adapter<SwapShelfAdapter.BookHolder> {

    public List<SwapDetail> swapDetailList;
    public Activity context;

    @Override
    public SwapShelfAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item_landing_page, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        SwapShelfAdapter.BookHolder dataObjectHolder = new SwapShelfAdapter.BookHolder(this.context, view);
        return dataObjectHolder;
    }

    public SwapShelfAdapter(List<SwapDetail> myDataset) {
        swapDetailList = myDataset;
    }

    @Override
    public void onBindViewHolder(SwapShelfAdapter.BookHolder holder, int position) {

        holder.mBookTitle.setText(swapDetailList.get(position).getBookOwner().getBookObj().getBookTitle());
        holder.mBookPrice.setText(swapDetailList.get(position).getBookOwner().getBookObj().getBookOriginalPrice().toString());
        Log.d("libroShet",swapDetailList.get(position).getBookOwner().getBookObj().getBookOriginalPrice().toString());
        String author = "No Author";
        Log.d("AuthorDisplay", author);
        if(swapDetailList.get(position).getBookOwner().getBookObj().getBookAuthor()!=null) {
            int size = swapDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().size();

            Log.d("inside", "author setTEXT");
            if (size > 1) {
                for (int i = 0; i < size; i++) {
                    author += swapDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().get(i).getAuthorFName();
                    Log.d("authorLoop", swapDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().get(i).getAuthorFName());
                    if (size - 1 > i) {
                        author += ", ";
                    }
                }
            }
            Log.d("authorName if", "inside");
            Log.d("authorName if", author);
        }
        holder.mBookAuthor.setText(author);
        Log.d("libroPanga",swapDetailList.get(position).getBookOwner().getBookObj().getBookFilename());
        Glide.with(context).load(swapDetailList.get(position).getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mBookFilename);
//        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getBookOwner().getBookObj().getBookFilename())).fit().into(holder.mBookFilename);
//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

        if(swapDetailList.get(position).getBookOwner().getBookObj().getBookFilename()==null){
            Log.d("displayImage", "is null");
        }else{
            Log.d("displayImage", "is not null");
        }
    }

    @Override
    public int getItemCount() {
        return swapDetailList.size();
    }


    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mBookTitle, mBookPrice, mBookAuthor;
        ImageView mBookFilename;
        SwapDetail swapDetailObj;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBookTitle = (TextView) itemView.findViewById(R.id.lpBookTitle);
            mBookAuthor = (TextView) itemView.findViewById(R.id.lpAuthor);
            mBookPrice = (TextView) itemView.findViewById(R.id.lpBookPrice);
            mBookFilename = (ImageView) itemView.findViewById(R.id.displayBookPic);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swapDetailObj = new SwapDetail();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
                    Intent intent = new Intent(SwapShelfAdapter.this.context, ViewBookSwapActivity.class);
                    swapDetailObj = SwapShelfAdapter.this.swapDetailList.get(position);
                    if(swapDetailObj==null){
                        Log.d("rentalDetailAdapter", "is null");
                    }else{
                        Log.d("rentalDetailAdapter", "is not null");
                    }
                    intent.putExtra("ViewBook", "fromAdapter");
                    bundle.putSerializable("View", swapDetailObj);
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

}
