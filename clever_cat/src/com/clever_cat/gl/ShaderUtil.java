package com.clever_cat.gl;

import android.opengl.GLES20;

public class ShaderUtil {
	
	private static final String VERTEX_SHADER_CODE =
	        "uniform mat4 uMVPMatrix;" +
		    "attribute vec4 vPosition;" +
		    "void main() {" +
		    "  gl_Position = uMVPMatrix * vPosition;" +
		    "}";

	private static final String FRAGMENT_SHADER_CODE =
	        "precision mediump float;" +
	        "uniform vec4 vColor;" +
	        "void main() {" +
	        "  gl_FragColor = vColor;" +
	        "}";

	public static int getVertexShader() {
		return loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE);
	}
	
	public static int getFragmentShader() {
		return loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE);
	}

	private static int loadShader(int type, String shaderCode){
	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(type);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);

	    return shader;
	}
}
