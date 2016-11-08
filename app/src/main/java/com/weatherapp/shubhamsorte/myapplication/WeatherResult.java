package com.weatherapp.shubhamsorte.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WeatherResult extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    static String requestURL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_result2);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        TextView t = (TextView) findViewById(R.id.textView2);
        t.setText(b.getString("temp") + " C");
        TextView t2 = (TextView) findViewById(R.id.textView3);
        t2.setText(""+ b.getString("cond"));


//        Button b1 = (Button) findViewById(R.id.checkButton);
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                TextView t = (TextView) findViewById(R.id.editText);
//                String inputText = t.getText().toString();
//                String queryText = inputText.replace(" ","%20");
//                requestURL = "https://api.havenondemand.com/1/api/sync/analyzesentiment/v2?text="+queryText+"&apikey=98e2ca9a-d5b6-4d8b-97c5-0a33bfcde401";
//                System.out.println(requestURL);
//                //new WeatherResult.GetAnalysis().execute();
//
//            }
//        });

    }
}
