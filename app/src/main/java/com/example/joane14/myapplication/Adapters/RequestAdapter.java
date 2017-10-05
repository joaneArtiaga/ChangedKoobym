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
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.R;

import java.util.List;

/**
 * Created by Joane14 on 04/10/2017.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.BookHolder> {

    public List<RentalHeader> bookList;
    public Activity context;
    public String fromWhere;

    @Override
    public RequestAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_request, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        RequestAdapter.BookHolder dataObjectHolder = new RequestAdapter.BookHolder(this.context, view);


        Log.d("RequestAdapter", String.valueOf(bookList.size()));
        return dataObjectHolder;
    }

    public RequestAdapter(List<RentalHeader> myDataset, String fromWhere) {
        bookList = myDataset;
        this.fromWhere = fromWhere;
    }

    @Override
    public void onBindViewHolder(RequestAdapter.BookHolder holder, int position) {

        if(fromWhere.equals("MyRequest")){
            holder.mRequestor.setText("You");
        }else{
            holder.mRequestor.setText(bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getUserFname()+" "+bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getUserLname());
        }
        holder.mBookReq.setText(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookTitle());
        Log.d("libroShet", String.valueOf(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookOriginalPrice()));
        String author = " ";
//        if(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookAuthor()!=null) {
//            int size = bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookAuthor().size();
//
//            Log.d("inside", "author setTEXT");
//            if (size > 1) {
//                for (int i = 0; i < size; i++) {
//                    author += bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookAuthor().get(i).getAuthorFName();
//                    Log.d("authorLoop", bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookAuthor().get(i).getAuthorFName());
//                    if (size - 1 > i) {
//                        author += ", ";
//                    }
//                }
//            }
//            Log.d("authorName if", "inside");
//            Log.d("authorName if", author);
//        }
//        holder.mBookAuthor.setText(author);
        Log.d("libroPanga",bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename());
        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mIvBookReq);
        Glide.with(context).load(bookList.get(position).getRentalDetail().getBookOwner().getUserObj().getImageFilename()).centerCrop().into(holder.mIvRequestor);

//        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getBookOwner().getBookObj().getBookFilename())).fit().into(holder.mBookFilename);
//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());

    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mRequestor, mBookReq;
        ImageView mIvRequestor, mIvBookReq;
        RentalHeader rentalHeaderObj;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mRequestor = (TextView) itemView.findViewById(R.id.tvRequestor);
            mBookReq = (TextView) itemView.findViewById(R.id.tvBookReq);
            mIvBookReq = (ImageView) itemView.findViewById(R.id.ivBookReq);
            mIvRequestor = (ImageView) itemView.findViewById(R.id.ivRequestor);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rentalHeaderObj = new RentalHeader();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    rentalHeaderObj = RequestAdapter.this.bookList.get(position);
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
