package com.jonuy.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jonuy.uy_app.R;

public class ParallaxView extends View {

	private static final String TAG = "ParallaxView";
	
	// Reference to image for the background view
	private int backgroundSrc;
	
	// Reference to image for the foreground view
	private int foregroundSrc;

	public ParallaxView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BGImageView, 0, 0);
		
		try {
			backgroundSrc = a.getResourceId(R.styleable.BGImageView_backgroundSrc, 0);
			foregroundSrc = a.getResourceId(R.styleable.BGImageView_foregroundSrc, 0);
		}
		finally {
			a.recycle();
		}
		
		Log.v(TAG, "ParallaxView constructed with bg: "+backgroundSrc+", and fg: "+foregroundSrc);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Log.v(TAG, "onDraw()");
	}

}
