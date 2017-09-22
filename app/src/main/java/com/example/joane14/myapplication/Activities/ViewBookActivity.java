package com.example.joane14.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.R;

public class ViewBookActivity extends AppCompatActivity {

    Bundle bundle;
    RentalDetail rentalDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        rentalDetail = new RentalDetail();
        if(getIntent().getExtras()!=null){
            bundle = getIntent().getExtras();
            rentalDetail = (RentalDetail) bundle.getSerializable("View" );

            if(rentalDetail==null){
                Log.d("rentalDetail", "is empty");

            }else{
                Log.d("bundle", "is not empty");
            }
            Log.d("bundle", "is not empty");
        }else{
            Log.d("bundle", "is empty");
        }
    }



}
