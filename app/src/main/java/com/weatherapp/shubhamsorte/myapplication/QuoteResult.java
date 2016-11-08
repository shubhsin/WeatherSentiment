package com.weatherapp.shubhamsorte.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class QuoteResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_result);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        Float analysisVal = b.getFloat("score");

    }
}
