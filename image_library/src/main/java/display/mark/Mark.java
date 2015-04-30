package display.mark;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Mark {
	
	protected int x;
	protected int y;
	protected int size;
	protected Color color;
	protected float width;
	
	public Mark() {
		x = 50;
		y = 50;
		size = 50;
		color = Color.red;
		width = 3;
	}
	
	public Mark setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Mark setSize(int size) {
		this.size = size;
		return this;
	}

	public Mark setColor(Color color) {
		this.color = color;
		return this;
	}
	
	public Mark setWidth(float width) {
		this.width = width;
		return this;
	}
	
	public abstract void drawIn(Graphics2D graphics);
}
