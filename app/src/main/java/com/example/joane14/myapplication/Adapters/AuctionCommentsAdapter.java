package com.example.joane14.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joane14.myapplication.Activities.SwapBookChooser;
import com.example.joane14.myapplication.Model.AuctionComment;
import com.example.joane14.myapplication.Model.AuctionCommentDetail;
import com.example.joane14.myapplication.Model.AuctionDetailModel;
import com.example.joane14.myapplication.Model.AuctionHeader;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joane14 on 06/10/2017.
 */

public class AuctionCommentsAdapter extends RecyclerView.Adapter<AuctionCommentsAdapter.BookHolder> {

    public List<AuctionCommentDetail> bookList;
    public AuctionDetailModel auctionDetailModel;
    AuctionHeader auctionHeader;
    public Activity context;


    @Override
    public AuctionCommentsAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_auction_comment, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        AuctionCommentsAdapter.BookHolder dataObjectHolder = new AuctionCommentsAdapter.BookHolder(this.context, view);
        return dataObjectHolder;
    }



    public AuctionCommentsAdapter(List<AuctionCommentDetail> myDataset, AuctionDetailModel auctionDetailModel) {
        auctionHeader = new AuctionHeader();
        bookList = myDataset;
        this.auctionDetailModel = auctionDetailModel;
        auctionHeader.setAuctionDetail(auctionDetailModel);
    }

    @Override
    public void onBindViewHolder(AuctionCommentsAdapter.BookHolder holder, final int position) {

        holder.mAuctionCommenterComment.setText(bookList.get(position).getAuctionComment().getAuctionComment()+" ");
        holder.mAuctionCommenterName.setText(bookList.get(position).getAuctionComment().getUser().getUserFname()+" "+bookList.get(position).getAuctionComment().getUser().getUserLname());

        Picasso.with(context).load(bookList.get(position).getAuctionComment().getUser().getImageFilename()).fit().into(holder.mAuctionCommenterImg);
//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mAuctionCommenterName, mAuctionCommenterComment;
        ImageView mAuctionCommenterImg;
        AuctionComment auctionCommentObj;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mAuctionCommenterName = (TextView) itemView.findViewById(R.id.auctionCommenterName);
            mAuctionCommenterComment = (TextView) itemView.findViewById(R.id.auctionCommenterComment);
            mAuctionCommenterImg = (ImageView) itemView.findViewById(R.id.ivAuctionCommenterImg);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auctionCommentObj = new AuctionComment();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(SwapCommentsAdapter.this.context, ViewBookSwapActivity.class);
//                    swapCommentObj = SwapCommentsAdapter.this.bookList.get(position);
//                    if(swapCommentObj==null){
//                        Log.d("rentalDetailAdapter", "is null");
//                    }else{
//                        Log.d("rentalDetailAdapter", "is not null");
//                    }
//                    intent.putExtra("ViewBookSwap", "fromAdapter");
//                    bundle.putSerializable("ViewSwap", swapCommentObj);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
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
