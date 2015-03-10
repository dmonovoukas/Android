package com.example.hmc_dtv;
/*
//////////////////////////////////////////////////////////////////////////////////////
// 																					//
//     ADDED from Ivans																//
//																					//
//////////////////////////////////////////////////////////////////////////////////////
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import com.example.hmc_dtv.R;
import com.example.hmc_dtv.MainActivity;

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
import android.widget.ToggleButton;

////////////////////////////////////////////////////////////////////////////////////*/

import android.media.AudioManager;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	
	private Button settingsButton;
	private Button favoritesButton;
	private Button remoteButton;
	private Button recordingsButton;
	private Button featuresButton;
	private Button voiceButton;
	private Button remoteShortcut;
/*	
//////////////////////////////////////////////////////////////////////////////////////
//																					//
//            ADDED from Ivans														//
//																					//
//////////////////////////////////////////////////////////////////////////////////////
	
	private static final String TAG = "bluetooth2";
    private MusicIntentReceiver myReceiver;
	public boolean headphones_flag = true;
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

//////////////////////////////////////////////////////////////////////////////////////*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
/*		
//////////////////////////////////////////////////////////////////////////////////////
//																					//
//			ADDED from Ivans														//
//																					//
//////////////////////////////////////////////////////////////////////////////////////
		
	    myReceiver = new MusicIntentReceiver();
	    AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
	    
	    myReceiver = new MusicIntentReceiver();
	    //on_off = (Button) findViewById(R.id.onoff);
	    //upvol = (Button) findViewById(R.id.upvolume);
	    mute = (Button) findViewById(R.id.mute);
	    //downvol = (Button) findViewById(R.id.downvolume);
	    launch_sw = (Button) findViewById(R.id.launch_soundwire);
	     
	    handleBluetooth();
	    
	    launch_sw.setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View view){ 
	            Intent launchintent = getPackageManager().getLaunchIntentForPackage("com.georgie.SoundWireFree");
	            startActivity(launchintent);
	    		
	    	}
	    });
//////////////////////////////////////////////////////////////////////////////////////*/
		
		voiceButton = (Button) findViewById(R.id.voice_main);
		favoritesButton = (Button) findViewById(R.id.favorites);
		remoteButton = (Button) findViewById(R.id.remote);
		recordingsButton = (Button) findViewById(R.id.recordings);
		featuresButton = (Button) findViewById(R.id.additional_features);
		settingsButton = (Button) findViewById(R.id.settings);
		remoteShortcut = (Button) findViewById(R.id.remote_shortcut);
		
		settingsButton.setOnClickListener(this);
		favoritesButton.setOnClickListener(this);
		remoteButton.setOnClickListener(this);
		recordingsButton.setOnClickListener(this);
		featuresButton.setOnClickListener(this);
		voiceButton.setOnClickListener(this);
		remoteShortcut.setOnClickListener(this);
		
		//AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		//audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}

	@Override
	public void onResume() {
		super.onResume();
		//AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		//audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == settingsButton) {
			//start gesture activity
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.SettingsActivity.class); 
	    	startActivity(intent);
	    	}
		
		else if (v == favoritesButton) {
			//start gesture activity
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.Favorites.class); 
	    	startActivity(intent);
	    	}
		
		else if (v == remoteButton || v == remoteShortcut) {
			//start gesture activity
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.Remote.class); 
	    	startActivity(intent);
	    	}
		
		else if (v == recordingsButton) {
			//start gesture activity
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.Recordings.class); 
	    	startActivity(intent);
	    	}
		
		else if (v == featuresButton) {
			//start gesture activity
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.AdditionalFeatures.class); 
	    	startActivity(intent);
	    	}
		
		else if (v == voiceButton) {
			//start gesture activity
			Intent intent = new Intent(getApplicationContext(), com.example.hmc_dtv.VoiceControl.class); 
	    	startActivity(intent);
	    	}
		/*// IVANS
		else if (v == mute) {
			// Mute
			mConnectedThread.write("3");
	    	}*/
	}
	
/*	
//////////////////////////////////////////////////////////////////////////////////////
//																					//
//			ADDED from Ivans														//
//																					//
//////////////////////////////////////////////////////////////////////////////////////
	private void handleBluetooth()
	   {
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
	          } catch (Exception e) {
	              Log.e(TAG, "Could not create Insecure RFComm Connection",e);
	          }
	      }
	      return  device.createRfcommSocketToServiceRecord(MY_UUID);
	  }
	  
	  private class MusicIntentReceiver extends BroadcastReceiver {
	      @Override 
	      public void onReceive(Context context, Intent intent) {
	      	
	          if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
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
	          }
	      }
	  }
	  
	  public void unsilencingphone(){
	  	AudioManager audioManager2 = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
	   	audioManager2.setStreamMute(AudioManager.STREAM_MUSIC,false);
	  	Toast.makeText(getBaseContext(), "Unsilenced (Headphones plugged in)!", 
	              Toast.LENGTH_SHORT).show();
	  	mConnectedThread.write("3");
	  }
	  public void silencingphone(){
	  	AudioManager audioManager2 = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
	   	audioManager2.setStreamMute(AudioManager.STREAM_MUSIC,true);
	  	Toast.makeText(getBaseContext(), "Silenced (Headphones Unplugged)", 
	              Toast.LENGTH_SHORT).show();
	  	mConnectedThread.write("3");
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
	    } catch (IOException e) {
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
	    } catch (IOException e) {
	      try {
	        btSocket.close();
	      } catch (IOException e2) {
	        errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
	      }
	    }
	      
	    // Create a data stream so we can talk to server.
	    Log.d(TAG, "...Create Socket...");
	    
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
	    } else {
	      if (btAdapter.isEnabled()) {
	        Log.d(TAG, "...Bluetooth ON...");
	      } else {
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
		  else if(requestCode == 2)
		  {
			  
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
	            } catch (IOException e) { }
	      
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
	                } catch (IOException e) {
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
	            } catch (IOException e) {
	                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");     
	              }
	        }
	    }
//////////////////////////////////////////////////////////////////////////////////////*/
}
