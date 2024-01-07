package com.example.lab15_393balanin_api;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class APIHelper {
    Context ctx;


    public APIHelper(Activity act)
    {
        cxt = act;
    }

    String http_get(String req, String payload) implements




    public void run()
    {

        try{
            final String res =


        }
    }
    public void send(String req, String payload) throws IOException
    {
        URL url = new URL(req);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        byte[] outmsg = payload.getBytes("utf-8");


        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type","application/json");
        con.setRequestProperty("Content-Length",String.valueOf(outmsg.length));
        con.setDoOutput(true);
        con.setDoInput(true);


        BufferedOutputStream out = BufferedOutputStream(con.getOutputStream());


        NetOp nop = new NetOp();
        nop.req = req;
        nop.payload = payload;
        Thread th = new Thread();
        th.start();
    }

}
