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

import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joane14 on 05/10/2017.
 */

public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.BookHolder> {

    public List<RentalHeader> bookList;
    public Activity context;

    @Override
    public CompleteAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_complete, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        CompleteAdapter.BookHolder dataObjectHolder = new CompleteAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public CompleteAdapter(List<RentalHeader> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(CompleteAdapter.BookHolder holder, int position) {


        holder.mBookRented.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());

        if(bookList.get(position).getUserId()==null){
            holder.mRenter.setText("Renter not Found");
        }else{
            holder.mRenter.setText(bookList.get(position).getUserId().getUserFname()+" "+bookList.get(position).getUserId().getUserLname());
        }


        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getImageFilename())).fit().into(holder.mIvRenter);
//        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mRenter, mBookRented, mReceiveDate, mReceiveTime, mReturnDate, mReturnTime , mPrice, mMU, mDaysRent;
        ImageView mIvRenter, mIvBookImg;
        RentalHeader rentalHeaderObj;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mRenter = (TextView) itemView.findViewById(R.id.completeRenter);
            mReceiveDate = (TextView) itemView.findViewById(R.id.receivedDate);
            mReceiveTime = (TextView) itemView.findViewById(R.id.receivedTime);
            mReturnDate = (TextView) itemView.findViewById(R.id.returnedDate);
            mReturnTime = (TextView) itemView.findViewById(R.id.returnedTime);
            mBookRented = (TextView) itemView.findViewById(R.id.completeBook);
            mIvRenter = (ImageView) itemView.findViewById(R.id.completeRenterImage);
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
                    rentalHeaderObj = CompleteAdapter.this.bookList.get(position);
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
