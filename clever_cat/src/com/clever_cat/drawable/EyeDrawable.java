package com.clever_cat.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class EyeDrawable extends Drawable {

	private static final double PUPIL_WIDTH_RATIO = 0.5;
	private static final double PUPIL_HEIGHT_RATIO = 0.4;
	
	ShapeDrawable eyeballDrawable;
	ShapeDrawable pupilDrawable;
	
	public EyeDrawable() {
		eyeballDrawable = new ShapeDrawable(new OvalShape());
		eyeballDrawable.getPaint().setColor(Color.WHITE);
		pupilDrawable = new ShapeDrawable(new OvalShape());
		pupilDrawable.getPaint().setColor(Color.BLACK);
	}
	
	@Override
	public void draw(Canvas canvas) {
		eyeballDrawable.draw(canvas);
		pupilDrawable.draw(canvas);
	}
	
	@Override
	public void setBounds(int left, int top, int right, int bottom) {
		super.setBounds(left, top, right, bottom);
	
		eyeballDrawable.setBounds(left, top, right, bottom);
		setPupilDirection(new Rect(-1, -1, 1, 1), new Point(0, 0));
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void setPupilDirection(Rect rect, Point point) {
		Rect eyeBounds = getBounds();
		int eyeWidth = eyeBounds.right - eyeBounds.left;
		int eyeHeight = eyeBounds.bottom - eyeBounds.top;
		int pupilWidth = (int) (eyeWidth * PUPIL_WIDTH_RATIO);
		int pupilHeight = (int) (eyeHeight * PUPIL_HEIGHT_RATIO);
		Rect pupilCornerBounds = new Rect(
				eyeBounds.left,
				eyeBounds.top,
				eyeBounds.right - pupilWidth,
				eyeBounds.bottom - pupilHeight);
		Point pupilCorner = RectUtil.mapPoint(point, rect, pupilCornerBounds);
		pupilDrawable.setBounds(
				pupilCorner.x,
				pupilCorner.y, 
				pupilCorner.x + pupilWidth,
				pupilCorner.y + pupilHeight);
	}
}
