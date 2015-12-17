package com.clever_cat.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

import com.clever_cat.image.BitmapProcessor;

public class CameraPreview extends Drawable implements BitmapProcessor {
	
	private Bitmap cameraViewBitmap;
	
	@Override
	public synchronized void processBitmap(Bitmap bitmap) {
		this.cameraViewBitmap = bitmap;
	}

	@Override
	public synchronized void draw(Canvas canvas) {
		if (cameraViewBitmap != null) {
			canvas.drawBitmap(cameraViewBitmap, null, getBounds(), null);
		}
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
}
