package com.rocrazy.signview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.roscrazy.signview.R;



public class SignActivity extends SherlockFragmentActivity {
	private static final String FRAGMENT_TAG = "tag";
	public static final String KEY_DATA = "data";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_layout);
		if(savedInstanceState == null){
			SignFragment fragment = new SignFragment();
			
			getSupportFragmentManager().beginTransaction().add(R.id.flContainer, fragment, FRAGMENT_TAG).commit();
		}
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.sign_menu, menu);
		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(R.id.menuSave == item.getItemId()){
			SignFragment fragment = (SignFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
			if(fragment != null){
				File file = new File(getIntent().getStringExtra(KEY_DATA) + "/" + System.currentTimeMillis() + ".png");
				try {
					FileOutputStream outputStream = new  FileOutputStream(file);
					fragment.save(outputStream);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			this.setResult(RESULT_OK, getIntent());
			finish();

		}
		return super.onOptionsItemSelected(item);
	}
	
	

}
