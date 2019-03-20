package com.example.fernet.easy_fut5al;
import android.view.View;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;


public class Profilo  extends Fragment {

    private View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profilo, container, false);
        
        //listapartecipanti = v.findViewById(R.id.listaInvitati);



        return v;
    }

}
