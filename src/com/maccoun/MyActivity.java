package com.maccoun;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyActivity extends Activity
{

    private HelloOpenglSurfaceView mGLView;
    private TextView curState;                  // Displays the current state

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mGLView = new HelloOpenglSurfaceView(this);
//
//
//        LinearLayout ll = new LinearLayout(this);
//        ll.setOrientation(LinearLayout.VERTICAL);
//        Button bStructArray = new Button(this);
//        bStructArray.setText("Word");
//        Button bRandom = new Button(this);
//        bRandom.setText("Meow");
//
//        curState = new TextView(this);
//        curState.setText("Cheese");
//        curState.setTextSize(30);
//        ll.addView(curState);
//
//        bRandom.setOnClickListener(new bRandListener());
//        bStructArray.setOnClickListener(new bArrayStructListener());
//
//
//        ll.addView(bRandom);
//        ll.addView(bStructArray);
//        ll.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
//        ll.addView(mGLView);


        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        Button bStructArray = new Button(this);
        bStructArray.setText("Word");
        Button bRandom = new Button(this);
        bRandom.setText("Meow");

        ll.addView(bStructArray);
        ll.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        ll.addView(mGLView);

        setContentView(ll);
    }

    class bRandListener implements View.OnClickListener{

        private int counter = 0;


        public void onClick(View v) {
        }
    }

    class bArrayStructListener implements View.OnClickListener{

        public void onClick(View v) {

        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}

class HelloOpenglSurfaceView extends GLSurfaceView {

    private GLES20TriangleRenderer gtr;

    public HelloOpenglSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        gtr = new GLES20TriangleRenderer(context);
        setRenderer(gtr);
    }

}

