package com.example.hmc_dtv;

import android.os.Bundle;

import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

// for gesture
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

// for remote
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;

public class GestureControl extends Activity implements OnClickListener, SensorEventListener {
	
	private Button homeButton;
	private Button remoteButton;
	
	// for gesture
	private SensorManager mSensorManager;
	private Sensor magnetic;
	private Sensor orientation;
	
	//for remote
	private String stb, channelUp, channelDown, advance, replay, power;
	
	// for scan
	private boolean upScanning, downScanning, checkPower;
	private Handler mHandler = new Handler();
	private static final int INTERVAL = 1000 * 5; //5 seconds

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gesture_control);
		
		homeButton = (Button) findViewById(R.id.home);
		homeButton.setOnClickListener(this);
		
		remoteButton = (Button) findViewById(R.id.remote_shortcut);
		remoteButton.setOnClickListener(this);
		
		// for gesture
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		orientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(this, orientation , SensorManager.SENSOR_DELAY_NORMAL);
        
        // for remote
        stb = "http://192.168.1.104:8080/";
		channelUp = "remote/processKey?key=chanup";
		channelDown = "remote/processKey?key=chandown";
		advance = "remote/processKey?key=advance";
		replay = "remote/processKey?key=replay";
		power = "remote/processKey?key=power";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gesture_control, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		if(v == homeButton) {
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.MainActivity.class); 
	    	startActivity(intent);
		}
		else if (v == remoteButton) {
			//start gesture activity
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.Remote.class); 
	    	startActivity(intent);
	    	}
	}
	
	// for gesture
	protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    
    @Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// can be safely ignored for this demo
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		
		/*if (x > 180 && x < 210) {
			if(!checkPower) {
				checkPower = !checkPower;
				startRepeatingTask();
			}
			else {
				// do nothing
			}
		}*/
		if (z > -90 && z < -65) {
			if(!upScanning) {
				upScanning = !upScanning;
				startRepeatingTask();
			}
			else {
				// do nothing
			}
		} 
		else if (z > 65 && z < 90) {
			if(!downScanning) {
				downScanning = !downScanning;
				startRepeatingTask();
			}
			else {
				// do nothing
			}
		}
		else if (x < 320 && x > 280) {
			new HTTPGETRequestToo().execute(stb+advance);
		}
		else if (x < 210 && x > 180) {
			new HTTPGETRequestToo().execute(stb+replay);
		}
		else {
			// do nothing
			stopRepeatingTask();
			upScanning = false;
			downScanning = false;
			checkPower = false;
		}
	}
	
	Runnable mHandlerTask = new Runnable()
	{
	     @Override 
	     public void run() {
	    	 if(upScanning){
		    	 new HTTPGETRequestToo().execute(stb+channelUp);
		    	 mHandler.postDelayed(mHandlerTask, INTERVAL);
	    	 }
	    	 else if (downScanning) {
	    		 new HTTPGETRequestToo().execute(stb+channelDown);
	    		 mHandler.postDelayed(mHandlerTask, INTERVAL);
	    	 }
	    	 else if (checkPower) {
	    		 new HTTPGETRequestToo().execute(stb+power);
	    		 mHandler.postDelayed(mHandlerTask, INTERVAL);
	    	 }
	     }
	};

	void startRepeatingTask()
	{
	    mHandlerTask.run(); 
	}

	void stopRepeatingTask()
	{
	    mHandler.removeCallbacks(mHandlerTask);
	}
	
////////////////////////////////////////////////////////	
	
	// for remote
	private class HTTPGETRequestToo extends AsyncTask<String, Void, String> {
    	@Override
		protected String doInBackground(String... arg0) {
    		HttpClient client = new DefaultHttpClient();
   		 	HttpGet httpget = new HttpGet(arg0[0]);
   		 	String result = "";
	   		 try {
	   			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	   	    	String responseBody = client.execute(httpget, responseHandler);
	   			JSONObject obj = new JSONObject(responseBody);
	   			result = obj.getString("callsign");
	   		 }
	   		 catch (Exception e) {
	   	    	
	   		 }
	   		 
	   		 return result;
		}
    }
}
