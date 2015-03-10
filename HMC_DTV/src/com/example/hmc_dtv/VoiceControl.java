package com.example.hmc_dtv;

// from voice recognition
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.*;

import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.example.hmc_dtv.R;
import com.example.hmc_dtv.VoiceControl;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.SearchManager;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
////////////////////////////////

import android.os.Bundle;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VoiceControl extends Activity implements OnClickListener {
	//from voice recognition
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
	private TextView metTextHint;
	private ListView mlvTextMatches;
	private Spinner msTextMatches;
	private Button btnSpeak;
	//////////////////////
	
	private Button homeButton;
	private Button remoteButton;
	
	private String stb;
	private String power;
	private String channelUp;
	private String channelDown;
	private String record;
	private String up;
	private String exit;
	private String left;
	private String select;
	private String right;
	private String previous;
	private String down;
	private String info;
	private String zero;
	private String one;
	private String two;
	private String three;
	private String four;
	private String five;
	private String six;
	private String seven;
	private String eight;
	private String nine;
	private String dash;
	private String enter;
	private String replay;
	private String rewind;
	private String play_pause;
	private String advance;
	private String fast_forward;
	private String tune;
	private String getTuned;
	private String menu;
	private String guide;
	private String list;

	//Map that connects channel names to channel numbers
	Map<String, Integer> map = new HashMap<String, Integer>();
	
	private boolean scanning;
	private Handler mHandler = new Handler();
	private static final int INTERVAL = 1000 * 5; //5 seconds
	
	//private SpeechRecognizer sr;
	
	private Messenger mServiceMessenger = null;
	private Messenger mClientMessenger = null;
	private boolean mBound = false;
	
	static final int MSG_RECOGNIZER_CANCEL = 2;
	static final int MSG_REGISTER = 3;
	static final int MSG_RESPONSE = 4;
	static final int MSG_PHRASE = 5;
	private IncomingHandler handler;

	private HandlerThread handlerThread;
	
	private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mServiceMessenger = new Messenger(service);
            Message msg = Message.obtain(null, MSG_REGISTER, 0, 0);
			msg.replyTo = mClientMessenger;
			
			try {
				mServiceMessenger.send(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mServiceMessenger = null;
            mBound = false;
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.voice_control);
		
		homeButton = (Button) findViewById(R.id.home);
		remoteButton = (Button) findViewById(R.id.remote_shortcut);
		
		homeButton.setOnClickListener(this);
		remoteButton.setOnClickListener(this);
		
		stb = "http://192.168.1.104:8080";
		power = "/remote/processKey?key=power";
		channelUp = "/remote/processKey?key=chanup";
		channelDown = "/remote/processKey?key=chandown";
		record = "/remote/processKey?key=record";
		up = "/remote/processKey?key=up";
		exit = "/remote/processKey?key=exit";
		left = "/remote/processKey?key=left";
		select = "/remote/processKey?key=select";
		right = "/remote/processKey?key=right";
		previous = "/remote/processKey?key=prev";
		down = "/remote/processKey?key=down";
		info = "/remote/processKey?key=info";
		zero = "/remote/processKey?key=0";
		one = "/remote/processKey?key=1";
		two = "/remote/processKey?key=2";
		three = "/remote/processKey?key=3";
		four = "/remote/processKey?key=4";
		five = "/remote/processKey?key=5";
		six = "/remote/processKey?key=6";
		seven = "/remote/processKey?key=7";
		eight = "/remote/processKey?key=8";
		nine = "/remote/processKey?key=9";
		dash = "/remote/processKey?key=dash";
		enter = "/remote/processKey?key=enter";
		replay = "/remote/processKey?key=replay";
		rewind = "/remote/processKey?key=rew";
		play_pause = "/remote/processKey?key=pause";
		advance = "/remote/processKey?key=advance";
		fast_forward = "/remote/processKey?key=ffwd";
        tune = "/tv/tune?major=";
        getTuned = "/tv/getTuned?";
        menu = "/remote/processKey?key=menu";
        guide = "/remote/processKey?key=guide";
        list = "/remote/processKey?key=list";
        
//from voice recognition
		metTextHint = (TextView) findViewById(R.id.tvText);
		//mlvTextMatches = (ListView) findViewById(R.id.lvTextMatches);
		//msTextMatches = (Spinner) findViewById(R.id.sNoOfMatches);
		btnSpeak = (Button) findViewById(R.id.voice_button);
		
        map.put("cbs", 2);
        map.put("nbc", 4);
        map.put("abc", 7);
        map.put("fox", 11);
        map.put("uni", 34);
        map.put("pbs", 50);
        map.put("cnbc", 355);
        map.put("cnn", 202);
        map.put("espn", 206);
        map.put("cartoon", 296);
        
        scanning = false;
        //sr = SpeechRecognizer.createSpeechRecognizer(this);       
        //sr.setRecognitionListener(new listener());   
        
        btnSpeak.setOnClickListener(this);
        
        handlerThread = new HandlerThread("HandlerThread");
		handlerThread.start();
		handler = new IncomingHandler(handlerThread);
		mClientMessenger = new Messenger(handler);
		
		//metTextHint.setText("About to start service");
		startService(new Intent(this, MyService.class));	
		 bindService(new Intent(this, MyService.class), mConnection, Context.BIND_AUTO_CREATE);
////////////////////////////////////////////////////////////////////////////////////////
	}
///////////////////////////////////////////////////////////////////////////////////////////
	
	public void onDestroy() {
		super.onDestroy();
		stopService(new Intent(this, MyService.class));
		unbindService(mConnection);
	}
	
	
	
    class IncomingHandler extends Handler {

		public IncomingHandler(HandlerThread thr) {
			super(thr.getLooper());
		}

		@Override
		public void handleMessage( Message msg) {
			switch (msg.what) {
			case MSG_RESPONSE:
				   runOnUiThread(new Runnable() {
				        @Override
				        public void run() {

							//wurd.setText("Messenger Setup Complete!");
				        	//metTextHint.setText("Messenger Setup Complete");
				        	metTextHint.setText("Ready to listen!");
				       }
				   });
				   break;
			case MSG_PHRASE:
				final String phrase = (String)msg.obj;
				voiceControl(phrase);
				runOnUiThread(new Runnable() {
			        @Override
			        public void run() {
			        	
						//wurd.setText("Received: " + phrase);

			       }
			   });
			   break;
			default:
				super.handleMessage(msg);
			}
		}
	}
	
	
	/*class listener implements RecognitionListener          
    {
             public void onReadyForSpeech(Bundle params)
             {
                      //Log.d(TAG, "onReadyForSpeech");
             }
             public void onBeginningOfSpeech()
             {
                      //Log.d(TAG, "onBeginningOfSpeech");
             }
             public void onRmsChanged(float rmsdB)
             {
                      //Log.d(TAG, "onRmsChanged");
             }
             public void onBufferReceived(byte[] buffer)
             {
                      //Log.d(TAG, "onBufferReceived");
             }
             public void onEndOfSpeech()
             {
                      //Log.d(TAG, "onEndofSpeech");
             }
             public void onError(int error)
             {
                      //Log.d(TAG,  "error " +  error);
                      metTextHint.setText("error " + error);
             }
             public void onResults(Bundle results)                   
             {
                      String str = new String();
                      //Log.d(TAG, "onResults " + results);
                      ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                      for (int i = 0; i < data.size(); i++)
                      {
                                //Log.d(TAG, "result " + data.get(i));
                                str += data.get(i);
                      }
                      voiceControl(data.get(0).toString());
                      //mText.setText("First: " + str + " # of results: "+String.valueOf(data.size()));        
             }
             
             public void onPartialResults(Bundle partialResults)
             {
                     // Log.d(TAG, "onPartialResults");
             }
             public void onEvent(int eventType, Bundle params)
             {
                      //Log.d(TAG, "onEvent " + eventType);
             }
    }
/*	public void speak(View view) {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		
		// Specify the calling package to identify your application
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
				.getPackage().getName());

		// Display an hint to the user about what he should say.
		//intent.putExtra(RecognizerIntent.EXTRA_PROMPT, metTextHint.getText()
			//	.toString());

		// Given an hint to the recognizer about what the user is going to say
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

		// If number of Matches is not selected then return show toast message
		if (msTextMatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
			Toast.makeText(this, "Please select No. of Matches from spinner",
					Toast.LENGTH_SHORT).show();
			return;
		}

		int noOfMatches = Integer.parseInt(msTextMatches.getSelectedItem()
				.toString());
		// Specify how many results you want to receive. The results will be
		// sorted where the first result is the one with higher confidence.

		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);

		intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 500);
		
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

			//If Voice recognition is successful then it returns RESULT_OK
			if(resultCode == RESULT_OK) {

				ArrayList<String> textMatchList = data
				.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				
				String[] command = new String[textMatchList.size()];
				command = textMatchList.toArray(command);
				voiceControl(command[0]);

				if (!textMatchList.isEmpty()) {
					// If first Match contains the 'search' word
					// Then start web search.
					if (textMatchList.get(0).contains("search")) {

						String searchQuery = textMatchList.get(0).replace("search",
						" ");
						Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
						search.putExtra(SearchManager.QUERY, searchQuery);
						startActivity(search);
					} else {
						// populate the Matches
						mlvTextMatches
						.setAdapter(new ArrayAdapter<String>(this,
								android.R.layout.simple_list_item_1,
								textMatchList));
					}

				}
			//Result code for various error.	
			}else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
				showToastMessage("Audio Error");
			}else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
				showToastMessage("Client Error");
			}else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
				showToastMessage("Network Error");
			}else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
				showToastMessage("No Match");
			}else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
				showToastMessage("Server Error");
			}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	*/
	Runnable mHandlerTask = new Runnable()
	{
	     @Override 
	     public void run() {
	    	 new HTTPGETRequestToo().execute(stb+channelUp);
	    	 runOnUiThread(new Runnable() {
	 	        @Override
	 	        public void run() {
	 	    		metTextHint.setText("Scanning");
	 	       }
	 	   });
	    	 mHandler.postDelayed(mHandlerTask, INTERVAL);
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
	
	private void voiceControl(String command) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	    		metTextHint.setText("Interpreting command...");
	       }
	   });

		boolean channelNumber = false;
		int extractedInt = 0;
		try {
    		String removeChars = command.replaceAll("[a-zA-Z]","").trim();
    		extractedInt = Integer.parseInt(removeChars);
    		channelNumber = true;
    		//metTextHint.setText(removeChars);
    	}
    	catch(Exception e)
    	{
    		//metTextHint.setText(e.toString());
    	}
		if (command.contains("scan")) {
			scanning = !scanning;
			if(scanning) {
				startRepeatingTask();
			}
			else {
				stopRepeatingTask();
				runOnUiThread(new Runnable() {
			        @Override
			        public void run() {
						metTextHint.setText("Scanning stopped");
			       }
			   });

			}
		}
		else if(command.contains("previous") || command.contains("last")) {
			new HTTPGETRequestToo().execute(stb+previous);
			//metTextHint.setText("Last");
		}
		else if (command.contains("channel")) {
			if (command.contains("up") || command.contains("next")) {
				new HTTPGETRequestToo().execute(stb+channelUp);
			}
			else if (command.contains("down"))
			{
				new HTTPGETRequestToo().execute(stb+channelDown);
			}
			else if (channelNumber)
			{
				final int chan = extractedInt;
				runOnUiThread(new Runnable() {
			        @Override
			        public void run() {
						metTextHint.setText("Tuning to channel : " + chan);
			       }
			   });
				new HTTPGETRequestToo().execute(stb+tune+Integer.toString(extractedInt));
			}
		}
		else if (command.contains("what") && command.contains(" on ")) {
			int location = command.indexOf(" on ");
			if (location >0)
			{
				//metTextHint.setText(Integer.toString(location));
    			String channelString = command.toLowerCase().substring(location+4);
        		//metTextHint.setText(channelString);
        		if( map.keySet().contains(channelString))
        		{
        			new HTTPGETRequestToo().execute(stb+tune+Integer.toString(map.get(channelString)));
        		}
			}
		}
		else if (command.contains("what") && command.contains("watch")) {
			new HTTPGETRequestToo().execute(stb+getTuned);
		}
		else if(command.contains("record")) {
			new HTTPGETRequestToo().execute(stb+record);
		}
		else if(command.contains("up")) {
			new HTTPGETRequestToo().execute(stb+up);
		}
		else if(command.contains("exit") || command.contains("back")) {
			new HTTPGETRequestToo().execute(stb+exit);
		}
		else if(command.contains("left")) {
			new HTTPGETRequestToo().execute(stb+left);
		}
		else if(command.contains("select")) {
			new HTTPGETRequestToo().execute(stb+select);
		}
		else if(command.contains("right")) {
			new HTTPGETRequestToo().execute(stb+right);
		}
		else if(command.contains("down")) {
			new HTTPGETRequestToo().execute(stb+down);
		}
		else if(command.contains("info")) {
			new HTTPGETRequestToo().execute(stb+info);
		}
		else if(command.contains("menu")) {
			new HTTPGETRequestToo().execute(stb+menu);
		}
		else if(command.contains("guide")) {
			new HTTPGETRequestToo().execute(stb+guide);
		}
		else if(command.contains("list")) {
			new HTTPGETRequestToo().execute(stb+list);
		}
		else if(command.equals("dash")) {
			new HTTPGETRequestToo().execute(stb+dash);
		}
		else if(command.equals("enter")) {
			new HTTPGETRequestToo().execute(stb+enter);
		}
		else if(command.equals("replay")) {
			new HTTPGETRequestToo().execute(stb+replay);
		}
		else if(command.equals("rewind")) {
			new HTTPGETRequestToo().execute(stb+rewind);
		}
		else if(command.equals("pause")) {
			new HTTPGETRequestToo().execute(stb+play_pause);
		}
		else if(command.equals("play")) {
			new HTTPGETRequestToo().execute(stb+play_pause);
		}
		else if(command.equals("advance")) {
			new HTTPGETRequestToo().execute(stb+advance);
		}
		else if(command.equals("fast forward")) {
			new HTTPGETRequestToo().execute(stb+fast_forward);
		}
		else if(command.equals("power")) {
			new HTTPGETRequestToo().execute(stb+power);
		}
		else
		{
			runOnUiThread(new Runnable() {
		        @Override
		        public void run() {
					metTextHint.setText("I didn't catch that. Could you try saying that again?");
		       }
		   });

		}

	}
	
	void showToastMessage(String message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.voice_control, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == homeButton) {
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.MainActivity.class); 
	    	startActivity(intent);
		}
		
		else if (v == remoteButton) {
			//start gesture activity
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.Remote.class); 
	    	startActivity(intent);
	    	}
		else if (v == btnSpeak) {
			//metTextHint.setText("Listening for command...");
			/*Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);        
          //  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.example.hmc_dtv");

            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5); 
            sr.startListening(intent);*/
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
	   			if (query.equals(getTuned)) {
	   				result = obj.getString("title");
	   			}
	   			else {
		   			result = query;
	   			}
	   		 }
	   		 catch (Exception e) {
	   	    	//metTextHint.setText(e.toString());
	   		 }
	   		 
	   		 return result;
		}
    	
    	protected void onPostExecute(String result)
    	{
    		//metTextHint.setText(result);
    		if (!scanning) {
    			runOnUiThread(new Runnable() {
    		        @Override
    		        public void run() {
    	    			metTextHint.setText("Command completed!");
    		       }
    		   });
    		}
    	}
    }

}
