package com.orpatservice.app.utils;

import android.view.ScaleGestureDetector;

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    private float mScaleFactor = 1.0f;

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        mScaleFactor *= scaleGestureDetector.getScaleFactor();
        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
      //  imageView.setScaleX(mScaleFactor);
      //  imageView.setScaleY(mScaleFactor);
        return true;
    }
}
