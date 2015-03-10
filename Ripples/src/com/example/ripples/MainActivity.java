package com.example.ripples;

import android.support.v7.app.ActionBarActivity;

import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	
	// Define Buttons
	private Button arduino;
	private Button android;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Find Buttons from layout
		arduino = (Button) findViewById(R.id.arduinoButton);
		android = (Button) findViewById(R.id.androidButton);
		
		// Set OnClickListener for buttons
		arduino.setOnClickListener(this);
		android.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	@Override
	public void onClick(View v) {
		if (v == arduino) {
			//start arduino data collection activity
			Intent intent = new Intent(getApplicationContext(), com.example.ripples.Arduino.class); 
	    	startActivity(intent);
	    	}
		
		else if (v == android) {
			//start android data collection activity
			Intent intent = new Intent(getApplicationContext(), com.example.ripples.Android.class); 
	    	startActivity(intent);
	    	}
	}

}
