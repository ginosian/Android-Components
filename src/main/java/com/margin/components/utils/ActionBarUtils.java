package com.margin.components.utils;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created on April 04, 2016.
 *
 * @author Marta.Ginosyan
 */
@SuppressWarnings("unused")
public class ActionBarUtils {

    /**
     * Sets up toolbar correspondingly.
     *
     * @param activity     hosting activity
     * @param toolbar      toolbar
     * @param toolbarTitle title to be displayed
     * @param showHomeAsUp boolean, indicating whether back arrow should be displayed
     * @return The constructed {@link ActionBar} instance.
     */
    public static ActionBar setupToolbar(AppCompatActivity activity, Toolbar toolbar, @Nullable String toolbarTitle, boolean showHomeAsUp) {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            if (null != toolbarTitle) actionBar.setTitle(toolbarTitle);
            if (showHomeAsUp)
                actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return actionBar;
    }

    /**
     * Sets up toolbar correspondingly.
     *
     * @param activity     hosting activity
     * @param toolbar      toolbar
     * @param showHomeAsUp boolean, indicating whether back arrow should be displayed
     * @return The constructed {@link ActionBar} instance.
     */
    public static ActionBar setupToolbar(AppCompatActivity activity, Toolbar toolbar, boolean showHomeAsUp) {
        return setupToolbar(activity, toolbar, null, showHomeAsUp);
    }

    /**
     * Sets up toolbar correspondingly.
     *
     * @param activity hosting activity
     * @param toolbar  toolbar
     * @return The constructed {@link ActionBar} instance.
     */
    public static ActionBar setupToolbar(AppCompatActivity activity, Toolbar toolbar) {
        return setupToolbar(activity, toolbar, null, false);
    }

    /**
     * Sets up toolbar correspondingly.
     *
     * @param activity     hosting activity
     * @param toolbar      toolbar
     * @param toolbarTitle title to be displayed
     * @param drawableId   Resource ID of a drawable to use for the up indicator
     * @return The constructed {@link ActionBar} instance.
     */
    public static ActionBar setupToolbar(AppCompatActivity activity, Toolbar toolbar, @Nullable String toolbarTitle, @Nullable @DrawableRes Integer drawableId) {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            if (null != toolbarTitle) actionBar.setTitle(toolbarTitle);
            if (null != drawableId) toolbar.setNavigationIcon(drawableId);
        }
        return actionBar;
    }

    /**
     * Sets up toolbar correspondingly.
     *
     * @param activity   hosting activity
     * @param toolbar    toolbar
     * @param drawableId Resource ID of a drawable to use for the up indicator
     * @return The constructed {@link ActionBar} instance.
     */
    public static ActionBar setupToolbar(AppCompatActivity activity, Toolbar toolbar, @Nullable @DrawableRes Integer drawableId) {
        return setupToolbar(activity, toolbar, null, drawableId);
    }


}
