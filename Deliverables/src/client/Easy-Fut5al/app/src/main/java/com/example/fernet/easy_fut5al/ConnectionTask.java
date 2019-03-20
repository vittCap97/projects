package com.example.fernet.easy_fut5al;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

import static android.content.Context.MODE_PRIVATE;
import static java.lang.System.out;

public  abstract  class ConnectionTask extends AsyncTask<Void, Void, ArrayList<String>>{

    private static  String URL;
    private HttpURLConnection connessione;
    private ArrayList<String> OutputElaborazione;


    public ConnectionTask(String nomeServlet, Context context){
        SharedPreferences prefs = context.getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
        URL = prefs.getString("URLserver", null) + "/EasyFut5al/"+nomeServlet;
    }

    @Override
    public ArrayList<String> doInBackground(Void... nada) {
        ArrayList<String> output = null;
        //Ricevi mex di riscontro dal Server di quell'URL
        output = getOutputFromUrl();
        return output;
    }


    //Ricevi dati da Server
    private ArrayList<String> getOutputFromUrl() {
        StringBuffer output = new StringBuffer("");
        ArrayList<String> StringheDati = new ArrayList<String>();
        try {
            InputStream stream = getHttpConnection(URL);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
            String s = "";

            //ricezione stringhe
            while ((s = buffer.readLine()) != null)
                StringheDati.add(s);

            stream.close();

            int i = 1;
            for (String a : StringheDati) {
                out.println(i + "Dato ricevuto: " + a);
                i++;
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return StringheDati;
    }



    protected abstract void inviaDatiAlServer();


    public HttpURLConnection getConnessione() {
        return connessione;
    }

    //Inizia una connessione Http e ritorna uno stream di input dove scrivere dati x il server
    private InputStream getHttpConnection(String urlString)
            throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            connessione = (HttpURLConnection) connection;
            connessione.setRequestMethod("POST");

            //Funzione da sovrascrivere invio dati
            inviaDatiAlServer();

            connessione.connect();

            if (connessione.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = connessione.getInputStream();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }


    protected  abstract void gestisciRispostaServer();

    public ArrayList<String> getOutputDalServer() {
        return OutputElaborazione;
    }

    //lavora sui dati presi dal server, dopo l'esecuzione del compito asincrono
    @Override
    protected void onPostExecute(ArrayList<String> output) {
        OutputElaborazione = output;
        gestisciRispostaServer();

    }


}
