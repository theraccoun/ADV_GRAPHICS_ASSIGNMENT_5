package com.maccoun;

/**
 * Created by IntelliJ IDEA.
 * User: theraccoun
 * Date: 3/4/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class FragmentShader {

    private static final String fShader =
             "precision mediump float;\n" +
             "varying vec2 vTextureCoord;\n" +
             "uniform sampler2D sTexture;\n" +


              "void main() {\n" +
              "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
              "}\n";

    public String getFragmentShader(){
        return fShader;
    }
}
