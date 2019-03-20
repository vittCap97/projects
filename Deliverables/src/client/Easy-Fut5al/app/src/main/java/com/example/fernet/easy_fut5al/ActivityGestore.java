package com.example.fernet.easy_fut5al;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import java.io.OutputStreamWriter;


public class ActivityGestore extends AppCompatActivity {

    private MenuItem iconaNotifica;
    private MenuItem email;
    private FrameLayout cal;
    private FragmentCalendario calendario;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestore);



        ConnectionTask task = new ConnectionTask("/GetCampettoServlet", getApplicationContext()) {
            @Override
            protected void inviaDatiAlServer() {
                try {
                    SharedPreferences prefs = getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                    String Email = prefs.getString("MyEmail", null);

                    String data = "myEmail="+Email;
                    getConnessione().setDoOutput(true);//abilita la scrittura
                    OutputStreamWriter wr = new OutputStreamWriter(getConnessione().getOutputStream());
                    wr.write(data);//scrittura del content
                    wr.flush();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            protected void gestisciRispostaServer() {
                if(!getOutputDalServer().get(0).equals("Errore")){
                    cal = findViewById(R.id.calendary);
                    cal.setVisibility(View.VISIBLE);
                    calendario = FragmentCalendario.newInstance(false, null, null);
                    getFragmentManager().beginTransaction().add(R.id.calendary, calendario).commit();
                    calendario.fissaCampetto(getOutputDalServer().get(0));}

            }
        };
        task.execute();




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
