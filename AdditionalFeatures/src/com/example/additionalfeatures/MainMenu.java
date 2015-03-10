package com.example.additionalfeatures;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener {
	
	private Button voiceButton;
	private Button gestureButton;
	private Button qrButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		voiceButton = (Button) findViewById(R.id.voice_button);
		gestureButton = (Button) findViewById(R.id.gesture_button);
		qrButton = (Button) findViewById(R.id.qr_button);
		
		voiceButton.setOnClickListener(this);
		gestureButton.setOnClickListener(this);
		qrButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if (v == voiceButton) {
			//start voice activity;
			Intent intent = new Intent(getApplicationContext(), com.example.additionalfeatures.VoiceControl.class); 
	    	startActivity(intent);
		} else if (v == gestureButton) {
			//start gesture activity
			Intent intent = new Intent(getApplicationContext(), com.example.additionalfeatures.GestureControl.class); 
	    	startActivity(intent);
		} else if (v == qrButton) {
			//start qr activity
			Intent intent = new Intent(getApplicationContext(), com.example.additionalfeatures.QRCodes.class); 
	    	startActivity(intent);
		}
		
	}

}
