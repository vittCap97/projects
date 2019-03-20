package com.example.fernet.easy_fut5al;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ActivityAtleta extends AppCompatActivity implements DialogCercaPartita.Communicator{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TextView mTextMessage;
    private MenuItem iconaNotifica;
    private FragmentManager fm;
    private Fragment frammentoCorrenteVisualizzato;
    private MenuItem email;
    private String queryCitta;
    private SharedPreferences.Editor editor;
    private String partitaScelta;
    private static String URL = "UniscitiServlet";
    private DialogVisualizzaInviti dialog;
    private ArrayList<JsonObject> inviti;


    //Menu in basso
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override //Al click di un item del menu in basso
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if(frammentoCorrenteVisualizzato!=null){
                fm.beginTransaction().remove(frammentoCorrenteVisualizzato).commit();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            mViewPager.setVisibility(View.INVISIBLE);

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setVisibility(View.VISIBLE);
                    aggiornaHome();
                    return true;

                case R.id.navigation_calendario:
                    frammentoCorrenteVisualizzato = new FragmentCalendario();
                    fm.beginTransaction().add(R.id.frammento, frammentoCorrenteVisualizzato).commit();
                    return true;

                case R.id.navigation_new:
                    frammentoCorrenteVisualizzato = new FragmentNuovaPartita();
                    fm.beginTransaction().add(R.id.frammento, frammentoCorrenteVisualizzato).commit();
                    return true;

                case R.id.navigation_profilo:
                    frammentoCorrenteVisualizzato = new Profilo();
                    fm.beginTransaction().add(R.id.frammento, frammentoCorrenteVisualizzato).commit();
                    return true;

                case R.id.navigation_search:
                    mViewPager.setVisibility(View.VISIBLE);
                    ShowDialogBox();
                    return true;


            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atleta);


        editor = getSharedPreferences("DatiApplicazione", MODE_PRIVATE).edit();
        viewAllMatch();

        fm = getFragmentManager();
        FrameLayout frame = findViewById(R.id.frammento);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#303A3D")));

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        aggiornaHome();

        //Inizia l'abilitazione alla ricezione inviti
        inviti = new ArrayList<>();
        Timer timerObj = new Timer();
        TimerTask controlloNotifiche = new TimerTask() {

            public void run() {
                @SuppressLint("StaticFieldLeak") ConnectionTask task = new ConnectionTask("CheckInvitiServlet",getApplicationContext()) {
                    @Override
                    protected void inviaDatiAlServer() {
                        try {
                            getConnessione().setDoOutput(true);//abilita la scrittura
                            OutputStreamWriter wr = new OutputStreamWriter(getConnessione().getOutputStream());

                            //Spedisci nome atleta
                            SharedPreferences prefs = getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                            String Email = prefs.getString("MyEmail", null);
                            wr.write(Email+"\n");

                            //Spedisci codice partita scelta
                            wr.write(partitaScelta+"\n");

                            wr.flush();

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    protected void gestisciRispostaServer() {

                        if(getOutputDalServer()==null) return;

                        //ho degli inviti, si dovrebbe illuminare


                        for (String invito : getOutputDalServer()) {
                            JsonObject jsonObject;
                            try {
                                jsonObject = new JsonParser().parse(invito).getAsJsonObject();
                            } catch (Exception e) {
                                jsonObject = null;
                            }

                            if (jsonObject != null) {
                                if (!inviti.contains(jsonObject)){ //se non è un invito che ho già trovato.. aggiungo agli inviti
                                    inviti.add(jsonObject);
                                   if(iconaNotifica!=null) iconaNotifica.setIcon(getResources().getDrawable(R.drawable.ic_notifications_yellow_24dp));

                                }
                            }
                        }
                        dialog =new DialogVisualizzaInviti(ActivityAtleta.this, inviti);
                    }};
                task.execute();
            }
        };
        timerObj.schedule(controlloNotifiche, 0, 60000); //Controlla una volta al minutp


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
            dialog.show();
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
        //Nada
    }


    public  void ShowDialogBox(){
        DialogCercaPartita customDialog =new DialogCercaPartita(this);
        customDialog.setCommunicator(this);
        customDialog.show();

    }

    //Prende dati dalla dialog box e salva la preferenza
    @Override
    public void respond(String CittaScelta) {
        SharedPreferences.Editor editor = getSharedPreferences("DatiApplicazione", MODE_PRIVATE).edit();
        editor.putString("queryCittaPartita", CittaScelta);
        editor.apply();

        aggiornaHome();

    }


    public void  aggiornaHome(){
        //Aggiorna home
        mViewPager =  findViewById(R.id.sezione);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    public void viewAllMatch(){
        editor.putString("queryCittaPartita", ""); //per comodità. con "" si intendono tutte le partite
        editor.apply();
    }


    //Funzione associata al tasto UNISCITI
    public void uniscitiPartitaPubblica(View view) {

        partitaScelta = (String) view.getTag();

        ConnectionTask task = new ConnectionTask(URL,getApplicationContext()) {
            @Override
            protected void inviaDatiAlServer() {
                try {
                    getConnessione().setDoOutput(true);//abilita la scrittura
                    OutputStreamWriter wr = new OutputStreamWriter(getConnessione().getOutputStream());

                    //Spedisci nome atleta
                    SharedPreferences prefs = getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                    String Email = prefs.getString("MyEmail", null);
                    wr.write(Email+"\n");

                    //Spedisci codice partita scelta
                    wr.write(partitaScelta+"\n");

                    wr.flush();

                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            protected void gestisciRispostaServer() {
                System.out.println("Risposta del server:"+getOutputDalServer().get(0)+"|");
                if(getOutputDalServer().get(0).equals("Operazione terminata con successo!")){
                    Toast.makeText(ActivityAtleta.this,"Ti sei unito alla partita!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ActivityAtleta.this, "Impossibile unirsi alla partita, già partecipi a questa partita per caso?", Toast.LENGTH_LONG).show();
                }
            }
        };
        task.execute();
    }
}

