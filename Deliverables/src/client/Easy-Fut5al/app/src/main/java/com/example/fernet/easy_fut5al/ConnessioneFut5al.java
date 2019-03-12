package com.example.fernet.easy_fut5al;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class ConnessioneFut5al {

    private static  String URL = "http://172.19.22.245:8080";

    public ConnessioneFut5al(Context c){
        SharedPreferences prefs = c.getSharedPreferences("DatiApplicazione", MODE_PRIVATE);
        URL = prefs.getString("URLserver", null) + "/EasyFut5al/CercaPartiteServlet";
    }

}
