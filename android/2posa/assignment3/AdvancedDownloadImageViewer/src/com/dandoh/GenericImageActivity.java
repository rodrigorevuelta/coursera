package com.dandoh;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;

public abstract class GenericImageActivity extends Activity {
	/**
	 * Tag for retained fragment
	 */
	
	protected static final String TAG_FRAGMENT = "tagfragment";
	
	protected RetainFragment mRetainFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FragmentManager fm = getFragmentManager();
		mRetainFragment = (RetainFragment) fm.findFragmentByTag(TAG_FRAGMENT);
		
		if (mRetainFragment == null) {
			mRetainFragment = new RetainFragment();
			fm.beginTransaction().add(mRetainFragment, TAG_FRAGMENT).commit();
		}
	}
	

	protected abstract Uri doInBackGroundHook(Uri uri);
	
}