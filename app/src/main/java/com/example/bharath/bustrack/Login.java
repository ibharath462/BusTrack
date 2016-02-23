package com.example.bharath.bustrack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Login extends AppCompatActivity {

    info.hoang8f.widget.FButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);


        setContentView(R.layout.activity_login);


        final String[] bus = {null};

        final EditText user,pass;

         user=(EditText)findViewById(R.id.user);
         pass=(EditText)findViewById(R.id.pass);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        login=(info.hoang8f.widget.FButton)findViewById(R.id.primary_button);

        final ProgressDialog progressDialog = new ProgressDialog(Login.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Firebase fire=new Firebase("https://bustrack.firebaseio.com/");


                progressDialog.setMessage("Logging you in...");
                progressDialog.show();


                fire.authWithPassword(user.getText().toString(), pass.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        progressDialog.cancel();
                        String s=authData.getUid();
                        String a="9f4289de-03f7-4069-8f25-2cf33e7dfb74";
                        String b="927c79c5-ee68-4671-a32f-35294c8fd76f";
                        if(a.equals(s))
                        {
                            bus[0] ="5A";
                            Toast.makeText(getApplicationContext(),bus[0],Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(Login.this,Driver.class).putExtra("bus",bus[0]);
                            startActivity(i);
                            finish();
                        }
                        else if(b.equals(s))
                        {
                            bus[0] ="T70";
                            Toast.makeText(getApplicationContext(),bus[0],Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(Login.this,Driver.class).putExtra("bus",bus[0]);
                            startActivity(i);
                            finish();
                        }

                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(),"Invalid Username/Password",Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

    }

}
