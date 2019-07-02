package com.example.fernet.easy_fut5al;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityAutentificazione extends AppCompatActivity {

    Button loginButton, registrazioneButton, registrazioneButtonGestore;
    private String Email;
    private String pwd;
    private String URLServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URLServer = "http://192.168.1.4:8080";

        SharedPreferences.Editor editor = getSharedPreferences("DatiApplicazione", MODE_PRIVATE).edit();
        editor.putString("URLserver", URLServer);
        editor.apply();

        //Vedi se hai salvato in precedenza email e password, in tal caso accedi direttamente
        SharedPreferences prefs = getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
        if (prefs.getString("MyEmail", null) != null && prefs.getString("Mypwd", null) != null) {
            Email = prefs.getString("MyEmail", null);
            pwd = prefs.getString("Mypwd", null);
            Boolean IamAtleta = prefs.getBoolean("IamAtleta", false);
            System.out.println("Accesso come " + Email);

            if (IamAtleta) {
                Intent openActivityGestore = new Intent(ActivityAutentificazione.this, ActivityAtleta.class);
                startActivity(openActivityGestore);
            } else {
                Intent openActivityGestore = new Intent(ActivityAutentificazione.this, ActivityGestore.class);
                startActivity(openActivityGestore);
            }
        }

        loginButton = (Button) findViewById(R.id.Login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLogin = new Intent(ActivityAutentificazione.this, LoginActivity.class);
                startActivity(openLogin);
            }
        });

        registrazioneButton = (Button) findViewById(R.id.Registrati);
        registrazioneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openRegistrationAtleta = new Intent(ActivityAutentificazione.this, RegistrationActivityAtleta.class);
                startActivity(openRegistrationAtleta);
            }
        });


        registrazioneButtonGestore = (Button) findViewById(R.id.RegGestore);
        registrazioneButtonGestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openRegistrationGestore = new Intent(ActivityAutentificazione.this, RegistrationActivityGestore.class);
                startActivity(openRegistrationGestore);
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences.Editor editor = getSharedPreferences("DatiApplicazione", MODE_PRIVATE).edit();
        editor.putString("URLserver", URLServer);
        editor.apply();


    }
}