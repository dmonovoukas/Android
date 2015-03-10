package com.example.bluetoothtest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.provider.MediaStore;

public class MainActivity extends ActionBarActivity {
	
	private final static String STORETEXT="storetext.txt";
	private EditText txtEditor;
	private TextView message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txtEditor=(EditText)findViewById(R.id.textbox);
		message=(TextView)findViewById(R.id.message);
		
		readFileInEditor();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	public void saveClicked(View v) {
		try {
			OutputStreamWriter out = new OutputStreamWriter(openFileOutput(STORETEXT, 0));
			out.write(txtEditor.getText().toString());
			out.close();
			Toast.makeText(this, "The contents are saved in the file.", Toast.LENGTH_LONG).show();
		}
		 
		catch (Throwable t) {
			Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();
		} 
	}
	
	public void sendAttached(View v) {
		//String data = "24 35 15";
		List<String> files= new ArrayList<String>();
		files.add("storetext.txt");
		
		ArrayList<Float> test1 = new ArrayList<Float>();
		test1.add((float) 5.5);
		test1.add((float) 6.2);
		test1.add((float) 42.0);
		
		ArrayList<Float> test2 = new ArrayList<Float>();
		test2.add((float) 1.2);
		test2.add((float) 3.2);
		test2.add((float) 4.0);
		
		String[] data = new String[2];
		data[0] = test1.get(0).toString() + ", " + test1.get(1).toString() + ", " + test1.get(2).toString();
		data[1] = test2.get(0).toString() + ", " + test2.get(1).toString() + ", " + test2.get(2).toString();
		
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		//sendIntent.putExtra(Intent.EXTRA_TEXT, data[0]);
		sendIntent.putExtra(Intent.EXTRA_TEXT, data);
		//sendIntent.putExtra(Intent.EXTRA_TEXT, data[1]);
		sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"dmonovoukas@gmail.com"});
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "IMU Data");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
	
	public void readFileInEditor()
	{
		try {
			InputStream in = openFileInput(STORETEXT);
			if (in != null) {
				InputStreamReader tmp=new InputStreamReader(in);
				BufferedReader reader=new BufferedReader(tmp);
				String str;
				StringBuilder buf=new StringBuilder();
			
				while ((str = reader.readLine()) != null) {
					buf.append(str+"n");
				}
			
				in.close();
				txtEditor.setText(buf.toString());
			}
		}
	 
		catch (java.io.FileNotFoundException e) {
			// that's OK, we probably haven't created it yet
		}
		
		catch (Throwable t) {
			Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();
		} 
	}
} 