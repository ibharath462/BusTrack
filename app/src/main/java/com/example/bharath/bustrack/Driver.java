package com.example.bharath.bustrack;

import android.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class Driver extends AppCompatActivity implements LocationListener {


    Firebase Btrack = null;

    info.hoang8f.widget.FButton logout;

    com.rey.material.widget.RadioButton from,to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        Firebase.setAndroidContext(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String bus = null;

       String stop=null;

        bus = getIntent().getStringExtra("bus");

        logout = (info.hoang8f.widget.FButton) findViewById(R.id.primary_button);

        from=(com.rey.material.widget.RadioButton)findViewById(R.id.from);
        to=(com.rey.material.widget.RadioButton)findViewById(R.id.to);

        //Toast.makeText(getApplicationContext(),""+bus,Toast.LENGTH_SHORT).show();


        if (bus.equals("5A")) {

            Btrack = new Firebase("https://bustrack.firebaseio.com/bus/0/");
            stop="Mylapore";
        } else if (bus.equals("T70")) {
            Btrack = new Firebase("https://bustrack.firebaseio.com/bus/1/");
            stop="Koyambedu";
        }

        from.setText("From "+stop);
        to.setText("To "+stop);

        final String finalStop = stop;
        from.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Btrack.child("id").setValue("From " + finalStop);
                    to.setChecked(false);
                    from.setChecked(true);
                }

            }
        });

        to.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Btrack.child("id").setValue("To " + finalStop);
                    from.setChecked(false);
                    to.setChecked(true);
                }

            }
        });


        final LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Btrack.unauth();
                if (ActivityCompat.checkSelfPermission(Driver.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Driver.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.removeUpdates(Driver.this);
                Intent i=new Intent(Driver.this,Home.class);
                startActivity(i);
                finish();

            }
        });


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);



    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }



    @Override
    public void onLocationChanged(Location location) {


            Toast.makeText(getApplicationContext(), "Location changed,", Toast.LENGTH_SHORT).show();
            Btrack.child("latitude").setValue(location.getLatitude());
            Btrack.child("longitude").setValue(location.getLongitude());


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
