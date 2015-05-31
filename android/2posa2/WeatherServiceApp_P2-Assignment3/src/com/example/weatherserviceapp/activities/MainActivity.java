package com.example.weatherserviceapp.activities;

import vandy.mooc.aidl.WeatherData;

import com.example.weatherserviceapp.R;
import com.example.weatherserviceapp.tasks.WeatherFetchTask;
import com.example.weatherserviceapp.utils.RetainedFragmentManager;
import com.example.weatherserviceapp.utils.Utils;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends LifecycleLoggingActivity
{
	private static final String TAG_TASK_FRAGMENT = "task_fragment";
	private RetainedFragmentManager mTaskFragment;
	
	private WeatherFetchTask mWeatherFetchTask;

	private TextView mTextViewCity;
	private TextView mTextViewDesc;
	private TextView mTextViewTemp;
	private TextView mTextViewWind;
	private TextView mTextViewHumidity;
	private TextView mTextViewSunrise;
	private TextView mTextViewSunset;
	private ImageView mImageViewWeatherIcon;
	private TextView mTextViewLastUpdatedTime;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// caching the UI components
		mTextViewCity = (TextView) findViewById(R.id.textViewCity);
		mTextViewDesc = (TextView) findViewById(R.id.textViewDesc);
		mTextViewTemp = (TextView) findViewById(R.id.textViewTemp);
		mTextViewWind = (TextView) findViewById(R.id.textViewWind);
		mTextViewHumidity = (TextView) findViewById(R.id.textViewHumidity);
		mTextViewSunrise = (TextView) findViewById(R.id.textViewSunrise);
		mTextViewSunset = (TextView) findViewById(R.id.textViewSunset);
		mImageViewWeatherIcon = (ImageView) findViewById(R.id.imageViewWeatherIcon);
		mTextViewLastUpdatedTime = (TextView) findViewById(R.id.textViewLastUpdated);
		
		handleConfigurationChanges();
	}

	/**
	 * Hook method called after onCreate() or after onRestart() (when the
	 * activity is being restarted from stopped state).
	 */
	@Override
	protected void onStart()
	{
		// Always call super class for necessary
		// initialization/implementation.
		super.onStart();

		// Initiate the service binding protocol.
		mWeatherFetchTask.bindService();
	}

	
    /**
     * Hook method called by Android when this Activity becomes
     * invisible.
     */
    @Override
    protected void onStop() {
        // Unbind from the Service.
    	mWeatherFetchTask.unbindService();

        // Always call super class for necessary operations when
        // stopping.
        super.onStop();
    }
    
	
	/**
	 * Handle hardware reconfigurations, such as rotating the display.
	 */
	protected void handleConfigurationChanges()
	{
		FragmentManager fm = getFragmentManager();
		mTaskFragment = (RetainedFragmentManager) fm
				.findFragmentByTag(TAG_TASK_FRAGMENT);
		
		// If the Fragment is non-null, then it is currently being
		// retained across a configuration change.
		if (mTaskFragment == null)
		{
			mTaskFragment = new RetainedFragmentManager();
			
			// add the tasks to the RetainedFragmentManager
			// mTaskFragment.put("dummyTask", new DummyTask());  // for testing only
			
			mWeatherFetchTask = new WeatherFetchTask();
			mTaskFragment.put("WeatherFetchTask", mWeatherFetchTask);
			
			fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT)
					.commit();
		}
		
		mWeatherFetchTask = (WeatherFetchTask) mTaskFragment.get("WeatherFetchTask");	
		
		// In the case where Android destroys background activities, 
		// the mWeatherFetchTask seems to get destroyed as well.
		// we need to recreate the task and add that to the fragment.
		// this does not happen very often but it could happen when
		// the "Don't keep activities" in Developer Settings is selected. 
		if (null == mWeatherFetchTask)
		{
			mWeatherFetchTask = new WeatherFetchTask();
			mTaskFragment.put("WeatherFetchTask", mWeatherFetchTask);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * Initiate the synchronous weather lookup when the user presses the
	 * "Async Lookup" button.
	 */
	public void fetchWeatherAsync(View v)
	{
		mWeatherFetchTask.fetchWeatherAsync(v);
	}

	
	/*
	 * Initiate the synchronous weather lookup when the user presses the
	 * "Sync Lookup" button.
	 */
	public void fetchWeatherSync(View v)
	{
		mWeatherFetchTask.fetchWeatherSync(v);
	}
	
	
	/**
	 * Show Toast on the screen to show any informational message on UI Thread
	 * 
	 * @param message
	 */
	public void showToast(final String message)
	{
		runOnUiThread(new Runnable()
		{
			
			@Override
			public void run()
			{
				Toast toastMsg = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
				toastMsg.show();
			}
		});
	}
	
	
	/**
	 * Display Weather Data on UI Thread
	 * 
	 * @param weatherData
	 */
	public void displayWeatherData(final WeatherData weatherData)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				mTextViewCity.setText(weatherData.getmName());
				mTextViewDesc.setText(weatherData.getmWeatherDesc());
				
				String humidityString = "Humidity " + Long.toString(weatherData.getmHumidity()) + "%";
				mTextViewHumidity.setText(humidityString);
				
				String sunriseString = "Sunrise " + Utils.convertLongToTime(weatherData.getmSunrise());
				mTextViewSunrise.setText(sunriseString);
				
				String sunsetString = "Sunset " + Utils.convertLongToTime(weatherData.getmSunset());
				mTextViewSunset.setText(sunsetString);
				
				String tempString = (int) weatherData.getmTemp() + " C";
				mTextViewTemp.setText(tempString);
				
				String windString = "Wind " + (int) weatherData.getmSpeed() + " km/h";
				mTextViewWind.setText(windString);
				
				String imageLocation = weatherData.getmImageDownloadedLocation();
				Uri imageUri = Uri.parse(imageLocation);
				mImageViewWeatherIcon.setImageURI(null);
				mImageViewWeatherIcon.setImageURI(imageUri);
				mImageViewWeatherIcon.invalidate();
				
				long lastUpdatedTime = weatherData.getmLastUpdated();
				String lastUpdatedTimeString = "Last Updated at " + Utils.getDateTimeFromMs(lastUpdatedTime);
				mTextViewLastUpdatedTime.setText(lastUpdatedTimeString);
				
			}
		});
	}
	

}
