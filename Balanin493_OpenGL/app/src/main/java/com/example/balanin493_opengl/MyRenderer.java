package com.example.balanin493_opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer
{
    MyModel mdl;
    Context ctx;

    public MyRenderer(Context ctx)
    {
        this.ctx = ctx;
    }

    //4 - float size


    float[] vertex_data =
            {
              0.0f, 0.5f, 1.0f, 0.0f, 0.0f,
              0.5f,0.0f, 0.0f, 1.0f, 0.0f,
              -0.5f,0.0f, 0.0f, 0.0f, 1.0f,
            };

    FloatBuffer fbuf;

    //vertex shader code
    String vs_code =
            "attribute vec2 in_pos"+
                    "varying vec4 vr_pos"+
            "attribute vec3 in_col" +
            "varying vr_col"+                                                                            //??
            "void main()"+
            "{ "+
                    "vr_col = in_col;"+

                    "gl_Position = vec4(in_pos.x,in_pos.y, 0.0, 1.0);"+
            "}";

    String fs_code =
            "uniform vec3 uf_light_pos"+
            "void main()"+
                    "{"+
                    "gl_FragColor=vec4(1.0);"+
                    "}";



    int load_shader(int type, String code)
    {
        int res = GLES20.glCreateShader(type);

        GLES20.glShaderSource(res,code);
        GLES20.glCompileShader(res);

        int[] status = new int[1];
        GLES20.glGetShaderiv(res,GLES20.GL_COMPILE_STATUS, status,0);
        if (status[0] == 0)
        {
            String info = GLES20.glGetProgramInfoLog(res);
            Log.e("shader","compilation failed");
            Log.e("shader",info);

            GLES20.glDeleteShader(res);
            return 0;
        }
        return res;
    }

    int vert_shader;
    int frag_shader;
    int prog;
    int tx;


    float[] light_position = new float[]
            {   10.0f, 10.0f, 10.0f     };

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig)
    {
        try {
            mdl = new MyModel("untitled.obj", ctx);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //need R.drawable.untitled file in drawable
        tx = MyHelper.create_texture("untitled.png",ctx,R.drawable.untitled);




        vert_shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vert_shader,vs_code);
        GLES20.glCompileShader(vert_shader);

        frag_shader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(frag_shader,vs_code);
        GLES20.glCompileShader(frag_shader);

        prog = GLES20.glCreateProgram();
        GLES20.glAttachShader(prog,vert_shader);
        GLES20.glAttachShader(prog,frag_shader);

        GLES20.glLinkProgram(prog);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {

        GLES20.glViewport(0,0,width,height);
        GLES20.glClearColor(1.0f,0.0f,0.0f,1.0f);

    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        GLES20.glClear(gl.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(prog);

        //in_pos?
        int pos = GLES20.glGetAttribLocation(prog,"in_pos");
        int col = GLES20.glGetAttribLocation(prog,"in_col");
        GLES20.glEnableVertexAttribArray(pos);
        GLES20.glEnableVertexAttribArray(col);

        //явно указать точку для значений цвета вертексного массива
        fbuf.position(0);
        GLES20.glVertexAttribPointer(pos,mdl.parameters_per_vertex, GLES20.GL_FLOAT,false,mdl.stride,fbuf);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,mdl.vertex_count);

        GLES20.glDisableVertexAttribArray(pos);

        int lp = GLES20.glGetUniformLocation(prog,"uf_light_pos");
        GLES20.glUniform3fv(lp,1,light_position,0);

    }
}
