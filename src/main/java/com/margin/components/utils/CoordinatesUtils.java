package com.margin.components.utils;

import android.graphics.Rect;
import android.view.View;

/**
 * Created on Mar 05, 2016.
 *
 * @author Marta.Ginosyan
 */
public class CoordinatesUtils {

    private static final int CLICK_ACTION_THRESHHOLD = 10;
    private static final int[] LOCATION = new int[2];

    /**
     * Return True if the starting and ending positions indicate that the object was clicked,
     * otherwise return False.
     */
    public static boolean isClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        return !(differenceX > CLICK_ACTION_THRESHHOLD ||
                differenceY > CLICK_ACTION_THRESHHOLD);
    }

    /**
     * Return True if the view was moved, otherwise return False.
     */
    public static boolean isMoved(float startX, float endX, float startY, float endY) {
        return !isClick(startX, endX, startY, endY);
    }

    /**
     * Return True if the views intersect, otherwise return False.
     */
    public static boolean isViewsIntersect(View firstView, View secondView) {
        Rect firstRect = new Rect();
        firstView.getGlobalVisibleRect(firstRect);
        Rect secondRect = new Rect();
        secondView.getGlobalVisibleRect(secondRect);
        return firstRect.intersect(secondRect);
    }

    /**
     * Return True if the point with 'rx' and 'ry' is located inside this view,
     * otherwise return False.
     */
    public static boolean isViewContains(View view, int rx, int ry) {
        view.getLocationOnScreen(LOCATION);
        int x = LOCATION[0];
        int y = LOCATION[1];
        int w = view.getWidth();
        int h = view.getHeight();

        return !(rx < x || rx > x + w || ry < y || ry > y + h);
    }
}
