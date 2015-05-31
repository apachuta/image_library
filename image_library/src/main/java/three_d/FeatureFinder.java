package three_d;

import java.awt.Color;
import java.util.List;

import nu.pattern.OpenCV;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import display.ImageDisplay;
import display.mark.CircleMark;

public class FeatureFinder {
	
	public static String PROJECT_PATH = System.getProperty("user.dir");
	public static String RESOURCES_PATH = PROJECT_PATH + "/src/main/resources";
	
	static { 
		OpenCV.loadLibrary(); 
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
		
		Mat input = Highgui.imread(RESOURCES_PATH + "/house.jpg");
		double scale = resize(input, 500);
		Imgproc.GaussianBlur(input, input, new Size(5, 5), 0);
		
		FeatureDetector fd = FeatureDetector.create(FeatureDetector.BRISK);
		MatOfKeyPoint keyPoints = new MatOfKeyPoint();
		fd.detect(input, keyPoints);
		
		ImageDisplay display = new ImageDisplay();
		display.setImage(input);
		
		List<KeyPoint> keyPointsList= keyPoints.toList();
		for (KeyPoint keyPoint : keyPointsList) {
			System.out.println(keyPoint.toString());
			display.addMark(new CircleMark()
	    	.setPosition((int)keyPoint.pt.x, (int)keyPoint.pt.y)
	    	.setColor(Color.blue)
	    	.setSize((int)keyPoint.size));
		}
	
		display.show();
	}
}
