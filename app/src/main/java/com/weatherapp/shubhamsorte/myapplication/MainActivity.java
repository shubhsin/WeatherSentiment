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

import static java.sql.Types.NULL;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    static String reqestURL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button b1 = (Button) findViewById(R.id.checkButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView t = (TextView) findViewById(R.id.cityField);
                String inputText = t.getText().toString();
                String queryText = inputText.replace(" ","%20");
                reqestURL = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+queryText+"%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
                System.out.println(reqestURL);
                new GetWeather().execute();

            }
        });

    }

    private class GetWeather extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(reqestURL);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr); // Got the json object here!! @shubhsin

                    System.out.println(jsonObj);

                    if (jsonObj != null) {
                        JSONObject queryObject = jsonObj.getJSONObject("query");
                        JSONObject resultObject = queryObject.getJSONObject("results");
                        JSONObject channelObject = resultObject.getJSONObject("channel");
                        JSONObject itemObject = channelObject.getJSONObject("item");
                        JSONObject conditionObject = itemObject.getJSONObject("condition");
                        String tempString = conditionObject.getString("temp");
                        String tempCond = conditionObject.getString("text");

                        Float celciusTemp = (float)(((Integer.parseInt(tempString) - 32) * 5)/9);
                        System.out.println(celciusTemp);
                        System.out.println(tempCond);


                        Bundle b = new Bundle();
                        b.putString("temp",celciusTemp.toString());
                        b.putString("cond",tempCond);
                        Intent I = new Intent(getBaseContext(),WeatherResult.class);
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
