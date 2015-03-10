package com.example.wave;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StartFragment extends Fragment implements View.OnClickListener {

	private Button mButton, button1;
	private RelativeLayout mStartLayout;
	private TextView text1, text2;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mStartLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_start,
                container, false);

		// note that we're looking for a button with id="@+id/myButton" in your inflated layout
		// Naturally, this can be any View; it doesn't have to be a button
		mButton = (Button) mStartLayout.findViewById(R.id.button1);
	    mButton.setOnClickListener(new OnClickListener() {

	         @Override
	         public void onClick(View v) {
	             // TODO Auto-generated method stub
	        	 if(v == mButton) {
	        		 System.out.println("Clicked!");
	        		 Intent intent = new Intent(getActivity(), MainActivity.class);
	        		 startActivity(intent);
	        	 }
	         }
		});
		
		text1 = (TextView) mStartLayout.findViewById(R.id.ride_the);
		text2 = (TextView) mStartLayout.findViewById(R.id.wave);
		button1 = (Button) mStartLayout.findViewById(R.id.button1);
		Typeface type=Typeface.createFromAsset(getActivity().getAssets(), "fonts/open-sans/OpenSans-Light.ttf");
		text1.setTypeface(type);
		text2.setTypeface(type);
		button1.setTypeface(type);
		
		return mStartLayout;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}