package com.example.fernet.easy_fut5al;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class RegistrationActivityAtleta extends AppCompatActivity {

    private RadioGroup sessoRadioGroup;
    private Button btnConferma;
    private boolean check;
    private EditText inputNome, inputCognome,inputUsername,inputCitta,inputDataNascita,inputRuolo, inputEmail, inputPassword, inputConfermaPassword;
    public static  String URL;
    //public static final String URL = "http://192.168.1.14:8080/EasyFut5al/JsonServlet";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_atleta);

        SharedPreferences prefs = getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
        URL = prefs.getString("URLserver", null) + "/EasyFut5al/RegistrazioneAtletaServlet";


        inputNome = (EditText) findViewById(R.id.input_nome);
        inputCognome = (EditText) findViewById(R.id.input_cognome);
        inputUsername=(EditText) findViewById(R.id.input_username);
        inputDataNascita=(EditText) findViewById(R.id.input_dataNascita);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputConfermaPassword= (EditText) findViewById(R.id.input_passwordMatch);
        inputCitta=(EditText) findViewById(R.id.input_città);
        inputRuolo=(EditText) findViewById(R.id.input_ruolo);


        btnConferma= (Button) findViewById(R.id.conferma);
        sessoRadioGroup = (RadioGroup)findViewById(R.id.sesso_radio_group);

        btnConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check=controlloCampi();
                if(check==false){
                    Toast.makeText(RegistrationActivityAtleta.this, "Registrazione fallita", Toast.LENGTH_SHORT).show();
                }
                else{ //se i campi sono inseriti bene
                    // Avvia un compito asincrono
                    RegistrazioneTask task = new RegistrazioneTask();
                    //Avvia il task asyncrono sull'url specificato
                    task.execute(new String[] { URL});
                }
            }
        });

    }


    public boolean controlloCampi(){
        boolean controllo=true;
        if(inputNome.getText().toString().length()==0){
            controllo=false;
            inputNome.setError(getResources().getString(R.string.check_reg));
        }
        if(inputCognome.getText().toString().length()==0){
            controllo=false;
            inputCognome.setError(getResources().getString(R.string.check_reg));
        }
        if(inputEmail.getText().toString().length()==0){
            controllo=false;
            inputEmail.setError(getResources().getString(R.string.check_reg));
        }
        if(inputUsername.getText().toString().length()==0){
            controllo=false;
            inputUsername.setError(getResources().getString(R.string.check_reg));
        }

        if(inputDataNascita.getText().toString().length()==0){
            controllo=false;
            inputDataNascita.setError(getResources().getString(R.string.check_reg));
        }
        if(inputPassword.getText().toString().length()==0){
            controllo=false;
            inputPassword.setError(getResources().getString(R.string.check_reg));
        }
        if(inputConfermaPassword.getText().toString().length()==0){
            controllo=false;
            inputConfermaPassword.setError(getResources().getString(R.string.check_reg));
        }

        if(!(inputPassword.getText().toString().equals(inputConfermaPassword.getText().toString()))){
            controllo=false;
            inputConfermaPassword.setError(getResources().getString(R.string.pass_diversa));
        }

        if(inputCitta.getText().toString().length()==0){
            controllo=false;
            inputCitta.setError(getResources().getString(R.string.check_reg));
        }


        return controllo;
    }



    private class RegistrazioneTask extends AsyncTask<String, Void, String> {

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
            try {
                InputStream stream = getHttpConnection(url);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
                String s = "";
                while ((s = buffer.readLine()) != null)
                    output.append(s);

                System.out.println(output.toString());


            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return output.toString();
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

                //Invio json al server--------------------
                OutputStream os = httpConnection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                JsonObject obj = new JsonObject();
                obj.addProperty("email", inputEmail.getText().toString());
                obj.addProperty("password", inputPassword.getText().toString());
                obj.addProperty("nome", inputNome.getText().toString());
                obj.addProperty("cognome", inputCognome.getText().toString());
                obj.addProperty("data", inputDataNascita.getText().toString());
                obj.addProperty("residenza", inputCitta.getText().toString());
                obj.addProperty("ruolo", inputRuolo.getText().toString());
                obj.addProperty("username", inputUsername.getText().toString());


                String sendMessage = obj.toString() + "\n";
                bw.write(sendMessage);
                bw.flush();
                System.out.println("Message sent to the server : "+sendMessage);
                //-------------------------------------------------

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
            //Se la registrazione è andata a buon fine allora vai al menu atleta
            if(output.toString().equals("Registrazione terminata con successo")){

                //Salva dati corretti
                SharedPreferences.Editor editor = getSharedPreferences("DatiApplicazione", MODE_PRIVATE).edit();
                editor.putString("MyEmail", inputEmail.getText().toString() );
                editor.putString("Mypwd", inputPassword.getText().toString() );
                editor.putBoolean("IamAtleta", true);
                editor.apply();

                Intent intentMain=new Intent(RegistrationActivityAtleta.this,ActivityAtleta.class);
                startActivity(intentMain);
            }
        }
    }



}
