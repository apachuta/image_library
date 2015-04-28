package image_library;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
	    
	    JFrame frame = new JFrame();
	    ImageIcon icon = new ImageIcon(image);
	    JLabel label = new JLabel(icon);
	    frame.add(label);
	    frame.setDefaultCloseOperation
	           (JFrame.EXIT_ON_CLOSE);
	    frame.pack();
	    frame.setVisible(true);
	}
	
}
