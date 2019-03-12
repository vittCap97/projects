package com.example.fernet.easy_fut5al;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Fragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static android.content.Context.MODE_PRIVATE;


public class FragmentCalendario extends Fragment {

    public static  String URL;
    private  AutoCompleteTextView casella;
    private ArrayList<String> partite;
    private CalendarView calendario;
    private LinearLayout griglia;
    private  TextView titolo;
    private  TextView descrizione;
    private static boolean modalitaPrenota;
    private static String orario;
    private static boolean Disponibilitaorario;
    private static Fragment FragmentFather;

    public FragmentCalendario(){
        modalitaPrenota = false;
        Disponibilitaorario = false;
    }

    public static FragmentCalendario newInstance(boolean descrizionevisibile, String orarioDatoInInput, Fragment padre ) {

        FragmentCalendario fragment = new FragmentCalendario();
        modalitaPrenota = descrizionevisibile;
        orario= orarioDatoInInput;
        FragmentFather = padre;
        return fragment;
    }

    public static boolean isDisponibilitaorario() {
        return Disponibilitaorario;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.calendario, container, false);
        titolo = v.findViewById(R.id.TitoloCalendario);
        descrizione = v.findViewById(R.id.descrizione);
        casella = v.findViewById(R.id.autoCompleteTextView);
        Button ok = v.findViewById(R.id.ButtonOk);
        calendario = v.findViewById(R.id.calendario);
        calendario.setVisibility(View.GONE);
        griglia = v.findViewById(R.id.Griglia);
        griglia.setVisibility(View.GONE);

        if(modalitaPrenota == true) oscuraDescrizioni();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendario.setVisibility(View.VISIBLE);
                griglia.setVisibility(View.VISIBLE);

            }}); //Per quel campetto scelto

        casella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendario.setVisibility(View.GONE);
                griglia.setVisibility(View.GONE);

            }}); //Per quel campetto scelto;

        casella.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) casella.showDropDown();
            }
        });

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {

                    int anno = i;
                    String mese = String.format("%02d", i1+1);
                    String giorno = String.format("%02d", i2);;

                    String DataScelta = i+"-"+mese+"-"+giorno;
                    ArrayList<String> orariOccupati = OrariOccupatiXcampetto(casella.getText().toString(), DataScelta);

                    griglia.removeAllViews();

                    if(orariOccupati.size()==0){ //non ci sono partite nell'orarioscelto
                        TextView b = new TextView(griglia.getContext());
                        b.setPadding(10,10,10,10);
                        b.setText("Non ci sono partite in programma per questa data");
                        b.setBackgroundColor(Color.YELLOW);
                        griglia.addView(b);
                        if(modalitaPrenota){
                            Disponibilitaorario = true;
                            Toast.makeText(getActivity().getApplicationContext(), "Orario scelto disponibile! :)", Toast.LENGTH_LONG).show();
                            FragmentNuovaPartita f = (FragmentNuovaPartita) FragmentFather;
                            f.continuaCreazionePartita(DataScelta, casella.getText().toString());
                        }
                    }
                    for(int j=0; j< orariOccupati.size(); j++){
                        TextView b = new TextView(griglia.getContext());
                        b.setPadding(10,10,10,10);
                        int orarioOccupato = Integer.parseInt(orariOccupati.get(j));
                        int orariofinale = orarioOccupato+1;
                        b.setText("Occupato dalle "+orarioOccupato+" alle "+orariofinale);
                        b.setBackgroundColor(Color.YELLOW);
                        griglia.addView(b);
                        if(modalitaPrenota) { //Modalita che usa creazione partita
                            System.out.println("confronto"+orariOccupati.get(j).equals(orario));
                            if (orariOccupati.get(j).equals(orario)) {//se l'orario scelto è un orario occupato
                                Disponibilitaorario = false;
                                Toast.makeText(getActivity().getApplicationContext(), "Orario scelto non disponibile... :(", Toast.LENGTH_LONG).show();
                                FragmentNuovaPartita f = (FragmentNuovaPartita) FragmentFather;
                                f.continuaCreazionePartita("Data scelta non disponibile", casella.getText().toString());

                            } else {
                                Disponibilitaorario = true;
                                Toast.makeText(getActivity().getApplicationContext(), "Orario scelto disponibile! :)", Toast.LENGTH_LONG).show();
                                FragmentNuovaPartita f = (FragmentNuovaPartita) FragmentFather;
                                f.continuaCreazionePartita(DataScelta, casella.getText().toString());
                                break;
                            }
                        }
                    }

                }
            });
        getInfoTask task = new getInfoTask();
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
        URL = prefs.getString("URLserver", null) + "/EasyFut5al/GetInfoServlet";
        task.execute(new String[] {URL});

        return v;
    }



    public void oscuraDescrizioni(){
        titolo.setVisibility(View.GONE);
        descrizione.setVisibility(View.GONE);
    }






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
                    System.out.println(i + " Campetti: " + a);
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
                String data = "tipo_oggetto=Campetto";
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
            casella.setAdapter(arrayAdapter);


        }
    }
    //---------------------------------------------

    public ArrayList<String> OrariOccupatiXcampetto(String CampettoScelto, String DataScelta) {

        ArrayList<String>  OrariOccupati = new ArrayList<>();

        //Leggi file con le partite(prese dall'asynch task.....(si modificherà in modo che prenda anche quelle sicure))
        try {
            FileInputStream fis = getActivity().getApplicationContext().openFileInput("Partite");
            ObjectInputStream is = new ObjectInputStream(fis);
            partite = (ArrayList<String>) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (String partita : partite ) {
            if(!partita.isEmpty()) {
                JsonObject jsonObject;
                    jsonObject  = new JsonParser().parse(partita).getAsJsonObject();
                    if(!(jsonObject.isJsonNull())) {
                        System.out.println("Match:" + jsonObject.toString());
                        System.out.println(CampettoScelto + " " + jsonObject.get("NomeCampetto").getAsString()+","+DataScelta+" "+jsonObject.get("Data").getAsString());
                        if (CampettoScelto.equals(jsonObject.get("NomeCampetto").getAsString()) && DataScelta.equals(jsonObject.get("Data").getAsString())) {
                            System.out.println("HURRRRRRRRA");
                            OrariOccupati.add(jsonObject.get("Ora").getAsString());
                        }


                    }
            }

        }
        return  OrariOccupati;
    }

}
