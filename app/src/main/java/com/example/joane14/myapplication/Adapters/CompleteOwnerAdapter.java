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
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joane14 on 14/10/2017.
 */

public class CompleteOwnerAdapter extends RecyclerView.Adapter<CompleteOwnerAdapter.BookHolder> {

    public List<RentalHeader> bookList;
    public Activity context;

    @Override
    public CompleteOwnerAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_complete, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        CompleteOwnerAdapter.BookHolder dataObjectHolder = new CompleteOwnerAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public CompleteOwnerAdapter(List<RentalHeader> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(CompleteOwnerAdapter.BookHolder holder, int position) {


        holder.mBookRented.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());

//        if(bookList.get(position).getUserId()==null){
//            holder.mRenter.setText("Renter not Found");
//        }else{
//            holder.mRenter.setText(bookList.get(position).getUserId().getUserFname()+" "+bookList.get(position).getUserId().getUserLname());
//        }


//        Picasso.with(context).load(bookList.get(position).getUserId().getImageFilename()).fit().into(holder.mIvRenter);
        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

        holder.mReceiveTime.setText(bookList.get(position).getUserDayTime().getDay().getStrDay()+", "+bookList.get(position).getUserDayTime().getTime().getStrTime());
        holder.mReturnTime.setText(bookList.get(position).getUserDayTime().getDay().getStrDay()+", "+bookList.get(position).getUserDayTime().getTime().getStrTime());

//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mRenter, mBookRented, mReceiveDate, mReceiveTime, mReturnDate, mReturnTime , mRenterText;
        ImageView mIvRenter, mIvBookImg;
        RentalHeader rentalHeaderObj;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
//            mRenter = (TextView) itemView.findViewById(R.id.completeRenter);
//            mRenterText = (TextView) itemView.findViewById(R.id.textRenter);
            mReceiveDate = (TextView) itemView.findViewById(R.id.receivedDate);
            mReceiveTime = (TextView) itemView.findViewById(R.id.receivedTime);
            mReturnDate = (TextView) itemView.findViewById(R.id.returnedDate);
            mReturnTime = (TextView) itemView.findViewById(R.id.returnedTime);
            mBookRented = (TextView) itemView.findViewById(R.id.completeBook);
//            mIvRenter = (ImageView) itemView.findViewById(R.id.completeRenterImage);
            mIvBookImg = (ImageView) itemView.findViewById(R.id.completeBookImage);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rentalHeaderObj = new RentalHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    rentalHeaderObj = CompleteOwnerAdapter.this.bookList.get(position);
                    if(rentalHeaderObj==null){
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

    public String getDetails(int position){
        String result = "";

        result = bookList.get(position).toString();



        return result;

    }
}
