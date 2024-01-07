package com.example.lab15_393balanin_notesrestapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class activity_modifyNote extends AppCompatActivity {

    String api_endpoint = "http://v1.fxnode.ru:8084";
    Note note; //recievednote
    String skey = "";

    EditText etTitle;
    EditText etBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note);

        etTitle = findViewById(R.id.et_Title);
        etBody = findViewById(R.id.et_Body);

        Intent i = getIntent();
        skey = i.getStringExtra("skey");

        note = new Note();
        note.nid = i.getIntExtra("nid",-1);
        note.ntitle = i.getStringExtra("ntitle");
        note.nbody = i.getStringExtra("nbody");

        etTitle.setText(note.ntitle);
        etBody.setText(note.nbody);

    }

    public void onButtonClose_Click(View v)
    {
        finish();
    }

    public void onButtonSave_Click(View v)
    {
        ApiHelper abc = new ApiHelper(this)
        {
            @Override
            public void on_ready(String res) {
                if (res.equals("false"))
                {
                    Log.i("YOU FAILED IN JSON FILE", "not here");
                    Toast.makeText(getApplicationContext(),
                            "Failed to save modified data", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    Log.i("YOU SUCCEDED", "not here");
                    Toast.makeText(getApplicationContext(),
                            "Note was successfully updated", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        };
        abc.send(api_endpoint + "/rpc/update_note",
                "{\"nid\": " +note.nid + ",\"nbody\":" +note.nbody + ", \"skey\":\"" + skey + "\",\"ntitle\":\"" + note.ntitle + "\"}");

        setResult(777);
        finish();

    }
    public void onButtonDelete_Click(View v)
    {
        ApiHelper abc = new ApiHelper(this)
        {
            @Override
            public void on_ready(String res) {
                if (res.equals("false"))
                {
                    Toast.makeText(getApplicationContext(),
                            "Failed to delete note", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Note was successfully deleted!", Toast.LENGTH_LONG).show();
                  finish();
                }
            }
        };
        abc.send(api_endpoint + "/rpc/delete_note",
                "{\"nid\":" + note.nid + ",\"skey\":\"" + skey + "\"}");

        setResult(777);
        finish();
    }
}