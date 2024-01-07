package com.example.balanin493_practice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.balanin493_practice.models.UserInformation;
import com.example.balanin493_practice.services.DB;
import com.example.balanin493_practice.services.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthorizationActivity extends AppCompatActivity {

    EditText etLogin, etPassword;
    CheckBox checkRememberMe;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        etLogin = findViewById(R.id.et_Registration_Login);
        etPassword = findViewById(R.id.et_Registration_Password);
        checkRememberMe = findViewById(R.id.check_RememberMe);

        db = new DB(this, getString(R.string.dbName), null, 1);

        UserInformation uinf = db.GetRememberedUser();
        if (uinf != null)
        {
            etLogin.setText(uinf.userLogin);
            etPassword.setText(uinf.userPassword);

            onButtonLogin_Click(new View(this));
        }
    }


    public void onButtonLogin_Click(View v)
    {
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();

        Request r = new Request (this){
            @Override
            public void onSuccess(String res, Context ctx) throws Exception
            {
                if (checkRememberMe.isChecked())
                {
                    db.RememberMe(login,password);
                }
                res = res.replace("\"","");
            //   Intent i = new Intent(ctx, MainActivity.class);
               // i.putExtra("token", res);
               // startActivityForResult(i,200);
                Toast.makeText(ctx,res, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(Activity ctx)
            {
                Toast.makeText(ctx,"Неправильный логин или пароль",Toast.LENGTH_SHORT).show();
            }
        };

        JSONObject obj = new JSONObject();
        try {
            obj.put("name",login);
            obj.put("secret",password);
        } catch (JSONException e) { }

        r.sendHttpPost("/rpc/sign_in",obj);

    }




    public void onButtonRegister_Click(View v)
    {
        Intent i = new Intent(this, RegistrationActivity.class);
        startActivityForResult(i,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if ( resultCode  == 100) {

               Toast.makeText(this, "Успешно создан аккаунт",Toast.LENGTH_SHORT).show();
               etLogin.setText(data.getStringExtra("login"));
               etPassword.setText(data.getStringExtra("password"));
            }
        } catch (Exception ex) {

        }

    }


}