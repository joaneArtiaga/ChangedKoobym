package com.example.joane14.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.R;

import org.w3c.dom.Text;

public class ViewBookActivity extends AppCompatActivity {

    Bundle bundle;
    RentalDetail rentalDetail;
    TextView mBookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        rentalDetail = new RentalDetail();

        mBookTitle = (TextView) findViewById(R.id.vbBookTitle);
        if(getIntent().getExtras()!=null){
            bundle = getIntent().getExtras();
            rentalDetail = (RentalDetail) bundle.getSerializable("View" );

            if(rentalDetail==null){
                Log.d("rentalDetail", "is empty");

            }else{
                Log.d("bundle", "is not empty");
                Log.d("RentalBookTitle", rentalDetail.getBookOwner().getBookObj().getBookTitle());
                mBookTitle.setText(rentalDetail.getBookOwner().getBookObj().getBookTitle());
            }
            Log.d("bundle", "is not empty");
        }else{
            Log.d("bundle", "is empty");
        }
    }



}
