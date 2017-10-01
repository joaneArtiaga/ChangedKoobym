package com.example.joane14.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.R;

public class TimeDateChooser extends AppCompatActivity {

    RentalDetail rentalDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_date_chooser);

        rentalDetail = new RentalDetail();
        if(getIntent().getExtras().getSerializable("rentalDetail")!=null){
            rentalDetail = (RentalDetail) getIntent().getExtras().getSerializable("rentalDetail");
            Log.d("TimeDateChooser", rentalDetail.getBookOwner().getUserObj().toString());
        }
    }
}
