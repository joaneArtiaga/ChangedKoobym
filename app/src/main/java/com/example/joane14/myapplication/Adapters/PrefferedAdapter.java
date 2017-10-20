package com.example.joane14.myapplication.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.joane14.myapplication.Activities.GsonDateDeserializer;
import com.example.joane14.myapplication.Fragments.MostRentedBookFrag;
import com.example.joane14.myapplication.Fragments.VolleyUtil;
import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Joane14 on 20/10/2017.
 */

public class PrefferedAdapter extends BaseAdapter {

    private Context context;
    List<RentalDetail> rentalDetailList;

    public PrefferedAdapter(Context context, List<RentalDetail> rentalDetailList){
        this.context = context;
        this.rentalDetailList = rentalDetailList;
    }

    @Override
    public int getCount(){
        return rentalDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if(convertView == null){
            grid = new View(context);
            grid = inflater.inflate(R.layout.cardview_item_landing_page, null);
            ImageView bookPic = (ImageView) grid.findViewById(R.id.displayBookPic);
            TextView bookTitle = (TextView) grid.findViewById(R.id.lpBookTitle);
            TextView bookAuthor = (TextView) grid.findViewById(R.id.lpAuthor);
            TextView bookPrice = (TextView) grid.findViewById(R.id.bookPrice);

            bookTitle.setText(rentalDetailList.get(position).getBookOwner().getBookObj().getBookTitle());
            bookPrice.setText(String.format("%.2f", rentalDetailList.get(position).getCalculatedPrice()));

            String author = " ";
            if(rentalDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().size()!=0){
                for(int init=0; init<rentalDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().size(); init++){
                    if(!(rentalDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName().equals(""))){
                        author+=rentalDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorFName()+" ";
                        if(!(rentalDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName().equals(""))){
                            author+=rentalDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().get(init).getAuthorLName();
                            if(init+1<rentalDetailList.get(position).getBookOwner().getBookObj().getBookAuthor().size()){
                                author+=", ";
                            }
                        }
                    }
                }
            }else{
                author="Unknown Author";
            }
            bookAuthor.setText(author);

        }else{
            grid = (View) convertView;
        }


        return grid;
    }
}