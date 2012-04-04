package com.maccoun;

/**
 * Created by IntelliJ IDEA.
 * User: theraccoun
 * Date: 3/4/12
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class VertexShader {

    private static final String vShader =

             "uniform mat4 uMVPMatrix;\n" +
             "attribute vec4 aPosition;\n" +
             "attribute vec2 aTextureCoord;\n" +
             "varying vec2 vTextureCoord;\n" +
             "uniform float ypos;\n" +


             "void main() {\n" +
             "  gl_Position = uMVPMatrix * aPosition;\n" +
             "  gl_Position.y += ypos;\n" +
             "  vTextureCoord = aTextureCoord;\n" +
             "}\n";


    public String getVShader(){
        return vShader;
    }
}
