package com.clever_cat;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.clever_cat.loop.CyclicTimerProvider;
import com.clever_cat.view.CatView;

public class CatActivity extends Activity {

	private static final String TAG = "CatActivity";

	private CatView catView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
        catView = (CatView) findViewById(R.id.cat_view);
   	}

	@Override
	protected void onResume() {
		super.onResume();
    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this,
        new BaseLoaderCallback(this) {
          @Override
          public void onManagerConnected(int status) {
            switch (status) {
              case SUCCESS:
                {
                  Log.i(TAG, "OpenCV loaded successfully");
                  catView.setOpenCVEnabled(true);
                }
                break;
              default:
                {
                  super.onManagerConnected(status);
                }
                break;
            }
          }
        });
    CyclicTimerProvider.startLogging();
	}

	@Override
	protected void onPause() {
		super.onPause();
		CyclicTimerProvider.stopLogging();
	}
}
