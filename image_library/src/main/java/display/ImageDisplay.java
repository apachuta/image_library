package display;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import display.mark.Mark;

public class ImageDisplay {
	
	private BufferedImage bufferedImage;
	private List<Mark> marks;
	
	public ImageDisplay() {
		bufferedImage = null;
	    marks = new ArrayList<Mark>();
	}
	
	public void setImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}
	
	public void addMark(Mark mark) {
		this.marks.add(mark);
	}
	
	public void show() {
		JFrame frame = new JFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Graphics2D graphics = bufferedImage.createGraphics();
	    for (Mark mark : marks) {
	    	mark.drawIn(graphics);
	    }
	    ImageIcon icon = new ImageIcon(bufferedImage);
	    JLabel label = new JLabel(icon);
	    frame.add(label);
	    frame.pack();
	    frame.validate();
	    frame.setVisible(true);
	}
	
}
