package com.clever_cat.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class PolygonDrawer {
    
    private static final int BYTES_PER_FLOAT = 4;
	private static final int COORDS_PER_VERTEX = 3;
	private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * BYTES_PER_FLOAT;
	
    private final int program;
    
	public PolygonDrawer() {
        int vertexShader = ShaderUtil.getVertexShader();
        int fragmentShader = ShaderUtil.getFragmentShader();

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
	}

    public void drawPolygon(float[] mvpMatrix, Point[] vertices, Color color) {
    	FloatBuffer vertexBuffer = createByteBuffer(vertices);    	
    	drawPolygon(mvpMatrix, color, vertexBuffer);
    }

	private FloatBuffer createByteBuffer(Point[] vertices) {
		float[] floats = new float[vertices.length * COORDS_PER_VERTEX];
        for (int i = 0; i < vertices.length; ++i) {
        	floats[i * COORDS_PER_VERTEX] = vertices[i].getX();
        	floats[i * COORDS_PER_VERTEX + 1] = vertices[i].getY();
        	floats[i * COORDS_PER_VERTEX + 2] = 0.0f;
        }
        
        ByteBuffer bb = ByteBuffer.allocateDirect(
                floats.length * BYTES_PER_FLOAT);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(floats);
        fb.position(0);
		return fb;
	}
	
	private void drawPolygon(float[] mvpMatrix, Color color, FloatBuffer vertexBuffer) {
		GLES20.glUseProgram(program);

        // Get handle to vertex shader's vPosition member
        int positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);
        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);

        int colorHandle = GLES20.glGetUniformLocation(program, "vColor");
        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1 /* count */, color.getValues() , 0 /* offset */);


        // get handle to shape's transformation matrix
        int mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);
        
        // Draw the triangles
        GLES20.glDrawArrays(
        		GLES20.GL_TRIANGLE_FAN, 
        		0 /* first */,
        		vertexBuffer.limit() / COORDS_PER_VERTEX /* count */);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
	}
}
