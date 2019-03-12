package com.example.fernet.easy_fut5al;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import static android.content.Context.MODE_PRIVATE;

public class DialogCercaPartita extends Dialog {
    public Activity activity;
    public Dialog dialog;
    public Button cerca;
    public EditText casellatesto;
    public Switch Switch;
    public LinearLayout ll;
    Communicator communicator;


    public DialogCercaPartita(Activity a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cerca);


        cerca = (Button) findViewById(R.id.btn_cerca);
        casellatesto = findViewById(R.id.casella);
        Switch = findViewById(R.id.switchh);
        ll = findViewById(R.id.ll);

        SharedPreferences prefs = activity.getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
        String CittaChevoglio = prefs.getString("queryCittaPartita", null);
        if(CittaChevoglio.equals("")) Switch.setChecked(true);
        else Switch.setChecked(false);

        if(Switch.isChecked()) ll.setVisibility(View.INVISIBLE);
        else ll.setVisibility(View.VISIBLE);


        Switch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) { //Se attivo, vedi tutte partite
                            ll.setVisibility(View.INVISIBLE);
                            communicator.respond(casellatesto.getText().toString());
                            dismiss();

                        } else { //se non è attivo, allora devo scegliere una partita
                            ll.setVisibility(View.VISIBLE);
                        }
                    }
                });


        cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.respond(casellatesto.getText().toString());
                dismiss();
            }

        });
    }


    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

    public interface Communicator {
        public void respond(String cittaScelta);
    }



}
