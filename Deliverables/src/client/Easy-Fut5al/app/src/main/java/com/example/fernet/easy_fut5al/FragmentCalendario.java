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
    public static  String URL2;

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
    private boolean campettofissato;
    private String NomeCampettoFissato;

    public FragmentCalendario(){
        modalitaPrenota = false;
        Disponibilitaorario = false;
        campettofissato = false;
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




    //Per gestore
    public void fissaCampetto(String CampettoSpecificato){
        campettofissato = true;
        NomeCampettoFissato = CampettoSpecificato;

    }



    public void trovaPartite(){
        ConnectionTask task1 = new ConnectionTask(URL2, getActivity().getApplicationContext()) {
            @Override
            protected void inviaDatiAlServer() {
                try {
                    //ricava nome utente
                    SharedPreferences prefs = getActivity().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                    String Email = prefs.getString("MyEmail", null);
                    String data = "tipo_partite=DaGiocare&miapartecipazione=indifferente&MyEmail="+Email;
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
                partite = getOutputDalServer();
            }
        };
        task1.execute();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        URL = "GetInfoServlet";
        URL2 = "CercaPartiteServlet";
        View v = inflater.inflate(R.layout.calendario, container, false);
        titolo = v.findViewById(R.id.TitoloCalendario);
        descrizione = v.findViewById(R.id.descrizione);
        casella = v.findViewById(R.id.autoCompleteTextView);
        Button ok = v.findViewById(R.id.ButtonOk);
        calendario = v.findViewById(R.id.calendario);
        calendario.setVisibility(View.GONE);
        griglia = v.findViewById(R.id.Griglia);
        griglia.setVisibility(View.GONE);

        trovaPartite();

        if(campettofissato){
            casella.setText(NomeCampettoFissato);
            casella.setEnabled(false);
            calendario.setVisibility(View.VISIBLE);
            griglia.setVisibility(View.VISIBLE);

        }



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
                trovaPartite();
                if(hasFocus) casella.showDropDown();
            }
        });

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                    trovaPartite();

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
        ConnectionTask task = new ConnectionTask(URL,getActivity().getApplicationContext()) {
            @Override
            protected void inviaDatiAlServer() {
                try {
                    String data = "tipo_oggetto=Campetto";
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
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_element, R.id.textViewList,  getOutputDalServer());
                casella.setAdapter(arrayAdapter);
            }
        };
        task.execute();

        return v;
    }



    public void oscuraDescrizioni(){
        titolo.setVisibility(View.GONE);
        descrizione.setVisibility(View.GONE);
    }




    public ArrayList<String> OrariOccupatiXcampetto(String CampettoScelto, String DataScelta) {

        ArrayList<String>  OrariOccupati = new ArrayList<>();

        /*Leggi file con le partite(prese dall'asynch task.....(si modificherà in modo che prenda anche quelle sicure))
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
        }*/

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
