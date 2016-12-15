package com.margin.components.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;

/**
 * Created on April 01, 2016.
 *
 * @author Marta.Ginosyan
 */
@SuppressWarnings("unused")
public class IntentUtils {

    /**
     * <p>Intent to show an applications details page in (Settings) com.android.settings</p>
     *
     * @param packageName        The package name of the application
     * @param excludeFromRecents If set to true, excludes activity from history stack.
     * @return the intent to open the application info screen.
     */
    public static Intent newAppDetailsIntent(String packageName, boolean excludeFromRecents) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            configureIntent(intent, excludeFromRecents);
            intent.setData(Uri.parse("package:" + packageName));
            return intent;
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            configureIntent(intent, excludeFromRecents);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("pkg", packageName);
            return intent;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        configureIntent(intent, excludeFromRecents);
        intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
        intent.putExtra("com.android.settings.ApplicationPkgName", packageName);
        return intent;
    }

    /**
     * Intent to show an applications details page in (Settings) com.android.settings.
     *
     * @param packageName The package name of the application
     * @return the intent to open the application info screen.
     */
    public static Intent newAppDetailsIntent(String packageName) {
        return newAppDetailsIntent(packageName, false);
    }

    /**
     * @param excludeFromRecents if true adds corresponding flags to intent, which result not showing activity in history stack.
     */
    private static Intent configureIntent(Intent intent, boolean excludeFromRecents) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (excludeFromRecents) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        }
        return intent;
    }

    /**
     * @return Intent that opens gallery.
     */
    public static Intent galleryIntent() {
        return new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }


}
