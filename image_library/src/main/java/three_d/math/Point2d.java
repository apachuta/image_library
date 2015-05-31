package three_d.math;

public class Point2d {
	
	public Point2d() {
		this(0, 0);
	}
	
	public Point2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	private double x, y;

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}