package com.example.joane14.myapplication.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.joane14.myapplication.Model.RentalDetail;
import com.example.joane14.myapplication.R;

public class ViewBookActivity extends AppCompatActivity {

    Bundle bundle;
    RentalDetail rentalDetail;
    TextView mBookTitle;
    TextView mBookAuthor;
    TextView mBookDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        mBookAuthor = (TextView) findViewById(R.id.vbBookAuthor);
        mBookDescription = (TextView) findViewById(R.id.vbBookDescription);

        rentalDetail = new RentalDetail();
        String author = "";

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
                Log.d("RentalBookAuthor", String.valueOf(rentalDetail.getBookOwner().getBookObj().getBookAuthor().get(0)));
                author+=rentalDetail.getBookOwner().getBookObj().getBookAuthor().get(0).getAuthorFName();
                mBookAuthor.setText(author);
                Log.d("RentalBookDescription", rentalDetail.getBookOwner().getBookObj().getBookDescription().toString());
                mBookDescription.setText((rentalDetail.getBookOwner().getBookObj().getBookDescription().toString()));
            }
            Log.d("bundle", "is not empty");
        }else{
            Log.d("bundle", "is empty");
        }
    }



}
