package com.example.joane14.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.joane14.myapplication.R;

public class SwapBookChooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_book_chooser);
        Toast.makeText(SwapBookChooser.this, "Inside Swap Book Chooser", Toast.LENGTH_SHORT).show();
    }
}
