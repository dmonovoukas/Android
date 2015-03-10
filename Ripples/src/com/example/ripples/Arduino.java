package com.example.ripples;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.TextView;
import android.widget.EditText;  
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class Arduino extends ActionBarActivity {
	
    TextView myLabel;
    EditText myTextbox;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    String dataStream = "";
    String data = "";
    String sorted_data = "";
    Boolean read = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arduino);
		
		// From Tutorial
        Button openButton = (Button)findViewById(R.id.open);
        Button sendButton = (Button)findViewById(R.id.send);
        Button closeButton = (Button)findViewById(R.id.close);
        Button startButton = (Button)findViewById(R.id.start);
        Button stopButton = (Button)findViewById(R.id.stop);
        myLabel = (TextView)findViewById(R.id.label);
        myTextbox = (EditText)findViewById(R.id.entry);
	
        //Open Button
        openButton.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		try 
        		{
        			findBT();
        			openBT();
        		}
        		catch (IOException ex) { }
        	}
        });
    
        //Send Button
        sendButton.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		try 
        		{
        			sendData();
        		}
        		catch (IOException ex) { }
        	}
        });
    
        //Close button
        closeButton.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		try 
        		{
        	    	sendStop();
        			closeBT();
        		}
        		catch (IOException ex) { }
        	}
        });
        
        //Start Button
        startButton.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		try 
        		{
        			sendStart();
        		}
        		catch (IOException ex) { }
        	}
        });
        
        //Stop Button
        stopButton.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		try 
        		{
        			sendStop();
        		}
        		catch (IOException ex) { }
        	}
        });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.arduino, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            myLabel.setText("No bluetooth adapter available");
        }
        
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 1);
        }
        
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("RN42-2845")) 
                {
                    mmDevice = device;
                    break;
                }
            }
        }
        myLabel.setText("Bluetooth Device Found");
    }
    
    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);        
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        
        beginListenForData();
        
        myLabel.setText("Bluetooth Opened");
    }
    
    void beginListenForData()
    {
        //final Handler handler = new Handler(); 
        //final byte delimiter = 10; //This is the ASCII code for a newline character
        
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
        	
            /*Handler mHandler = new Handler() { 
            	@Override
            	public void handleMessage(Message msg) {
            		byte[] writeBuf = (byte[]) msg.obj;
            		int begin = (int)msg.arg1;
            		int end = (int)msg.arg2;
            		switch(msg.what) {
            		case 1:
            			String writeMessage = new String(writeBuf);
            			writeMessage = writeMessage.substring(begin, end);
            			break;
            		}
            	}
            };*/ 
            
            public void run() {
            	byte[] buffer = new byte[1024]; 
            	int begin = 0; 
            	int bytes = 0;
            	while (true) {
            		dataStream = "";
            		bytes = 0;
            		try {
            			bytes += mmInputStream.read(buffer, bytes, buffer.length - bytes);
            			for(int i = begin; i < bytes; i++) {
            				char activeData = (char)buffer[i];
            				dataStream = dataStream + activeData;
            			}
            		}
            		catch (IOException e) {
            			break;
            		}
            		
            		data += dataStream;
            		System.out.println("reading...");
            		
            	}
            } 
        });
        
        workerThread.start();
    }
    
    void sendData() throws IOException
    {
        String msg = myTextbox.getText().toString();
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        System.out.println("Android is sending: " + msg.getBytes());
        myLabel.setText("Data Sent");
    }
    
    void sendStart() throws IOException
    {
    	String msg = "1";
        mmOutputStream.write(msg.getBytes());
        System.out.println("You have started recording...");
        myLabel.setText("Start swimming!");
        read = true;
    }
    
    void sendStop() throws IOException
    {
    	String msg = "0";
        mmOutputStream.write(msg.getBytes());
        System.out.println("You have stopped recording.");
        myLabel.setText("Finished recording.");
        sendEmail();
        read = false;
        data = "";
        sorted_data = "";
    }
    
    void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        myLabel.setText("Bluetooth Closed");
        
        System.out.println(data);
        sorted_data = parseArduinoData(data);
        System.out.println(sorted_data);
    }
    
    void sendEmail()
    {
        sorted_data = parseArduinoData(data);
        System.out.println(sorted_data);
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, sorted_data);
		sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"dmonovoukas@gmail.com"});
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Physical Sensor Data");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
    }
    
    public static String parseArduinoData(String csv) {
        StringBuilder[] dataArrays = new StringBuilder[9];
        for(int i = 0; i < dataArrays.length; i++){
            dataArrays[i] = new StringBuilder();
        }

        String star = "*";
        String[] splitCSV = csv.split(",");
        int starIndex = 0;
        if(!splitCSV[9].equals(star)){
            for(int i = 0; i < splitCSV.length; i++){
                if(splitCSV[i].equals(star)){
                    starIndex = i;
                    break;
                }
            }
            splitCSV = Arrays.copyOfRange(splitCSV,starIndex + 1,splitCSV.length);
        }
        
        for (int i = 0; i < splitCSV.length; i++) {
            int index = i % 10;
            if(index < 9){
                dataArrays[index].append(splitCSV[i]);
                dataArrays[index].append(",");
            }
        }

        StringBuilder fullCSV = dataArrays[0];
        StringBuilder sizes = new StringBuilder();
        sizes.append(dataArrays[0].length() / 2);
        for(int i = 1; i < 9; i++){
            fullCSV.append(dataArrays[i]);
            sizes.append(",");
            sizes.append(dataArrays[i].length() / 2);
        }
        fullCSV.append(sizes);

        return fullCSV.toString();

    }
}