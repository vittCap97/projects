package com.example.fernet.easy_fut5al;
import android.content.SharedPreferences;
import android.view.View;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;


public class Profilo  extends Fragment {

    private View v;
    private JsonObject infopersonali;
    private TextView Nome;
    private TextView Cognome;
    private TextView Username;
    private TextView Email;
    private TextView Passowrd;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profilo, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
        String ruolo = prefs.getString("MyEmail", null);
        System.out.println(ruolo);
        boolean Iamatleta = prefs.getBoolean("IamAtleta", false);

        if(Iamatleta){
            LinearLayout latleta = v.findViewById(R.id.sezioneAtleta);
            latleta.setVisibility(View.VISIBLE);
            setAtletaProfilo();

        }
        else {
            LinearLayout lgestore = v.findViewById(R.id.sezioneGestore);
            lgestore.setVisibility(View.VISIBLE);
            setGestoreProfilo();
        }



        return v;
    }

    private void setGestoreProfilo() {
        ConnectionTask task = new ConnectionTask("GetProfiloServlet", getActivity().getApplicationContext()) {
            @Override
            protected void inviaDatiAlServer() {
                try {
                    //ricava nome utente
                    SharedPreferences prefs = getActivity().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                    String Email = prefs.getString("MyEmail", null);
                    String data = "IamAtleta=No&MyEmail="+Email;

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
                if(!getOutputDalServer().get(0).equals("Errore!")){
                    infopersonali = new JsonParser().parse(getOutputDalServer().get(0)).getAsJsonObject();
                    Nome = v.findViewById(R.id.Nome);
                    Nome.setText(infopersonali.get("Nome").getAsString());
                    Cognome = v.findViewById(R.id.Cognome);
                    Cognome.setText(infopersonali.get("Cognome").getAsString());
                    Username = v.findViewById(R.id.Username);
                    Username.setText(infopersonali.get("Username").getAsString());
                    Email = v.findViewById(R.id.Email);
                    Email.setText(infopersonali.get("Email").getAsString());
                    Passowrd = v.findViewById(R.id.Passord);
                    Passowrd.setText(infopersonali.get("Password").getAsString());

                    TextView NomeCampetto = v.findViewById(R.id.NomeCampetto);
                    NomeCampetto.setText(infopersonali.get("NomeCampetto").getAsString());

                    TextView Tariffa =  v.findViewById(R.id.Tariffa);
                    Tariffa.setText(infopersonali.get("Tariffa").getAsString());

                    TextView Valutazione =  v.findViewById(R.id.Valutazione);
                    Valutazione.setText(infopersonali.get("Valutazione").getAsString());

                }

            }
        };
        task.execute();
    }

    private void setAtletaProfilo() {

        ConnectionTask task = new ConnectionTask("GetProfiloServlet", getActivity().getApplicationContext()) {
            @Override
            protected void inviaDatiAlServer() {
                try {
                    //ricava nome utente
                    SharedPreferences prefs = getActivity().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                    String Email = prefs.getString("MyEmail", null);
                    String data = "IamAtleta=Si&MyEmail="+Email;

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
                if(!getOutputDalServer().get(0).equals("Errore!")){
                    infopersonali = new JsonParser().parse(getOutputDalServer().get(0)).getAsJsonObject();
                    //setta widget atleta(visibilità solo linearLayout Atleta)
                    Nome = v.findViewById(R.id.Nome);
                    Nome.setText(infopersonali.get("Nome").getAsString());
                    Cognome = v.findViewById(R.id.Cognome);
                    Cognome.setText(infopersonali.get("Cognome").getAsString());
                    Username = v.findViewById(R.id.Username);
                    Username.setText(infopersonali.get("Username").getAsString());
                    Email = v.findViewById(R.id.Email);
                    Email.setText(infopersonali.get("Email").getAsString());
                    Passowrd = v.findViewById(R.id.Passord);
                    Passowrd.setText(infopersonali.get("Password").getAsString());

                    TextView DataDiNascita = v.findViewById(R.id.DataDiNascita);
                    DataDiNascita.setText(infopersonali.get("DataDiNascita").getAsString());

                    TextView Residenza= v.findViewById(R.id.Citta);
                    Residenza.setText(infopersonali.get("Residenza").getAsString());

                    TextView Ruolo = v.findViewById(R.id.Ruolo);
                    Ruolo.setText(infopersonali.get("Ruolo").getAsString());


                }

            }
        };
        task.execute();

    }

}
