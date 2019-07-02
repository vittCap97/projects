package com.example.fernet.easy_fut5al;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.OutputStreamWriter;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class DialogVisualizzaInviti extends Dialog {
    public Activity activity;
    public Dialog dialog;
    private ListView listView;
    private List<JsonObject> Inviti;


    public DialogVisualizzaInviti(Activity a, List<JsonObject> inviti) {
        super(a);
        this.activity = a;
        Inviti = inviti;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bacheca_inviti);

        listView = findViewById(R.id.partite_invitato);

        CustomAdapter customAdapter = new CustomAdapter(getContext(), R.layout.list_elem_inviti, Inviti);
        listView.setAdapter(customAdapter); //Magari questa operazione la faccio dentro al task asincrono

    }


    class CustomAdapter extends ArrayAdapter<JsonObject> {
        private int resource;
        private LayoutInflater inflater;

        public CustomAdapter(Context context, int resourceId, List<JsonObject> objects) {
            super(context, resourceId, objects);
            resource = resourceId;
            inflater = LayoutInflater.from(getContext());

        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            if (v == null) {
                v = inflater.inflate(R.layout.list_elem_inviti, null);

            }

            JsonObject p = getItem(position);

            TextView nomeCampetto;
            TextView ora;
            TextView codice;
            TextView citta;
            TextView tariffa;
            TextView data;
            final Button partecipa;
            final Button rifiuta;

            partecipa =  v.findViewById(R.id.partecipa);
            partecipa.setTag(p.get("CodicePartita").getAsString());
            rifiuta = v.findViewById(R.id.rifiuta);
            rifiuta.setTag(p.get("CodicePartita").getAsString());


            partecipa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Do forfait per cancellare il gioca con stato "riservato e fare una nuova partecipazione con lo stato "Sicuro"
                    daiForfait(partecipa.getTag());
                    uniscitiPartitaPubblica(partecipa.getTag());
                    listView.setVisibility(View.INVISIBLE);
                }});


            rifiuta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daiForfait(rifiuta.getTag());
                    listView.setVisibility(View.INVISIBLE);

                }});


            nomeCampetto = v.findViewById(R.id.nome_campo);
            ora = v.findViewById(R.id.ora);
            codice = v.findViewById(R.id.cod_partita);
            citta = v.findViewById(R.id.citta);
            tariffa = v.findViewById(R.id.Tariffa);
            data = v.findViewById(R.id.data);

            nomeCampetto.setText(p.get("NomeCampetto").getAsString());
            ora.setText("Ore\n"+p.get("Ora").getAsString());
            codice.setText("cod. partita:"+p.get("CodicePartita").getAsString());
            citta.setText(p.get("Citta").getAsString());
            tariffa.setText("Quota singola:"+p.get("Tariffa").getAsString());
            data.setText(p.get("Data").getAsString());


            return v;
        }




        public void daiForfait(final Object tag) {
            String URL = "ForfaitServlet";
            ConnectionTask task3 = new ConnectionTask(URL,getContext()) {
                @Override
                protected void inviaDatiAlServer() {
                    try {
                        //ricava nome utente
                        SharedPreferences prefs = getContext().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                        String Email = prefs.getString("MyEmail", null);
                        System.out.println(" email:"+Email+" partita="+tag.toString());
                        String data = "partita="+tag.toString()+"&MyEmail="+Email;

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
                    if(getOutputDalServer().get(0).equals("Rimozione completata"))
                        Toast.makeText(getContext().getApplicationContext(), "Hai rifiutato l'invito", Toast.LENGTH_LONG).show();

                    else Toast.makeText(getContext().getApplicationContext(), "Ops, abbiamo riscontrato un problema", Toast.LENGTH_LONG).show();

                }};
            task3.execute();
        }



        public void uniscitiPartitaPubblica(final Object tag) {

            final String partitaScelta = (String) tag.toString();
            String URL = "UniscitiServlet";

            ConnectionTask task = new ConnectionTask(URL,getContext()) {
                @Override
                protected void inviaDatiAlServer() {
                    try {
                        getConnessione().setDoOutput(true);//abilita la scrittura
                        OutputStreamWriter wr = new OutputStreamWriter(getConnessione().getOutputStream());

                        //Spedisci nome atleta
                        SharedPreferences prefs = getContext().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
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
                        Toast.makeText(getContext().getApplicationContext(),"Hai confermato la partecipazione alla partita!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getContext().getApplicationContext(), "Impossibile partecipare alla partita", Toast.LENGTH_LONG).show();
                    }
                }
            };
            task.execute();
        }


    }













}









