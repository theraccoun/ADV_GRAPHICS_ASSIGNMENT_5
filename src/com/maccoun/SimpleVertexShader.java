package com.maccoun;

/**
 * Created by IntelliJ IDEA.
 * User: theraccoun
 * Date: 4/5/12
 * Time: 2:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleVertexShader {

    private static final String vShader =

             "uniform mat4 uMVPMatrix;\n" +
             "attribute vec4 aPosition;\n" +
             "attribute vec2 aTextureCoord;\n" +
             "uniform float ypos;\n" +


             "void main() {\n" +
             "  gl_Position = uMVPMatrix * aPosition;\n" +
             "  gl_Position.y += ypos;\n" +
             "}\n";


    public String getVShader(){
        return vShader;
    }
}
