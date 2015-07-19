package com.clever_cat.gl;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class CatGLSurfaceView extends GLSurfaceView {

	private final CatRenderer catRenderer;
	
	public CatGLSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		catRenderer = new CatRenderer(context);
		setRenderer(catRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		catRenderer.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		catRenderer.onResume();
	}

}
