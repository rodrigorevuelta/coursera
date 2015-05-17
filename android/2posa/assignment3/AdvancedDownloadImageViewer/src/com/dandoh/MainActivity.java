/**
 * class <tt>MainActivity</tt> is main activity of advance download image viewer
 */
package com.dandoh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.advanceddownloadimageviewer.R;

public class MainActivity extends Activity {



	/**
	 * Variable to separate request, use for handling activity result
	 */
	private static final int DOWNLOAD_REQUEST = 25;
	private static final int FILTER_REQUEST = 42;

	private final String TAG = getClass().getSimpleName();
	/**
	 * Interface for handling activity result
	 */
	interface ResultHandler {
		void excute(Intent data);

		void printError(Intent data);
	}

	/**
	 * SpareArray to put and get ResultHandler
	 */

	private SparseArray<ResultHandler> mHandlerArray;
	private Uri mDefaultUrl = Uri
			.parse("http://www.dre.vanderbilt.edu/~schmidt/kitten.png");
	private EditText mEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate...");
		setContentView(R.layout.activity_main);

		mEditText = (EditText) findViewById(R.id.url);
		mHandlerArray = new SparseArray<ResultHandler>();

		mHandlerArray.put(DOWNLOAD_REQUEST, new ResultHandler() {

			@Override
			public void printError(Intent data) {
				Toast.makeText(getApplicationContext(),
						"Invalid url or downloading error", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void excute(Intent data) {
				Log.d(TAG, "received " + data.getDataString());
				final Intent intent = makeFilterImageIntent(data.getData());
				startActivityForResult(intent, FILTER_REQUEST);
//				Intent intent = makeGalleryIntent(data.getDataString());
//				Log.i(TAG, "show image");
//				startActivity(intent);

			}
		});

		mHandlerArray.put(FILTER_REQUEST, new ResultHandler() {

			@Override
			public void printError(Intent data) {
				Toast.makeText(getApplicationContext(),
						"Error while filtering image", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void excute(Intent data) {
				Intent intent = makeGalleryIntent(data.getDataString());
				startActivity(intent);
			}
		});

		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadImage(v);
			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onResult");
		if (resultCode == RESULT_OK) {
			mHandlerArray.get(requestCode).excute(data);
		} else {
			mHandlerArray.get(requestCode).printError(data);
		}
	}
	
	public void downloadImage(View view) {
		try {
			hideKeyboard(this, mEditText.getWindowToken());

			Uri url = getUrl();
			if (url != null) {
				Intent downloadIntent = makeDownloadImageIntent(url);
				startActivityForResult(downloadIntent, DOWNLOAD_REQUEST);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Intent makeDownloadImageIntent(Uri uri) {
		return new Intent(DownloadImageActivity.ACTION_DOWNLOAD, uri);
	}
	
	private Intent makeFilterImageIntent(Uri uri) {
		return new Intent(FilterImageActivity.ACTION_FILTER).setDataAndType(uri, "image/*");
	}
	
	private Intent makeGalleryIntent(String pathToImageFile) {
		return new Intent(Intent.ACTION_VIEW).
				setDataAndType(Uri.parse("file://" + pathToImageFile), "image/*");
	}
	

	protected Uri getUrl() {
		Uri url = null;

		// Get the text the user typed in the edit text (if anything).
		url = Uri.parse(mEditText.getText().toString());

		// If the user didn't provide a URL then use the default.
		String uri = url.toString();
		if (uri == null || uri.equals(""))
			url = mDefaultUrl;

		if (URLUtil.isValidUrl(url.toString()))
			return url;
		else {
			Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
			return null;
		}
	}
	
	/**
	 * This method is used to hide a keyboard after a user has finished typing
	 * the url.
	 */
	public void hideKeyboard(Activity activity, IBinder windowToken) {
		InputMethodManager mgr = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(windowToken, 0);
	}


}
