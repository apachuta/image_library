package image_library;

import nu.pattern.OpenCV;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class HelloWorld {
	
	private String text;
	
	public HelloWorld(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	static { OpenCV.loadLibrary(); }
	
	public static void main(String[] args) {
		System.out.println(new HelloWorld("Hello world!"));
		
	    System.out.println("Welcome to OpenCV " + Core.VERSION);
	    Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
	    System.out.println("OpenCV Mat: " + m);
	    Mat mr1 = m.row(1);
	    mr1.setTo(new Scalar(1));
	    Mat mc5 = m.col(3);
	    mc5.setTo(new Scalar(5));
	    System.out.println("OpenCV Mat data:\n" + m.dump());
	}
}
