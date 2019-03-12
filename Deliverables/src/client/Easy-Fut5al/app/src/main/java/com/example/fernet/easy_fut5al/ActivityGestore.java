package com.example.fernet.easy_fut5al;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;



public class ActivityGestore extends AppCompatActivity {

    private MenuItem iconaNotifica;
    private MenuItem email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestore);

    }

    //Alla creazione del menu sopra
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        iconaNotifica = menu.findItem(R.id.action_notifica);

        //ricava nome utente
        SharedPreferences prefs = getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
        String Email = prefs.getString("MyEmail", null);
        email =  menu.findItem(R.id.myemail);
        email.setTitle(Email);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Al click della notifica
        if (id == R.id.action_notifica) {
            System.out.println("Controlla notificheee");
            iconaNotifica.setIcon(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
        }

        if(id == R.id.logout){
            System.out.println("Logout in corso...");
            //Rimuovi eventuali dati vecchi di autenticazione
            SharedPreferences sharedPreferences = getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //Se premo indietro chiudo app
    @Override
    public void onBackPressed() {
        //nanda
    }





}
