package com.image_app.activities;

import org.opencv.core.Mat;

import com.image_app.dominant_colors.DominantColors;

public class DominantColorsActivity extends CameraActivity {

	@Override
	public Mat onCameraFrame(Mat inputFrame) {
		DominantColors dominantColors = new DominantColors();
		return dominantColors.processImage(inputFrame, width, height);
	}

}
