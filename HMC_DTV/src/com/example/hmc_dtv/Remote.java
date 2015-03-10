package com.example.hmc_dtv;

import org.apache.http.client.*;

import org.apache.http.client.methods.*;
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
import android.widget.ImageButton;

// for bluetooth
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import com.example.hmc_dtv.R;
import com.example.hmc_dtv.Remote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.media.AudioManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;;

public class Remote extends Activity implements OnClickListener {
	// Initialize buttons
	private Button btnVolUp, btnVolDown, btnPower, btnChanUp, btnChanDown, btnRecord, btnExit, btnSelect, btnPrevious, btnMute, btnMenu;
	private Button btnInfo, btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnEnter, btnDash, homeButton, btnGuide, btnList;
	private ImageButton btnReplay, btnRewind, btnPlayPause, btnAdvance, btnFastForward, btnUp, btnLeft, btnRight, btnDown;
	
	// Initialize strings
	private String stb, volumeUp, volumeDown, power, channelUp, channelDown, record, up, exit, left, select, right;
	private String previous, down, info, zero, one, two, three, four, five, six, seven, eight, nine, dash, enter;
	private String replay, rewind, play_pause, advance, fast_forward, guide, menu, list;
	
	//////////////////////////////////////////////////////////////////////////////////////
	//																					//
	//			ADDED from Ivans														//
	//																					//
	//////////////////////////////////////////////////////////////////////////////////////
	
	private static final String TAG = "bluetooth2";
	private MusicIntentReceiver myReceiver;
	public boolean headphones_flag = true;
	public boolean checkMute = false;
	Button on_off,button1, launch_sw;
	Button upvol, downvol, mute;
	Handler h;
	
	final int RECIEVE_MESSAGE = 1;        // Status  for Handler
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;
	private StringBuilder sb = new StringBuilder();
	
	private ConnectedThread mConnectedThread;
	
	// SPP UUID service
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	// MAC-address of Bluetooth module (you must edit this line)
	private static String address = "00:06:66:04:B0:EA";
	
	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.remote);
		
		homeButton = (Button) findViewById(R.id.home_remote);
        btnVolUp = (Button) findViewById(R.id.volumeUp_button);
        btnVolDown = (Button) findViewById(R.id.volumeDown_button);
        btnMute = (Button) findViewById(R.id.mute);
        btnPower = (Button) findViewById(R.id.power_button);
        btnChanUp = (Button) findViewById(R.id.channelUp_button);
        btnChanDown = (Button) findViewById(R.id.channelDown_button);
        btnRecord = (Button) findViewById(R.id.record_button);
        btnUp = (ImageButton) findViewById(R.id.up_button);
        btnExit = (Button) findViewById(R.id.exit_button);
        btnLeft = (ImageButton) findViewById(R.id.left_button);
        btnSelect = (Button) findViewById(R.id.select_button);
        btnRight = (ImageButton) findViewById(R.id.right_button);
        btnPrevious = (Button) findViewById(R.id.last_button);
        btnDown = (ImageButton) findViewById(R.id.down_button);
        btnInfo = (Button) findViewById(R.id.info_button);
        btn0 = (Button) findViewById(R.id.num0_button);
        btn1 = (Button) findViewById(R.id.num1_button);
        btn2 = (Button) findViewById(R.id.num2_button);
        btn3 = (Button) findViewById(R.id.num3_button);
        btn4 = (Button) findViewById(R.id.num4_button);
        btn5 = (Button) findViewById(R.id.num5_button);
        btn6 = (Button) findViewById(R.id.num6_button);
        btn7 = (Button) findViewById(R.id.num7_button);
        btn8 = (Button) findViewById(R.id.num8_button);
        btn9 = (Button) findViewById(R.id.num9_button);
        btnDash = (Button) findViewById(R.id.dash_button);
        btnEnter = (Button) findViewById(R.id.enter_button);
        btnReplay = (ImageButton) findViewById(R.id.replay_button);
        btnRewind = (ImageButton) findViewById(R.id.rewind_button);
        btnPlayPause = (ImageButton) findViewById(R.id.play_pause_button);
        btnAdvance = (ImageButton) findViewById(R.id.advance_button);
        btnFastForward = (ImageButton) findViewById(R.id.fast_forward_button);
        btnGuide = (Button) findViewById(R.id.guide_button);
        btnMenu = (Button) findViewById(R.id.menu_button);
        btnList = (Button) findViewById(R.id.list_button);
		
		homeButton.setOnClickListener(this);
		btnVolUp.setOnClickListener(this);
		btnVolDown.setOnClickListener(this);
		btnMute.setOnClickListener(this);
		btnPower.setOnClickListener(this);
		btnChanUp.setOnClickListener(this);
		btnChanDown.setOnClickListener(this);
		btnRecord.setOnClickListener(this);
		btnUp.setOnClickListener(this);
		btnExit.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		btnSelect.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);
		btnDown.setOnClickListener(this);
		btnInfo.setOnClickListener(this);
		btn0.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn9.setOnClickListener(this);
		btnDash.setOnClickListener(this);
		btnEnter.setOnClickListener(this);
		btnReplay.setOnClickListener(this);
		btnRewind.setOnClickListener(this);
		btnPlayPause.setOnClickListener(this);
		btnAdvance.setOnClickListener(this);
		btnFastForward.setOnClickListener(this);
		btnGuide.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		btnList.setOnClickListener(this);
		
        stb = "http://192.168.1.104:8080/";
		volumeUp = "remote/processKey?key=power";
		volumeDown = "remote/processKey?key=power";
		power = "remote/processKey?key=power";
		channelUp = "remote/processKey?key=chanup";
		channelDown = "remote/processKey?key=chandown";
		record = "remote/processKey?key=record";
		up = "remote/processKey?key=up";
		exit = "remote/processKey?key=exit";
		left = "remote/processKey?key=left";
		select = "remote/processKey?key=select";
		right = "remote/processKey?key=right";
		previous = "remote/processKey?key=prev";
		down = "remote/processKey?key=down";
		info = "remote/processKey?key=info";
		zero = "remote/processKey?key=0";
		one = "remote/processKey?key=1";
		two = "remote/processKey?key=2";
		three = "remote/processKey?key=3";
		four = "remote/processKey?key=4";
		five = "remote/processKey?key=5";
		six = "remote/processKey?key=6";
		seven = "remote/processKey?key=7";
		eight = "remote/processKey?key=8";
		nine = "remote/processKey?key=9";
		dash = "remote/processKey?key=dash";
		enter = "remote/processKey?key=enter";
		replay = "remote/processKey?key=replay";
		rewind = "remote/processKey?key=rew";
		play_pause = "remote/processKey?key=pause";
		advance = "remote/processKey?key=advance";
		fast_forward = "remote/processKey?key=ffwd";
		guide = "remote/processKey?key=guide";
		menu = "remote/processKey?key=menu";
		list = "remote/processKey?key=list";
		
		//////////////////////////////////////////////////////////////////////////////////////
		//																					//
		//			ADDED from Ivans														//
		//																					//
		//////////////////////////////////////////////////////////////////////////////////////
		
		//myReceiver = new MusicIntentReceiver();
		//AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		
		myReceiver = new MusicIntentReceiver();
		launch_sw = (Button) findViewById(R.id.launch_soundwire);
		
		handleBluetooth();
		
		launch_sw.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View view){ 
		Intent launchintent = getPackageManager().getLaunchIntentForPackage("com.georgie.SoundWireFree");
		startActivity(launchintent);
		
		}
		});
		//////////////////////////////////////////////////////////////////////////////////////
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.remote, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		if(v == homeButton) {
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.MainActivity.class); 
	    	startActivity(intent);
		}
		else if(v == btnVolUp) {
			mConnectedThread.write("1");
		}
		else if(v == btnVolDown) {
			mConnectedThread.write("2");
		}
		else if(v == btnMute) {
			mConnectedThread.write("3");
		}
		else if(v == btnGuide) {
			new HTTPGETRequestToo().execute(stb+guide);
		}
		else if(v == btnMenu) {
			new HTTPGETRequestToo().execute(stb+menu);
		}
		else if(v == btnList) {
			new HTTPGETRequestToo().execute(stb+list);
		}
		else if(v == btnPower) {
			new HTTPGETRequestToo().execute(stb+power);
		}
		else if(v == btnChanUp) {
			new HTTPGETRequestToo().execute(stb+channelUp);
		}
		else if(v == btnChanDown) {
			new HTTPGETRequestToo().execute(stb+channelDown);
		}
		else if(v == btnRecord) {
			new HTTPGETRequestToo().execute(stb+record);
		}
		else if(v == btnUp) {
			new HTTPGETRequestToo().execute(stb+up);
		}
		else if(v == btnExit) {
			new HTTPGETRequestToo().execute(stb+exit);
		}
		else if(v == btnLeft) {
			new HTTPGETRequestToo().execute(stb+left);
		}
		else if(v == btnSelect) {
			new HTTPGETRequestToo().execute(stb+select);
		}
		else if(v == btnRight) {
			new HTTPGETRequestToo().execute(stb+right);
		}
		else if(v == btnPrevious) {
			new HTTPGETRequestToo().execute(stb+previous);
		}
		else if(v == btnDown) {
			new HTTPGETRequestToo().execute(stb+down);
		}
		else if(v == btnInfo) {
			new HTTPGETRequestToo().execute(stb+info);
		}
		else if(v == btn0) {
			new HTTPGETRequestToo().execute(stb+zero);
		}
		else if(v == btn1) {
			new HTTPGETRequestToo().execute(stb+one);
		}
		else if(v == btn2) {
			new HTTPGETRequestToo().execute(stb+two);
		}
		else if(v == btn3) {
			new HTTPGETRequestToo().execute(stb+three);
		}
		else if(v == btn4) {
			new HTTPGETRequestToo().execute(stb+four);
		}
		else if(v == btn5) {
			new HTTPGETRequestToo().execute(stb+five);
		}
		else if(v == btn6) {
			new HTTPGETRequestToo().execute(stb+six);
		}
		else if(v == btn7) {
			new HTTPGETRequestToo().execute(stb+seven);
		}
		else if(v == btn8) {
			new HTTPGETRequestToo().execute(stb+eight);
		}
		else if(v == btn9) {
			new HTTPGETRequestToo().execute(stb+nine);
		}
		else if(v == btnDash) {
			new HTTPGETRequestToo().execute(stb+dash);
		}
		else if(v == btnEnter) {
			new HTTPGETRequestToo().execute(stb+enter);
		}
		else if(v == btnReplay) {
			new HTTPGETRequestToo().execute(stb+replay);
		}
		else if(v == btnRewind) {
			new HTTPGETRequestToo().execute(stb+rewind);
		}
		else if(v == btnPlayPause) {
			new HTTPGETRequestToo().execute(stb+play_pause);
		}
		else if(v == btnAdvance) {
			new HTTPGETRequestToo().execute(stb+advance);
		}
		else if(v == btnFastForward) {
			new HTTPGETRequestToo().execute(stb+fast_forward);
		}
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
	   			JSONObject obj = new JSONObject(responseBody);
	   			result = obj.getString("callsign");
	   		 }
	   		 catch (Exception e) {
	   	    	
	   		 }
	   		 
	   		 return result;
		}
    }
    
	//////////////////////////////////////////////////////////////////////////////////////
	//																					//
	//			ADDED from Ivans														//
	//																					//
	//////////////////////////////////////////////////////////////////////////////////////
	private void handleBluetooth() {
		h = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case RECIEVE_MESSAGE:                                                   // if receive massage
					byte[] readBuf = (byte[]) msg.obj;
					String strIncom = new String(readBuf, 0, msg.arg1);                 // create string from bytes array
					sb.append(strIncom);                                                // append string
					int endOfLineIndex = sb.indexOf("\r\n"); 
					endOfLineIndex = 0;
					//findingK = 0;
					//Log.d("blblbllb", "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
					break;
				}
	
			};
		};
	
		btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
		checkBTState();
	}
	
	private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
		if(Build.VERSION.SDK_INT >= 10){
			try {
				final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
				return (BluetoothSocket) m.invoke(device, MY_UUID);
			} 
			catch (Exception e) {
				Log.e(TAG, "Could not create Insecure RFComm Connection",e);
			}
		}
		return  device.createRfcommSocketToServiceRecord(MY_UUID);
		}
	
	private class MusicIntentReceiver extends BroadcastReceiver {
	@Override 
		public void onReceive(Context context, Intent intent) {
	
		/*if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
			int state = intent.getIntExtra("state", -1);
			switch (state) {
			case 0:
				//Toast.makeText(getBaseContext(), "Headset is unplugged", 
				//        Toast.LENGTH_SHORT).show();
				Log.d(TAG, "Headset is unplugged");
				headphones_flag = true;
				silencingphone();
				break;
			case 1:
				//Toast.makeText(getBaseContext(), "Headset is plugged in", 
				//        Toast.LENGTH_SHORT).show();
				Log.d(TAG, "Headset is plugged");
				if(headphones_flag==true){
					headphones_flag = false;
					//Intent launchintent = getPackageManager().getLaunchIntentForPackage("com.georgie.SoundWireFree");
					//startActivity(launchintent);
					unsilencingphone();
				}
				break;
			default:
				Toast.makeText(getBaseContext(), "I have no idea what the headset is doing", 
						Toast.LENGTH_SHORT).show();
				Log.d(TAG, "I have no idea what the headset state is");
				//silencingphone();
			}
		}*/
		}
	}
	
	public void unsilencingphone(){
		AudioManager audioManager2 = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		audioManager2.setStreamMute(AudioManager.STREAM_MUSIC,false);
		Toast.makeText(getBaseContext(), "Unsilenced (Headphones plugged in)!", 
				Toast.LENGTH_SHORT).show();
		mConnectedThread.write("3");
		checkMute = true;
	}
	
	public void silencingphone(){
		AudioManager audioManager2 = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		audioManager2.setStreamMute(AudioManager.STREAM_MUSIC,true);
		Toast.makeText(getBaseContext(), "Silenced (Headphones Unplugged)", 
				Toast.LENGTH_SHORT).show();
		if(checkMute){
			mConnectedThread.write("3");
		}
		else {
			// do nothing
		}
	}
	
	public void onStop() {
		super.onStop();
		try {
			mConnectedThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
		registerReceiver(myReceiver, filter);
	
		Log.d(TAG, "...onResume - try connect...");
	
		// Set up a pointer to the remote node using it's address.
		BluetoothDevice device = btAdapter.getRemoteDevice(address);
	
		// Two things are needed to make a connection:
		//   A MAC address, which we got above.
		//   A Service ID or UUID.  In this case we are using the
		//     UUID for SPP.
	
		try {
			btSocket = createBluetoothSocket(device);
		} 
		catch (IOException e) {
			errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
		}
	
		// Discovery is resource intensive.  Make sure it isn't going on
		// when you attempt to connect and pass your message.
		btAdapter.cancelDiscovery();
	
		// Establish the connection.  This will block until it connects.
		Log.d(TAG, "...Connecting...");
		try {
			btSocket.connect();
			Log.d(TAG, "....Connection ok...");
		} 
		catch (IOException e) {
			try {
				btSocket.close();
			} 
			catch (IOException e2) {
				errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
			}
		}
	
		// Create a data stream so we can talk to server.
		Log.d(TAG, "...Create Socket...");
		checkMute = false;
		mConnectedThread = new ConnectedThread(btSocket);
		mConnectedThread.start();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(myReceiver);
	
		Log.d(TAG, "...In onPause()...");
	
		try     {
			btSocket.close();
		} catch (IOException e2) {
			errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
		}
	}
	
	private void checkBTState() {
		// Check for Bluetooth support and then check to make sure it is turned on
		// Emulator doesn't support Bluetooth and will return null
		if(btAdapter==null) { 
			errorExit("Fatal Error", "Bluetooth not support");
		} 
		else {
			if (btAdapter.isEnabled()) {
				Log.d(TAG, "...Bluetooth ON...");
			} 
			else {
				//Prompt user to turn on Bluetooth
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, 2);
			}
		}
	}
	
	private void errorExit(String title, String message){
		Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
		finish();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if(resultCode == RESULT_OK){      
				String result=data.getStringExtra("result");
				//alarmCode = result;
			}
			if (resultCode == RESULT_CANCELED) {    
			}
		}
		else if(requestCode == 2) {
			//nothing
		}
	}
	
	private class ConnectedThread extends Thread {
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
	
		public ConnectedThread(BluetoothSocket socket) {
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
	
			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} 
			catch (IOException e) { }
	
			mmInStream = tmpIn;
			mmOutStream = tmpOut;
	}
	
	public void run() {
		byte[] buffer = new byte[256];  // buffer store for the stream
		int bytes; // bytes returned from read()
	
		// Keep listening to the InputStream until an exception occurs
		while (true) {
			try {
				// Read from the InputStream
				bytes = mmInStream.read(buffer);        // Get number of bytes and message in "buffer"
				h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
			} 
			catch (IOException e) {
				break;
			}
		}
	}
	
	// Call this from the main activity to send data to the remote device 
	public void write(String message) {
		Log.d(TAG, "...Data to send: " + message + "...");
		byte[] msgBuffer = message.getBytes();
		try {
			mmOutStream.write(msgBuffer);
		} 
		catch (IOException e) {
			Log.d(TAG, "...Error data send: " + e.getMessage() + "...");     
		}
	}
	}
	//////////////////////////////////////////////////////////////////////////////////////
}
