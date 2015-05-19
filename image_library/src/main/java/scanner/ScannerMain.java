package scanner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import nu.pattern.OpenCV;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import display.ImageDisplay;

public class ScannerMain {

	public static String PROJECT_PATH = System.getProperty("user.dir");
	public static String RESOURCES_PATH = PROJECT_PATH + "/src/main/resources";

	static { OpenCV.loadLibrary(); }
	
	public static void main(String[] args) throws IOException {
		
		Mat image = Highgui.imread(RESOURCES_PATH + "/sheet.jpg");
		Mat orig = image.clone();

		double scale = resize(image, 500);
		
		Mat grey = new Mat();
		Imgproc.cvtColor(image, grey, Imgproc.COLOR_RGB2GRAY);
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
			throw new IllegalStateException("Picture doesn't contain a document.");
		}
		
		Imgproc.drawContours(image, contours, contourIdx, new Scalar(255));

		int width = 800;
		int height = 600;
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
		
		String output_filename = "/tmp/image.png";

		resize(orig, 800);
		Highgui.imwrite(output_filename, orig);
		
	    ImageDisplay imageDisplay = new ImageDisplay();
	    imageDisplay.setImage(openImage(output_filename));
	    imageDisplay.show();
	    
	    Highgui.imwrite(output_filename, document);
		
	    imageDisplay = new ImageDisplay();
	    imageDisplay.setImage(openImage(output_filename));
	    imageDisplay.show();
	}
	
	private static double resize(Mat image, int maxDimension) {
		double scale = Math.min(
			maxDimension / image.size().height,
			maxDimension / image.size().width);
		Size newSize = new Size(image.size().width * scale, image.size().height * scale);
		Imgproc.resize(image, image, newSize);
		return scale;
	}
	
	private static BufferedImage openImage(String filename) throws IOException {
		BufferedImage image = null;
	    try {
			image = ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	    return image;
	}
	
}
