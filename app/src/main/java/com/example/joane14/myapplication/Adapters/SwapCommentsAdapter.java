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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.ViewBookSwapActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joane14 on 06/10/2017.
 */

public class SwapCommentsAdapter extends RecyclerView.Adapter<SwapCommentsAdapter.BookHolder> {

    public List<SwapComment> bookList;
    public Activity context;


    @Override
    public SwapCommentsAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_view_swap_comment, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        SwapCommentsAdapter.BookHolder dataObjectHolder = new SwapCommentsAdapter.BookHolder(this.context, view);
        return dataObjectHolder;
    }



    public SwapCommentsAdapter(List<SwapComment> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(SwapCommentsAdapter.BookHolder holder, final int position) {

        holder.mSwapCommenterComment.setText(bookList.get(position).getSwapComment());
        holder.mSwapCommenterName.setText(bookList.get(position).getUser().getUserFname()+" "+bookList.get(position).getUser().getUserLname());

        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getUser().getImageFilename())).fit().into(holder.mSwapCommenterImg);
//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());
        holder.mBtnSwapSeeShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SwapComment onClick", bookList.get(position).getSwapComment());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mSwapCommenterName, mSwapCommenterComment;
        ImageView mSwapCommenterImg;
        SwapComment swapCommentObj;
        Button mBtnSwapSeeShelf;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mSwapCommenterName = (TextView) itemView.findViewById(R.id.swapCommenterName);
            mSwapCommenterComment = (TextView) itemView.findViewById(R.id.swapCommenterComment);
            mBtnSwapSeeShelf = (Button) itemView.findViewById(R.id.swapCommentSeeShelfBtn);
            mSwapCommenterImg = (ImageView) itemView.findViewById(R.id.ivSwapCommenterImg);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swapCommentObj = new SwapComment();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
                    Intent intent = new Intent(SwapCommentsAdapter.this.context, ViewBookSwapActivity.class);
                    swapCommentObj = SwapCommentsAdapter.this.bookList.get(position);
                    if(swapCommentObj==null){
                        Log.d("rentalDetailAdapter", "is null");
                    }else{
                        Log.d("rentalDetailAdapter", "is not null");
                    }
                    intent.putExtra("ViewBookSwap", "fromAdapter");
                    bundle.putSerializable("ViewSwap", swapCommentObj);
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
