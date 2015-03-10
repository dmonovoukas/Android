package com.example.ircombination;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
  
import com.example.ircombination.R;

  
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
  
public class checkKeypad extends Activity {
  private static final String TAG = "bluetooth2";
  final String ACCESS_CODE = "code?";
	final String ACTIVATION = "activate";
	final String LEDS = "led";
	final String ID_BUTTON = "id_button";
	StringBuilder codeString = new StringBuilder();
	String code;
	String led;
	int idButton;
	boolean status;
	int counterForCode = 0;
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
    
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    setContentView(R.layout.keypad_display);
    final Button button1 = (Button) findViewById(R.id.Button1);
    final Button button2 = (Button) findViewById(R.id.Button2);
    final Button button3 = (Button) findViewById(R.id.Button3);
    final Button button4 = (Button) findViewById(R.id.Button4);
    final Button button5 = (Button) findViewById(R.id.Button5);
    final Button button6 = (Button) findViewById(R.id.Button6);
    final Button button7 = (Button) findViewById(R.id.Button7);
    final Button button8 = (Button) findViewById(R.id.Button8);
    final Button button9 = (Button) findViewById(R.id.Button9);
    final Button buttonCancel = (Button) findViewById(R.id.ButtonCancel);
     
    handleBluetooth();
    
    // Tells you whehter the alarm is armed.
    Intent intent = getIntent();
    if (intent != null) {
 	   code = intent.getStringExtra(ACCESS_CODE);
 	   status = intent.getBooleanExtra(ACTIVATION, true);
 	   idButton = intent.getIntExtra(ID_BUTTON, 0);
 	   led = intent.getStringExtra(LEDS);
    }
    button1.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			compareCode('1');
		}
	    });
    button2.setOnClickListener(new OnClickListener() {	
		@Override
		public void onClick(View v) {
			compareCode('2');
			}
	    });
    button3.setOnClickListener(new OnClickListener() {
			
		@Override
		public void onClick(View v) {
			compareCode('3');
			}
	    });
    button4.setOnClickListener(new OnClickListener() {
			
		@Override
		public void onClick(View v) {
			compareCode('4');
			}
		
	    });
    button5.setOnClickListener(new OnClickListener() {
			
		@Override
		public void onClick(View v) {
			compareCode('5');
			}
	    });
    button6.setOnClickListener(new OnClickListener() {
			
		@Override
		public void onClick(View v) {
			compareCode('6');
			}
	    });
    button7.setOnClickListener(new OnClickListener() {
			
		@Override
		public void onClick(View v) {
			compareCode('7');
			}
	    });
    button8.setOnClickListener(new OnClickListener() {
			
		@Override
		public void onClick(View v) {
			compareCode('8');
			}	
	    });
    button9.setOnClickListener(new OnClickListener() {
			
		@Override
		public void onClick(View v) {
			compareCode('9');
			}   		
	    });
    buttonCancel.setOnClickListener(new OnClickListener() {
 	   @Override
   		public void onClick(View v) {
 		 Intent returnIntent = new Intent();
			 returnIntent.putExtra("result",code);
			 returnIntent.putExtra("check",true);
			returnIntent.putExtra("button",idButton);
			 setResult(RESULT_OK,returnIntent);     
			 finish();
   		}
    });
    
  }
   private void handleBluetooth()
   {
	   
	      
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
  
  
@Override
  
  public void onStop() {
	  super.onStop();
	  try {
		mConnectedThread.join();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public void onDestroy(){
	  super.onDestroy();
  }
    
  @Override
  public void onResume() {
    super.onResume();
   
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
  private void compareCode(char c)
  {
  
  	{
  		if(code.charAt(counterForCode) == c)
	    	{
	    		if(counterForCode == code.length()-1)
	    		{
	    			Intent returnIntent = new Intent();
	    			 if(idButton == 2)
	    			 {
	    				 mConnectedThread.write(code + "0" + "1" + led);
	    				 returnIntent.putExtra("check",false);
	    			 }
	    			 if(idButton == 1)
	    			 {
	    					if(!status)
	    					{
	    						 mConnectedThread.write(code + "0" + "0" + led);
	    						 returnIntent.putExtra("check",false);
	    					}
	    					else
	    					{
	    						 mConnectedThread.write(code + "0" + "1" + led);
	    						 returnIntent.putExtra("check",true);
	    					}
	    			 }
	    			 
	    			 returnIntent.putExtra("result",code);
	    			 returnIntent.putExtra("button",idButton);
	    			 
	    			 setResult(RESULT_OK,returnIntent);     
	    			 finish();
	    		}
	    		else
	    			counterForCode = counterForCode + 1;
	    	}
	    	else
	    		counterForCode = 0;
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
      
        /* Call this from the main activity to send data to the remote device */
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
}