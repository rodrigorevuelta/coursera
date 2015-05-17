/**
 * class <tt>RetainFragment</tt> define a 
 */

package com.dandoh;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

public class RetainFragment extends Fragment {
	

	private GenericImageActivity mCallBack;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallBack = (GenericImageActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// retain fragment across configuration change
		setRetainInstance(true);
		new LongTask().execute(mCallBack.getIntent().getData());
	}

	private class LongTask extends AsyncTask<Uri, Void, Uri> {

		@Override
		protected Uri doInBackground(Uri... params) {
			return mCallBack.doInBackGroundHook(params[0]);
		}

		@Override
		protected void onPostExecute(Uri result) {
			setActivityResult(result);
			mCallBack.finish();
		}

		private void setActivityResult(Uri result) {
			if (result != null) {
				Intent intent = new Intent();
				intent.setData(result);
				mCallBack.setResult(Activity.RESULT_OK, intent);
			} else {
				mCallBack.setResult(Activity.RESULT_CANCELED);
			}

		}

	}

}
