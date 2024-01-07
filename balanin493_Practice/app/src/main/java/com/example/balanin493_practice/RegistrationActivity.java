package com.example.balanin493_practice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.balanin493_practice.services.DB;
import com.example.balanin493_practice.services.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {

    EditText etLogin, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etLogin = findViewById(R.id.et_Registration_Login);
        etPassword = findViewById(R.id.et_Registration_Password);
    }


    public void OnButtonRegister_Click(View v)
    {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();

        Request r = new Request (this){
            @Override
            public void onSuccess(String res, Context ctx) throws Exception
            {

                Intent j = new Intent();
                j.putExtra("login",login);
                j.putExtra("password",password);

                setResult(100,j);
                finish();
            }

            public void onFail(Activity ctx)
            {
                Toast.makeText(ctx,"Пользователь с таким логином уже сущесвует",Toast.LENGTH_SHORT).show();
            }
        };

        JSONObject obj = new JSONObject();
        try {
            obj.put("name",login);
            obj.put("secret",password);
        } catch (JSONException e) { }

        r.sendHttpPost("/rpc/register_account",obj);


    }
}