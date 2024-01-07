package com.example.clientserverlab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onButton_Click(View v) {
        MyTask tsk = new MyTask();
        Thread t = new Thread(tsk);
    }
}