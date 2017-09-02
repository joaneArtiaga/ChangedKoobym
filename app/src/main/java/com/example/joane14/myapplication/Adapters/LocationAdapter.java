package com.example.joane14.myapplication.Adapters;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.joane14.myapplication.Model.LocationModel;
import com.example.joane14.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joane14 on 31/08/2017.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationObjHolder>{

    private List<LocationModel> mLocationList;
    private static MyClickListener myClickListener;


    public static class LocationObjHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title;
        TextView mLatitude, mLongitude;
        Button mChange;

        public LocationObjHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.locationName);
            mLatitude = (TextView) itemView.findViewById(R.id.latitude);
            mLongitude = (TextView) itemView.findViewById(R.id.longitude);
            mChange = (Button) itemView.findViewById(R.id.btnChange);
            Log.i("Data object hlder", "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public LocationAdapter(List<LocationModel> myDataset) {
        mLocationList = myDataset;
    }

    @Override
    public LocationObjHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_cardview, parent, false);

        LocationObjHolder locationObjHolder = new LocationObjHolder(view);

        return locationObjHolder;
    }

    @Override
    public void onBindViewHolder(LocationObjHolder holder, int position) {
        holder.title.setText(mLocationList.get(position).getLocationName());
        holder.mLongitude.setText(Float.toString((float) mLocationList.get(position).getLongitude()));
        holder.mLatitude.setText(Float.toString((float) mLocationList.get(position).getLatitude()));
    }

    public void addItem(LocationModel dataObj, int index) {
        mLocationList.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mLocationList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mLocationList.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}

