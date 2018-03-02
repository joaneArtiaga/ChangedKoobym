package com.example.joane14.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Fragments.Constants;
import com.example.joane14.myapplication.Model.RentalHeader;
import com.example.joane14.myapplication.Model.SwapDetail;
import com.example.joane14.myapplication.Model.SwapHeader;
import com.example.joane14.myapplication.Model.User;
import com.example.joane14.myapplication.R;
import com.example.joane14.myapplication.Utilities.SPUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.util.Log.d;

/**
 * Created by Joane14 on 08/10/2017.
 */

public class SwapBookChooserAdapter extends ArrayAdapter<SwapDetail> {

    public List<SwapDetail> bookList, selectedSwap;
    SwapDetail swapDetailObj;
    SwapHeader swapHeader;
    public Activity context;

    static class ViewHolder{
        protected TextView bookTitleSwap;
        protected CheckBox checkSwap;
        protected ImageView bookImage;
    }

    public SwapBookChooserAdapter(Activity context, List<SwapDetail> swapHeaderList) {
        super(context, R.layout.swap_row, swapHeaderList);
        this.context = context;
        this.bookList = swapHeaderList;
        selectedSwap = new ArrayList<SwapDetail>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View view = null;
        if(convertView==null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.swap_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.bookTitleSwap = (TextView) view.findViewById(R.id.tvTitleSwap);
            viewHolder.bookImage = (ImageView) view.findViewById(R.id.ivBookSwap);
            viewHolder.checkSwap = (CheckBox) view.findViewById(R.id.checkSwap);
            view.setTag(viewHolder);
        }else{
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.bookTitleSwap.setText(bookList.get(position).getBookOwner().getBookObj().getBookTitle());
        Glide.with(context).load(bookList.get(position).getBookOwner().getBookObj().getBookFilename()).centerCrop().into(holder.bookImage);

        holder.checkSwap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedSwap.add(bookList.get(position));
                }else{
                    for(int init=0; init<selectedSwap.size(); init++){
                        if(bookList.get(position).getSwapDetailId()==selectedSwap.get(init).getSwapDetailId()){
                            selectedSwap.remove(init);
                        }
                    }
                }
                for(int init=0; init<selectedSwap.size(); init++){
                    Log.d("SizeSelected", selectedSwap.get(init).getBookOwner().getBookObj().getBookTitle());
                }

            }
        });

        return view;
    }


}
