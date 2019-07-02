package com.example.fernet.easy_fut5al;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Thread.sleep;

public  class SezioneHome extends  Fragment{

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public ListView listView;
    public ArrayList<JsonObject> partite;
    public ArrayList<JsonObject> Miepartite;
    private ArrayList<JsonObject> Miepartiteterminate;
    private ArrayList<JsonObject> MiePartiteDaGiocare;

    public static  String URL = "CercaPartiteServlet";
    private LayoutInflater Inflater;
    private ViewGroup Container;
    private static int pagina=0;

    public SezioneHome() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SezioneHome newInstance(int sectionNumber) {
        SezioneHome fragment = new SezioneHome();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);



        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = null;
       Inflater = inflater;
       Container = container;

        System.out.println("pagina "+pagina);

        //Faccio visualizzare solo la scheda che mi interessa(quella selezionata)
       //Se è la prima scheda
        if(getArguments().getInt(ARG_SECTION_NUMBER)==0) {
            partite = new ArrayList<>();
            ConnectionTask task = new ConnectionTask(URL, getActivity().getApplicationContext()) {
                @Override
                protected void inviaDatiAlServer() {
                    try {
                        //ricava nome utente
                        SharedPreferences prefs = getActivity().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                        String Email = prefs.getString("MyEmail", null);
                        String data = "tipo_partite=InSospeso&miapartecipazione=no&MyEmail="+Email;

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

                    if(getOutputDalServer()==null) {
                        Toast.makeText(getActivity().getApplicationContext(), "Nessuna partita trovata", Toast.LENGTH_LONG).show();
                        return;
                    }

                    /* Salvare file con partite trovate
                    try {
                        FileOutputStream fos = getContext().openFileOutput("Partite", Context.MODE_PRIVATE);
                        ObjectOutputStream os = new ObjectOutputStream(fos);
                        os.writeObject(getOutputDalServer());
                        os.close();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    SharedPreferences prefs = getActivity().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                    String CittaChevoglio = prefs.getString("queryCittaPartita", null);


                    for (String partita : getOutputDalServer()) {
                        JsonObject jsonObject;
                        try {
                            jsonObject = new JsonParser().parse(partita).getAsJsonObject();
                        } catch (Exception e) {
                            jsonObject = null;
                        }

                        if (jsonObject != null) {
                            String NomeCampettoCorrente = jsonObject.get("Citta").toString();
                            NomeCampettoCorrente = NomeCampettoCorrente.substring(1, NomeCampettoCorrente.length() - 1);

                            if (CittaChevoglio.equals(NomeCampettoCorrente) || CittaChevoglio.length() == 0)
                                partite.add(jsonObject);

                        }
                    }
                    listView = Inflater.inflate(R.layout.sezione_partite_pubbliche, Container).findViewById(R.id.lista_partite_pubbliche);
                    CustomAdapter customAdapter = new CustomAdapter(getContext(), R.layout.list_elem_partita, partite, true);
                    listView.setAdapter(customAdapter); //Magari questa operazione la faccio dentro al task asincrono
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String str = listView.getItemAtPosition(position).toString();
                            //Fai qualcosa
                        }
                    });
                }
            };
            task.execute();
            //Visualizz partite pubbliche
            rootView = inflater.inflate(R.layout.sezione_partite_pubbliche, container, false);
        }


        //Se è la seconda scheda(mie partite InSospeso/Terminate... forse faccio vedere anche quelle a cui partecipero')
        if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {
            Miepartiteterminate = new ArrayList<>();
            Miepartite = new ArrayList<>();
            MiePartiteDaGiocare = new ArrayList<>();

            ConnectionTask task2 = new ConnectionTask(URL,getActivity().getApplicationContext()) {
                @Override
                protected void inviaDatiAlServer() {
                    try {
                        //ricava nome utente
                        SharedPreferences prefs = getActivity().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                        String Email = prefs.getString("MyEmail", null);
                        String data = "tipo_partite=InSospeso&miapartecipazione=si&MyEmail="+Email;

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

                    if(getOutputDalServer()==null) {
                        Toast.makeText(getActivity().getApplicationContext(), "Nessuna partita trovata", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (String partita : getOutputDalServer()) {
                        JsonObject jsonObject;
                        try {
                            jsonObject = new JsonParser().parse(partita).getAsJsonObject();
                        } catch (Exception e) {
                            jsonObject = null;
                        }

                        if (jsonObject != null) Miepartite.add(jsonObject);

                    }
                    listView = Inflater.inflate(R.layout.mie_partite, Container).findViewById(R.id.partite_inCorso);
                    CustomAdapter customAdapter = new CustomAdapter(getContext(), R.layout.list_elem_partita, Miepartite, false);
                    listView.setAdapter(customAdapter); //Magari questa operazione la faccio dentro al task asincrono


            }};


            ConnectionTask task4 = new ConnectionTask(URL,getActivity().getApplicationContext()) {
                @Override
                protected void inviaDatiAlServer() {
                    try {
                        //ricava nome utente
                        SharedPreferences prefs = getActivity().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                        String Email = prefs.getString("MyEmail", null);
                        String data = "tipo_partite=DaGiocare&miapartecipazione=si&MyEmail="+Email;

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

                    if(getOutputDalServer()==null) {
                        Toast.makeText(getActivity().getApplicationContext(), "Nessuna partita trovata", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (String partita : getOutputDalServer()) {
                        JsonObject jsonObject;
                        try {
                            jsonObject = new JsonParser().parse(partita).getAsJsonObject();
                        } catch (Exception e) {
                            jsonObject = null;
                        }

                        if (jsonObject != null) MiePartiteDaGiocare.add(jsonObject);

                    }
                    listView = Inflater.inflate(R.layout.mie_partite, Container).findViewById(R.id.partite_DaGiocare);
                    CustomAdapter customAdapter = new CustomAdapter(getContext(), R.layout.list_elem_partita, MiePartiteDaGiocare, false);
                    listView.setAdapter(customAdapter); //Magari questa operazione la faccio dentro al task asincrono


                }};

            ConnectionTask task3 = new ConnectionTask(URL,getActivity().getApplicationContext()) {
                @Override
                protected void inviaDatiAlServer() {
                    try {
                        //ricava nome utente
                        SharedPreferences prefs = getActivity().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                        String Email = prefs.getString("MyEmail", null);
                        String data = "tipo_partite=Terminata&miapartecipazione=si&MyEmail="+Email;

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

                    if(getOutputDalServer()==null) {
                        Toast.makeText(getActivity().getApplicationContext(), "Nessuna partita trovata", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (String partita : getOutputDalServer()) {
                        JsonObject jsonObject;
                        try {
                            jsonObject = new JsonParser().parse(partita).getAsJsonObject();
                        } catch (Exception e) {
                            jsonObject = null;
                        }

                        if (jsonObject != null) Miepartiteterminate.add(jsonObject);

                    }
                    listView = Inflater.inflate(R.layout.mie_partite, Container).findViewById(R.id.lista_partite_giocate);
                    AdapterEndGame Adapter = new AdapterEndGame(getContext(), R.layout.list_elem_partite_davalutare, Miepartiteterminate);
                    listView.setAdapter(Adapter); //Magari questa operazione la faccio dentro al task asincrono


                }};
            task2.execute();
            task3.execute();
            task4.execute();

            rootView = inflater.inflate(R.layout.mie_partite, container, false);
        }

        return rootView;
    }





class CustomAdapter extends ArrayAdapter<JsonObject> {
    private int resource;
    private LayoutInflater inflater;
    private boolean mod_unisciti;

    public CustomAdapter(Context context, int resourceId, List<JsonObject> objects, boolean modUnisciti) {
        super(context, resourceId, objects);
        resource = resourceId;
        inflater = LayoutInflater.from(context);
        mod_unisciti = modUnisciti;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            v = inflater.inflate(R.layout.list_elem_partita, null);

        }

        JsonObject p = getItem(position);

        TextView nomeCampetto;
        TextView ora;
        TextView codice;
        TextView partecipanti;
        TextView citta;
        TextView tariffa;
        TextView data;
        final Button unisciti;


        //Così quando premerò unisciti avrò un riferimento alla partita
        unisciti = v.findViewById(R.id.tastoUnisciti);
        unisciti.setTag(p.get("CodicePartita").getAsString());

        //Modalità rimuovi anziché unisciti
        if(!mod_unisciti){
            unisciti.setText("Dai forfait");
            unisciti.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    daiForfait(unisciti.getTag());
                }
            });}

        nomeCampetto = v.findViewById(R.id.nome_campo);
        ora = v.findViewById(R.id.ora);
        codice = v.findViewById(R.id.cod_partita);
        partecipanti = v.findViewById(R.id.num_partecipanti);
        citta = v.findViewById(R.id.citta);
        tariffa = v.findViewById(R.id.Tariffa);
        data = v.findViewById(R.id.data);

        nomeCampetto.setText(p.get("NomeCampetto").getAsString());
        ora.setText("Ore\n"+p.get("Ora").getAsString());
        codice.setText("cod. partita:"+p.get("CodicePartita").getAsString());
        partecipanti.setText(p.get("NumPartecipanti").getAsString()+"\\10");
        citta.setText(p.get("Citta").getAsString());
        tariffa.setText("Quota singola:"+p.get("Tariffa").getAsString());
        data.setText(p.get("Data").getAsString());


        return v;
    }



    private void daiForfait(final Object tag) {
        String URL = "ForfaitServlet";
        ConnectionTask task3 = new ConnectionTask(URL,getActivity().getApplicationContext()) {
            @Override
            protected void inviaDatiAlServer() {
                try {
                    //ricava nome utente
                    SharedPreferences prefs = getActivity().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
                    String Email = prefs.getString("MyEmail", null);
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
                    Toast.makeText(getActivity().getApplicationContext(), "Hai appena dato buca alla partita!", Toast.LENGTH_LONG).show();
                else Toast.makeText(getActivity().getApplicationContext(), "Ops, abbiamo riscontrato un problema", Toast.LENGTH_LONG).show();

            }};
        task3.execute();
    }
}



    class AdapterEndGame extends ArrayAdapter<JsonObject> {
        private int resource;
        private LayoutInflater inflater;
        private boolean mod_unisciti;

        public AdapterEndGame(Context context, int resourceId, List<JsonObject> objects) {
            super(context, resourceId, objects);
            resource = resourceId;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            if (v == null) {
                v = inflater.inflate(R.layout.list_elem_partite_davalutare, null);

            }

            final JsonObject p = getItem(position);

            final TextView nomeCampetto;
            TextView ora;
            TextView codice;
            TextView citta;
            TextView tariffa;
            TextView data;
            RatingBar valutazione;

            nomeCampetto = v.findViewById(R.id.nome_campo);
            ora = v.findViewById(R.id.ora);
            codice = v.findViewById(R.id.cod_partita);
            citta = v.findViewById(R.id.citta);
            tariffa = v.findViewById(R.id.Tariffa);
            data = v.findViewById(R.id.data);
            valutazione = v.findViewById(R.id.ratingBar);


            nomeCampetto.setText(p.get("NomeCampetto").getAsString());
            ora.setText("Ore\n" + p.get("Ora").getAsString());
            codice.setText("cod. partita:" + p.get("CodicePartita").getAsString());
            citta.setText(p.get("Citta").getAsString());
            tariffa.setText("Quota singola:" + p.get("Tariffa").getAsString());
            data.setText(p.get("Data").getAsString());

            valutazione = (RatingBar) v.findViewById(R.id.ratingBar);
            valutazione.setMax(5);
            valutazione.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(RatingBar ratingBar, final float rating,
                                            boolean fromUser) {
                    ConnectionTask taskValuta = new ConnectionTask("ValutaCampettoServlet", getActivity().getApplicationContext()) {
                        @Override
                        protected void inviaDatiAlServer() {
                            try {
                                String data = "valutazione="+String.valueOf((int) rating)+"&campetto="+p.get("NomeCampetto").getAsString();
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
                            if(getOutputDalServer().get(0).equals("operazione completata"))
                                Toast.makeText(getActivity().getApplicationContext(), "Valutazione inviata!", Toast.LENGTH_LONG).show();
                        }
                    };
                    taskValuta.execute();


                }
            });


            return v;
        }
    }
}