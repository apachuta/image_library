package com.clever_cat.image;

import java.nio.ByteBuffer;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

/**
 * Utility class for converting {@link Image}.
 */
public final class ImageUtil {

	private ImageUtil() {}
	
	public static Bitmap imageToBitmap(Image image) {
		ByteBuffer buffer = image.getPlanes()[0].getBuffer();
		buffer.rewind();
		byte[] bytes = new byte[buffer.remaining()];
		buffer.get(bytes);
		
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	public static Mat bitmapToMat(Bitmap bitmap) {
		Bitmap bitmap32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		Mat mat = new Mat();
		Utils.bitmapToMat(bitmap32, mat);
		return mat;
	}

	public static Mat imageToMat(Image image) {
		Bitmap bitmap = imageToBitmap(image);
		Bitmap bitmap32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		Mat mat = new Mat();
		Utils.bitmapToMat(bitmap32, mat);
/*
		ByteBuffer buffer = image.getPlanes()[0].getBuffer();
		buffer.rewind();
		byte[] bytes = new byte[buffer.remaining()];
		buffer.get(bytes);

	    Mat mat = new Mat();
	    mat.put(0, 0, bytes);
*/
		return mat;
	}
}
