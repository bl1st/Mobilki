package com.example.balanin493_opengl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyHelper {




    public static FloatBuffer createBuffer(int vertex_count, int parameters_per_vertex, ArrayList<Float> vertex_data)
    {
        float[] farr = new float[vertex_data.size()];
        for (int i =0;i < farr.length; i++)
        {
            farr[i] = vertex_data.get(i);
        }

        int stride = parameters_per_vertex * 4;

        ByteBuffer bbuf = ByteBuffer.allocateDirect(vertex_count * stride );
        bbuf.order(ByteOrder.nativeOrder());

        FloatBuffer fbuf = bbuf.asFloatBuffer();

        fbuf.put(farr);

        return fbuf;

    }

    public static int create_texture(String filename, Context ctx, int rcid)
    {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        Resources res = ctx.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res,rcid,opts);

        int[] tx = new int[1];
        GLES20.glGenTextures(1,tx,0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,tx[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bmp,0);

        bmp.recycle();

        return tx[0];
    }

}
