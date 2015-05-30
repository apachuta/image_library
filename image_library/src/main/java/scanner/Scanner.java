package scanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nu.pattern.OpenCV;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import common.ImageProcessor;
import common.VideoProcessor;

public class Scanner implements ImageProcessor {

	static {
		OpenCV.loadLibrary();
	}
	
	public Mat processImage(Mat inputImage, int width, int height) {
		Mat orig = inputImage.clone();

		double scale = resize(inputImage, 500);
		
		Mat grey = new Mat();
		Imgproc.cvtColor(inputImage, grey, Imgproc.COLOR_RGB2GRAY);
		Imgproc.GaussianBlur(grey, grey, new Size(5, 5), 0);
		Imgproc.Canny(grey, grey, 50, 150);
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(grey, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		
		Collections.sort(contours, new Comparator<MatOfPoint>() {
			public int compare(MatOfPoint o1, MatOfPoint o2) {
				return ((Double)Imgproc.contourArea(o2))
						.compareTo(Imgproc.contourArea(o1));
			}});
		
		int contourIdx = 0;
		MatOfPoint2f approx = new MatOfPoint2f();
		for (contourIdx = 0; contourIdx < contours.size(); ++contourIdx) {
			MatOfPoint2f contour2f = new MatOfPoint2f();
			contours.get(contourIdx).convertTo(contour2f, CvType.CV_32FC2);
			
			double length = Imgproc.arcLength(contour2f, true);
			Imgproc.approxPolyDP(contour2f, approx, length * 0.02, true);
			if (Math.abs(approx.size().height - 4) < 1e-6) {
				break;
			}
		}
		if (contourIdx == contours.size()) {
			return grey;
		}
		
		Imgproc.drawContours(inputImage, contours, contourIdx, new Scalar(255));

		MatOfPoint2f corners = new MatOfPoint2f(
				new Point(0,0),
				new Point(0,height-1),
				new Point(width-1, height-1),
				new Point(width-1, 0));
		MatOfPoint2f scaled = new MatOfPoint2f();
		Core.multiply(approx, new Scalar(1/scale, 1/scale), scaled);
		scaled.convertTo(scaled, CvType.CV_32FC2);
		//System.out.println(approx.dump());
		//System.out.println(scaled.dump());
		Mat transform = Imgproc.getPerspectiveTransform(scaled, corners);
		Mat document = new Mat();
		Imgproc.warpPerspective(orig, document, transform, new Size(width, height), Imgproc.INTER_LINEAR);

		return document;
	}
	
	private static double resize(Mat image, int maxDimension) {
		double scale = Math.min(
			maxDimension / image.size().height,
			maxDimension / image.size().width);
		Size newSize = new Size(image.size().width * scale, image.size().height * scale);
		Imgproc.resize(image, image, newSize);
		return scale;
	}
	
	public static void main(String[] args) {
		VideoProcessor videoProcessor = new VideoProcessor(new Scanner(), 800, 600);
		videoProcessor.run();
	}

}
