package com.clever_cat.image;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.Image.Plane;

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
		return mat;
	}

	public static Bitmap imageRgb565ToBitmap(Image image) {
		ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
		byte[] array = new byte[byteBuffer.capacity()];
		byteBuffer.get(array, 0, array.length);
		Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.RGB_565);
		bitmap.copyPixelsFromBuffer(byteBuffer);
		return bitmap;
	}
	
	public static Bitmap imageYuv420ToBitmap(Image image) {
		int alpha = 255;
		int width = image.getWidth();
		int height = image.getHeight();
		Plane yPlane = image.getPlanes()[0];
		//Plane uPlane = image.getPlanes()[1];
		//Plane vPlane = image.getPlanes()[2];

		ByteBuffer yBuffer = yPlane.getBuffer();
		//ByteBuffer uBuffer = uPlane.getBuffer();
		//ByteBuffer vBuffer = vPlane.getBuffer();
		int yRowStride = yPlane.getRowStride();
		//int yPixelStride = yPlane.getPixelStride();
		//int uRowStride = uPlane.getRowStride();
		//int uPixelStride = uPlane.getPixelStride();
		//int vRowStride = vPlane.getRowStride();
		//int vPixelStride = vPlane.getPixelStride();

		IntBuffer intBuffer = IntBuffer.allocate(width * height);
		intBuffer.rewind();
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				//int y2 = y/2;
				//int x2 = x/2;
				int Y = yBuffer.get(y * yRowStride + x) & 0xff;  // y pixel stride is 1
				//int Cb = uBuffer.get(y2 * uRowStride + x2 * uPixelStride) & 0xff;
				//int Cr = vBuffer.get(y2 * vRowStride + x2 * vPixelStride) & 0xff;
				
				float Yf = 1.164f * (Y - 16.0f);
				//float Cbf = Cb - 128.0f;
				//float Crf = Cr - 128.0f;
				int R = (int) (Yf);  // + 1.402 * Crf);
				int G = (int) (Yf);  // - 0.34414 * Cbf - 0.71414 * Crf);
				int B = (int) (Yf);  // + 1.772 * Cbf);
				
				R = R < 0 ? 0 : R > 255 ? 255 : R;
				G = G < 0 ? 0 : G > 255 ? 255 : G;
				B = B < 0 ? 0 : B > 255 ? 255 : B;

				intBuffer.put(alpha * (1 << 24) + R * (1 << 16) + G * (1 << 8) + B);
			}
		}
		intBuffer.rewind();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.copyPixelsFromBuffer(intBuffer);
		return bitmap;
	}
}
