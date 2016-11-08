package com.weatherapp.shubhamsorte.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherResult extends AppCompatActivity {
    private String TAG = WeatherResult.class.getSimpleName();
    private ProgressDialog pDialog;
    static String requestURL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_result2);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        TextView t = (TextView) findViewById(R.id.textView2);
        t.setText(b.getString("temp") + "°C");
        TextView t2 = (TextView) findViewById(R.id.textView3);
        t2.setText(""+ b.getString("cond"));


        Button b1 = (Button) findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView t = (TextView) findViewById(R.id.editText);
                String inputText = t.getText().toString();
                String queryText = inputText.replace(" ","%20");
                requestURL = "https://api.havenondemand.com/1/api/sync/analyzesentiment/v2?text="+queryText+"&apikey=98e2ca9a-d5b6-4d8b-97c5-0a33bfcde401";
                System.out.println(requestURL);
                new WeatherResult.GetAnalysis().execute();

            }
        });

    }

    private class GetAnalysis extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(WeatherResult.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(requestURL);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr); // Got the json object here!! @shubhsin

                    System.out.println(jsonObj);

                    if (jsonObj != null) {
                        JSONArray queryObject = jsonObj.getJSONArray("sentiment_analysis");
                        JSONObject firstObj = queryObject.getJSONObject(0);
                        JSONObject aggregateObj = firstObj.getJSONObject("aggregate");
                        String analyResult = aggregateObj.getString("sentiment");
                        String score = aggregateObj.getString("score");

                        System.out.println(analyResult);
                        System.out.println(score);

                        Float scoreFloat = Float.parseFloat(score);
                        System.out.println(scoreFloat);

                        Bundle b = new Bundle();
                        b.putFloat("score",scoreFloat);
                        Intent I = new Intent(getBaseContext(),QuoteResult.class);
                        I.putExtras(b);
                        startActivity(I);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get data from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Check city name provided",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
//            ListAdapter adapter = new SimpleAdapter(
//                    MainActivity.this, contactList,
//                    R.layout.list_item, new String[]{"name", "email",
//                    "mobile"}, new int[]{R.id.name,
//                    R.id.email, R.id.mobile});
//
//            lv.setAdapter(adapter);
        }

    }
}
