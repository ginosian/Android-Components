package com.margin.components.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created on April 04, 2016.
 *
 * @author Marta.Ginosyan
 */
@SuppressWarnings("unused")
public class ViewUtils {

    public static void openKeyboard(Context context, View targetView) {
        targetView.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(targetView, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void closeKeyboard(Context context, View targetView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
    }

}
