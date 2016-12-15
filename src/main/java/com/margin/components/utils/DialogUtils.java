package com.margin.components.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

/**
 * Created on April 06, 2016.
 *
 * @author Marta.Ginosyan
 */
public class DialogUtils {

    /**
     * Shows dialog with specified input.
     *
     * @param activity              Hosting activity
     * @param titleRes              Resource identifier of dialog's title.
     * @param messageRes            Content message's resource id.
     * @param iconRes               Dialog's icon resource id.
     * @param positiveButtonRes     The resource id of the string, that would be displayed on positive button.
     * @param positiveClickListener The callback code, that would be called when positive button is clicked.
     * @param negativeButtonRes     The resource id of the string, that would be displayed on negative button.
     * @param negativeClickListener The callback code, that would be called when negative button is clicked.
     */
    public static void showDialog(final Activity activity, @StringRes int titleRes, @StringRes int messageRes, @DrawableRes int iconRes,
                                  @StringRes int positiveButtonRes, DialogInterface.OnClickListener positiveClickListener,
                                  @StringRes int negativeButtonRes, DialogInterface.OnClickListener negativeClickListener) {
        new AlertDialog.Builder(activity).setTitle(titleRes)
                .setMessage(messageRes)
                .setIcon(iconRes)
                .setPositiveButton(positiveButtonRes, positiveClickListener)
                .setNegativeButton(negativeButtonRes, negativeClickListener)
                .create()
                .show();
    }

    /**
     * Shows dialog with specified input.
     *
     * @param activity              Hosting activity
     * @param titleRes              Resource identifier of dialog's title.
     * @param messageRes            Content message's resource id.
     * @param positiveButtonRes     The resource id of the string, that would be displayed on positive button.
     * @param positiveClickListener The callback code, that would be called when positive button is clicked.
     * @param negativeButtonRes     The resource id of the string, that would be displayed on negative button.
     * @param negativeClickListener The callback code, that would be called when negative button is clicked.
     */
    public static void showDialog(final Activity activity, @StringRes int titleRes, @StringRes int messageRes, @StringRes int positiveButtonRes,
                                  DialogInterface.OnClickListener positiveClickListener, @StringRes int negativeButtonRes, DialogInterface.OnClickListener negativeClickListener) {
        new AlertDialog.Builder(activity).setTitle(titleRes)
                .setMessage(messageRes)
                .setPositiveButton(positiveButtonRes, positiveClickListener)
                .setNegativeButton(negativeButtonRes, negativeClickListener)
                .create()
                .show();
    }

}
