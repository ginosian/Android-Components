package com.margin.components.utils;

import android.support.v7.widget.PopupMenu;
import android.view.View;

/**
 * Created on Mar 05, 2016.
 *
 * @author Marta.Ginosyan
 */
public class MenuUtils {

    /**
     * Showing popup menu according to selected view
     *
     * @param v                       view that used to show popup menu
     * @param onMenuItemClickListener listen to menu item pressed events
     * @param resourceMenuId          resourceId of menu
     */
    public static void showDeviceMenu(View v, PopupMenu.OnMenuItemClickListener
            onMenuItemClickListener, int resourceMenuId) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.inflate(resourceMenuId);
        popup.setOnMenuItemClickListener(onMenuItemClickListener);
        popup.show();
    }
}
