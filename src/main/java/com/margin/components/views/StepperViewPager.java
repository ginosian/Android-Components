package com.margin.components.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created on Mar 30, 2016.
 *
 * @author Marta.Ginosyan
 */
public class StepperViewPager extends ViewPager {

    private boolean isPagingEnabled = true;

    public StepperViewPager(Context context) {
        super(context);
    }

    public StepperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    /**
     * Turn on or off pages swiping
     */
    public void setPagingEnabled(boolean isPagingEnabled) {
        this.isPagingEnabled = isPagingEnabled;
    }
}
