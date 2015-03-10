package com.example.wave.adapter;

import com.example.wave.ExplanationFragment;

import com.example.wave.StartFragment;
import com.example.wave.TrackingFragment;
import com.example.wave.WelcomeFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new WelcomeFragment();
		case 1:
			// Games fragment activity
			return new ExplanationFragment();
		case 2:
			// Movies fragment activity
			return new TrackingFragment();
		case 3:
			// Movies fragment activity
			return new StartFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
