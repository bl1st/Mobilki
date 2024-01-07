package com.example.a493balanin_lab7_communalapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a493balanin_lab7_communalapp.R;
import com.example.a493balanin_lab7_communalapp.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText etLogin, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etLogin = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        Intent i = getIntent();
        String login = i.getStringExtra("login");
        String password;
        if (!login.equals(""))
        {
            password =  i.getStringExtra("password");

            etLogin.setText(login);
            etLogin.setText(password);
        }

    }





    public void OnButtonRegister_Click(View v)
    {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();

        Request r = new Request(this)
        {
            @Override
            public void onSuccess(String res, Context ctx) throws Exception
            {
                boolean result = Boolean.parseBoolean(res);
                if (result)
                {
                    Intent i =new Intent();
                    i.putExtra("login",login);
                    i.putExtra("password",password);
                    setResult(200,i);
                    finish();
                }
                else
                {
                    Toast.makeText(ctx,"This user already exists",Toast.LENGTH_SHORT).show();
                }

            }
        };

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name1", login);
            requestBody.put("password1", login);
        }
        catch (JSONException e) { }

        r.sendHttpPost("rpc/sign_in",requestBody);
    }


    public void OnButtonBack_Click(View v)
    {
        setResult(308);
        finish();
    }

}