package dominant_colors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KMeans {

	public static List<double[]> findMeans(List<double[]> values, int k, int iterations) {
		if (values.size() < k || k < 1 || values.isEmpty()) {
			throw new RuntimeException("Invalid input");
		}
		
		List<double[]> means = new ArrayList<double[]>();
		for (int i = 0; i < k; i++) {
			means.add(values.get(i));
		}
		
		List<List<double[]>> clusters = new ArrayList<List<double[]>>();
		for (int i = 0; i < k; i++) {
			clusters.add(new ArrayList<double[]>());
		}
		
		for(int round = 0; round < iterations; round++) {
			
			// group values in clusters
			for (double[] value : values) {
				int clusterNumber = 0;
				double bestDistance = Double.MAX_VALUE;
				for (int i = 0; i < k; i++) {
					double distance = distance(value, means.get(i));
					if (distance < bestDistance) {
						bestDistance = distance;
						clusterNumber = i;
					}
				}
				clusters.get(clusterNumber).add(value);
			}
			// find new means for clusters
			for (int i = 0; i < k; i++) {
				double[] newMean = mean(clusters.get(i));
				if (newMean != null) {
					means.set(i, newMean);
				}
			}
		}
		
		return means;
	}
	
	private static double distance(double[] a, double[] b) {
		if (a.length != b.length) {
			throw new RuntimeException("Invlid input");
		}
		double result = 0;
		for (int i = 0; i < a.length; i++) {
			result += (a[i] - b[i]) * (a[i] - b[i]);
		}
		return Math.sqrt(result);
	}
	
	private static double[] mean(List<double[]> cluster) {
		if (cluster.isEmpty()) {
			return null;
		}
		
		int size = cluster.get(0).length;
		double[] mean = new double[size];
		for (double[] point : cluster) {
			for (int i = 0; i < size; i++) {
				mean[i] += point[i];
			}
		}
		for (int i = 0; i < size; i++) {
			mean[i] /= cluster.size();
		}
		return mean;
	}
	
	public static void main(String[] args) {
		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[]{0,0});
		values.add(new double[]{1,0});
		values.add(new double[]{0,2});
		values.add(new double[]{1,1});
		values.add(new double[]{100,101});
		values.add(new double[]{110,110});
		values.add(new double[]{110,100});
		values.add(new double[]{-100,-101});
		values.add(new double[]{-110,-110});
		values.add(new double[]{-110,-100});
		List<double[]> means = findMeans(values, 3, 100);
		
		for (double[] m : means) {
			System.out.println(Arrays.toString(m));
		}
	}
}
