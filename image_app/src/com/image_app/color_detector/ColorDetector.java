package com.image_app.color_detector;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.image_app.common.ImageProcessor;

public class ColorDetector implements ImageProcessor {
	
	private Scalar lowerBound;
	private Scalar upperBound;
	
	public ColorDetector() {

		// blue?
		lowerBound = new Scalar(112, 48, 80);
		upperBound = new Scalar(127, 255, 255);
		
		// purple?
		//lowerBound = new Scalar(170, 80, 40);
		//upperBound = new Scalar(195, 255, 255);
	}

	public Mat processImage(Mat inputImage, int width, int height) {
		Mat resultMat = new Mat();
		
		Mat skinMask = new Mat();
		Mat hsvMat = new Mat();
		Imgproc.resize(inputImage, inputImage, new Size(width, height));
		Imgproc.cvtColor(inputImage, hsvMat, Imgproc.COLOR_BGR2HSV);
		Core.inRange(hsvMat, lowerBound, upperBound, skinMask);
		//Core.bitwise_not(skinMask, skinMask);
		
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
		Imgproc.erode(skinMask, skinMask, kernel, new Point(-1, -1), 2);
		Imgproc.dilate(skinMask, skinMask, kernel, new Point(-1, -1), 2);
		
		Imgproc.GaussianBlur(skinMask, skinMask, new Size(3, 3), 0);
		
		/*
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(skinMask, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		
		Collections.sort(contours, new Comparator<MatOfPoint>() {
			public int compare(MatOfPoint o1, MatOfPoint o2) {
				return ((Double)Imgproc.contourArea(o2))
						.compareTo(Imgproc.contourArea(o1));
			}});
		
		Imgproc.drawContours(imageMat, contours, 0, new Scalar(255), Core.FILLED);
		*/
		
		Core.bitwise_and(inputImage, inputImage, resultMat, skinMask);	
		return resultMat;
	}

}
