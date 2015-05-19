package color_detector;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class FacePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (image == null) {
			return;
		}
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
	}
}
