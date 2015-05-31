package three_d.math;

public class Point3d {

	public Point3d() {
		this(0, 0, 0);
	}

	public Point3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Point3d vectorProduct(Point3d a, Point3d b) {
		return new Point3d(
				a.y * b.z - a.z * b.y,
				a.z * b.x - a.x * b.z,
				a.x * b.y - a.y * b.x);
	}
	
	public static double scalarProduct(Point3d a, Point3d b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	public static double angle(Point3d a, Point3d b) {
		return Math.asin(vectorProduct(a, b).norm() / a.norm() / b.norm());
	}
	
	public double norm() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public Point3d normalize() {
		double norm = norm();
		return new Point3d(x/norm, y/norm, z/norm);
	}
	
	public Point3d multiply(double a) {
		return new Point3d(x*a, y*a, z*a);
	}
	
	public Point3d plus(Point3d b) {
		return new Point3d(x+b.x, y+b.y, z+b.z);
	}
	
	public Point3d minus(Point3d b) {
		return new Point3d(x-b.x, y-b.y, z-b.z);
	}
	
	private double x, y, z;
}
