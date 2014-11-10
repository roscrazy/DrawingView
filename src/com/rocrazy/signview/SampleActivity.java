package com.rocrazy.signview;

import java.io.File;

import com.roscrazy.signview.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;

public class SampleActivity extends Activity implements OnClickListener{
	
	
	private static final String DEFAULT_DIRECTORY = "/SignView/";
	
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sample_activity);
		findViewById(R.id.button).setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		
		if(arg0.getId() == R.id.button){
			Intent intent = new Intent(this, SignActivity.class);
			String dirPath = Environment.getExternalStorageDirectory().toString() + DEFAULT_DIRECTORY;
			File file = new File(dirPath);
			if(!file.exists())
				file.mkdir();
			
			intent.putExtra(SignActivity.KEY_DATA, dirPath);
			startActivity(intent);
		}
		
	};
}
