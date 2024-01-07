package com.example.balanin493_opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MyGLView extends GLSurfaceView {
    public MyGLView(Context context, AttributeSet attrs) {
        super(context, attrs);

        MyRenderer r = new MyRenderer();
        this.setRenderer(r);

        //onDrawFrame вызывается так часто как это возможно
        //Можно поменять это
        //this.setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
}
