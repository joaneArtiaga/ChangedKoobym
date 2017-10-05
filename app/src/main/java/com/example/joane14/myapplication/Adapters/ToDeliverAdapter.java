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
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joane14 on 05/10/2017.
 */

public class ToDeliverAdapter extends RecyclerView.Adapter<ToDeliverAdapter.BookHolder>{

    public List<RentalHeader> bookList;
    public Activity context;

    @Override
    public ToDeliverAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_to_deliver, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        ToDeliverAdapter.BookHolder dataObjectHolder = new ToDeliverAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public ToDeliverAdapter(List<RentalHeader> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(ToDeliverAdapter.BookHolder holder, int position) {


        holder.mBookRented.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());
        holder.mDaysRent.setText("Available "+bookList.get(position).getRentalDetail().getDaysForRent()+" days for rent.");
        holder.mMU.setText(bookList.get(position).getLocation().getLocationName());
        holder.mPrice.setText(String.valueOf(bookList.get(position).getRentalDetail().getCalculatedPrice()));

        if(bookList.get(position).getUser()==null){
            holder.mRenter.setText("Renter not Found");
        }else{
            holder.mRenter.setText(bookList.get(position).getUser().getUserFname()+" "+bookList.get(position).getUser().getUserLname());
        }

        holder.mReminder.setText("Return book and receive by DATE.");


        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getImageFilename())).fit().into(holder.mIvRenter);
        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookImg);

//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mRenter, mBookRented, mReminder, mPrice, mMU, mDaysRent;
        ImageView mIvRenter, mIvBookImg;
        RentalHeader rentalHeaderObj;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mRenter = (TextView) itemView.findViewById(R.id.deliverRenter);
            mReminder = (TextView) itemView.findViewById(R.id.deliverReminder);
            mMU = (TextView) itemView.findViewById(R.id.deliverMU);
            mDaysRent = (TextView) itemView.findViewById(R.id.deliverDaysForRent);
            mPrice = (TextView) itemView.findViewById(R.id.deliverPrice);
            mBookRented = (TextView) itemView.findViewById(R.id.deliverBook);
            mIvRenter = (ImageView) itemView.findViewById(R.id.deliverRenterImage);
            mIvBookImg = (ImageView) itemView.findViewById(R.id.deliverBookImage);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rentalHeaderObj = new RentalHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    rentalHeaderObj = ToDeliverAdapter.this.bookList.get(position);
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
