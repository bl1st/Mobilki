package com.example.lab15_393balanin_notesrestapi;

import android.app.Activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiHelper
{
    Activity ctx;

    public ApiHelper(Activity ctx)
    {
        this.ctx = ctx;
    }

    public void on_ready(String res)
    {
    }

    public void on_fail()
    {
    }

    String http_get(String req, String payload) throws IOException
    {
        URL url = new URL(req);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        byte[] outmsg = payload.getBytes("utf-8");

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Content-Length", String.valueOf(outmsg.length));
        con.setDoOutput(true);
        con.setDoInput(true);
        

        BufferedOutputStream out = new BufferedOutputStream(con.getOutputStream());
        out.write(outmsg);
        out.flush();

        BufferedInputStream inp = new BufferedInputStream(con.getInputStream());

        byte[] buf = new byte[512];
        String res = "";

        while (true)
        {
            int num = inp.read(buf);
            if (num < 0) break;

            res += new String(buf, 0, num);
        }
        con.disconnect();

        return res;
    }



    public class NetOp implements Runnable
    {
        public String req, payload;

        public void run()
        {
            try
            {
                final String res = http_get(req, payload);

                ctx.runOnUiThread(() -> {on_ready(res);});
            }
            catch (Exception ex)
            {
                on_fail();
            }
        }
    }

    public void send(String req, String payload)
    {
        NetOp nop = new NetOp();
        nop.req = req;
        nop.payload = payload;

        Thread th = new Thread(nop);
        th.start();
    }

}