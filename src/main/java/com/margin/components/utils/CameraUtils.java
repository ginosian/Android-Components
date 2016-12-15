package com.margin.components.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.view.Surface;

import java.io.File;
import java.util.List;

/**
 * Created on Feb 09, 2016.
 *
 * @author Marta.Ginosyan
 */
public class CameraUtils {

    private static final float ASPECT_TOLERANCE = 0.1f;

    /**
     * This method is used to rotate preview correctly when you're using front
     * or back camera.
     *
     * @param activity - it's used to get current device rotation
     * @param cameraId - it can be whether front or back camera
     * @param camera   - camera object to set final preview orientation. The
     *                 {@link Camera#setDisplayOrientation(int)} method lets
     *                 you change how the preview is displayed without affecting
     *                 how the image is recorded.
     */
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {

        if (camera == null) {
            return;
        }

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // Compensate the mirror
        } else { // Back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * This method modifies the image <b>after</b> it was already recorded.
     *
     * @param context - context object to get current orientation mode
     * @param bitmap  - Bitmap that should be rotated
     */
    public static Bitmap rotateImageIfNeeded(Context context, Bitmap bitmap) {
        if (context.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            // Setting post rotate to 90
            return CameraUtils.rotateImage(bitmap, 90);
        } else {// LANDSCAPE MODE
            //No need to reverse width and height
            return bitmap;
        }
    }

    /**
     * Convert a byte array to a Bitmap image.
     */
    public static Bitmap byteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);
    }

    /**
     * This method modifies the Bitmap image <b>after</b> it was already recorded.
     *
     * @param bm      Bitmap to rotate
     * @param degrees Number of degrees to rotate the bitmap
     */
    public static Bitmap rotateImage(Bitmap bm, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }

    /**
     * This method will calculate screen ratio based on those values and then from the
     * list of supportedPreviewSizes it will choose the best from available ones and set
     * it to the camera. For more info see {@link Camera.Parameters#getSupportedPreviewSizes()}
     */
    public static void setOptimalPreviewSize(Camera camera, int width, int height) {

        List<Camera.Size> previewSizes = camera.getParameters().getSupportedPreviewSizes();

        if (previewSizes == null) {
            return;
        }

        double targetRatio = (double) height / width;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        for (Camera.Size size : previewSizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
                continue;
            }
            if (Math.abs(size.height - height) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - height);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : previewSizes) {
                if (Math.abs(size.height - height) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - height);
                }
            }
        }

        if (optimalSize != null) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(optimalSize.width, optimalSize.height);
            camera.setParameters(parameters);
        }
    }

    /**
     * Checks if camera supports autofocus and set it to the camera
     */
    public static void setAutoFocus(Camera camera) {
        Camera.Parameters params = camera.getParameters();
        //It's important to test whether the phone is supporting your chosen mode before
        // attempting to use it, otherwise setParameters() will throw a runtime exception.
        if (params.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        camera.setParameters(params);
    }

    /**
     * Crop and scale image to fit the aspect ratio and size of the device screen.
     */
    public static Bitmap cropAndScaleImageIfNeeded(Context context, Bitmap original) {
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        if (screenWidth == original.getWidth() && screenHeight == original.getHeight()) {
            return original;
        }
        Bitmap result;
        float screenRatio = screenWidth / (float) screenHeight;
        float bitmapRatio = original.getWidth() / (float) original.getHeight();
        if (Math.abs(screenRatio - bitmapRatio) < ASPECT_TOLERANCE) {
            // Just scale the bitmap
            result = Bitmap.createScaledBitmap(original, screenWidth, screenHeight, true);
        } else {
            // Do scale and center crop
            result = scaleCenterCrop(original, screenHeight, screenWidth);
        }
        return result;
    }

    /**
     * Scale the image uniformly (maintain the image's aspect ratio) so that both
     * dimensions (width and height) of the image will be equal to or larger than
     * the corresponding dimension of the view (minus padding).
     */
    private static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);
        return dest;
    }

    /**
     * Decodes bitmap located in {@code imageLocation} with sizes {@code width} and {@code height}.
     */
    @Nullable
    public static Bitmap decodeBitmapFromLocation(String imageLocation, int width, int height) {
        File imageFile = new File(imageLocation);
        if (imageFile.exists()) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageLocation, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, width, height);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(imageLocation, options);
        }
        return null;
    }

    /**
     * Decodes bitmap located from {@code bytes} with sizes {@code width} and {@code height}.
     */
    public static Bitmap decodeSampledBitmapFromBytes(byte[] bytes, int reqWidth,
                                                      int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    /**
     * Calculates optimal sampling size.
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int width, int height) {

        // Raw height and width of image
        final int w = options.outWidth;
        final int h = options.outHeight;
        int inSampleSize = 1;

        if (h > height || w > width) {

            final int halfHeight = h / 2;
            final int halfWidth = w / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > height
                    && (halfWidth / inSampleSize) > width) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
