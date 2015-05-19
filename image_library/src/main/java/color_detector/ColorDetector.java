package color_detector;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import nu.pattern.OpenCV;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class ColorDetector {
	
	static {
		OpenCV.loadLibrary();
	}
	
	private FacePanel panel;
	private Scalar lowerBound;
	private Scalar upperBound;
	
	public ColorDetector() {
		initPanel();
		//lowerBound = new Scalar(0, 48, 80);
		//upperBound = new Scalar(20, 255, 255);
		lowerBound = new Scalar(170, 80, 40);
		upperBound = new Scalar(195, 255, 255);
	}
	
	public void run() {
		VideoCapture camera = new VideoCapture(0);
		Mat imageMat = new Mat();
		while (true) {
			if (!camera.grab()) {
				break;
			}
			camera.read(imageMat);
			Mat resultMat = new Mat();
			
			Mat skinMask = new Mat();
			Mat hsvMat = new Mat();
			Imgproc.resize(imageMat, imageMat, new Size(400, 300));
			Imgproc.cvtColor(imageMat, hsvMat, Imgproc.COLOR_BGR2HSV);
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
			
			Core.bitwise_and(imageMat, imageMat, resultMat, skinMask);	
			
			panel.setImage(matToImage(resultMat));
			panel.repaint();
		}
	}

	private BufferedImage matToImage(Mat mat) {
		MatOfByte mob = new MatOfByte();
		Highgui.imencode(".jpg", mat, mob);
		byte[] byteArray = mob.toArray();
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new ByteArrayInputStream(byteArray));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}
	
	private void initPanel() {
		JFrame frame = new JFrame("BasicPanel");  
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    frame.setSize(400, 300);  
	    panel = new FacePanel();
	    frame.setContentPane(panel);       
	    frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		ColorDetector colorDetector = new ColorDetector();
		colorDetector.run();
	}
}
