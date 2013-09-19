package com.jonuy.uy_app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Activity for displaying projects. Project views are broken out into fragments
 * that the user can swipe through because of the ViewPager.
 * 
 * Reference source: http://developer.android.com/training/animation/screen-slide.html
 */
public class Projects extends FragmentActivity {
	
	private static final int NUM_PAGES = 6;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_projects);
		
		// Use position 0 for initial page
		updateActivityOnPageChange(0);
		
		mPager = (ViewPager)findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setPageTransformer(true, new ZoomOutPageTransformer());
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				updateActivityOnPageChange(position);	
			}
		});
		
		Button backButton = (Button)this.findViewById(R.id.button_back);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	/**
	 * Updates the UI of the activity to respond to whatever page the ViewPager
	 * has changed to.
	 * 
	 * @param position Page that the ViewPager has changed to
	 */
	private void updateActivityOnPageChange(int position) {
		LinearLayout container = (LinearLayout)findViewById(R.id.pager_dots);
		container.removeAllViews();
		
		LayoutInflater inflater = getLayoutInflater();
		for (int i = 0; i < NUM_PAGES; i++) {
			int dotResId = R.layout.shape_pager_dot_open;
			if (i == position) {
				dotResId = R.layout.shape_pager_dot_filled;
			}
			
			View v = inflater.inflate(dotResId, container, false);
			container.addView(v);
		}
	}
	
	/**
	 * Pager adapter to provide the views for the ViewPager 
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ProjectFragment.create(position);
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}
	
	/**
	 * Page transformer for custom animating the pager transitions
	 */
	private class ZoomOutPageTransformer implements ViewPager.PageTransformer {
		private final float MIN_SCALE = 0.9f;
		private final float MIN_ALPHA = 0.85f;
		
		public void transformPage(View v, float pos) {
			int pageWidth = v.getWidth();
			int pageHeight = v.getHeight();
			
			// Position of page is off-screen to the left
			if (pos < -1) {
				v.setAlpha(0);
			}
			else if (pos <= 1) {
				// Modify the default slide transition to shrink the page too
				float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(pos));
				float vMargin = pageHeight * (1 - scaleFactor) / 2;
				float hMargin = pageWidth * (1 - scaleFactor) / 2;
				
				if (pos < 0) {
					v.setTranslationX(hMargin - vMargin / 2);
				}
				else {
					v.setTranslationX(-hMargin + vMargin / 2);
				}
				
				// Scale page down (between MIN_SCALE and 1)
				v.setScaleX(scaleFactor);
				v.setScaleY(scaleFactor);
				
				// Fade the page relative to its size
				v.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
			}
			// Position of page is off-screen to the right
			else {
				v.setAlpha(0);
			}
		}
	}
}
