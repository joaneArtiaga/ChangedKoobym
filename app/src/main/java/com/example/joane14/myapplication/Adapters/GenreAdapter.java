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

public class GenreAdapter extends BaseAdapter {

    private List<GenreModel> genres;
    private Context context;
    private LayoutInflater mInflater;

    public GenreAdapter(Context context, List<GenreModel> genres) {
        this.context = context;
        this.genres = genres;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return genres.size();
    }

    @Override
    public Object getItem(int i) {
        return genres.get(i);
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

        GenreModel genreModel = genres.get(i);

        TextView genreName = (TextView) view.findViewById(R.id.textView_genreName);
        genreName.setText(genreModel.getGenreName());

        if (genreModel.isSelected()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                genreName.setBackground(ContextCompat.getDrawable(context, R.drawable.button_selected));
            }
        } else if (!genreModel.isSelected()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                genreName.setBackground(ContextCompat.getDrawable(context, R.drawable.button_normal));
            }
        }

        return view;
    }

}
