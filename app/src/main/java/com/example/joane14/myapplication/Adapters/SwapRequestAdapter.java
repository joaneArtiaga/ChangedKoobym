package com.example.joane14.myapplication.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.SwapHeaderDetail;
import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joane14 on 08/10/2017.
 */

public class SwapRequestAdapter extends ArrayAdapter<SwapHeaderDetail> {

    public List<SwapHeaderDetail> bookList;
    SwapDetail swapDetailObj;
    SwapHeader swapHeader;
    public Activity context;

    static class ViewHolder{
        protected TextView bookTitleSwap;
        protected ImageView bookImage;
    }

    public SwapRequestAdapter(Activity context, List<SwapHeaderDetail> swapHeaderList) {
        super(context, R.layout.swap_row, swapHeaderList);
        this.context = context;
        this.bookList = swapHeaderList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View view = null;
        if(convertView==null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.swap_row_request, null);
            final ViewHolder viewHolder = new ViewHolder();

            viewHolder.bookTitleSwap = (TextView) view.findViewById(R.id.tvTitleSwap);
            viewHolder.bookImage = (ImageView) view.findViewById(R.id.ivBookSwap);
            view.setTag(viewHolder);
        }else{
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        if(bookList.get(position).getSwapType().equals("Requestee")){
            holder.bookTitleSwap.setText(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookTitle());
            Glide.with(context).load(bookList.get(position).getSwapDetail().getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.bookImage);
        }

        return view;
    }
}
