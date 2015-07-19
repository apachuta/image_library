package com.image_app.activities;

import org.opencv.core.Mat;

import com.image_app.color_detector.ColorDetector;

public class ColorDetectorActivity extends CameraActivity {

	@Override
	public Mat onCameraFrame(Mat inputFrame) {
		ColorDetector colorDetector = new ColorDetector();
		return colorDetector.processImage(inputFrame, width, height);
	}

}
