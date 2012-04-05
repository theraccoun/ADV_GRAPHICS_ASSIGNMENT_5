package com.maccoun;

import android.app.NativeActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: theraccoun
 * Date: 4/3/12
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class GLES20TriangleRenderer implements GLSurfaceView.Renderer{

    private Context mContext;
    private static String TAG = "GLES20TriangleRenderer";

    private int mProgram;
    private int maPositionHandle;
    private String mVertexShader;
    private String mFragmentShader;
    private int maTextureHandle;
    private int muMVPMatrixHandle;

    private float[] mMVPMatrix = new float[16];
    private float[] mProjMatrix = new float[16];
    private float[] mMMatrix = new float[16];
    private float[] mVMatrix = new float[16];

    private int[] textures;
    private int mTextureID;
    private FloatBuffer mSquareStructArrayVerticesBuffer;
    private FloatBuffer textureVerticesBuffer;


    private float time = 0;

    private static final int FLOAT_SIZE_BYTES = 4;

    public GLES20TriangleRenderer(Context context) {
        mContext = context;

        mSquareStructArrayVerticesBuffer = ByteBuffer.allocateDirect(mSquareVerticesData.length * FLOAT_SIZE_BYTES).
                order(ByteOrder.nativeOrder()).asFloatBuffer();
        mSquareStructArrayVerticesBuffer.put(mSquareVerticesData).position(0);
        textureVerticesBuffer = ByteBuffer.allocateDirect(textureVertices.length * FLOAT_SIZE_BYTES).
                order(ByteOrder.nativeOrder()).asFloatBuffer();
        textureVerticesBuffer.put(textureVertices).position(0);

        createSphere(2,30, 30);

    }

    public void onSurfaceCreated(GL10 unused, EGLConfig eglConfig) {

        mVertexShader  = new VertexShader().getVShader();
        mFragmentShader = new FragmentShader().getFragmentShader();

        mProgram = createProgram(mVertexShader, mFragmentShader);
        if (mProgram == 0) {
            return;
        }
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        checkGlError("glGetAttribLocation aPosition");
        if (maPositionHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }
        maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        checkGlError("glGetAttribLocation aTextureCoord");

        if (maTextureHandle == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        checkGlError("glGetUniformLocation uMVPMatrix");
        if (muMVPMatrixHandle == -1) {
            throw new RuntimeException("Could not get attrib location for uMVPMatrix");
        }

        /*
        * Create our texture. This has to be done each time the
        * surface is created.
        */

        textures = new int[2];
        GLES20.glGenTextures(2, textures, 0);

        mTextureID = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_REPEAT);


        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.raw.cool);


        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);


        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_REPEAT);

        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.raw.dog);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);


        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 30);
    }

    public void onDrawFrame(GL10 unused) {

        long time = SystemClock.uptimeMillis() % 4000L;

        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        checkGlError("glUseProgram");

        GLES20.glUseProgram(mProgram);


//        Matrix.setIdentityM(mMMatrix, 0);
//
//        float dogDepth = ((float)time/ 4000.0f)*10.0f;
//        Matrix.translateM(mMMatrix, 0, 0.0f, 0.0f, dogDepth);
//
//
//        float angle = 0.090f * ((int) time);
//
//        Matrix.rotateM(mMMatrix, 0, angle, 0.0f, 0.0f, 1.0f);
//
//        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
//
//        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
//
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
//
//        drawTexturedSquare();


        Matrix.setIdentityM(mMMatrix, 0);
        Matrix.translateM(mMMatrix, 0, 0.0f, 0.0f, 15.0f);
        float angle = 0.090f * ((int) time);

        Matrix.rotateM(mMMatrix, 0, angle, 0.0f, 0.0f, 1.0f);

        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        drawSphere();
    }



    private int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    private int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);

        if (vertexShader == 0) {
            return 0;
        }

        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program: ");
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    void drawTexturedSquare()
    {
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false,
                0, mSquareStructArrayVerticesBuffer);
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        checkGlError("glVertexAttribPointer maPosition");
        GLES20.glVertexAttribPointer(maTextureHandle, 2, GLES20.GL_FLOAT, false,
                0, textureVerticesBuffer);
        GLES20.glEnableVertexAttribArray(maTextureHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    public void createSphere(int r, int latLines, int longLines)
    {
        int latNumber, longNumber;

        ArrayList<Float> sphereVertices = new ArrayList<Float>();
        ArrayList<Float> sphereTextureCoords = new ArrayList<Float>();

        for(latNumber = 0; latNumber < latLines; ++ latNumber)
        {
            float theta = (float)latNumber * (PI / (float)latLines);
            float sinTheta = (float)Math.sin(theta);
            float cosTheta = (float)Math.cos(theta);

            for(longNumber = 0; longNumber < longLines; ++ longNumber)
            {
                float phi = ((2.0f * PI)/(float)longLines) * (float)longNumber;

                float sinPhi = (float)Math.sin(phi);
                float cosPhi = (float)Math.cos(phi);


                float x = r * cosPhi * sinTheta;
                float y = r * cosTheta;
                float z = r * sinPhi * sinTheta;

                sphereVertices.add(x);
                sphereVertices.add(y);
                sphereVertices.add(z);

                float s = 1 - (longNumber/longLines);
                float t = 1 - (latNumber/latLines);

                sphereTextureCoords.add(s);
                sphereTextureCoords.add(t);

            }
        }


        // Now compute the indices for drawing the sphere
        for(latNumber = 0; latNumber < latLines; ++ latNumber)
        {
            for(longNumber = 0; longNumber < longLines; ++ longNumber)
            {
                Integer first = (latNumber * (longLines + 1)) + longNumber;
                Integer second = first + longLines + 1;

                // First triangle
                sphereIndeces.add(first.shortValue());
                sphereIndeces.add(second.shortValue());
                Integer nextFirst = first + 1;
                sphereIndeces.add(nextFirst.shortValue());

                //Second triangle
                sphereIndeces.add(second.shortValue());
                Integer nextSecond = second + 1;
                sphereIndeces.add(nextSecond.shortValue());
                sphereIndeces.add(nextFirst.shortValue());


            }

        }

        sphereVerticesBuffer = ByteBuffer.allocateDirect(sphereVertices.size()*FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
        float[] sphereVertArray = new float[sphereVertices.size()];
        for(int i = 0; i < sphereVertices.size(); ++i){
            sphereVertArray[i] = sphereVertices.get(i);
        }
        sphereVerticesBuffer.put(sphereVertArray).position(0);


        sphereTextureCoordsBuffer = ByteBuffer.allocateDirect(sphereIndeces.size()*FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
        float[] sphereTextArray = new float[sphereTextureCoords.size()];
        for(int i = 0; i < sphereTextureCoords.size(); ++i)
            sphereTextArray[i] = sphereTextureCoords.get(i);
        sphereTextureCoordsBuffer.put(sphereTextArray).position(0);



        short[] sphereIndecesArray = new short[sphereIndeces.size()];
        for(int i = 0; i < sphereIndeces.size(); ++i)
            sphereIndecesArray[i] = sphereIndeces.get(i);

        sphereIndecesBuffer = ByteBuffer.allocateDirect(sphereIndecesArray.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        sphereIndecesBuffer.put(sphereIndecesArray).position(0);


//        GLES20.glGenBuffers(3, sphereBuffers);
//
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, sphereBuffers.get(0));
//        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, sphereVertices.size()*FLOAT_SIZE_BYTES, sphereVerticesBuffer, GLES20.GL_STATIC_DRAW);
//
//        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, sphereBuffers.get(1));
//        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, sphereIndeces.size()*2, sphereIndecesBuffer, GLES20.GL_STATIC_DRAW);


    }

    void drawSphere()
    {
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false,
                0, sphereVerticesBuffer);
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        checkGlError("glVertexAttribPointer maPosition");
        GLES20.glVertexAttribPointer(maTextureHandle, 2, GLES20.GL_FLOAT, false,
                0, sphereTextureCoordsBuffer);
        GLES20.glEnableVertexAttribArray(maTextureHandle);

//        Log.e("MEOW", "sphereIndeces size: " + sphereIndeces.size());
//        Log.e("MEOW", "sphereIndecesBuffer: " + sphereIndecesBuffer.get(2));

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, sphereIndeces.size(), GLES20.GL_UNSIGNED_SHORT, sphereIndecesBuffer);

    }



    private final float[] mSquareVerticesData = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f,
            0.5f,  0.5f, 0.0f,
    };

    private final float textureVertices[] = {
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f,  1.0f,
            0.0f,  0.0f,
    };

    private final float PI = (float)Math.PI;

    private FloatBuffer sphereVerticesBuffer;
    private FloatBuffer sphereTextureCoordsBuffer;
    private ShortBuffer sphereIndecesBuffer;
    private ArrayList<Short> sphereIndeces = new ArrayList<Short>();

    private IntBuffer sphereBuffers = IntBuffer.allocate(3);


}
