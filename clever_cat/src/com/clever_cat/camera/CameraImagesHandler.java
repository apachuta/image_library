package com.clever_cat.camera;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import com.clever_cat.image.ImageProcessor;
import com.clever_cat.loop.CyclicTimer;
import com.clever_cat.loop.CyclicTimerProvider;

import android.hardware.camera2.CameraAccessException;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;

public class CameraImagesHandler {

	private static Logger logger = Logger.getLogger("CameraImagesHandler");
	
	private CameraController cameraController;
	private List<ImageProcessor> processors;

	private ImageProcessingRunnable processingRunnable;
	private Thread processingThread;
	
	public CameraImagesHandler(CameraController cameraController) {
		this.cameraController = cameraController;
		processors = new ArrayList<ImageProcessor>();
	}
	
	public synchronized void addImageProcessor(ImageProcessor processor) {
		processors.add(processor);
	}


	public synchronized void clearImageProcessors() {
		processors.clear();
	}

	public synchronized void startServingImages() {
		Log.d("CatView", "startServingImages");
		cameraController.runWhenCameraAvailable(createProcessImagesLoopCallback());
	}
	
	public void stopServingImages() {
		Log.d("CatView", "stopServingImages");
		try {
			// This order is necessary to prevent runnable from ending before it event starts.
			cameraController.stopServingImages();
			synchronized (this) {
				if (processingRunnable != null) {
					logger.info("stop");
					processingRunnable.stop();
					logger.info("join");
					processingThread.join();
					logger.info("joined!");
				}
			}
		} catch (CameraAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Runnable createProcessImagesLoopCallback() {
		return new Runnable() {
			@Override
			public void run() {
				try {
					ImageReader imageReader = cameraController.startServingImages();
					synchronized (CameraImagesHandler.this) {
						processingRunnable = new ImageProcessingRunnable(imageReader);
						processingThread = new Thread(processingRunnable);
						processingThread.start();
					}
				} catch (CameraAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	
	private class ImageProcessingRunnable implements Runnable {

		private static final int ACTIVE_WAITING_DELAY_MS = 50;
		
		private ImageReader imageReader;
		private AtomicBoolean isActive;
		
		public ImageProcessingRunnable(ImageReader imageReader) {
			this.imageReader = imageReader;
			isActive = new AtomicBoolean(true);
		}

		public void stop() {
			isActive.set(false);
		}
		
		@Override
		public void run() {
			CyclicTimer cyclicTimer = CyclicTimerProvider.newCyclicTimer("Camera image retrieval");
			while (isActive.get()) {
				cyclicTimer.measureBefore();
				Image latestImage = imageReader.acquireLatestImage();
				if (latestImage == null) {
					try {
						Thread.sleep(ACTIVE_WAITING_DELAY_MS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				cyclicTimer.measureAfter();
				
				for (ImageProcessor processor : processors) {
					processor.processImage(latestImage);
				}
				
				latestImage.close();
			}
		}
		
	}
}
