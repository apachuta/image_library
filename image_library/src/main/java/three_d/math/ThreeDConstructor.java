package three_d.math;

import java.util.ArrayList;
import java.util.List;

public class ThreeDConstructor {
	
	private static final int ITERATIONS = 200;
	
	public static List<Point3d> findThreeDMapping(
			List<Point2d> firstImageFeatures,
			List<Point2d> secondImageFeatures) {
		if (firstImageFeatures.size() != secondImageFeatures.size()) {
			throw new IllegalArgumentException(
					"Specified feature lists of different sizes");
		}
		
		ThreeDConstructor constructor = new ThreeDConstructor(
				firstImageFeatures, secondImageFeatures);
		
		int left = ITERATIONS;
		while (left > 0) {
			constructor.improve(Math.sqrt(left) / ITERATIONS);
		}
		
		return constructor.getCoordinates();
	}
	
	private ThreeDConstructor(
			List<Point2d> a_coord,
			List<Point2d> b_coord) {
		this.a_coord = a_coord;
		this.b_coord = b_coord;
	
		a_pos = new Point3d();
		b_pos = new Point3d();
		a_dir = new Point3d(0, 0, 1);
		b_dir = new Point3d(0, 0, 1);
		a_side = new Point3d(0, 1, 0);
		b_side = new Point3d(0, 1, 0);
		a_f = 1.;
		b_f = 1.;
		coord = new ArrayList<Point3d>();
		for (int i = 0; i < a_coord.size(); ++i) {
			coord.add(new Point3d(0, 0, 2));
		}
	}

	private void improve(double alpha) {
		// TODO: implement.
	}
	
	private double cost() {
		return cost(a_pos, a_dir, a_side, a_f, a_coord, coord)
				+ cost(b_pos, b_dir, b_side, b_f, b_coord, coord);
	}
	
	private static double cost(Point3d pos, Point3d dir, Point3d side,
			double f, List<Point2d> coord, List<Point3d> point) {
		double sum = 0;
		for (int i = 0; i < point.size(); ++i) {
			sum += cost(pos, dir, side, f, coord.get(i), point.get(i));
		}
		return sum;
	}

	private static double cost(Point3d pos, Point3d dir, Point3d side,
			double f, Point2d point2d, Point3d point3d) {
		Point3d perp = Point3d.vectorProduct(dir, side).normalize();
		Point3d diff = dir.normalize().multiply(f)
				.plus(side.normalize().multiply(point2d.getX()))
				.plus(perp.normalize().multiply(point2d.getY()));
		return Point3d.angle(diff, point3d.minus(pos));
	}

	private List<Point3d> getCoordinates() {
		return coord;
	}

	private final List<Point2d> a_coord;
	private final List<Point2d> b_coord;
	
	private Point3d a_pos;
	private Point3d b_pos;
	private Point3d a_dir;
	private Point3d b_dir;
	private Point3d a_side;
	private Point3d b_side;
	private double a_f;
	private double b_f;
	private List<Point3d> coord;
}
