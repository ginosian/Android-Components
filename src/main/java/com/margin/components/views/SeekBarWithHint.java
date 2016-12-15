package com.margin.components.views;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.margin.components.R;

/**
 * Created on Feb 25, 2016.
 *
 * @author Marta.Ginosyan
 */
public class SeekBarWithHint extends AppCompatSeekBar implements OnSeekBarChangeListener {

    private int mPopupWidth;
    private int mYLocationOffset;

    private PopupWindow mPopup;
    private TextView mPopupTextView;
    private View mBackground;

    private OnSeekBarChangeListener mInternalListener;
    private OnSeekBarChangeListener mExternalListener;

    private OnSeekBarHintChangeListener mHintChangeListener;

    public SeekBarWithHint(Context context) {
        super(context);
        initHintPopup();
    }

    public SeekBarWithHint(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHintPopup();
    }

    public SeekBarWithHint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHintPopup();
    }

    /**
     * Init all parameters of hint, including setting popupWidth, Y offset, background color,
     * creating new {@link PopupWindow}
     */
    private void initHintPopup() {

        setOnSeekBarChangeListener(this);

        String popupText = null;
        int drawableResourceId = R.color.black;

        if (mHintChangeListener != null) {
            HintType hintType = mHintChangeListener.onHintChanged(this, getProgress());
            popupText = hintType.getName();
            drawableResourceId = hintType.getDrawableResourceId();
        }

        mYLocationOffset = (int) getResources().getDimension(R.dimen.hint_top_offset);
        mPopupWidth = (int) getResources().getDimension(R.dimen.hint_width_normal);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View hintView = inflater.inflate(R.layout.seekbar_hint, null);

        mBackground = hintView.findViewById(R.id.background);
        mBackground.setBackgroundColor(getResources().getColor(drawableResourceId));
        mPopupTextView = (TextView) hintView.findViewById(R.id.text);
        mPopupTextView.setText(popupText != null ? popupText : "");

        mPopup = new PopupWindow(hintView, mPopupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, false);

        mPopup.setAnimationStyle(R.style.fade_animation);

    }

    private void showPopup() {
        mPopup.showAtLocation(this, Gravity.START | Gravity.TOP,
                (int) (this.getX() + (int) getXPosition()),
                getYPosition() - mYLocationOffset - this.getHeight());
    }

    private void hidePopup() {
        if (mPopup.isShowing()) {
            mPopup.dismiss();
        }
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        if (mInternalListener == null) {
            mInternalListener = l;
            super.setOnSeekBarChangeListener(l);
        } else {
            mExternalListener = l;
        }
    }

    public void setOnHintChangeListener(OnSeekBarHintChangeListener listener) {
        mHintChangeListener = listener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        String popupText = null;
        int drawableResourceId = R.color.black;

        if (mHintChangeListener != null) {
            HintType hintType = mHintChangeListener.onHintChanged(this, getProgress());
            popupText = hintType.getName();
            drawableResourceId = hintType.getDrawableResourceId();
        }

        if (mExternalListener != null) {
            mExternalListener.onProgressChanged(seekBar, progress, b);
        }

        mPopupTextView.setText(popupText != null ? popupText : "");
        mBackground.setBackgroundColor(getResources().getColor(drawableResourceId));

        mPopup.update((int) (this.getX() + (int) getXPosition()),
                getYPosition() - mYLocationOffset - this.getHeight(), -1, -1);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mExternalListener != null) {
            mExternalListener.onStartTrackingTouch(seekBar);
        }

        showPopup();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mExternalListener != null) {
            mExternalListener.onStopTrackingTouch(seekBar);
        }

        hidePopup();
    }

    /**
     * Calculate new hint X position
     */
    private float getXPosition() {
        float val = (((float) getProgress() * (float) (getWidth() -
                (getThumbOffset() << 1))) / getMax());
        float offset = getThumbOffset();

        int textWidth = mPopupWidth;
        float textCenter = (textWidth / 2.0f);

        return val + offset - textCenter;
    }

    /**
     * Calculate seekBar Y position on screen
     */
    private int getYPosition() {
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        return location[1];
    }

    public enum HintType {

        // TODO: Color values need to be stored in strings.xml
        Mild("Minor", R.color.lightGreen500),
        Medium("Intermediate", R.color.orange500),
        Severe("Severe", R.color.red500),
        None("None", R.color.black);

        private String name;
        private int drawableResourceId;

        HintType(String name, int drawableResourceId) {
            this.name = name;
            this.drawableResourceId = drawableResourceId;
        }

        public String getName() {
            return name;
        }

        public int getDrawableResourceId() {
            return drawableResourceId;
        }
    }

    /**
     * Should be overridden to set proper hint presentation
     */
    public interface OnSeekBarHintChangeListener {

        /**
         * It's invoked every time when progress was changed
         */
        HintType onHintChanged(SeekBarWithHint seekBarHint, int progress);
    }
}
