package com.margin.components.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * A handy class for performing permission checking on Android SDKs above API 23.
 * <p>
 * Created on March 31, 2016.
 *
 * @author Marta.Ginosyan
 */
public class PermissionUtils {

    /**
     * Checks for granted permissions.
     *
     * @param context     The context, where the permission is being checked from.
     * @param permissions An array of permission strings, that are required to be granted.
     * @return If all the permissions from {@code permissions} are granted - true. Otherwise - false.
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isPermissionsGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    /**
     * Checks for granted permission.
     *
     * @param context    The context, where the permission is being checked from.
     * @param permission A permission string, that is required to be granted.
     * @return True - if permission is granted, false otherwise.
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isPermissionGranted(Context context, String permission) {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
