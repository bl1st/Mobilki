package com.example.a493balanin_lab7_communalapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a493balanin_lab7_communalapp.R;
import com.example.a493balanin_lab7_communalapp.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthorizationActivity extends AppCompatActivity {

    EditText etLogin, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autorization);

        etLogin = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == 200)
            {
                etLogin.setText(data.getStringExtra("login"));
                etPassword.setText(data.getStringExtra("password"));
            }
            if (resultCode == 201)
            {
                Toast.makeText(this,"Returned to authorization activity",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,"Error occurred while returning to activity",Toast.LENGTH_SHORT).show();
        }

    }


    public void OnButtonSignIn_Click(View v)
    {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();

        Request r = new Request(this)
        {
            @Override
            public void onSuccess(String res, Context ctx) throws Exception
            {
                String token = res.replace("\"","");
               Log.e("Token", token);

                Intent i = new Intent(ctx, MainActivity.class);
                i.putExtra("token",token);
                startActivityForResult(i,300);
            }
        };

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name1", login);
            requestBody.put("password1", password);
        }
        catch (JSONException e) { }

        r.sendHttpPost("/rpc/sign_in",requestBody);

    }


    public void OnButtonRegister_Click(View v)
    {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();

        Intent i = new Intent(this, RegisterActivity.class);
        if (!login.equals("") && !password.equals(""))
        {
            i.putExtra("login",login);
            i.putExtra("password",password);
        }
        startActivityForResult(i,200);

    }
}