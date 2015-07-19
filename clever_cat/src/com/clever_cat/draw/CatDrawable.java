package com.clever_cat.draw;

import com.clever_cat.gl.PolygonDrawer;


public class CatDrawable extends AbstractDrawable {

	private EyeDrawable leftEye;
	private EyeDrawable rightEye;
	
	public CatDrawable(PolygonDrawer polygonDrawer) {
		leftEye = new EyeDrawable(polygonDrawer);
		rightEye = new EyeDrawable(polygonDrawer);
	}

	@Override
	public void setDrawingArea(float left, float width, float bottom,
			float height, float[] mvpMatrix) {
		super.setDrawingArea(left, width, bottom, height, mvpMatrix);
		float eyeMargin = width / 10;
		float eyeWidth = width / 3;
		float eyeBottom = bottom + height / 2;
		float eyeHeight = height / 3;
		leftEye.setDrawingArea(
				eyeMargin/* left */,
				eyeWidth /* width */,
				eyeBottom /* bottom */,
				eyeHeight /* height */,
				mvpMatrix);
		rightEye.setDrawingArea(
				width - eyeMargin - eyeWidth /* left */,
				eyeWidth /* width */,
				eyeBottom /* bottom */,
				eyeHeight /* height */,
				mvpMatrix);
	}
	
	@Override
	public void draw() {
		leftEye.draw();
		rightEye.draw();
	}

}
