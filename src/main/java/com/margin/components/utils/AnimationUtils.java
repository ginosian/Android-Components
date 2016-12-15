package com.margin.components.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.FloatRange;
import android.view.View;

/**
 * Created on Feb 25, 2016.
 *
 * @author Marta.Ginosyan
 */
public class AnimationUtils {

    /**
     * Provides fade in animation
     *
     * @param view        - view to animate
     * @param duration    - duration in milliseconds
     * @param targetAlpha - target alpha after animating
     */
    public static void fadeInAnimation(View view, int duration,
                                       @FloatRange(from = 0.0, to = 1.0) float targetAlpha) {
        fadeAnimation(view, duration, targetAlpha, View.VISIBLE);
    }

    /**
     * Provides fade in animation
     *
     * @param view     - view to animate
     * @param duration - duration in milliseconds
     */
    public static void fadeInAnimation(View view, int duration) {
        fadeInAnimation(view, duration, 1.0f);
    }

    /**
     * Provides fade out animation
     *
     * @param view        - view to animate
     * @param duration    - duration in milliseconds
     * @param targetAlpha - target alpha after animating
     */
    public static void fadeOutAnimation(View view, int duration,
                                        @FloatRange(from = 0.0, to = 1.0) float targetAlpha) {
        fadeAnimation(view, duration, targetAlpha, View.GONE);
    }

    /**
     * Provides fade out animation
     *
     * @param view     - view to animate
     * @param duration - duration in milliseconds
     */
    public static void fadeOutAnimation(View view, int duration) {
        fadeOutAnimation(view, duration, 0.0f);
    }

    /**
     * Provides fade animation
     *
     * @param view             - view to animate
     * @param duration         - duration in milliseconds
     * @param targetAlpha      - target alpha after animating
     * @param targetVisibility - target visibility after animating
     */
    public static void fadeAnimation(final View view, int duration,
                                     @FloatRange(from = 0.0, to = 1.0) float targetAlpha,
                                     final int targetVisibility) {
        view.animate()
                .alpha(targetAlpha)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(targetVisibility);
                    }
                });
    }

    /**
     * Provides fade animation
     *
     * @param view        - view to animate
     * @param duration    - duration in milliseconds
     * @param scaleFactor - the value to be animated to.
     */
    public static void resizeAnimation(View view, int duration, float scaleFactor) {
        view.animate()
                .scaleX(scaleFactor)
                .scaleY(scaleFactor)
                .setDuration(duration);
    }

    /**
     * Provides move animation
     *
     * @param view     - view to animate
     * @param duration - duration in milliseconds
     * @param x        - <code>x</code> property to be animated by the specified value.
     * @param y        - <code>y</code> property to be animated by the specified value.
     */
    public static void moveAnimation(final View view, int duration, float x, float y) {
        view.animate()
                .x(x)
                .y(y)
                .setDuration(duration)
                .start();
    }
}
