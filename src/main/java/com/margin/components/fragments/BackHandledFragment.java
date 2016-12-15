package com.margin.components.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created on Feb 22, 2016.
 *
 * @author Marta.Ginosyan
 */
public abstract class BackHandledFragment extends Fragment {

    protected IBackPressedHandler mBackPressedHandler;

    /**
     * Do your work on backPressed event. Then return true if you want to consume this
     * event in fragment, and false if you want to pass it to activity.
     */
    public abstract boolean onBackPressed();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mBackPressedHandler = (IBackPressedHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement IBackPressedHandler!");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Mark this fragment as the selected Fragment.
        mBackPressedHandler.setSelectedFragment(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBackPressedHandler.setSelectedFragment(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // We should set all listeners in null to avoid memory leaks
        mBackPressedHandler = null;
    }

    /**
     * This interface should be implemented in activity. Override {@link
     * android.app.Activity#onBackPressed} and let activity handle onBackPressed event
     * only if the event doesn't get consumed at fragment level. The example of usage here:
     * <pre>
     * <code>
     *
     * {@literal}@Override
     * public void onBackPressed() {
     *     if(selectedFragment == null || !selectedFragment.onBackPressed()) {
     *         super.onBackPressed();
     *     }
     * }
     * </code>
     * </pre>
     */
    public interface IBackPressedHandler {

        /**
         * Implement this method to keep reference of BackHandledFragment. Then use it
         * in overriding {@link android.app.Activity#onBackPressed}
         */
        void setSelectedFragment(BackHandledFragment backHandledFragment);
    }
}
