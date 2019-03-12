package com.example.fernet.easy_fut5al;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class FragmentNuovaPartita  extends Fragment {

    private ArrayList<String> partite;
    private ArrayList<String> CampettiDisponibili;
    public static  String URL;
    private AutoCompleteTextView textView;
    private ListView listData;
    private ListView listHour;
    private AutoCompleteTextView casellaorario;
    private Button ok;
    private String[] orari= {"6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
    private FrameLayout cal;
    private View v;
    private LinearLayout layoutinviti;
    private  AutoCompleteTextView CasellaNuovoInvitato;
    private Button buttonInvita;
    private Button conferma;
    private  ArrayList<String> lista_inv;
    private LinearLayout listapartecipanti;
    private  int nNinvitati =0;
    private String DataOttenuta;
    private String campettoScelto;


    public FragmentNuovaPartita(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.crea_nuova_partita, container, false);
        listapartecipanti = v.findViewById(R.id.listaInvitati);
        conferma = v.findViewById(R.id.fine);
        layoutinviti = v.findViewById(R.id.invitatiMenu);
        buttonInvita = v.findViewById(R.id.confermaInvitato);
        CasellaNuovoInvitato = v.findViewById(R.id.inserisciInvitato);
        CasellaNuovoInvitato.showDropDown();
        cal = v.findViewById(R.id.calendary);
        cal.setVisibility(View.GONE);
        ok = v.findViewById(R.id.ButtonOk);

        casellaorario = v.findViewById(R.id.casellaorario);
        casellaorario.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) casellaorario.showDropDown();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_element, R.id.textViewList,  orari);
        casellaorario.setAdapter(arrayAdapter);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.setVisibility(View.VISIBLE);
                FragmentCalendario calendario = FragmentCalendario.newInstance(true, casellaorario.getText().toString(), FragmentNuovaPartita.this);
                getActivity().getFragmentManager().beginTransaction().add(R.id.calendary, calendario).commit();

            }});


        return v;
    }

    //Chiamato dal calendario
    public void  continuaCreazionePartita(String dataOttenuta, String CampettoScelto){
        DataOttenuta = dataOttenuta;
        campettoScelto = CampettoScelto;
        if(dataOttenuta.equals("Data scelta non disponibile")){
            layoutinviti.setVisibility(View.GONE);
            return;
        }
        System.out.println("Creazione partita su campetto libero il "+dataOttenuta+" alle ore "+ casellaorario.getText().toString());
        layoutinviti.setVisibility(View.VISIBLE);
        getInfoTask task = new getInfoTask();
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
        URL = prefs.getString("URLserver", null) + "/EasyFut5al/GetInfoServlet";
        task.execute(new String[] {URL});

        lista_inv = new ArrayList<>();

        buttonInvita.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vs) {
                if(nNinvitati>9) {
                    Toast.makeText(getActivity(), "Superato limite invitati!", Toast.LENGTH_LONG).show();
                    return;}
                nNinvitati++;

                TextView t = new TextView(getActivity().getApplicationContext());
                t.setText(CasellaNuovoInvitato.getText().toString());
                t.setBackgroundColor(getResources().getColor(R.color.gay));
                t.setTextSize(26);
                t.setPadding(5,5,5,5);
                t.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        v.setVisibility(View.GONE);
                        v.setTag("cancellato");
                        nNinvitati--;
                        TextView T = (TextView) v;
                        lista_inv.remove(T.getText().toString());

                        return false;
                    }
                } );

                listapartecipanti.addView(t);
                lista_inv.add(t.getText().toString());
            }

        });;


        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreaPartitaTask taskPartita = new CreaPartitaTask();
                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                URL = prefs.getString("URLserver", null) + "/EasyFut5al/CreaPartitaServlet";
                taskPartita.execute(new String[] {URL});

            }
        });







    }

//----------------------------------------------------------------------------------------
    private class getInfoTask extends AsyncTask<String, Void, ArrayList<String> > {



        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            ArrayList<String> output = null;
            for (String url : urls) {
                //Ricevi mex di riscontro dal Server di quell'URL
                output = getOutputFromUrl(url);
            }
            return output;
        }

        //Ricevi dati da Server
        private ArrayList<String> getOutputFromUrl(String url) {
            StringBuffer output = new StringBuffer("");
            ArrayList<String> Partite = new ArrayList<String>();
            try {
                InputStream stream = getHttpConnection(url);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
                String s = "";

                while ((s = buffer.readLine()) != null)
                    Partite.add(s);

                stream.close();

                int i = 1;
                for (String a : Partite) {
                    System.out.println(i + " Atleti: " + a);
                    i++;
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return Partite;
        }

        //Inizia una connessione Http e ritorna uno stream di input(CANALE DOVE LEGGERE DATI DAL SERVER)
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("POST");
                String data = "tipo_oggetto=Atleta";
                connection.setDoOutput(true);//abilita la scrittura
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(data);//scrittura del content
                wr.flush();
                httpConnection.connect();
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }



        //lavora sui dati presi dal server, dopo l'esecuzione del compito asincrono
        @Override
        protected void onPostExecute(ArrayList<String> output) {

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_element, R.id.textViewList,  output);
            CasellaNuovoInvitato.setAdapter(arrayAdapter);
            CasellaNuovoInvitato.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    CasellaNuovoInvitato.showDropDown();
                }
            });
        }
    }
    //---------------------------------------------




    private class  CreaPartitaTask extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... urls) {
            String output = null;
            for (String url : urls) {
                //Ricevi mex di riscontro dal Server di quell'URL
                output = getOutputFromUrl(url);
            }
            return output;
        }

        //Ricevi dati da Server
        private String getOutputFromUrl(String url) {
            StringBuffer output = new StringBuffer("");
            String esito = null;
            try {
                InputStream stream = getHttpConnection(url);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
                String s = "";

                esito =  buffer.readLine();

                stream.close();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return esito;
        }

        //Inizia una connessione Http e ritorna uno stream di input(CANALE DOVE LEGGERE DATI DAL SERVER)
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("POST");

                String data = "tipo_oggetto=Atleta";
                connection.setDoOutput(true);//abilita la scrittura
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

                //Spedisci nome organizzatore
                SharedPreferences prefs = getActivity().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                String Email = prefs.getString("MyEmail", null);
                wr.write(Email+"\n");

                //spedisco orario
                wr.write(casellaorario.getText().toString()+"\n");

                //Spedisco data
                wr.write(DataOttenuta+"\n");

                //spedisco nome campetto
                wr.write(campettoScelto+"\n");

                //Spedisco nomi inviati
                for(String invitati: lista_inv) {
                    wr.write(invitati+"\n");
                }


                wr.flush();


                httpConnection.connect();
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }



        //lavora sui dati presi dal server, dopo l'esecuzione del compito asincrono
        @Override
        protected void onPostExecute(String output) {
            if(output.equals("Partita registrata con successo!")){
                Toast.makeText(getActivity(), "Partita creata con successo!", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getActivity(), "Errore creazione partita.. riprova :(", Toast.LENGTH_LONG).show();
            }
        }
    }
    //---------------------------------------------








}
