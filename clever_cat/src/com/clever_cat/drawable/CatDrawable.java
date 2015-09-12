package com.clever_cat.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class CatDrawable extends Drawable {

	private static double EYE_WIDTH_RATIO = 0.2;
	private static double EYE_HEIGHT_RATIO = 0.4;
	private static double EYE_LEFT_MARGIN_RATIO = 0.2;
	private static double EYE_TOP_MARGIN_RATIO = 0.2;
	
	EyeDrawable leftEye;
	EyeDrawable rightEye;
	
	public CatDrawable() {
		leftEye = new EyeDrawable();
		rightEye = new EyeDrawable();
	}
	
	@Override
	public void setBounds(int left, int top, int right, int bottom) {
		super.setBounds(left, top, right, bottom);
		
		int catWidth = right - left;
		int catHeight = bottom - top;
		
		int eyeLeft = (int) (catWidth * EYE_LEFT_MARGIN_RATIO);
		int eyeRight = (int) (eyeLeft + (catWidth * EYE_WIDTH_RATIO));
		int eyeTop = (int) (catHeight * EYE_TOP_MARGIN_RATIO);
		int eyeBottom = (int) (eyeTop + (catHeight * EYE_HEIGHT_RATIO));
		
		leftEye.setBounds(new Rect(eyeLeft, eyeTop, eyeRight, eyeBottom));
		rightEye.setBounds(catWidth - eyeRight, eyeTop, catWidth - eyeLeft, eyeBottom);
	}
	
	@Override
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.GRAY);
		canvas.drawRect(getBounds(), paint);

		leftEye.draw(canvas);
		rightEye.draw(canvas);
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLookDirection(Rect rect, Point point) {
		leftEye.setPupilDirection(rect, point);
		rightEye.setPupilDirection(rect, point);
		
	}

}
