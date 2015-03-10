package com.example.wave;

import android.support.v4.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExplanationFragment extends Fragment {

	private TextView text1, text2, text3, text4, text5, text6, text7;
	private RelativeLayout mStartLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mStartLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_explanation,
                container, false);
		
		text1 = (TextView) mStartLayout.findViewById(R.id.weekly_goals);
		text2 = (TextView) mStartLayout.findViewById(R.id.TextView02);
		text3 = (TextView) mStartLayout.findViewById(R.id.TextView03);
		text4 = (TextView) mStartLayout.findViewById(R.id.TextView04);
		text5 = (TextView) mStartLayout.findViewById(R.id.TextView05);
		text6 = (TextView) mStartLayout.findViewById(R.id.compare_workouts);
		text7 = (TextView) mStartLayout.findViewById(R.id.track_activity);
		Typeface type=Typeface.createFromAsset(getActivity().getAssets(), "fonts/open-sans/OpenSans-Light.ttf");
		text1.setTypeface(type);
		text2.setTypeface(type);
		text3.setTypeface(type);
		text4.setTypeface(type);
		text5.setTypeface(type);
		text6.setTypeface(type);
		text7.setTypeface(type);
		
		return mStartLayout;
	}
}