package com.image_app.activities;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public abstract class CameraActivity extends Activity implements CvCameraViewListener {

    protected static final String TAG = "IMAGE_APP";
    
	private CameraBridgeViewBase mOpenCvCameraView;
	protected int width;
	protected int height;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mOpenCvCameraView = (CameraBridgeViewBase) new JavaCameraView(this, -1);
        setContentView(mOpenCvCameraView);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

	@Override
	protected void onResume() {
		super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, new BaseLoaderCallback(this) {
        	@Override
        	public void onManagerConnected(int status) {
                switch (status) {
	                case SUCCESS: {
	                    Log.i(TAG, "OpenCV loaded successfully");
	            		mOpenCvCameraView.enableView();
	                } break;
	                default: {
	            		super.onManagerConnected(status);
	                } break;
                }
        	}
		});

	}
	
	@Override
	protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
}
	
	@Override
	public void onCameraViewStarted(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
	}

	@Override
	public abstract Mat onCameraFrame(Mat inputFrame);
}
