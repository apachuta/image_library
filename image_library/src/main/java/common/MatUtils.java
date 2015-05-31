package common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class MatUtils {

	public static BufferedImage matToImage(Mat mat) {
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
