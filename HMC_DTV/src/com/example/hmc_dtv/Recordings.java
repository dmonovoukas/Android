package com.example.hmc_dtv;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Recordings extends Activity implements OnClickListener {
	
	private Button homeButton;
	private Button recordingsButton;
	private Button remoteButton;

	private String stb, list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recordings);
		
		homeButton = (Button) findViewById(R.id.home_recordings);		
		homeButton.setOnClickListener(this);
		
		remoteButton = (Button) findViewById(R.id.remote_shortcut);
		remoteButton.setOnClickListener(this);
		
		recordingsButton = (Button) findViewById(R.id.btn_recordings);
		recordingsButton.setOnClickListener(this);
		
		stb = "http://192.168.1.104:8080";
		list = "/remote/processKey?key=list";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recordings, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == homeButton) {
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.MainActivity.class); 
	    	startActivity(intent);
		}
		else if (v == recordingsButton) {
			new HTTPGETRequestToo().execute(stb+list);	
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.Remote.class);
			startActivity(intent);
		}
		
		else if (v == remoteButton) {
			//start gesture activity
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.Remote.class); 
	    	startActivity(intent);
	    	}
	}
	
	private class HTTPGETRequestToo extends AsyncTask<String, Void, String> {
    	@Override
		protected String doInBackground(String... arg0) {
    		HttpClient client = new DefaultHttpClient();
   		 	HttpGet httpget = new HttpGet(arg0[0]);
   		 	String result = ":)";
	   		 try {
	   			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	   	    	String responseBody = client.execute(httpget, responseHandler);
	   			JSONObject obj = new JSONObject(responseBody);
	   			JSONObject status = obj.getJSONObject("status");
	   			String query = status.getString("query");
	   		 }
	   		 catch (Exception e) {
	   	    	//metTextHint.setText(e.toString());
	   		 }
	   		 
	   		 return result;
		}
    }
}
