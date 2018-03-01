package com.example.joane14.myapplication.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.joane14.myapplication.Model.GenreModel;
import com.example.joane14.myapplication.R;

import java.util.List;

/**
 * Created by Joane14 on 30/07/2017.
 */

public class CategoryAdapter extends BaseAdapter {

    private List<String> category;
    private Context context;
    private LayoutInflater mInflater;

    public CategoryAdapter(Context context, List<String> category) {
        this.context = context;
        this.category = category;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return category.size();
    }

    @Override
    public Object getItem(int i) {
        return category.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = mInflater.inflate(R.layout.genre_item, null);
        }


        TextView genreName = (TextView) view.findViewById(R.id.textView_genreName);
        genreName.setText(category.get(i));

        for(int init=0; init<category.size();init++){
            if(category.get(init).equals(category.get(i))){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    genreName.setBackground(ContextCompat.getDrawable(context, R.drawable.button_selected));
                }
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    genreName.setBackground(ContextCompat.getDrawable(context, R.drawable.button_normal));
                }
            }
        }

        return view;
    }

}
