package com.jonuy.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;

/**
 * TODO
 */
public class ParallaxViewLayer extends ImageView {

    private float mTranslationPercentage;

    public ParallaxViewLayer(Context context) {
        super(context);
    }

    public void setTranslationPercentage(float pct) {
        mTranslationPercentage = pct;
    }

    public float getTranslationPercentage() {
        return mTranslationPercentage;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
