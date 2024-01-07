package com.example.lab15_393balanin_api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButton_Click(View v)
    {
        String user = gettextfromtextfielld();
        String pass = getTextFromTextField(); //получаем из полей активити


        APIHelper helper = new APIHelper(this){

            @Override
            public void on_ready(String res)
            {
                if (res.equals("null"))
                {
                    //Toast.makeText(this,0,3, "Wrong password/login").show();
                }
                else
                {
                   String key = res.replace("\"","");


                }
                //в случае успеха получаем токен
                //тут свой функционал
                Log.e("result",res);
                Log.i("result", "key -" + key);
            }
        }
        helper.send(api_endpoint + "rpc/open_session",
                "{ \"usr\":\"\",\"pass\":\"\" }");
    }
}