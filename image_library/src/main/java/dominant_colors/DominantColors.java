package dominant_colors;

//import static java.util.Collections.shuffle;
import static java.util.Collections.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import nu.pattern.OpenCV;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import common.ImageProcessor;
import common.VideoProcessor;

public class DominantColors implements ImageProcessor {
	
	public Mat processImage(Mat inputImage, int width, int height) {
		Size inputSize = inputImage.size();
		
		int sampleRatio = 10;
		
		Mat smallImage = new Mat();
		Size smallSize = new Size(inputSize.width / sampleRatio, inputSize.height / sampleRatio);
		Imgproc.resize(inputImage, smallImage, smallSize);
		
		List<double[]> pixelsList = new ArrayList<double[]>();
		for (int i = 0; i < smallSize.height; ++i) {
			for (int j = 0; j < smallSize.width; ++j) {
				pixelsList.add(smallImage.get(i, j));
			}
		}
		//shuffle(pixelsList);
		//List<double[]> pixelsSample = pixelsList.subList(0, pixelsList.size() / sampleRatio);
		
		int k = 5;
		int iterations = 50;
		List<double[]> means = KMeans.findMeans(pixelsList, k, iterations);
		
		sort(means, new Comparator<double[]>() {
			public int compare(double[] o1, double[] o2) {
				return new Double(o1[0] + o1[1] + o1[2]).compareTo(o2[0] + o2[1] + o2[2]);
			}
		});
		
		//Mat outputImage = new Mat(new Size(width, height), CvType.CV_32FC3);
		Mat outputImage = new Mat();
		Imgproc.resize(inputImage, outputImage, new Size(width, height));
		for (int i = 0; i < k; ++i) {
			int left = i * width / k;
			int right = (i + 1) * width / k;
			for (int x = left; x < right; ++x) {
				for (int y = 0; y < height/10; ++y) {
					outputImage.put(y, x, means.get(i));
				}
			}
		}
		
		return outputImage;
	}

	public static void main(String[] args) {
		VideoProcessor videoProcessor = new VideoProcessor(
				new DominantColors(), 800, 600);
		videoProcessor.run();
	}
}
