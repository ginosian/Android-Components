package com.margin.components.presenters;

/**
 * Created on Feb 22, 2016.
 *
 * @author Marta.Ginosyan
 */
public interface INumSelectorPresenter {

    /**
     * The apply button was pressed.
     * Send the callback to the activity.
     */
    void onApplyButtonPressed(int currentValue);

    /**
     * The cancel button was pressed.
     */
    void onCancelButtonPressed();

    /**
     * The plus button was pressed.
     */
    void onPlusButtonPressed();

    /**
     * The minus button was pressed.
     */
    void onMinusButtonPressed();

    /**
     * Make necessary actions when view is destroying
     */
    void onDestroy();
}
