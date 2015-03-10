package com.example.hmc_dtv;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsActivity extends Activity implements OnClickListener {
	
	private Button homeButton;
	private Button remoteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);
		
		homeButton = (Button) findViewById(R.id.home_settings);
		remoteButton = (Button) findViewById(R.id.remote_shortcut);
		
		homeButton.setOnClickListener(this);
		remoteButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == homeButton) {
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.MainActivity.class); 
	    	startActivity(intent);
		}
	}
}