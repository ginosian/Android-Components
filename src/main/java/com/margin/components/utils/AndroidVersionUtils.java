package com.margin.components.utils;

import android.os.Build;

/**
 * A utility class to check Android SDK version.
 * <p>
 * Created by  Marta.Ginosyan
 */
public class AndroidVersionUtils {

    /**
     * @return true, if Android SDK is higher/equal to Kit-Kat. False - otherwise.
     */
    @SuppressWarnings("unused")
    public static boolean isHigherEqualToKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * @return true, if Android SDK is higher/equal to Lollipop. False - otherwise.
     */
    @SuppressWarnings("unused")
    public static boolean isHigherEqualToLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * @return true, if Android SDK is higher/equal to Marshmallow. False - otherwise.
     */
    @SuppressWarnings("unused")
    public static boolean isHigherEqualToMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
