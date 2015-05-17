package com.dandoh;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


public class FilterImageActivity extends GenericImageActivity {
	/**
	 * Variable for action filtering
	 */
	public static final String ACTION_FILTER = "com.dandoh.action.FILTER";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("Filter", "start filter");
		
	}
	@Override
	protected Uri doInBackGroundHook(Uri uri) {
		return Utils.grayScaleFilter(getApplicationContext(), uri);
	}
}