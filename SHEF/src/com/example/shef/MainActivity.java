package com.example.shef;

import org.apache.http.client.*;

import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView command;
	private EditText channel;
	
	private Button btnChanUp;
	private Button btnChanDown;
	private Button btnPower;
	private Button btnTune;
	
	private String stb;
	private String power;
	private String channelUp;
	private String channelDown;
	private String tune;
	private String info;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        command = (TextView) findViewById(R.id.textView1);
        channel = (EditText) findViewById(R.id.channel);
        
        btnChanUp = (Button) findViewById(R.id.channelUp);
        btnChanDown = (Button) findViewById(R.id.channelDown);
        btnPower = (Button) findViewById(R.id.power);
        btnTune = (Button) findViewById(R.id.tune);
        
        stb = "http://192.168.1.104:8080/";
        power = "remote/processKey?key=power";
        channelUp = "remote/processKey?key=active";
        channelDown = "remote/processKey?key=advance";
        tune = "tv/tune?major=";
        info = "tv/getProgInfo?major=";
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void tune(View view) {
    	command.setText("Tune");
    	//new HTTPGETRequestToo().execute(stb+tune+channel.getText());
    	new HTTPGETRequestToo().execute(stb+info+channel.getText());
    }
    
    public void power(View view) {
    	command.setText("Power");	
    	new HTTPGETRequestToo().execute(stb+power);
    }
    
    public void down(View view) {
    	command.setText("Channel Down");
    	new HTTPGETRequestToo().execute(stb+channelDown);
    }
    
    public void up(View view) {
    	command.setText("Channel Up");
    	new HTTPGETRequestToo().execute(stb+channelUp);
    }
    
    private class HTTPGETRequestToo extends AsyncTask<String, Void, String> {
    	@Override
		protected String doInBackground(String... arg0) {
    		HttpClient client = new DefaultHttpClient();
   		 	HttpGet httpget = new HttpGet(arg0[0]);
   		 	String result = "";
	   		 try {
	   			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	   	    	String responseBody = client.execute(httpget, responseHandler);
	   			//result = responseBody;
	   	    	//HttpResponse response = client.execute(httpget);
	   	    	//HttpEntity entity = response.getEntity();
	   	    	//StatusLine status = response.getStatusLine();
	   	    	//Header [] stations = response.getAllHeaders();
	   	    	//Header station = response.getFirstHeader("Content-type");
	   	    	//result = station.getValue();
	   	    	//result = stations[3].getName();
	   	    	//result = entity.getContent().toString();
	   			JSONObject obj = new JSONObject(responseBody);
	   			result = obj.getString("callsign");
	   		 }
	   		 catch (Exception e) {
	   	    	
	   		 }
	   		 
	   		 return result;
		}
    	
    	protected void onPostExecute(String result)
    	{
    		command.setText(result);
    		
    	}
    }
    
}