package com.clever_cat.draw;

public interface Drawable {
	
	/**
	 * Draws an object. 
	 */
	void draw();

	/**
	 * A drawn object should fit [{@code left}, {@code right}] x [{@code bottom}, {@code top}].
	 * 
	 * @param mvpMatrix parameter for drawing methods
	 */
	public void setDrawingArea(
			float left, float right, float bottom, float top, float[] mvpMatrix);
}
