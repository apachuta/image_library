package com.clever_cat.gl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import com.clever_cat.draw.CatDrawable;

public class CatRenderer implements Renderer {

	private Context context;
	private long lastTimeMillis;
	
	PolygonDrawer polygonDrawer;
	
	// mMVPMatrix is an abbreviation for "Model View Projection Matrix"
	private final float[] mvpMatrix = new float[16];
	private final float[] projectionMatrix = new float[16];
	private final float[] viewMatrix = new float[16];
	
	CatDrawable cat;
	
	public CatRenderer(Context context) {
		this.context = context;
		updateLastTime();
	}

	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	public void onResume() {
		updateLastTime();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        clearBackground();
        polygonDrawer = new PolygonDrawer();
        cat = new CatDrawable(polygonDrawer);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        computeGLMatrices(width, height);
        cat.setDrawingArea(0, width, 0, height, mvpMatrix);
	}

	private void computeGLMatrices(int width, int height) {
		// This defines how the observable world is scaled.
		// By default, OpenGL ES scales scales [-1,1]x[-1,1] to be displayed on a screen.
		// See glViewport method call.
		// See also frustumM method.
		Matrix.orthoM(projectionMatrix, 0, 0, width, 0, height, -1, 1);
		// This defines how the observable world is observed.
		// See also setLookAtM method.
		Matrix.setIdentityM(viewMatrix, 0);
		// This combines these two definitions.
		Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
        reclearBackground();
        cat.draw();
	}

	private void clearBackground() {
		// Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	private void reclearBackground() {
		// Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
	}
	
	/**
	 * Updates the last time to now.
	 * @return elapsed time in milliseconds
	 */
	private long updateLastTime() {
		long currTimeMillis = System.currentTimeMillis();
		long elapsedMillis = lastTimeMillis - currTimeMillis;
		lastTimeMillis = currTimeMillis;
		return elapsedMillis;
	}
}
