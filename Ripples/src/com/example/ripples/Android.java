package com.example.ripples;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Android extends ActionBarActivity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor compass, gyroscope, accelerometer;
	ArrayList<Float> accelDataX = new ArrayList<Float>();
	ArrayList<Float> accelDataY = new ArrayList<Float>();
	ArrayList<Float> accelDataZ = new ArrayList<Float>();
	ArrayList<Float> gyroDataX = new ArrayList<Float>();
	ArrayList<Float> gyroDataY = new ArrayList<Float>();
	ArrayList<Float> gyroDataZ = new ArrayList<Float>();
	ArrayList<Float> compDataX = new ArrayList<Float>();
	ArrayList<Float> compDataY = new ArrayList<Float>();
	ArrayList<Float> compDataZ = new ArrayList<Float>();
	private boolean toggle = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometer , SensorManager.SENSOR_DELAY_NORMAL);
		gyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, gyroscope , SensorManager.SENSOR_DELAY_NORMAL);
		compass = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(this, compass , SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.android, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void startData(View v) {
        toggle = true;
        
        System.out.println("Start collecting data!");
	}
	
	public void stopData(View v) {
        toggle = false;
        
        System.out.println("Accel x data points: " + accelDataX.size());
        System.out.println("Accel y data points: " + accelDataY.size());
        System.out.println("Accel z data points: " + accelDataZ.size());
        System.out.println("");
        System.out.println("Gyro x data points: " + gyroDataX.size());
        System.out.println("Gyro y data points: " + gyroDataY.size());
        System.out.println("Gyro z data points: " + gyroDataZ.size());
        System.out.println("");
        System.out.println("Compass x data points: " + compDataX.size());
        System.out.println("Compass y data points: " + compDataY.size());
        System.out.println("Compass z data points: " + compDataZ.size());
        System.out.println("");
	}
	
	public void emailData(View v) {
		String[] data = new String[9];
		
		data[0] = accelDataX.get(0).toString();
		data[1] = accelDataY.get(0).toString();
		data[2] = accelDataZ.get(0).toString();
		data[3] = gyroDataX.get(0).toString();
		data[4] = gyroDataY.get(0).toString();
		data[5] = gyroDataZ.get(0).toString();
		data[6] = compDataX.get(0).toString();
		data[7] = compDataY.get(0).toString();
		data[8] = compDataZ.get(0).toString();
		
		for(int i=1; i<accelDataX.size(); i++) {
			data[0] += ", " + accelDataX.get(i).toString();
			data[1] += ", " + accelDataY.get(i).toString();
			data[2] += ", " + accelDataZ.get(i).toString();
		}
		
		for(int i=1; i<gyroDataX.size(); i++) {
			data[3] += ", " + gyroDataX.get(i).toString();
			data[4] += ", " + gyroDataY.get(i).toString();
			data[5] += ", " + gyroDataZ.get(i).toString();
		}
		
		for(int i=1; i<compDataX.size(); i++) {
			data[6] += ", " + compDataX.get(i).toString();
			data[7] += ", " + compDataY.get(i).toString();
			data[8] += ", " + compDataZ.get(i).toString();
		}
		
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, data[0] + ", " + data[1] + ", " + data[2]
									  + ", " + data[3] + ", " + data[4] + ", " + data[5]
									  + ", " + data[6] + ", " + data[7] + ", " + data[8]
									  + ", " + accelDataX.size() + ", " + gyroDataX.size() + ", " + compDataX.size());
		sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"dmonovoukas@gmail.com"});
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Physical Sensor Data");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(toggle == true) {
	        Sensor sensor = event.sensor;
	        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				accelDataX.add(event.values[0]);
				accelDataY.add(event.values[1]);
				accelDataZ.add(event.values[2]);
		        /*System.out.println("Accel x data points: " + accelDataX.get(accelDataX.size()-1));
		        System.out.println("Accel y data points: " + accelDataY.get(accelDataY.size()-1));
		        System.out.println("Accel z data points: " + accelDataZ.get(accelDataZ.size()-1));*/
	        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
				gyroDataX.add(event.values[0]);
				gyroDataY.add(event.values[1]);
				gyroDataZ.add(event.values[2]);
		        /*System.out.println("Gyro x data points: " + gyroDataX.get(gyroDataX.size()-1));
		        System.out.println("Gyro y data points: " + gyroDataY.get(gyroDataY.size()-1));
		        System.out.println("Gyro z data points: " + gyroDataZ.get(gyroDataZ.size()-1));*/
	        } else if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
				compDataX.add(event.values[0]);
				compDataY.add(event.values[1]);
				compDataZ.add(event.values[2]);
	        	/*System.out.println("Compass x data points: " + compDataX.get(compDataX.size()-1));
	        	System.out.println("Compass y data points: " + compDataY.get(compDataY.size()-1));
	        	System.out.println("Compass z data points: " + compDataZ.get(compDataZ.size()-1));*/
	        }
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {	
	}
	
	protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}

// SEND EMAIL
//
/*String data = "gyro";
List<String> files= new ArrayList<String>();
files.add("storetext.txt");

Intent sendIntent = new Intent();
sendIntent.setAction(Intent.ACTION_SEND);
sendIntent.putExtra(Intent.EXTRA_TEXT, data);
sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"dmonovoukas@gmail.com"});
sendIntent.putExtra(Intent.EXTRA_SUBJECT, "IMU Data");
sendIntent.setType("text/plain");
sendIntent.setType(ACCESSIBILITY_SERVICE);
startActivity(sendIntent);

//has to be an ArrayList
ArrayList<Uri> uris = new ArrayList<Uri>();
//convert from paths to Android friendly Parcelable Uri's
for (String file : files)
{
    File fileIn = new File(file);
    Uri u = Uri.fromFile(fileIn);
    uris.add(u);
}
sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
//context.startActivity(Intent.createChooser(sendIntent, "Send mail..."));*/
