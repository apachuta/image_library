package com.clever_cat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.camera2.CameraAccessException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.clever_cat.camera.CameraController;
import com.clever_cat.camera.CameraControllerProvider;
import com.clever_cat.camera.CameraImagesHandler;
import com.clever_cat.drawable.CameraPreview;
import com.clever_cat.drawable.CatDrawable;
import com.clever_cat.image.BitmapProviderProcessor;
import com.clever_cat.image.FaceFinder;
import com.clever_cat.loop.Clock;
import com.clever_cat.loop.DiscreteClock;
import com.clever_cat.loop.MainLoop;

public class CatView extends SurfaceView implements SurfaceHolder.Callback {

	private static final double PREVIEW_LENGTH_RATIO = 0.4;
	private static final double PREVIEW_MARGIN_RATIO = 0.05;
	
	//private RotationState rotationState;
	private SurfaceHolder surfaceHolder;
	private CatDrawable catDrawable;
	private CameraImagesHandler cameraImagesHandler;
	private CameraPreview cameraPreview;
	private FaceFinder faceFinder;
	private boolean openCVEnabled = false;
	private MainLoop mainLoop;
	private DiscreteClock discreteClock;
	
	public CatView(Context context, AttributeSet attrs) throws CameraAccessException {
		super(context, attrs);
		getHolder().addCallback(this);
		
		Log.d("CatView", "CatView");
		discreteClock = new DiscreteClock();
		discreteClock.setTimeMillis(System.currentTimeMillis());
		catDrawable = new CatDrawable((Clock) discreteClock);
		cameraPreview = new CameraPreview();
		faceFinder = new FaceFinder(context, catDrawable);
        mainLoop = new MainLoop(this, discreteClock);
		
		CameraController cameraController = CameraControllerProvider.getFrontCameraController(context);
		cameraImagesHandler = new CameraImagesHandler(cameraController);
	}

	public void setOpenCVEnabled(boolean isEnabled) {
		Log.i("CatView", "OpenCV enabled: " + isEnabled);
		this.openCVEnabled = isEnabled;
		if (isEnabled) {
			faceFinder.enable();
		}
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("CatView", "surfaceCreated");
		surfaceHolder = getHolder();
		BitmapProviderProcessor bitmapProviderProcessor =
				new BitmapProviderProcessor();
		bitmapProviderProcessor.addBitmapProcessor(cameraPreview);
		bitmapProviderProcessor.addBitmapProcessor(faceFinder);
		cameraImagesHandler.addImageProcessor(bitmapProviderProcessor);
		cameraImagesHandler.startServingImages();
        mainLoop.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d("CatView", "surfaceChanged");
		//rotationState.setDisplayRotation(getDisplayRotation());
		catDrawable.setBounds(0, 0, width, height);
		int previewLeft = (int) (Math.min(width, height) * PREVIEW_MARGIN_RATIO);
		int previewBottom = height - previewLeft;
		int previewRight = (int) (previewLeft + width * PREVIEW_LENGTH_RATIO);
		int previewTop = (int) (previewBottom - height * PREVIEW_LENGTH_RATIO);
		OrientationState.getInstance().setDisplayRotation(getDisplayRotation());
		cameraPreview.setBounds(previewLeft, previewTop, previewRight, previewBottom);
		redraw();
	}
	
	private int getDisplayRotation() {
		WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		return windowManager.getDefaultDisplay().getRotation();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("CatView", "surfaceDestroyed");
		mainLoop.stop();
		cameraImagesHandler.stopServingImages();
		cameraImagesHandler.clearImageProcessors();
		surfaceHolder = null;
		Log.d("CatView", "surfaceDestroyed2");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//catDrawable.setLookDirection(surfaceHolder.getSurfaceFrame(), 
		//	new Point((int) event.getX(), (int) event.getY()));
		redraw();
		return true;
	}
	
	public void redraw() {
		Log.d("CatView", "redraw");
		Canvas canvas = surfaceHolder.lockCanvas();
		if (canvas == null) {
			return;
		}
		catDrawable.draw(canvas);
		cameraPreview.draw(canvas);
		surfaceHolder.unlockCanvasAndPost(canvas);
	}
}
