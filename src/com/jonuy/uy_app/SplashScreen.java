package com.jonuy.uy_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

	private final int SPLASH_DURATION = 3000; // in milliseconds
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        
        new Handler().postDelayed(new Runnable() {
        	@Override
        	public void run() {
        		Intent intent = new Intent(SplashScreen.this, Home.class);
        		startActivity(intent);
        		
        		finish();
        	}
        }, SPLASH_DURATION);
    }
    
}
