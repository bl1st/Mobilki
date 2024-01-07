package com.example.balanin493_opengl;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MyGLView mgv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //openGL 2.0 подключить
        // чето с renderer

        MyRenderer r = new MyRenderer();


        mgv = findViewById(R.id.myGLView);
        mgv.setRenderer(r);
    }










}