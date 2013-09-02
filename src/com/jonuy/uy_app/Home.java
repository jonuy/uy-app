package com.jonuy.uy_app;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Home extends RoboActivity {
	
	private final int BG_IMAGE_DURATION = 5000;
	
	@InjectView(R.id.image1) private ImageView mBgImage1;
	@InjectView(R.id.image2) private ImageView mBgImage2;
	private ImageView mOldImage;
	private ImageView mNewImage;
	
	private Handler mHandler;
	private Runnable mUpdateBgImageTask;
	
	private final int[] mBgImageList = {
		R.drawable.bg1,
		R.drawable.bg2,
		R.drawable.bg3,
		R.drawable.bg4,
		R.drawable.bg5,
	};
	
	private int mCurrentBgImageIndex = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		
		mBgImage1.setDrawingCacheEnabled(true);
		mBgImage2.setDrawingCacheEnabled(true);
		mBgImage1.setDrawingCacheBackgroundColor(Color.BLACK);
		mBgImage2.setDrawingCacheBackgroundColor(Color.BLACK);
		
		// Task to update the background image on set time intervals
		mHandler = new Handler();
		mUpdateBgImageTask = new Runnable() {
			@Override
			public void run() {
				updateBgImage();
				mHandler.postDelayed(mUpdateBgImageTask, BG_IMAGE_DURATION);
			}
		};
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		mUpdateBgImageTask.run();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		mHandler.removeCallbacks(mUpdateBgImageTask);
	}
	
	/**
	 * Setup sources and transitions for the background ImageViews
	 */
	private void updateBgImage() {
		// Swap ImageViews that'll transition in and out
		if (mOldImage != null && mOldImage.equals(mBgImage1)) {
			mOldImage = mBgImage2;
			mNewImage = mBgImage1;
		}
		else {
			mOldImage = mBgImage1;
			mNewImage = mBgImage2;
		}
		
		// Select next image to display
		mCurrentBgImageIndex++;
		if (mCurrentBgImageIndex >= mBgImageList.length) {
			mCurrentBgImageIndex = 0;
		}
		mNewImage.setImageResource(mBgImageList[mCurrentBgImageIndex]);
		
		// Animation for ImageView transitioning out
		Animation currAnim = AnimationUtils.loadAnimation(Home.this, R.anim.slide_out_left);
		mOldImage.startAnimation(currAnim);
		currAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				mOldImage.setVisibility(View.GONE);
				mOldImage.setImageDrawable(null);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationStart(Animation animation) {}
		});
		
		// Animation for ImageView transitioning in
		Animation nextAnim = AnimationUtils.loadAnimation(Home.this, R.anim.slide_in_left);
		mNewImage.startAnimation(nextAnim);
		nextAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationStart(Animation animation) {
				mNewImage.setVisibility(View.VISIBLE);
			}
		});
	}
}
