package face_recogintion;

import java.awt.Color;

import nu.pattern.OpenCV;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import common.ImageProcessor;
import common.VideoProcessor;

public class FaceRecognizer implements ImageProcessor {

	static {
		OpenCV.loadLibrary();
	}
	
	private CascadeClassifier classifier;
	
	public FaceRecognizer() {
		String path = getClass().getClassLoader()
				.getResource("haarcascade_frontalface_alt.xml").getPath();
		classifier = new CascadeClassifier(path);
	}
	
	@Override
	public Mat processImage(Mat inputImage, int width, int height) {
		Imgproc.resize(inputImage, inputImage, new Size(width, height));
		MatOfRect faceDetections = new MatOfRect();
		classifier.detectMultiScale(inputImage, faceDetections);
		for (Rect rect : faceDetections.toArray()) {
			Core.rectangle(inputImage, 
					new Point(rect.x, rect.y), 
					new Point(rect.x + rect.width, rect.y + rect.height) , 
					new Scalar(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue()));
		}
		return inputImage;
	}

	public static void main(String[] args) {
		ImageProcessor faceRecognizer = new FaceRecognizer();
		VideoProcessor videoProcessor = new VideoProcessor(faceRecognizer, 400, 300);
		videoProcessor.run();
	}
}
