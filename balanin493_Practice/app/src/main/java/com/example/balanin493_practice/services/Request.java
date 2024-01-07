package com.example.balanin493_practice.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Request {

    Activity ctx;
    final String base = "http://spbcoit.ru/lab/budget/api";

    public Request(Activity ctx) {
        this.ctx =ctx;
    }

    public void onSuccess(String res, Context ctx) throws Exception {

    }

    public void onFail(Activity ctx) {

    }

    public void sendHttpGet(String request)
    {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(base + request);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept", "application/json");

                    InputStream is = con.getInputStream();
                    BufferedInputStream inp = new BufferedInputStream(is);

                    byte[] buf = new byte[1024];
                    String str = "";

                    while (true) {
                        int len = inp.read(buf);
                        if (len < 0) break;

                        str += new String(buf, 0, len);
                    }
                    con.disconnect();

                    final String res = str;

                    ctx.runOnUiThread(() ->
                    {
                        try {
                            onSuccess(res, ctx);
                        } catch (Exception e) {
                            Log.e("HttpGetError: ", e.getMessage().toString());
                            onFail(ctx);
                        }
                    });

                } catch (Exception e)
                {
                    Log.e("HttpGetError: ", e.getMessage().toString());
                }
            }
        };
        Thread t = new Thread(r);
        t.start();

    }


    public void sendHttpPost(String request, JSONObject body)
    {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                int resultCode = 0;
                try {

                    URL _url = new URL(base + request);
                    HttpURLConnection urlConn =(HttpURLConnection)_url.openConnection();
                    urlConn.setRequestMethod("POST");
                    urlConn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    urlConn.setRequestProperty("Accept", "application/json");
                    urlConn.setDoOutput(true);
                    urlConn.connect();

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConn.getOutputStream()));
                    writer.write(body.toString());
                    writer.flush();
                    writer.close();

                    Log.e(base+request, "  RESULT CODE: "+ urlConn.getResponseCode());

                    resultCode = urlConn.getResponseCode();
                    if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        is = urlConn.getInputStream();
                    } else {
                        is = urlConn.getErrorStream();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    onFail(ctx);
                } catch (IOException e) {
                    e.printStackTrace();
                    onFail(ctx);
                }

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    is.close();
                    final String response = sb.toString();

                    Log.e("JSON:" , response);
                    if (response.equals("{}") || response.equals("null") || resultCode >= 400)
                    {
                        Log.e("JSON:" , "Calling onFail because result is {} or null");
                        ctx.runOnUiThread(()->
                        {
                            try {
                                onFail(ctx);
                            } catch (Exception e) {
                                Log.e("Error","Failed in onFail after receiving response from server");
                            }
                        });

                    }
                    else
                    {
                        ctx.runOnUiThread(()->
                        {
                            try {
                                onSuccess(response,ctx);
                            } catch (Exception e) {
                                Log.e("Error","Failed in OnSuccess after receiving response from server");
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                    onFail(ctx);
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

}