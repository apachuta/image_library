package display.mark;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class CircleMark extends Mark {
	
	@Override
	public void drawIn(Graphics2D graphics) {
		graphics.setPaint(color);
		graphics.setStroke(new BasicStroke(width));
		graphics.drawOval(x - size/2, y - size/2, size, size);
	}
}
