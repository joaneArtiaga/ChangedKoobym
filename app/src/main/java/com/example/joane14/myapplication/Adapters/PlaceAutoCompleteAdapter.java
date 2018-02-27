package com.example.joane14.myapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.example.joane14.myapplication.Model.Place;
import com.example.joane14.myapplication.Model.UserDayTime;
import com.example.joane14.myapplication.R;

import java.util.List;

/**
 * Created by Joane14 on 27/02/2018.
 */

public class PlaceAutoCompleteAdapter extends BaseAdapter {

    public List<Place> places;
    public Context context;

    public PlaceAutoCompleteAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int i) {
        return places.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Place p = places.get(i);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = (View) inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        TextView suggestion = (TextView) convertView.findViewById(android.R.id.text1);
        suggestion.setText(p.getDescription());
        return convertView;
    }
}
