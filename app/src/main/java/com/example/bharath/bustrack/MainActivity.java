package com.example.bharath.bustrack;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.logging.Handler;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    String s,id;
    String bus;
    Double lats=13.026611111111112,longs=80.26583333333333;
    GoogleMap t=null;
    TextView durr;
    info.hoang8f.widget.FButton back;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        t=googleMap;

    }

    enum stops5A{

        Mylapore, Mandaveli, AMS_Hospital, Adayar, Anna_University,Saidapet,T_Nagar;

    }

    enum stopsT70{

        CMBT,MMDA_Colony,Jaffarkhanpet,Cipet,Guindy_Tvk_Estate,Anna_University,Adyar_BS,Adyar_Depot,Thiruvanmiyur;
    }

    stops5A st5A=null;
    stopsT70 stt70=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Firebase.setAndroidContext(this);
       // Firebase Btrack = new Firebase("https://bustrack.firebaseio.com/");

        Firebase Btrack = null;

        s=getIntent().getStringExtra("stop");
        bus=getIntent().getStringExtra("bus");
        durr=(TextView)findViewById(R.id.durr);
        back=(info.hoang8f.widget.FButton)findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,BusStop.class);
                startActivity(i);
                finish();
            }
        });



        if(bus.equals("5B")) {

            st5A=stops5A.valueOf(s);
            Btrack = new Firebase("https://bustrack.firebaseio.com/bus/0/");
            // Toast.makeText(getApplicationContext(),"Bus is "+bus,Toast.LENGTH_LONG).show();
        }
        else if(bus.equals("T70"))
        {
            stt70=stopsT70.valueOf(s);
            Btrack = new Firebase("https://bustrack.firebaseio.com/bus/1/");
            //Toast.makeText(getApplicationContext(),"Bus is "+bus,Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Database yet to be added..",Toast.LENGTH_SHORT).show();
        }


        String []destinations5A={"13.03425,80.26802777777777",
                "13.026611111111112,80.26583333333333",
                "13.017805555555556,80.26063888888889",
                "13.006805555555555,80.25558333333333",
                "13.007361111111111,80.23672222222223",
                "13.020861111111111,80.22522222222223",
                "13.0345,80.23027777777779"};

        String []destinationsT70={"13.069162,80.204624",
                "13.065174,80.211393",
                "13.029213,80.208849",
                "13.015478,80.204942",
                "13.009941,80.211276",
                "13.008289,80.233449",
                "13.008289,80.233449",
                "12.997715,80.256733",
                "12.986826,80.258997"};

        String destination=null;

        String destination5A=null;
        String destinationT70=null;



        if(st5A!=null && stt70==null) {

            switch (st5A) {
                case Mylapore: {
                    destination5A = destinations5A[0];
                    break;
                }
                case Mandaveli: {
                    destination5A = destinations5A[1];
                    break;
                }
                case AMS_Hospital: {
                    destination5A = destinations5A[2];
                    break;
                }
                case Adayar: {
                    destination5A = destinations5A[3];
                    break;
                }
                case Anna_University: {
                    destination5A = destinations5A[4];
                    break;
                }
                case Saidapet: {
                    destination5A = destinations5A[5];
                    break;
                }
                case T_Nagar: {
                    destination5A = destinations5A[6];
                    break;
                }
            }

            destination=destination5A;
        }

        else if(stt70!=null && st5A==null)
        {
            switch (stt70)
            {
                case CMBT:
                {
                    destinationT70=destinationsT70[0];
                    break;
                }
                case MMDA_Colony:
                {
                    destinationT70=destinationsT70[1];
                    break;
                }
                case Jaffarkhanpet:
                {
                    destinationT70=destinationsT70[2];
                    break;
                }
                case Cipet:
                {
                    destinationT70=destinationsT70[3];
                    break;
                }
                case Guindy_Tvk_Estate:
                {
                    destinationT70=destinationsT70[4];
                    break;
                }
                case Anna_University:
                {
                    destinationT70=destinationsT70[5];
                    break;
                }
                case Adyar_BS:
                {
                    destinationT70=destinationsT70[6];
                    break;
                }
                case Adyar_Depot:
                {
                    destinationT70=destinationsT70[7];
                    break;
                }
                case Thiruvanmiyur:
                {
                    destinationT70=destinationsT70[8];
                    break;
                }
            }
            destination=destinationT70;
        }

        final double latSTOP,lonSTOP;

        latSTOP=Double.parseDouble(destination.split(",")[0]);
        lonSTOP=Double.parseDouble(destination.split(",")[1]);
        //Toast.makeText(getApplicationContext(),"Stop is : "+destination,Toast.LENGTH_LONG).show();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("BusTrack");
        setSupportActionBar(toolbar);

        durr.setText("Estimating the time of arrival, calculation speed may vary according to your network.");
        durr.setTextColor(Color.BLACK);


        Btrack.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<Double, Object> data = (HashMap<Double, Object>) dataSnapshot.getValue();
                lats = (Double) data.get("latitude");
                longs = (Double) data.get("longitude");
                id=(String)data.get("id");
                busPos(lats,longs,t,s,latSTOP,lonSTOP,bus);
                JSONAsyncTask JSONfetcher = new JSONAsyncTask();
                JSONfetcher.execute(lats.toString(),longs.toString(),s);
                //Toast.makeText(getApplicationContext(),"Data Changed..",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void busPos(Double lat,Double lon,GoogleMap m,String s,Double x,Double y,String z)
    {
        m.clear();
        LatLng sydney = new LatLng(lat, lon);
        LatLng stop=new LatLng(x,y);

        Marker bus = m.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(z)
                .snippet(id)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.bus1)));

        Marker sss = m.addMarker(new MarkerOptions()
                .position(new LatLng(x,y))
                .title("Stop")
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.stop)));


        sss.showInfoWindow();
        bus.showInfoWindow();

        //m.addMarker(marker);

        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(sydney, 15);
        m.animateCamera(yourLocation);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    class JSONAsyncTask extends AsyncTask<String,String,String> {


        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {

            //progressDialog.setMessage("Downloading your results...");
            //progressDialog.show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    durr.setTextColor(Color.BLACK);
                }
            });
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

            String []destinations5A={"13.03425,80.26802777777777",
                    "13.026611111111112,80.26583333333333",
                    "13.017805555555556,80.26063888888889",
                    "13.006805555555555,80.25558333333333",
                    "13.007361111111111,80.23672222222223",
                    "13.020861111111111,80.22522222222223",
                    "13.0345,80.23027777777779"};

            String []destinationsT70={"13.069162,80.204624",
            "13.065174,80.211393",
            "13.029213,80.208849",
            "13.015478,80.204942",
            "13.009941,80.211276",
            "13.008289,80.233449",
            "13.008289,80.233449",
            "12.997715,80.256733",
            "12.986826,80.258997"};

            String destination=null;

            String destination5A=null;
            String destinationT70=null;



            if(st5A!=null && stt70==null) {

                switch (st5A) {
                    case Mylapore: {
                        destination5A = destinations5A[0];
                        break;
                    }
                    case Mandaveli: {
                        destination5A = destinations5A[1];
                        break;
                    }
                    case AMS_Hospital: {
                        destination5A = destinations5A[2];
                        break;
                    }
                    case Adayar: {
                        destination5A = destinations5A[3];
                        break;
                    }
                    case Anna_University: {
                        destination5A = destinations5A[4];
                        break;
                    }
                    case Saidapet: {
                        destination5A = destinations5A[5];
                        break;
                    }
                    case T_Nagar: {
                        destination5A = destinations5A[6];
                        break;
                    }
                }

                destination=destination5A;
            }

            else if(stt70!=null && st5A==null)
            {
                switch (stt70)
                {
                    case CMBT:
                    {
                        destinationT70=destinationsT70[0];
                        break;
                    }
                    case MMDA_Colony:
                    {
                        destinationT70=destinationsT70[1];
                        break;
                    }
                    case Jaffarkhanpet:
                    {
                        destinationT70=destinationsT70[2];
                        break;
                    }
                    case Cipet:
                    {
                        destinationT70=destinationsT70[3];
                        break;
                    }
                    case Guindy_Tvk_Estate:
                    {
                        destinationT70=destinationsT70[4];
                        break;
                    }
                    case Anna_University:
                    {
                        destinationT70=destinationsT70[5];
                        break;
                    }
                    case Adyar_BS:
                    {
                        destinationT70=destinationsT70[6];
                        break;
                    }
                    case Adyar_Depot:
                    {
                        destinationT70=destinationsT70[7];
                        break;
                    }
                    case Thiruvanmiyur:
                    {
                        destinationT70=destinationsT70[8];
                        break;
                    }
                }
                destination=destinationT70;
            }

            try {

                String origin="="+urls[0]+","+urls[1];

                String URL="https://maps.googleapis.com/maps/api/directions/json?origin"+origin+"&destination="+destination+"&sensor=false&mode=driving&key=AIzaSyAkVgHeJ7P4QAmCRjX6LK7YlTpCuAcdjDQ";


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
            Integer time=0;
            try {
                jsonObject = new JSONObject(result);

                JSONArray routesArray = jsonObject.getJSONArray("routes");

                JSONObject route = routesArray.getJSONObject(0);

                JSONArray legs = route.getJSONArray("legs");

                JSONObject leg = legs.getJSONObject(0);


                for(int i=0;i<leg.length();i++){

                    //time+=Integer.parseInt(legs.getJSONObject(i).toString());
                    JSONObject t=legs.getJSONObject(i);
                    JSONObject durationObject = t.getJSONObject("duration");
                    duration = durationObject.getString("value");

                    time+=Integer.parseInt(duration);


                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            //this.progressDialog.dismiss();

            int sec;


           final  float t=(float)time/60;

            time=time/60;

            sec=(int)(60*(t-time));

            final Integer finalDuration = time;

            final Integer fsec=sec;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                   // Toast.makeText(getApplicationContext(), "Duration:" + finalDuration +" mins and"+" "+fsec+" seconds", Toast.LENGTH_LONG).show();
                    durr.setText("Estd.Time of arrival:" + finalDuration+" mins and "+fsec+" seconds");
                    durr.setTextColor(Color.RED);
                }
            });

        }
    }
}

