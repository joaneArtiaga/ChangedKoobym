package com.example.joane14.myapplication.Class;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Joane14 on 11/10/2017.
 */

public class NonScollableVP extends ViewPager {

    public NonScollableVP(Context context) {
        super(context);
    }
    public NonScollableVP(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }
    @Override public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages return false;
        return false;
    }
}
