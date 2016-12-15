package com.margin.components.listeners;

import android.graphics.Bitmap;

/**
 * Created on Apr 06, 2016.
 *
 * @author Marta.Ginosyan
 */
public interface OnSignatureChangedListener {

    /**
     * Whenever the finger has been picked up from the screen,
     * a callback lets the parent know that the signature has been updated
     */
    void onSignatureChanged(Bitmap signature);
}
