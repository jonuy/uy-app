package com.jonuy.uy_app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class Projects extends FragmentActivity {
	
	private static final int NUM_PAGES = 3;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_projects);
		
		mPager = (ViewPager)findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
		mPager.setAdapter(mPagerAdapter);
	}
	
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new ProjectFragment();
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}
}
