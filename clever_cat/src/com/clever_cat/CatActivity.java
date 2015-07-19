package com.clever_cat;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.clever_cat.gl.CatGLSurfaceView;

public class CatActivity extends Activity {

	private GLSurfaceView GLSurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GLSurfaceView = new CatGLSurfaceView(this);
		setContentView(R.layout.activity_cat);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.cat_layout);
		layout.addView(GLSurfaceView);
	}	
}
