package com.example.joane14.myapplication.Adapters;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.joane14.myapplication.Activities.LocationChooser;
import com.example.joane14.myapplication.Fragments.MostRentedBookFrag;
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
    private onDeleteListener deleteListener;


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

    public LocationAdapter(List<LocationModel> myDataset, onDeleteListener deleteListener) {
        mLocationList = myDataset;
        this.deleteListener = deleteListener;
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
    public void onBindViewHolder(LocationObjHolder holder, final int position) {
        final LocationModel locModel = mLocationList.get(position);
        holder.title.setText(locModel.getLocationName());
        holder.mLongitude.setText(locModel.getLongitude());
        holder.mLatitude.setText(locModel.getLatitude());

        holder.mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("inside", "onClickChange");
                deleteItem(locModel);
                Log.d("position cardview", Integer.toString(position));
                Log.d("getPositionItem", Integer.toString(getPositionOfItem(locModel)));
                deleteListener.onDeleteClick(getPositionOfItem(locModel));
            }
        });
    }

    private int getPositionOfItem(LocationModel locationModel){
        int flag = 0;
        for(int init = 0; init< mLocationList.size(); init++){
            if(locationModel == mLocationList.get(init)){
                flag = init;
                break;
            }
        }
        return flag;
    }

    private void deleteItem(LocationModel locModel){
        mLocationList.remove(locModel);
        notifyDataSetChanged();
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

    public interface onDeleteListener{
        void onDeleteClick(int position);
    }
}

