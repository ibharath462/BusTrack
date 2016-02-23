package com.example.bharath.bustrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class BusStop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




       final  MaterialSpinner stop = (MaterialSpinner) findViewById(R.id.stop);
        final MaterialSpinner bus = (MaterialSpinner) findViewById(R.id.bus);

        info.hoang8f.widget.FButton go;

        go=(info.hoang8f.widget.FButton)findViewById(R.id.primary_button);


        bus.setItems("T70", "45A", "23C","5A");
        stop.setItems("CMBT", "MMDA_Colony", "Jaffarkhanpet", "Cipet", "Guindy_Tvk_Estate", "Anna_University", "Adyar_BS", "Adyar_Depot", "Thiruvanmiyur");

        bus.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                switch (position) {
                    case 3: {
                        stop.setItems("Mylapore", "Mandaveli", "AMS_Hospital", "Adayar", "Anna_University", "Saidapet", "T_Nagar");
                        break;
                    }
                    case 2: {
                        stop.setItems("Broadway", "Secretariat", "Chepauk", "Q.M.C", "Fore Shore Estate", "A.M.S.Hospital", "Adayar B.S.", "Eng.College", "Concorde ", "Jn.Of Race Course Rd", "Gurunanak College", "Velachery");
                    }
                    case 1: {
                        stop.setItems("Thiruvanmiyur", "Adyar depot", "Adyar B.S.", "Anna Universtiy", "Guindy RS", "Butt road", "Ramapuram", "Mugalivakkam", "Porur", "Ramachandra Medical college");
                    }
                    case 0: {
                        stop.setItems("CMBT","MMDA_Colony","Jaffarkhanpet", "Cipet", "Guindy_Tvk_Estate", "Anna_University", "Adyar_BS", "Adyar_Depot", "Thiruvanmiyur");
                    }
                }
            }
        });

        int t=0;

        stop.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {


            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(BusStop.this,MainActivity.class).putExtra("stop",stop.getText().toString()).putExtra("bus",bus.getText().toString());
                startActivity(i);
            }
        });

    }

}
