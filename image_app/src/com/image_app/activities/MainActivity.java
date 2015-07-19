package com.image_app.activities;

import com.example.image_app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ListView menuListView = (ListView) findViewById(R.id.menu);
		menuListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch ((int)id) {
				 case 0:
				 {
					 Intent intent = new Intent(
							 MainActivity.this, 
							 DominantColorsActivity.class);
					 intent.putExtra("message", "hello!");
					 startActivity(intent);
					 break;
				 }
				 case 1:
				 {
					 Intent intent = new Intent(
							 MainActivity.this,
							 ColorDetectorActivity.class);
					 startActivity(intent);
					 break;
				 } 
				}
			}
		});
	}
}
