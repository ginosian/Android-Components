package com.margin.components.utils;

import android.app.Activity;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created on March 31, 2016.
 *
 * @author Marta.Ginosyan
 */
public class SnackbarUtils {

    /**
     * Constructs and shows {@link Snackbar} with provided options.
     *
     * @param activity        Hosting activity
     * @param stringTitle     Title of snackbar
     * @param stringAction    Action text of snackbar. If null - no action would be displayed.
     * @param length          How long to display the message. Either {@link Snackbar#LENGTH_SHORT}, {@link Snackbar#LENGTH_LONG} or {@link Snackbar#LENGTH_INDEFINITE}.
     * @param onClickListener Callback to be invoked when the action is clicked.
     * @return The snackbar instance.
     */
    public static Snackbar showSnackbar(Activity activity, @StringRes int stringTitle,
                                        @Nullable @StringRes Integer stringAction,
                                        @IntRange(from = -2, to = 0) int length,
                                        @Nullable View.OnClickListener onClickListener) {
        View view = activity.findViewById(android.R.id.content);

        Snackbar snackbar = null;
        if (null != view) {
            snackbar = Snackbar.make(view, activity.getResources().getString(stringTitle), length);
            if (null != stringAction)
                snackbar.setAction(activity.getResources().getString(stringAction), onClickListener);
            snackbar.show();
        }
        return snackbar;
    }

    /**
     * Constructs and shows {@link Snackbar} with provided options.
     *
     * @param activity    Hosting activity
     * @param stringTitle Title of snackbar
     * @param length      How long to display the message. Either {@link Snackbar#LENGTH_SHORT},
     *                    {@link Snackbar#LENGTH_LONG} or {@link Snackbar#LENGTH_INDEFINITE}.
     * @return The snackbar instance.
     */
    public static Snackbar showSnackbar(Activity activity, @StringRes int stringTitle,
                                        @IntRange(from = -2, to = 0) int length) {
        return showSnackbar(activity, stringTitle, null, length, null);
    }

    /**
     * Constructs and shows {@link Snackbar} with provided options.
     *
     * @param activity        Hosting activity
     * @param stringTitle     Title of snackbar
     * @param stringAction    Action text of snackbar. If null - no action would be displayed.
     * @param length          How long to display the message. Either {@link Snackbar#LENGTH_SHORT},
     *                        {@link Snackbar#LENGTH_LONG} or {@link Snackbar#LENGTH_INDEFINITE}.
     * @param onClickListener Callback to be invoked when the action is clicked.
     * @return The snackbar instance.
     */
    public static Snackbar showSnackbar(Activity activity, String stringTitle,
                                        @Nullable String stringAction,
                                        @IntRange(from = -2, to = 0) int length,
                                        @Nullable View.OnClickListener onClickListener) {
        View view = activity.findViewById(android.R.id.content);

        Snackbar snackbar = null;
        if (null != view) {
            snackbar = Snackbar.make(view, stringTitle, length);
            if (null != stringAction)
                snackbar.setAction(stringAction, onClickListener);
            snackbar.show();
        }
        return snackbar;
    }


    /**
     * Constructs and shows {@link Snackbar} with provided options.
     *
     * @param activity    Hosting activity
     * @param stringTitle Title of snackbar
     * @param length      How long to display the message. Either {@link Snackbar#LENGTH_SHORT},
     *                    {@link Snackbar#LENGTH_LONG} or {@link Snackbar#LENGTH_INDEFINITE}.
     * @return The snackbar instance.
     */
    public static Snackbar showSnackbar(Activity activity, String stringTitle,
                                        @IntRange(from = -2, to = 0) int length) {
        return showSnackbar(activity, stringTitle, null, length, null);
    }
}
