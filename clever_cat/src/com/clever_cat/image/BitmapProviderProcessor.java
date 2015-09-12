package com.clever_cat.image;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;

import com.clever_cat.view.OrientationState;

public class BitmapProviderProcessor implements ImageProcessor {

	private List<BitmapProcessor> processors;
	
	public BitmapProviderProcessor() {
		processors = new ArrayList<BitmapProcessor>();
	}
	
	@Override
	public void processImage(Image image) {
		Bitmap baseBitmap = ImageUtil.imageToBitmap(image);
		Matrix rotationMatrix = new Matrix();
		rotationMatrix.postRotate(OrientationState.getInstance().getRelativeCameraRotation());
		rotationMatrix.postScale(-1, 1);
		Bitmap rotatedBitmap = Bitmap.createBitmap(baseBitmap, 
				0, 0, baseBitmap.getWidth(), baseBitmap.getHeight(), rotationMatrix, true);
		
		for (BitmapProcessor processor : processors) {
			processor.processBitmap(rotatedBitmap);
		}
	}

	public synchronized void addBitmapProcessor(BitmapProcessor processor) {
		processors.add(processor);
	}
	
	
	public synchronized void clearBitmapProcessors() {
		processors.clear();
	}

}
