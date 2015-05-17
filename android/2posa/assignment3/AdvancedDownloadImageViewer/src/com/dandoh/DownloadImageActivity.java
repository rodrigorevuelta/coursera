package com.dandoh;

import android.net.Uri;
import android.os.Bundle;


public class DownloadImageActivity extends GenericImageActivity {
	
	/**
	 * Variable for action filtering
	 */
	public static final String ACTION_DOWNLOAD = "com.dandoh.action.DOWNLOAD";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	protected Uri doInBackGroundHook(Uri uri) {
		return Utils.downloadImage(getApplicationContext(), uri);
	}
	
}