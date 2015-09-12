package com.clever_cat.camera;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;

public class CameraControllerProvider {

	private static CameraController frontCameraController;
	
	public static CameraController getFrontCameraController(Context context) throws CameraAccessException {
		if (frontCameraController == null) {
			synchronized (CameraControllerProvider.class) {
				if (frontCameraController == null) {
					frontCameraController = new CameraController(context);
					frontCameraController.acquireFrontCamera();
				}
			}
		}
		return frontCameraController;
	}
}
