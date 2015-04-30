package image_library;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import display.ImageDisplay;
import display.mark.CircleMark;

public class SampleDrawImage {
	
	public static String PROJECT_PATH = System.getProperty("user.dir");
	public static String RESOURCES_PATH = PROJECT_PATH + "/src/main/resources";
	
	public static void main(String[] args) {
		
		String image_path = RESOURCES_PATH + "/house.jpg";
		BufferedImage image = null;
	    try {
			image = ImageIO.read(new File(image_path));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	    ImageDisplay imageDisplay = new ImageDisplay();
	    imageDisplay.setImage(image);
	    imageDisplay.addMark(new CircleMark());
	    imageDisplay.addMark(new CircleMark()
	    	.setPosition(200, 100)
	    	.setColor(Color.blue));
	    imageDisplay.show();
	}
	
}
