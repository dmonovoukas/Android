package com.example.dtv_demo;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	
	private Button voiceButton;
	private Button gestureButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		voiceButton = (Button) findViewById(R.id.voice_button);
		gestureButton = (Button) findViewById(R.id.gesture_button);
		
		voiceButton.setOnClickListener(this);
		gestureButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if (v == voiceButton) {
			//start voice activity;
			Intent intent = new Intent(getApplicationContext(), com.example.dtv_demo.VoiceControl.class); 
	    	startActivity(intent);
		} else if (v == gestureButton) {
			//start gesture activity
			Intent intent = new Intent(getApplicationContext(), com.example.dtv_demo.GestureControl.class); 
	    	startActivity(intent);
	}
	}

}
