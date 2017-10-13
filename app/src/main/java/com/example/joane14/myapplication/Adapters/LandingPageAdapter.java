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
import com.example.joane14.myapplication.R;

import java.util.List;

/**
 * Created by Joane14 on 08/08/2017.
 */

public class LandingPageAdapter extends RecyclerView.Adapter<LandingPageAdapter.BookHolder>{

    public List<RentalDetail> bookList;
    public Activity context;


    @Override
    public LandingPageAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item_landing_page, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        BookHolder dataObjectHolder = new BookHolder(this.context, view);
        return dataObjectHolder;
    }



    public LandingPageAdapter(List<RentalDetail> myDataset) {
        bookList = myDataset;
    }

    @Override
    public void onBindViewHolder(LandingPageAdapter.BookHolder holder, int position) {

        holder.mBookTitle.setText(bookList.get(position).getBookOwner().getBookObj().getBookTitle());
        holder.mBookPrice.setText(bookList.get(position).getBookOwner().getBookObj().getBookOriginalPrice().toString());
        Log.d("libroShet",bookList.get(position).getBookOwner().getBookObj().getBookOriginalPrice().toString());
        String author = " ";
        if(bookList.get(position).getBookOwner().getBookObj().getBookAuthor().size()!=0){
            for(int init=0; init<bookList.get(position).getBookOwner().getBookObj().getBookAuthor().size(); init++){
                if(!(bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                    author+=bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                    if(!(bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                        author+=bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                        if(init+1<bookList.get(position).getBookOwner().getBookObj().getBookAuthor().size()){
                            author+=", ";
                        }
                    }
                }
            }
        }else{
            author="Unknown Author";
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
        RentalDetail rentalDetailObj;
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
                    rentalDetailObj = new RentalDetail();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
                    Intent intent = new Intent(LandingPageAdapter.this.context, ViewBookActivity.class);
                    rentalDetailObj = LandingPageAdapter.this.bookList.get(position);
                    if(rentalDetailObj==null){
                        Log.d("rentalDetailAdapter", "is null");
                    }else{
                        Log.d("rentalDetailAdapter", "is not null");
                    }
                    intent.putExtra("ViewBook", "fromAdapter");
                    bundle.putSerializable("View", rentalDetailObj);
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

