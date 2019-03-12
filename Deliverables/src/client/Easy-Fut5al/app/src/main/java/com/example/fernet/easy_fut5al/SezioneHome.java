package com.example.fernet.easy_fut5al;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.widget.ToggleButton;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.System.*;

public  class SezioneHome extends  Fragment{

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public ListView listView;
    public ArrayList<JsonObject> partite;
    public static  String URL;

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


        //Faccio visualizzare solo la scheda che mi interessa(quella selezionata)
       //Se è la prima scheda
        if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {
            partite = new ArrayList<>();
            CercaPartitePubblicheTask task = new CercaPartitePubblicheTask(inflater,container);

            SharedPreferences prefs = getContext().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
            URL = prefs.getString("URLserver", null) + "/EasyFut5al/CercaPartiteServlet";

            task.execute(new String[] {URL});
            //Visualizz partite pubbliche
            rootView = inflater.inflate(R.layout.sezione_partite_pubbliche, container, false);

        }

        //Se è la seconda scheda
        else{
            rootView = inflater.inflate(R.layout.activity_main, container, false);
        }

        return rootView;
    }





    //++++++++++++++++++++++++++++++++++++++++++++++++
    private class CercaPartitePubblicheTask extends AsyncTask<String, Void, ArrayList<String> > {

        private LayoutInflater inflater;
        public ViewGroup container;


        public CercaPartitePubblicheTask(LayoutInflater inflater, ViewGroup container) {
            this.inflater = inflater;
            this.container = container;
        }


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
                //ricezione stringhe json
                while ((s = buffer.readLine()) != null)
                    Partite.add(s);

                stream.close();

                int i = 1;
                for (String a : Partite) {
                    out.println(i + " partita pubblica: " + a);
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


            // Salvare file con partite trovate
            try {
                FileOutputStream fos = getContext().openFileOutput("Partite", Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(output);
                os.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            SharedPreferences prefs = getActivity().getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
            String CittaChevoglio = prefs.getString("queryCittaPartita", null);


            for (String partita : output) {
                JsonObject jsonObject;
                try {
                    jsonObject = new JsonParser().parse(partita).getAsJsonObject();
                } catch (Exception e) {
                    out.println("Partita Vuota");
                    jsonObject = null;
                }

                if (jsonObject != null) {
                    String NomeCampettoCorrente = jsonObject.get("Citta").toString();
                    NomeCampettoCorrente = NomeCampettoCorrente.substring(1, NomeCampettoCorrente.length() - 1);

                    if (CittaChevoglio.equals(NomeCampettoCorrente) || CittaChevoglio.length() == 0)
                        partite.add(jsonObject);

                }
            }

            listView = inflater.inflate(R.layout.sezione_partite_pubbliche, container).findViewById(R.id.lista_partite_pubbliche);
            out.println("MMMMh " + partite);
            CustomAdapter customAdapter = new CustomAdapter(getContext(), R.layout.list_elem_partita, partite);
            listView.setAdapter(customAdapter); //Magari questa operazione la faccio dentro al task asincrono
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String str = listView.getItemAtPosition(position).toString();
                    //Fai qualcosa
                }
            });

        }
    }
    //---------------------------------------------


class CustomAdapter extends ArrayAdapter<JsonObject> {
    private int resource;
    private LayoutInflater inflater;

    public CustomAdapter(Context context, int resourceId, List<JsonObject> objects) {
        super(context, resourceId, objects);
        resource = resourceId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            Log.d("DEBUG","Inflating view");
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
        Button unisciti;

        //Così quando premerò unisciti avrò un riferimento alla partita
        unisciti = v.findViewById(R.id.tastoUnisciti);
        unisciti.setTag(p.get("CodicePartita").getAsString());


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
}}