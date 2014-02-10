package com.rocrazy.signview;

import java.io.OutputStream;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.hanbiro.signview.R;


public class SignFragment extends SherlockFragment{
	private SignView signView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		signView = (SignView) inflater.inflate(R.layout.sign_layout_fm, null);
		
		return signView;
	}
	
	public void save(OutputStream outputStream){
		signView.save(outputStream);
	}
	


	
}
