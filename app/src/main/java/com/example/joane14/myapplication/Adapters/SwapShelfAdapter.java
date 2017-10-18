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
        Log.d("libroShet",String.valueOf(swapDetailList.get(position).getSwapPrice()));
        String author = "";
        Log.d("AuthorDisplay", author);
        if(swapDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().size()!=0){
            for(int init=0; init<swapDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().size(); init++){
                if(!(swapDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                    author+=swapDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                    if(!(swapDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                        author+=swapDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                        if(init+1<swapDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().size()){
                            author+=", ";
                        }
                    }
                }
            }
        }else{
            author="Unknown Author";
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

}
