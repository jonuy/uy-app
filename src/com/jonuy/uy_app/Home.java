package com.jonuy.uy_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class Home extends RoboActivity {

    private final int BG_IMAGE_DURATION = 10000;

    @InjectView(R.id.drawer_layout) private DrawerLayout mDrawerLayout;
    @InjectView(R.id.left_drawer) private ListView mDrawerList;
    @InjectView(R.id.drawer_toggle) private Button mDrawerToggle;
    @InjectView(R.id.image1) private ImageView mBgImage1;
    @InjectView(R.id.image2) private ImageView mBgImage2;
    private ImageView mOldImage;
    private ImageView mNewImage;

    private Handler mHandler;
    private Runnable mUpdateBgImageTask;

    private final int[] mBgImageList = {
            R.drawable.bg1,
            R.drawable.bg6,
            R.drawable.bg4,
    };

    private int mCurrentBgImageIndex;

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

        // Drawer setup for app navigation
        String[] drawerItems = {
                getResources().getString(R.string.nav_about),
                getResources().getString(R.string.nav_projects),
                getResources().getString(R.string.nav_contact)
        };

        mDrawerList.setAdapter(new DrawerListAdapter<String>(
                this, R.layout.drawer_list_item, drawerItems));
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // About
                        startActivity(new Intent(Home.this, About.class));
                        break;
                    case 1: // Projects
                        startActivity(new Intent(Home.this, Projects.class));
                        break;
                    case 2: // Contact
                        startActivity(new Intent(Home.this, Contact.class));
                        break;
                }
            }
        });

        // Button listener to open and closer the drawer menu
        mDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(mDrawerList))
                    mDrawerLayout.closeDrawer(mDrawerList);
                else
                    mDrawerLayout.openDrawer(mDrawerList);
            }
        });
    }

    private class DrawerListAdapter<T> extends ArrayAdapter<String> {
        public DrawerListAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentBgImageIndex = -1;
        mUpdateBgImageTask.run();
    }

    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeCallbacks(mUpdateBgImageTask);
        mDrawerList.setSelected(false);
        mDrawerLayout.closeDrawers();
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
        if (mNewImage == null || mNewImage.equals(mBgImage2)) {
            mOldImage = mBgImage2;
            mNewImage = mBgImage1;
        }
        else {
            mOldImage = mBgImage1;
            mNewImage = mBgImage2;
        }

        // Do not animate if this is the first frame
        boolean bAnimate = true;
        if (mCurrentBgImageIndex < 0) {
            bAnimate = false;
        }

        // Select next image to display
        mCurrentBgImageIndex++;
        if (mCurrentBgImageIndex >= mBgImageList.length) {
            mCurrentBgImageIndex = 0;
        }
        mNewImage.setImageResource(mBgImageList[mCurrentBgImageIndex]);

        if (bAnimate) {
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
}
