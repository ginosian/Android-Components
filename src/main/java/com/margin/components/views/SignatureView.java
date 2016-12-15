package com.margin.components.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.margin.components.listeners.OnSignatureChangedListener;

import java.io.ByteArrayOutputStream;

/**
 * Created on Apr 06, 2016.
 *
 * @author Marta.Ginosyan
 */
public class SignatureView extends View {

    private static final float STROKE_WIDTH = 6f;
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private static final int JPEG_QUALITY = 90;
    private static final int WIDTH_LIMIT = 320;
    private static final int HEIGHT_LIMIT = 240;
    private Paint paint = new Paint();
    private Path path = new Path();
    private float lastTouchX;
    private float lastTouchY;
    private RectF drawingRect = new RectF();
    private OnSignatureChangedListener mSignatureChangedListener;

    public SignatureView(Context context) {
        super(context);
        init();
    }

    public SignatureView(Context context, OnSignatureChangedListener onSignatureChangedListener) {
        super(context);
        init();
        setSignatureChangedListener(onSignatureChangedListener);
    }

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Set callback to listen to signature updates
     */
    public void setSignatureChangedListener(
            OnSignatureChangedListener onSignatureChangedListener) {
        mSignatureChangedListener = onSignatureChangedListener;
    }

    /**
     * Prepare signature view to draw
     */
    private void init() {
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
        setDrawingCacheEnabled(false);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWeight, int oldHeight) {
        super.onSizeChanged(width, height, oldWeight, oldHeight);

        if ((width == 0 && height == 0) || (width == oldWeight && height == oldHeight)
                || (oldWeight == 0) && (oldHeight == 0)) {
            return;
        }
        // we should update the already drawn path according to the new size
        updateDrawnPath(width, height, oldWeight, oldHeight);
    }

    /**
     * Update drawn path when signature size has been changed
     *
     * @param width     new width of signature view
     * @param height    new height of signature view
     * @param oldWidth  old width of signature view
     * @param oldHeight old height of signature view
     */
    private void updateDrawnPath(int width, int height, int oldWidth, int oldHeight) {
        // check the current rotate state
        boolean portrait = (height > oldHeight);
        // calculate ratio between old values and new ones according to the new
        // rotate state
        float ratio;
        if (portrait) {
            ratio = (float) oldWidth / (float) width;
        } else {
            ratio = (float) oldHeight / (float) height;
        }
        // find the new scale factor based on new ratio
        float scaleFactor = 1.0f / ratio;
        // create Matrix object for scaling the drawn path
        Matrix matrix = new Matrix();
        matrix.preScale(scaleFactor, scaleFactor);
        // scale path according to the new scale factor
        path.transform(matrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(path, paint);
        if (!path.isEmpty()) {
            setDrawingCacheEnabled(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                return true;

            case MotionEvent.ACTION_MOVE:
                drawTheLine(event);
                break;
            case MotionEvent.ACTION_UP:
                drawTheLine(event);
                if (mSignatureChangedListener != null) {
                    if (isDrawingCacheEnabled()) {
                        mSignatureChangedListener.onSignatureChanged(
                                Bitmap.createBitmap(getDrawingCache()));
                    }
                }
                break;

            default:
                return false;
        }
        // we should only invalidate the drawing area, it's not necessary to
        // redraw the whole signature view
        invalidate((int) (drawingRect.left - HALF_STROKE_WIDTH),
                (int) (drawingRect.top - HALF_STROKE_WIDTH),
                (int) (drawingRect.right + HALF_STROKE_WIDTH),
                (int) (drawingRect.bottom + HALF_STROKE_WIDTH));

        lastTouchX = eventX;
        lastTouchY = eventY;

        return true;
    }

    /**
     * Draw the line with current {@link MotionEvent} event
     *
     * @param event current MotionEvent object
     */
    private void drawTheLine(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        // reset drawing rectangle first according to the last
        // touch coordinates
        resetDrawingRect(eventX, eventY);
        // ACTION_MOVE event contains many historical points and we can't
        // just draw the line between last event and current. Otherwise we
        // will see set of acute angles instead of ellipses. So we should
        // check all of historical points and draw the lines between them
        int historySize = event.getHistorySize();
        for (int i = 0; i < historySize; i++) {
            float historicalX = event.getHistoricalX(i);
            float historicalY = event.getHistoricalY(i);
            // increase size of drawing rect if it's needed
            expandDrawingRect(historicalX, historicalY);
            // draw the line between previous historical point and current
            path.lineTo(historicalX, historicalY);
        }
        // draw the line between the last historical point and current
        // event coordinates
        path.lineTo(eventX, eventY);
    }

    /**
     * Expand drawing rect while the action event is ACTION_MOVE.
     * We should update the borders of the drawing rectangle if
     * it's needed. We can do drawings after this.
     *
     * @param eventX x coordinate of current motion event
     * @param eventY y coordinate of current motion event
     */
    private void expandDrawingRect(float eventX, float eventY) {
        // check which side we should update left or right
        if (eventX < drawingRect.left) {
            drawingRect.left = eventX;
        } else if (eventX > drawingRect.right) {
            drawingRect.right = eventX;
        }
        // check which side we should update top or bottom
        if (eventY < drawingRect.top) {
            drawingRect.top = eventY;
        } else if (eventY > drawingRect.bottom) {
            drawingRect.bottom = eventY;
        }
    }

    /**
     * Reset all drawing rect with current {@link MotionEvent} event values.
     * It should be invoked before expanding the drawing rect and drawing.
     *
     * @param eventX x coordinate of current motion event
     * @param eventY y coordinate of current motion event
     */
    private void resetDrawingRect(float eventX, float eventY) {
        // the min value of lastTouchX and currentX will be the left side
        // of drawing rectangle
        drawingRect.left = Math.min(lastTouchX, eventX);
        // the max value will be the right side
        drawingRect.right = Math.max(lastTouchX, eventX);
        // the min value of lastTouchY and currentY will be the top side
        drawingRect.top = Math.min(lastTouchY, eventY);
        // the max value will be the bottom side
        drawingRect.bottom = Math.max(lastTouchY, eventY);
    }

    /**
     * Clear the signature view to reset all drawings
     */
    public void clear() {
        path.reset();
        invalidate();
        setDrawingCacheEnabled(false);
    }

    /**
     * Convert bitmap to byte array. Bitmap will be compressed and scaled
     * to reduce the size
     */
    public byte[] bitmapToByteArray(Bitmap bmp) {

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        float scale = Math.max(width / WIDTH_LIMIT, height / HEIGHT_LIMIT);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, Math.round(width / scale),
                Math.round(height / scale), true);
        scaledBmp.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, stream);

        return stream.toByteArray();
    }
}
