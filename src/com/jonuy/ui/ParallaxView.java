package com.jonuy.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.jonuy.uy_app.R;

import java.util.Timer;
import java.util.TimerTask;

public class ParallaxView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = "ParallaxView";
    private static final long UPDATE_INTERVAL = 33; // in milliseconds
	
	// Reference to image for the background view
	private int mBackgroundRes;
	
	// Reference to image for the foreground view
	private int mForegroundRes;

    // Time in seconds to complete one animation loop of the background image
    private float mBackgroundDuration;

    // Time in seconds to complete one animation loop of the foreground image
    private float mForegroundDuration;

    // Current X translation of the background image
    private float mBackgroundTranslateXPct;

    // Current X translation of the foreground image
    private float mForegroundTranslateXPct;

    private Bitmap bmpBackground;
    private Bitmap bmpForeground;
    private int mBmpBackgroundWidth;
    private int mBmpBackgroundHeight;
    private int mBmpForegroundWidth;
    private int mBmpForegroundHeight;
    private int mScreenWidth; // in pixels
    private int mScreenHeight; // in pixels
    private Rect bgSubset;
    private Rect fgSubset;
    private RectF bgDest;
    private RectF fgDest;

	public ParallaxView(Context context, AttributeSet attrs) {
		super(context, attrs);

        if (!isInEditMode()) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        getHolder().addCallback(this);

        // Initial values
        mBackgroundTranslateXPct = 0.f;
        mForegroundTranslateXPct = 0.f;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;

        // Retrieve values from XML attributes
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ParallaxView, 0, 0);
		try {
			mBackgroundRes = a.getResourceId(R.styleable.ParallaxView_backgroundSrc, 0);
			mForegroundRes = a.getResourceId(R.styleable.ParallaxView_foregroundSrc, 0);
            mBackgroundDuration = a.getFloat(R.styleable.ParallaxView_backgroundDuration, 0.f);
            mForegroundDuration = a.getFloat(R.styleable.ParallaxView_foregroundDuration, 0.f);
		}
		finally {
			a.recycle();
		}
		
		Log.v(TAG, "ParallaxView constructed with bg: "+mBackgroundRes+", fg: "+mForegroundRes+", bgDuration: "+mBackgroundDuration+", fgDuration: "+mForegroundDuration);

        bmpBackground = BitmapFactory.decodeResource(getResources(), mBackgroundRes);
        bmpForeground = BitmapFactory.decodeResource(getResources(), mForegroundRes);
        mBmpBackgroundWidth = bmpBackground.getWidth();
        mBmpBackgroundHeight = bmpBackground.getHeight();
        mBmpForegroundWidth = bmpForeground.getWidth();
        mBmpForegroundHeight = bmpForeground.getHeight();
        bgSubset = new Rect();
        fgSubset = new Rect();
        bgDest = new RectF();
        fgDest = new RectF();
	}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false); // Allows invalidate() to call onDraw()

        // Execute timer at fixed intervals to update the background
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ParallaxTimerTask(this), 0, UPDATE_INTERVAL);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    private class ParallaxTimerTask extends TimerTask {
        private ParallaxView parallaxView;

        public ParallaxTimerTask(ParallaxView v) {
            parallaxView = v;
        }

        public void run() {
            // Update new x translations
            float intervalSeconds = (float)UPDATE_INTERVAL / 1000.f;
            mBackgroundTranslateXPct += (intervalSeconds / mBackgroundDuration);
            if (mBackgroundTranslateXPct >= 1.f) {
                mBackgroundTranslateXPct = 0.f;
            }

            mForegroundTranslateXPct += (intervalSeconds / mForegroundDuration);
            if (mForegroundTranslateXPct >= 1.f) {
                mForegroundTranslateXPct = 0.f;
            }

            // Can't invalidate() a View from another thread. So postInvalidate()
            parallaxView.postInvalidate();
        }
    }
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

        // Draw background
        int bgLeftPos = Math.round(mBmpBackgroundWidth * mBackgroundTranslateXPct);
        int bgRightPos = bgLeftPos + mScreenWidth;
        int bgRightDest = mScreenWidth;
        boolean bgNeedsSubset2 = false;

        if (bgRightPos > mBmpBackgroundWidth) {
            bgRightPos = mBmpBackgroundWidth;
            bgRightDest = mBmpBackgroundWidth - bgLeftPos;
            bgNeedsSubset2 = true;
        }

        bgSubset.set(bgLeftPos, 0, bgRightPos, mBmpBackgroundHeight);
        bgDest.set(0, 0, bgRightDest, mScreenHeight);

        // Draw background onto the Canvas
        canvas.drawBitmap(bmpBackground, bgSubset, bgDest, null);

        if (bgNeedsSubset2) {
            int bgRightPos2 = mScreenWidth - (bgRightPos - bgLeftPos);
            // TODO: don't do allocations in onDraw()
            Rect bgSubset2 = new Rect(0, 0, bgRightPos2, mScreenHeight);
            RectF bgDest2 = new RectF(bgRightDest, 0, mScreenWidth, mScreenHeight);
            canvas.drawBitmap(bmpBackground, bgSubset2, bgDest2, null);
        }

        // Draw foreground
        int fgLeftPos = Math.round(mBmpForegroundWidth * mForegroundTranslateXPct);
        int fgRightPos = fgLeftPos + mScreenWidth;
        int fgRightDest = mScreenWidth;
        boolean fgNeedsSubset2 = false;

        if (fgRightPos > mBmpForegroundWidth) {
            fgRightPos = mBmpForegroundWidth;
            fgRightDest = mBmpForegroundWidth - fgLeftPos;
            fgNeedsSubset2 = true;
        }

        fgSubset.set(fgLeftPos, 0, fgRightPos, mBmpForegroundHeight);
        fgDest.set(0, 0, fgRightDest, mScreenHeight);

        // Draw foreground onto the Canvas
        canvas.drawBitmap(bmpForeground, fgSubset, fgDest, null);

        if (fgNeedsSubset2) {
            int fgRightPos2 = mScreenWidth - (fgRightPos - fgLeftPos);
            Rect fgSubset2 = new Rect(0, 0, fgRightPos2, mScreenHeight);
            RectF fgDest2 = new RectF(fgRightDest, 0, mScreenWidth, mScreenHeight);
            canvas.drawBitmap(bmpForeground, fgSubset2, fgDest2, null);
        }
	}

}
