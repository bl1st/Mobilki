package com.example.practice2023_pccomponents.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.practice2023_pccomponents.R;
import com.example.practice2023_pccomponents.activities.MainActivity;
import com.example.practice2023_pccomponents.services.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthorizationActivity extends AppCompatActivity {

    EditText etLogin;
    EditText etPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        etLogin = findViewById(R.id.et_Login);
        etPassword = findViewById(R.id.et_Password);

    }

    public void OnButtonSignIn_Click(View v)
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, 100);

        ///////////////////////////////////////////////////////////////////////////////////
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();

        Request r = new Request (this){
            @Override
            public void onSuccess(String res, Context ctx) throws Exception
            {
                res = res.replace("\"","");
                Toast.makeText(ctx,res, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ctx, MainActivity.class);
                i.putExtra("token", res);
                startActivityForResult(i,200);

            }

            @Override
            public void onFail(Activity ctx)
            {
                Log.e("Fail sign_in","Well, gg");
                // Toast.makeText((Context)ctx,"Неправильный логин или пароль",Toast.LENGTH_SHORT).show();
            }
        };

        JSONObject obj = new JSONObject();
        try {
            obj.put("login",login);
            obj.put("secret",password);
        } catch (JSONException e) { }

      //  r.sendHttpPost("/api/Login/sign_in",obj);
    }

    public void OnButtonLoginAsGuest_Click(View v)
    {

    }
}