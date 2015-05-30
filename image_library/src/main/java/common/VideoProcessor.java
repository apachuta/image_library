package common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class VideoProcessor {
	private FacePanel panel;
	private ImageProcessor imageProcessor;
	private final int width;
	private final int height;
	
	public VideoProcessor(ImageProcessor imageProcessor, int width, int height) {
		this.width = width;
		this.height = height;
		this.imageProcessor = imageProcessor;
		initPanel();
	}

	public void run() {
		VideoCapture camera = new VideoCapture(0);
		Mat imageMat = new Mat();
		while (true) {
			if (!camera.grab()) {
				break;
			}
			camera.read(imageMat);
			try {
				Mat resultMat = imageProcessor.processImage(imageMat, width, height);
				panel.setImage(matToImage(resultMat));
				panel.repaint();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	private void initPanel() {
		JFrame frame = new JFrame("BasicPanel");  
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    frame.setSize(width, height);  
	    panel = new FacePanel();
	    frame.setContentPane(panel);       
	    frame.setVisible(true);
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
}
		
