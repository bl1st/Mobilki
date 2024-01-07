package com.example.lab15_393balanin_notesrestapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class notes_activity extends AppCompatActivity {

    ListView lv;
    Intent i;
    String token="";
    String api_endpoint = "http://v1.fxnode.ru:8084";
    ArrayList<Note> list = new ArrayList<>();
    ArrayAdapter<Note> adp;
    String currentBody;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        i = getIntent();
        token = i.getStringExtra("token");
        token = token.replace("\"","");

        lv = findViewById(R.id.list_notes);
        adp  = new ArrayAdapter<>(this,  android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adp);

        lv.setOnItemClickListener((parent, view, position, id) -> {

            Note n = list.get(position);

            Intent ab = new Intent(this, activity_modifyNote.class);
            ab.putExtra("skey" ,token);
            ab.putExtra("nid",n.nid);
            ab.putExtra("ntitle",n.ntitle);
            ab.putExtra("nbody", n.nbody);

            startActivityForResult(ab,777);

        });

        UpdateListView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (resultCode == 777) //if answer is from activity_ModfiyNote
        {
            UpdateListView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onButtonLogOut_Click(View v)
    {
        ApiHelper abc = new ApiHelper(this)
        {
            @Override
            public void on_ready(String res) {
                if (res.equals("false"))
                {
                    Toast.makeText(getApplicationContext(),
                            "Failed to log out!!! What have you done?", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    String key = res.replace("\"", "");
                    Log.i("CLOSING SESSION COMPLETED|", "token - " + token + "|| state -" + key);
                    setResult(666);
                    finish();
                }
                Log.e("result", res);
            }
        };
        abc.send(api_endpoint + "/rpc/close_session",
                "{ \"skey\":\"" + token + "\"}");
    }


    public void UpdateBodyById(int id) {

        ApiHelper def = new ApiHelper(this) {
            @Override
            public void on_ready(String res) {
                if (res.equals("null")) {

                    Toast.makeText(getApplicationContext(),
                            "Failed to log out!!! What have you done?", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    String key = res.replace("\"", "");
                    currentBody = res;
                    currentBody = currentBody.replace("\"","");
                    PlaceBody(id, res);
                }

            }
        };
        def.send(api_endpoint + "/rpc/get_contents",
                "{\"nid\":" + id + ", \"skey\": \""+ token + "\"}");


    }


    public void UpdateListView()
    {
        list.clear();
        ApiHelper abc = new ApiHelper(this)
        {
            @Override
            public void on_ready(String res) {
                if (res.equals("false"))
                {
                    Toast.makeText(getApplicationContext(),
                            "Failed to download notes from server", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    Log.i("Recieved notes from server, displaying them", "token - " + token);
                    try {

                        JSONArray obj = new JSONArray(res);
                        for (int i =0; i < obj.length(); i++)
                        {
                            Note note = new Note();
                            JSONObject jsobj = obj.getJSONObject(i);
                            Log.i("JSONOBJECT FROM MASSIVE", " NID = " + jsobj.getInt("nid") + " | TITLE = " + jsobj.getString("ntitle"));
                            note.nid = jsobj.getInt("nid");
                            note.ntitle = jsobj.getString("ntitle");
                            UpdateBodyById(note.nid); //sets in variable currentBody new value
                            note.nbody = currentBody;
                            list.add(note);
                        }

                        adp.notifyDataSetChanged();

                    }
                    catch (JSONException e) {   }


                }
                Log.e("result", res);
            }
        };
        abc.send(api_endpoint + "/rpc/list_notes",
                "{ \"skey\":\"" + token + "\"}");

    }

    public void PlaceBody(int id, String body) //Я не знаю что я сделал но оно работает
    {
        body =  body.replace("\"","");
        for (int i=0; i< list.stream().count(); i++)
        {
            Note n = list.get(i);
            if (n.nid == id)
            {
                n.nbody = body;
                list.set(i,n);
                adp.notifyDataSetChanged();
            }
        }

    }

}