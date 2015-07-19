package com.image_app.dominant_colors;

//import static java.util.Collections.shuffle;
import static java.util.Collections.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.image_app.common.ImageProcessor;

import android.util.Log;

public class DominantColors implements ImageProcessor {
	
	public Mat processImage(Mat inputImage, int width, int height) {
		Size inputSize = inputImage.size();

        Log.i("DominantColors", "=== next frame ===");
        Log.i("DominantColors", inputSize.toString());
        Log.i("DominantColors", new Size(width, height).toString());
		
		int sampleRatio = 20;
		
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
		int iterations = 20;
		List<double[]> means = KMeans.findMeans(pixelsList, k, iterations);
		
		sort(means, new Comparator<double[]>() {
			public int compare(double[] o1, double[] o2) {
				return Double.valueOf(o1[0] + o1[1] + o1[2]).compareTo(o2[0] + o2[1] + o2[2]);
			}
		});
		
		//Mat outputImage = new Mat(new Size(width, height), CvType.CV_32FC3);
		Mat outputImage = new Mat();
		Imgproc.resize(inputImage, outputImage, new Size(width, height));
		
		for (int i = 0; i < k; ++i) {
			Core.rectangle(outputImage,
					new Point((double) i * width / k, 0),
					new Point((double) (i+1) * width / k, (double) height / 10),
					new Scalar(means.get(i)),
					Core.FILLED);
		}
		return outputImage;
	}
}
