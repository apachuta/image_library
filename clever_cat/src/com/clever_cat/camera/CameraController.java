package com.clever_cat.camera;

import java.util.Arrays;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.util.Log;

import com.clever_cat.view.OrientationState;

public class CameraController {

	private static final String TAG = "CameraController";
	
	private Context context;
	private CameraManager cameraManager;
	private CameraDevice cameraDevice;
	private CameraCaptureSession cameraCaptureSession;
	
	private Runnable onOpenedCallback;
	
	public CameraController(Context context) {
		this.context = context;
		cameraManager = (CameraManager) this.context.getSystemService(Context.CAMERA_SERVICE);
	}
	
	/**
	 * Schedules a callback to be run once the camera is available (possibly now).
	 */
	public synchronized void runWhenCameraAvailable(final Runnable callback) {
		synchronized (this) {
			if (cameraDevice == null) {
				onOpenedCallback = callback;
				return;
			}  // else run callback.
		}
		callback.run();
	}
	
	/**
	 * Opens front camera.
	 */
	public void acquireFrontCamera() throws CameraAccessException {
		Log.i(TAG, "acquireFrontCamera");
		String cameraId = findFrontCamera();
		cameraManager.openCamera(cameraId, new StateCallback() {
			@Override
			public void onOpened(CameraDevice camera) {
				Log.d("CameraController", "onOpened");
				try {
					Runnable callbackToRun = null;
					synchronized (CameraController.this) {
						cameraDevice = camera;
						callbackToRun = onOpenedCallback;
					}				
					
					OrientationState.getInstance().setCameraRotation(getCameraRotation());
					
					if (callbackToRun != null) {
						callbackToRun.run();
					}
				} catch (CameraAccessException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(CameraDevice camera, int error) {
				Log.d("CameraController", "onError"); 
				Log.d("CameraController", String.format("Could not open the camera: %d.", error));
			}
			
			@Override
			public void onDisconnected(CameraDevice camera) {
				Log.e("CameraController", "onDisconnected");
				synchronized (this) {
					cameraDevice = null;
					onOpenedCallback = null;
				}
			}
		}, null);
	}

	private String findFrontCamera() throws CameraAccessException {
		String[] cameraIds = cameraManager.getCameraIdList();
		for (String cameraId : cameraIds) {
			CameraCharacteristics cameraCharacteristics = 
					cameraManager.getCameraCharacteristics(cameraId);
			if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)
					.equals(CameraCharacteristics.LENS_FACING_FRONT)) {
				return cameraId;
			}
		}
		throw new IllegalStateException("Device is missing a front camera");
	}

	/**
	 * Closes camera.
	 */
	public synchronized void releaseFrontCamera() {
		cameraDevice.close();
		cameraDevice = null;
	}

	/**
	 * Starts serving preview images to the returned {@link ImageReader}.
	 */
	public ImageReader startServingImages() throws CameraAccessException {
		/*
		CameraCharacteristics cameraCharacteristics =
				cameraManager.getCameraCharacteristics(cameraDevice.getId());
		StreamConfigurationMap streamConfigurationMap =
				cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
		Size[] outputSizes = streamConfigurationMap.getOutputSizes(ImageReader.class);

		Log.i("XXXXX", "XXXXX");
		for (Size outputSize: outputSizes) {
			Log.i("XXXXX", outputSize.toString());
		}
		throw new IllegalStateException();
		*/
		
		// TODO(check size)
		ImageReader imageReader = ImageReader.newInstance(320, 240, ImageFormat.YUV_420_888, 2);
		
		CaptureRequest.Builder captureBuilder =
	            cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
		captureBuilder.addTarget(imageReader.getSurface());
		
		final CaptureRequest captureRequest = captureBuilder.build();
		
		cameraDevice.createCaptureSession(
				Arrays.asList(imageReader.getSurface()),
				new CameraCaptureSession.StateCallback() {
					@Override
					public void onConfigured(CameraCaptureSession session) {
						try {
							session.setRepeatingRequest(captureRequest, null, null);
							synchronized (this) {
								cameraCaptureSession = session;
							}
						} catch (CameraAccessException e) {
							e.printStackTrace();
						}
					}
	
					@Override
					public void onConfigureFailed(CameraCaptureSession session) {
						Log.e(TAG, "Capture session configuration failed.");
					}
				},
				null);
	
		return imageReader;
		
	}
	
	/**
	 * Stops serving images.
	 */
	public synchronized void stopServingImages() throws CameraAccessException {
		if (onOpenedCallback != null) {
			onOpenedCallback = null;
		}
		if (cameraCaptureSession != null) {
			cameraCaptureSession.abortCaptures();
			cameraCaptureSession = null;
		}
	}

	public int getCameraRotation() throws CameraAccessException {
		return cameraManager.getCameraCharacteristics(cameraDevice.getId()).get(CameraCharacteristics.SENSOR_ORIENTATION);
	}
}
