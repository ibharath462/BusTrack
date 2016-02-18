package com.example.bharath.bustrack;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new JSONAsyncTask().execute("");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class JSONAsyncTask extends AsyncTask<String,String,String> {

        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {

            progressDialog.setMessage("Downloading your results...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    JSONAsyncTask.this.cancel(true);
                }
            });

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... urls) {

            String data=null;


            try {

                //------------------>>
                //String query = URLEncoder.encode("EC3N4AB&destination=EC4M8AD&sensor=false&mode=transit&departure_time=1343376768", "utf-8");
                String URL="https://maps.googleapis.com/maps/api/directions/json?origin=13.012561,80.215621&destination=13.068617,80.206389&sensor=false&mode=transit&transit_mode=bus&key=AIzaSyAkVgHeJ7P4QAmCRjX6LK7YlTpCuAcdjDQ";
                HttpGet httppost = new HttpGet(URL);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    data = EntityUtils.toString(entity);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        protected void onPostExecute(final String result) {

            JSONObject jsonObject;
            String duration=null;
            try {
                jsonObject = new JSONObject(result);
                // routesArray contains ALL routes
                JSONArray routesArray = jsonObject.getJSONArray("routes");
// Grab the first route
                JSONObject route = routesArray.getJSONObject(0);
// Take all legs from the route
                JSONArray legs = route.getJSONArray("legs");
// Grab first leg
                JSONObject leg = legs.getJSONObject(0);

                JSONObject durationObject = leg.getJSONObject("duration");
                duration = durationObject.getString("text");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            this.progressDialog.dismiss();
            final String finalDuration = duration;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Duration:"+ finalDuration, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}

