package com.clever_cat.view;

import android.view.Surface;

public class OrientationState {

	private int cameraOrientationDegrees;
	private int surfaceOrientationDegrees;

	private static OrientationState instance;
	
	public static OrientationState getInstance() {
		if (instance == null) {
			synchronized (OrientationState.class) {
				if (instance == null) {
					instance = new OrientationState();
				}
			}
		}
		return instance;
	}
	
	
	public OrientationState() {
		this.cameraOrientationDegrees = 270;
		this.surfaceOrientationDegrees = 0;
	}
	
	public int getRelativeCameraRotation() {
		return cameraOrientationDegrees + surfaceOrientationDegrees;
	}

	public void setCameraRotation(float[] cameraRotation) {
		// TODO: implement for API 23.
		throw new UnsupportedOperationException("OrientationState.setCameraRotation not implemented.");
	}
	
	public void setDisplayRotation(int displayRotation) {
		switch (displayRotation) {
		case Surface.ROTATION_0:
			surfaceOrientationDegrees = 0;
			break;
		case Surface.ROTATION_90:
			surfaceOrientationDegrees = 90;
			break;
		case Surface.ROTATION_180:
			surfaceOrientationDegrees = 180;
			break;
		case Surface.ROTATION_270:
			surfaceOrientationDegrees = 270;
			break;
		}
	}
}
