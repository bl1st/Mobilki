package com.example.a493balanin_raymarching_sdf;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    public int w = 256, h = 256;
    public int rays = 16;
    public Bitmap  res;
    int steps = 10;
    float dthreshhold = 5.0f;
    Circle[] obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv);
        res = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        int n = 2;
        Thread[] t = new Thread[n];
        Worker[] worker = new Worker[n];
        int s = h / n;
        //long startTime = System.currentTimeMillis();

        for (int i = 0; i < t.length; i++) {
            worker[i] = new Worker();
            worker[i].thread_index = i;
            worker[i].thread_count = n;
            t[i] = new Thread(worker[i]);
        }

        obj = new Circle[4];
        obj[0] = new Circle(127,127,20,255,0,0);
        obj[1] = new Circle(64,64,20,0,0,255);
        obj[2] = new Circle(192,64,20,0,255,0);
        obj[3] = new Circle(64,192,20,255,255,0);

        TimerTask tt = new TimerTask(){

            @Override
            public void run()
            {
                for (int i = 0; i < t.length; i++) {
                    t[i].start();
                }

                for (int i = 0; i < n; i++) {
                    try {  t[i].join(); }
                    catch (InterruptedException e) { e.printStackTrace(); }
                }

                iv.setImageBitmap(res);
            }
        };
        Timer tm = new Timer();
    }

    public void myFunction(View view)
    {

    }

    class Worker implements Runnable {

        public int y0, y1;
        public int thread_count ,thread_index;

        private Circle trace(float x, float y, float vx, float vy)
        {
            float dorg = 0.0f;

            for (int k =0;k < steps; k++)
            {
                float rx = x + vx * dorg;
                float ry = y + vy * dorg;

                float dmin = 100000000.0f;

                for (int i = 0; i < obj.length; i++) {
                    float d = obj[i].sdf(rx, ry);
                    if  (d < dthreshhold)
                    {
                        return obj[i];
                    }
                    if (d < dmin) {
                        dmin = d;
                    }
                }
                dorg += dmin;
            }
            return null;
        }

        @Override
        public void run() {

            for (int y = thread_index; y < h; y+= thread_count) {


                for (int x = 0; x < w; x++) {


                    float red = 0.0f;
                    float green = 0.0f;
                    float blue = 0.0f;

                    for (int j=0;j< rays; j++) {

                        //float a = (360.0f / rays * j) /360.0f * 3.14f;//RADIANS
                        float a = (float)Math.random() * 2* (float)Math.PI;

                        float vx = (float) Math.cos(a);
                        float vy = (float) Math.sin(a);

                        Circle c = trace(x,y,vx,vy);
                        if (c !=null)
                        {
                            red  += c.cr;
                            green += c.cg;
                            blue += c.cb;
                        }

                    }
                    red /= (float)rays;
                    green /= (float)rays;
                    blue /= (float)rays;

                    res.setPixel(x, y, Color.rgb((int)red, (int)green, (int)blue));
                }
            }
        }
    }

    class Circle{

        public float x0,y0,r;
        public float cb, cr, cg;

        public Circle(float x,float y,float r, float cr, float cg, float cb){
            this.x0 = x;
            this.y0 = y;
            this.r = r;
            this.cr = cr;
            this.cg = cg;
            this.cb = cb;
        }

        public float sdf(float x, float y) {
            float dx = x - x0;
            float dy = y - y0;
            float d = (float) Math.sqrt(dx * dx + dy * dy);
            return d - r;
        }
    }
}