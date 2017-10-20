package com.example.joane14.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joane14 on 11/10/2017.
 */

public class CompleteSwapAdapter extends RecyclerView.Adapter<CompleteSwapAdapter.BookHolder> {

    public List<SwapHeader> bookList;
    SwapHeader swapHeader;
    public Activity context;

    @Override
    public CompleteSwapAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_complete_swap, parent, false);

        this.context = (Activity) parent.getContext();
        swapHeader = new SwapHeader();
        Log.d("LandingPAgeAdapter","inside");
        CompleteSwapAdapter.BookHolder dataObjectHolder = new CompleteSwapAdapter.BookHolder(this.context, view);

        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public CompleteSwapAdapter(List<SwapHeader> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(CompleteSwapAdapter.BookHolder holder, final int position) {


        User user = new User();
        user = (User) SPUtility.getSPUtil(context).getObject("USER_OBJECT", User.class);
        if(bookList.get(position).getRequestedSwapDetail().getBookOwner().getUserObj().getUserId()==user.getUserId()){
            holder.mMyBook.setText(bookList.get(position).getRequestedSwapDetail().getBookOwner().getBookObj().getBookTitle());
            Glide.with(context).load(bookList.get(position).getRequestedSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mMyIvBookImg);
            Glide.with(context).load(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);
            holder.mBookRented.setText(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookTitle());
        }else{
            holder.mMyBook.setText(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookTitle());
            Glide.with(context).load(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mMyIvBookImg);
            Glide.with(context).load(bookList.get(position).getRequestedSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);
            holder.mBookRented.setText(bookList.get(position).getRequestedSwapDetail().getBookOwner().getBookObj().getBookTitle());
        }

        holder.mMU.setText(bookList.get(position).getLocation().getLocationName());
        holder.mPrice.setText(String.format("%.2f", bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookOriginalPrice()));

//        if(bookList.get(position).getUser()==null){
//            holder.mRenter.setText("Renter not Found");
//        }else{
//            holder.mRenter.setText(bookList.get(position).getUser().getUserFname()+" "+bookList.get(position).getUser().getUserLname());
//        }

        holder.mReminder.setText("Receive book on "+ bookList.get(position).getDateTimeStamp()+", "+bookList.get(position).getUserDayTime().getDay().getStrDay()+" at "+bookList.get(position).getUserDayTime().getTime().getStrTime());


//        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getUser().getImageFilename())).fit().into(holder.mIvRenter);

//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mRenter, mBookRented, mReminder, mPrice, mMU, mMyBook;
        ImageView mIvRenter, mIvBookImg, mMyIvBookImg;
        SwapHeader swapHeaderObj;
        Context context;


        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
//            mRenter = (TextView) itemView.findViewById(R.id.toReceiveRenter);
            mReminder = (TextView) itemView.findViewById(R.id.toReceiveReminder);
            mMU = (TextView) itemView.findViewById(R.id.toReceiveMU);
            mPrice = (TextView) itemView.findViewById(R.id.toReceivePrice);
            mBookRented = (TextView) itemView.findViewById(R.id.toReceiveBook);
            mMyBook = (TextView) itemView.findViewById(R.id.toReceiveMyBook);
//            mIvRenter = (ImageView) itemView.findViewById(R.id.toReceiveRenterImage);
            mIvBookImg = (ImageView) itemView.findViewById(R.id.toReceiveBookImage);
            mMyIvBookImg = (ImageView) itemView.findViewById(R.id.toReceiveMyBookImage);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swapHeaderObj = new SwapHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    swapHeaderObj = CompleteSwapAdapter.this.bookList.get(position);
                    if(swapHeaderObj==null){
                        Log.d("rentalHeaderAdapter", "is null");
                    }else{
                        Log.d("rentalHeaderAdapter", "is not null");
                    }
//                    intent.putExtra("ViewBook", "fromAdapter");
//                    bundle.putSerializable("View", rentalDetailObj);
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
