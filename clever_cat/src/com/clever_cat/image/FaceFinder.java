package com.clever_cat.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;

import com.clever_cat.R;
import com.clever_cat.drawable.CatDrawable;

public class FaceFinder implements BitmapProcessor {

	private Context context;
	private CatDrawable catDrawable;

	private CascadeClassifier classifier;


	public FaceFinder(Context context, CatDrawable catDrawable) {
		this.catDrawable = catDrawable;
		this.context = context;
	}

	private void initializeClassifier(Context context) {
		try {
			InputStream is = context.getResources()
					.openRawResource(R.raw.haarcascade_frontalface_alt);
			File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
			File cascadeFile = new File(cascadeDir, "cascade_frontal_face.xml");
			FileOutputStream os = new FileOutputStream(cascadeFile);
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			is.close();
			os.close();
			classifier = new CascadeClassifier(cascadeFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void processBitmap(Bitmap bitmap) {
		if (classifier == null) {
			return;
		}

		Mat mat = ImageUtil.bitmapToMat(bitmap);
		MatOfRect faceDetections = new MatOfRect();
		classifier.detectMultiScale(mat, faceDetections);
		Rect selectedRect = selectRectangle(faceDetections);
		if (selectedRect != null) {
			catDrawable.setLookDirection(
					new android.graphics.Rect(0, 0, mat.width(), mat.height()),
					new Point(selectedRect.x + selectedRect.width / 2,
							selectedRect.y + selectedRect.height / 2));
		}
	}

	private Rect selectRectangle(MatOfRect matOfRect) {
		Rect[] rects = matOfRect.toArray();
		if(rects.length != 0) {
			return rects[0];
		}
		return null;
	}

	public void enable() {
		initializeClassifier(context);
	}
}
