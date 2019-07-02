package com.example.fernet.easy_fut5al;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
    private EditText inputNome, inputCognome,inputUsername,inputCitta,inputRuolo, inputEmail, inputPassword, inputConfermaPassword;
    private DatePicker inputDataNascita;
    public static  String URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_atleta);

        URL = "RegistrazioneAtletaServlet";


        inputNome = (EditText) findViewById(R.id.input_nome);
        inputCognome = (EditText) findViewById(R.id.input_cognome);
        inputUsername=(EditText) findViewById(R.id.input_username);
        inputDataNascita=(DatePicker) findViewById(R.id.input_dataNascita);
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
                    ConnectionTask task = new ConnectionTask(URL, getApplicationContext()) {
                        @Override
                        protected void inviaDatiAlServer() {
                            try {
                                //Invio json al server--------------------
                                OutputStream os = getConnessione().getOutputStream();
                                OutputStreamWriter osw = new OutputStreamWriter(os);
                                BufferedWriter bw = new BufferedWriter(osw);

                                JsonObject obj = new JsonObject();
                                obj.addProperty("email", inputEmail.getText().toString());
                                obj.addProperty("password", inputPassword.getText().toString());
                                obj.addProperty("nome", inputNome.getText().toString());
                                obj.addProperty("cognome", inputCognome.getText().toString());

                                String data = inputDataNascita.getYear()+"-"+inputDataNascita.getMonth()+"-"+inputDataNascita.getDayOfMonth();
                                obj.addProperty("data", data);

                                obj.addProperty("residenza", inputCitta.getText().toString());
                                obj.addProperty("ruolo", inputRuolo.getText().toString());
                                obj.addProperty("username", inputUsername.getText().toString());


                                String sendMessage = obj.toString() + "\n";
                                bw.write(sendMessage);
                                bw.flush();
                                System.out.println("Message sent to the server : " + sendMessage);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        protected void gestisciRispostaServer() {

                            //Se la registrazione è andata a buon fine allora vai al menu atleta
                            if(getOutputDalServer().get(0).toString().equals("Registrazione terminata con successo")){
                            //Salva dati corretti
                            SharedPreferences.Editor editor = getSharedPreferences("DatiApplicazione", MODE_PRIVATE).edit();
                            editor.putString("MyEmail", inputEmail.getText().toString() );
                            editor.putString("Mypwd", inputPassword.getText().toString() );
                            editor.putBoolean("IamAtleta", true);
                            editor.apply();

                            Intent intentMain;
                                intentMain = new Intent(RegistrationActivityAtleta.this,ActivityAtleta.class);
                                startActivity(intentMain);}

                            else{
                                Toast.makeText(RegistrationActivityAtleta.this, "l'Email specificata risulta già registrata.", Toast.LENGTH_LONG).show();

                            }
                        }
                    };
                    task.execute();
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

        /*
        if(inputDataNascita.getText().toString().length()==0){
            controllo=false;
            inputDataNascita.setError(getResources().getString(R.string.check_reg));
        }*/

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

}
