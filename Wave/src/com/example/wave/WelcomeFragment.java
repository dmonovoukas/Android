package com.example.wave;

import android.support.v4.app.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WelcomeFragment extends Fragment {
	
	private TextView text1, text2, text3, text4;
	private RelativeLayout mStartLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mStartLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_welcome,
                container, false);
		
		text1 = (TextView) mStartLayout.findViewById(R.id.ride_the);
		text2 = (TextView) mStartLayout.findViewById(R.id.wave);
		text3 = (TextView) mStartLayout.findViewById(R.id.first_wearable);
		text4 = (TextView) mStartLayout.findViewById(R.id.for_swimming);
		Typeface type=Typeface.createFromAsset(getActivity().getAssets(), "fonts/open-sans/OpenSans-Light.ttf");
		text1.setTypeface(type);
		text2.setTypeface(type);
		text3.setTypeface(type);
		text4.setTypeface(type);
		
		return mStartLayout;
	}
}