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

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.RequestActivity;
import com.example.joane14.myapplication.Activities.SwapBookChooser;
import com.example.joane14.myapplication.Activities.SwapMeetUpChooser;
import com.example.joane14.myapplication.Activities.TimeDateChooser;
import com.example.joane14.myapplication.Activities.TransactionActivity;
import com.example.joane14.myapplication.Activities.ViewBookSwapActivity;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.Model.SwapComment;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Joane14 on 08/10/2017.
 */

public class SwapChooserAdapter extends RecyclerView.Adapter<SwapChooserAdapter.BookHolder> {

    public List<SwapDetail> bookList;
    SwapDetail swapDetailObj;
    SwapHeader swapHeader;
    public Activity context;


    @Override
    public SwapChooserAdapter.BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item_swap, parent, false);

        this.context = (Activity) parent.getContext();
        Log.d("LandingPAgeAdapter","inside");
        SwapChooserAdapter.BookHolder dataObjectHolder = new SwapChooserAdapter.BookHolder(this.context, view);
        return dataObjectHolder;
    }



    public SwapChooserAdapter(List<SwapDetail> myDataset, SwapHeader swapBook) {
        bookList = myDataset;
        this.swapHeader = swapBook;
    }

    @Override
    public void onBindViewHolder(SwapChooserAdapter.BookHolder holder, final int position) {


        swapHeader.setRequestedSwapDetail(bookList.get(position));
        String author = "";
        holder.mBookTitle.setText(bookList.get(position).getBookOwner().getBookObj().getBookTitle());
        holder.mBookPrice.setText(String.valueOf(bookList.get(position).getSwapPrice()));
//        Picasso.with(context).load(String.format(Constants.IMAGE_URL, bookList.get(position).getUser().getImageFilename())).fit().into(holder.mSwapCommenterImg);
//        Log.d("displayImage", bookList.get(position).getBookOwner().getBookObj().getBookFilename());
        Glide.with(context).load(bookList.get(position).getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.mBookPic);

        if(bookList.get(position).getBookOwner().getBookObj().getBookAuthor().size()!=0) {
            int size = bookList.get(position).getBookOwner().getBookObj().getBookAuthor().size();

           /* for(int init=0; init<bookList.size(); init++ ){
                author+= bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" "+
                        bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                if(init>0){
                    author+=", ";
                }
            }
            Log.d("inside", "author setTEXT");
            if (size > 1) {
                for (int i = 0; i < size; i++) {
                    author += bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(i).getAuthorFName();
                    Log.d("authorLoop", bookList.get(position).getBookOwner().getBookObj().getBookAuthor().get(i).getAuthorFName());
                    if (size - 1 > i) {
                        author += ", ";
                    }
                }
            }*/
        }else{
            author = "Unknown Author";
        }

        holder.mBookAuthor.setText(author);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public class BookHolder extends RecyclerView.ViewHolder {
        TextView mBookTitle, mBookAuthor, mBookPrice;
        ImageView mBookPic;
        SwapDetail swapDetail;
        Context context;

        public BookHolder(final Context context, View itemView) {
            super(itemView);

            this.context = context;
            mBookTitle = (TextView) itemView.findViewById(R.id.swapBookTitle);
            mBookAuthor = (TextView) itemView.findViewById(R.id.swapAuthor);
            mBookPrice = (TextView) itemView.findViewById(R.id.swapBookPrice);
            mBookPic = (ImageView) itemView.findViewById(R.id.swapBookPic);
//            itemView.setOnClickListener(this);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swapDetail = new SwapDetail();
                    Bundle bundle = new Bundle();
                    int position = getAdapterPosition();
                    showConfirmation(position);
//                    Log.d("AdapterPosition", "inside "+Integer.toString(position));
//                    Intent intent = new Intent(SwapChooserAdapter.this.context, ViewBookSwapActivity.class);
//                    swapDetail = SwapChooserAdapter.this.bookList.get(position);
//                    if(swapDetail==null){
//                        Log.d("rentalDetailAdapter", "is null");
//                    }else{
//                        Log.d("rentalDetailAdapter", "is not null");
//                    }
//                    intent.putExtra("ViewBookSwap", "fromAdapter");
//                    bundle.putSerializable("ViewSwap", swapDetail);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
                }
            });
        }

    }

    public void showConfirmation(int position){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Meet Up Summary");
        alertDialogBuilder.setMessage("Are you sure you want to swap your book "+swapHeader.getSwapDetail().getBookOwner().getBookObj().getBookTitle()+" " +
                "with "+bookList.get(position).getBookOwner().getUserObj().getUserFname()+" "+bookList.get(position).getBookOwner().getUserObj().getUserLname()+
                "'s book, "+bookList.get(position).getBookOwner().getBookObj().getBookTitle()+"?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        Intent intent = new Intent(context, SwapMeetUpChooser.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("swapHeader", swapHeader);
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {


            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
