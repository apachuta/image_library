package com.image_app.common;

import org.opencv.core.Mat;

public interface ImageProcessor {

	public Mat processImage(Mat inputImage, int width, int height);
	
}
