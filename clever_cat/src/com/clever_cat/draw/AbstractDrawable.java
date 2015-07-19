package com.clever_cat.draw;

public abstract class AbstractDrawable implements Drawable {

	protected float left;
	protected float width;
	protected float bottom;
	protected float height;
	protected float[] mvpMatrix;
	
	public abstract void draw();

	@Override
	public void setDrawingArea(float left, float width, float bottom,
			float height, float[] mvpMatrix) {
		this.left = left;
		this.width = width;
		this.bottom = bottom;
		this.height = height;
		this.mvpMatrix = mvpMatrix;
	}

}
