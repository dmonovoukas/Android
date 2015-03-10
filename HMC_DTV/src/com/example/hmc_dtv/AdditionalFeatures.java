package com.example.hmc_dtv;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AdditionalFeatures extends Activity implements OnClickListener {
	
	private Button homeButton;
	private Button voiceButton;
	private Button gestureButton;
	private Button audioButton;
	private Button remoteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.additional_features);
		
		homeButton = (Button) findViewById(R.id.home_features);
		voiceButton = (Button) findViewById(R.id.voice_button);
		gestureButton = (Button) findViewById(R.id.gesture_button);
		audioButton = (Button) findViewById(R.id.audio_button);
		remoteButton = (Button) findViewById(R.id.remote_shortcut);
		
		homeButton.setOnClickListener(this);
		voiceButton.setOnClickListener(this);
		gestureButton.setOnClickListener(this);
		audioButton.setOnClickListener(this);
		remoteButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.additional_features, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == homeButton) {
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.MainActivity.class); 
	    	startActivity(intent);
		}
		else if(v == voiceButton) {
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.VoiceControl.class); 
	    	startActivity(intent);
		}
		else if(v == gestureButton) {
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.GestureControl.class); 
	    	startActivity(intent);
		}
		else if(v == audioButton) {
			//Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.ircombination");
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.AudioStreaming.class); 
	    	startActivity(intent);
		}
		else if (v == remoteButton) {
			//start gesture activity
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.Remote.class); 
	    	startActivity(intent);
	    	}
	}
}
