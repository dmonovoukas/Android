package com.example.practice;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SendFavorites extends Activity implements OnClickListener {
	
	private Button button1;
	public String clickedButton1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_favorites);
		
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);
		clickedButton1 = "false";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_favorites, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == button1) {
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			i.putExtra(clickedButton1, "true");
			startActivity(i);
		}
	}

}
