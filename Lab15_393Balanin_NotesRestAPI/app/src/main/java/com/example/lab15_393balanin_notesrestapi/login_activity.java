package com.example.lab15_393balanin_notesrestapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class login_activity extends AppCompatActivity {

    String api_endpoint = "http://v1.fxnode.ru:8084";
    Intent i;
    EditText etLogin;
    EditText etPassword;
    CheckBox checkRememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etLogin = findViewById(R.id.et_login);
        etPassword = findViewById(R.id.et_password);
        checkRememberMe = findViewById(R.id.check_RememberMe);

        //call From Db if there is row with login and password from previous sign in


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == 555) // If successfully registrated
        {
            String login = i.getStringExtra("login");
            String password = i.getStringExtra("password");

            etLogin.setText(login);
            etPassword.setText(password);
            onButtonSignIn_Click(new View(this));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onButtonSignIn_Click(View v)
    { String result = "";
        String login = etLogin.getText().toString();
        String password =etPassword.getText().toString();

        ApiHelper abc = new ApiHelper(this)
        {
            @Override
            public void on_ready(String res) {
                if (res.equals("null"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Failed to sing in. Check if your login and password are corrent", Toast.LENGTH_LONG);
                    return;
                }
                else
                {
                    String key = res.replace("\"", "");
                    Log.i("SUCCESSFULL RETRIEVING SKEY! result", "token - " + key);
                    Intent j = new Intent(login_activity.this, notes_activity.class);
                    j.putExtra("login",login);
                    j.putExtra("password",password);
                    j.putExtra("token", res);
                    startActivityForResult(j,666);
                }
                Log.e("result", res);
            }
        };
        abc.send(api_endpoint + "/rpc/open_session",
                "{ \"usr\": \"" + login + "\", \"pass\": \"" + password + "\"}");
    }

    public void onButtonSignUp_Click(View v)
    {
        i = new Intent(this, signup_activity.class);
        startActivityForResult(i,555); //555 - ok code for to automaticlly sign in after registration

    }

}