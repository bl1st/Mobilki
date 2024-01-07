package com.example.lab15_393balanin_notesrestapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class signup_activity extends AppCompatActivity {
    Intent i;
    String api_endpoint = "http://v1.fxnode.ru:8084";
    public EditText etLogin;
    public EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etLogin = findViewById(R.id.et_login);
        etPassword = findViewById(R.id.et_password);

    }


    public void onButtonOK_click(View v)
    {
        String result = "";
        String login = etLogin.getText().toString();
        String password =etPassword.getText().toString();

        ApiHelper abc = new ApiHelper(this)
        {
            @Override
            public void on_ready(String res) {
                if (res.equals("null"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Failed to create user. Maybe this user already exist", Toast.LENGTH_LONG);
                    return;
                }
                else
                {
                    String key = res.replace("\"", "");
                    Log.i("result", "key - " + key);
                    i = new Intent();
                    i.putExtra("login",login);
                    i.putExtra("password",password);

                   setResult(555,i);
                    finish();
                }
                Log.e("result", res);
            }
        };
        abc.send(api_endpoint + "/rpc/reg_account",
                "{ \"usr\": \"" + login + "\", \"pass\": \"" + password + "\"}");
    }

    public void onButtonCancel_Click(View v)  {finish();}


}