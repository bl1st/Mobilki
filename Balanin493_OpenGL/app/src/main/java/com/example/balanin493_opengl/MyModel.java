package com.example.balanin493_opengl;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class MyModel {
    AssetManager am;
    int parameters_per_vertex = 3 + 3 + 2;
    int stride = parameters_per_vertex * 4;
    int vertex_count = 3;

    FloatBuffer fbuf;
    ArrayList<Float> arr_v = new ArrayList<Float>();
    ArrayList<Float> arr_vt = new ArrayList<Float>();
    ArrayList<Float> arr_vn = new ArrayList<Float>();

    ArrayList<Float> arr_f = new ArrayList<Float>();

    public MyModel(String fileName, Context ctx) throws IOException {
        am = ctx.getAssets();
        InputStream is = am.open("untitled.obj");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        while(true)
        {

            String s =  br.readLine();
            if (s == null) break;

            String[] t = s.split(" ");

            //v 0.00 0.00 0.00
            if (t[0].equals("v"))
            {
                for (int i =1; i<4; i++)
                {
                    arr_v.add(Float.parseFloat(t[i]));
                }

            }
            if (t[0].equals("vn"))
            {
                for (int i =1; i<4; i++)
                {
                    arr_vn.add(Float.parseFloat(t[i]));
                }

            }
            if (t[0].equals("vt"))
            {
                for (int i =1; i<3; i++)
                {
                    arr_vt.add(Float.parseFloat(t[i]));
                }

            }
            if (t[0].equals("f"))
            {
                for (int i =1; i < 4; i++)
                {
                    String[] g = t[i].split("/");
                    int p_idx = (Integer.parseInt(g[0])-1) * 3;
                    int t_idx = (Integer.parseInt(g[1])-1) * 2;
                    int n_idx = (Integer.parseInt(g[2])-1) * 3;
                    arr_f.add(arr_v.get(p_idx));
                    arr_f.add(arr_v.get(p_idx + 1));
                    arr_f.add(arr_v.get(p_idx + 2));

                    arr_f.add(arr_vn.get(n_idx));
                    arr_f.add(arr_vn.get(n_idx + 1));
                    arr_f.add(arr_vn.get(n_idx + 2));

                    arr_f.add(arr_vt.get(t_idx));
                    arr_f.add(arr_vt.get(t_idx + 1));




                }

            }
        }




        br.close();


        Log.e("Loader",String.format("v %d, vt %d, vn %d, f %d", arr_v.size(), arr_vt.size(),arr_vn.size() / 24));
         vertex_count = arr_f.size() / 8;

        fbuf = MyHelper.createBuffer(vertex_count,);
    }

}
