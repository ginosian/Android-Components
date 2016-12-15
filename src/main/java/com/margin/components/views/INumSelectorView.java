package com.margin.components.views;

/**
 * Created on Feb 22, 2016.
 *
 * @author Marta.Ginosyan
 */
public interface INumSelectorView {

    /**
     * The 'cancel' button was pressed.
     * Make necessary actions and close the fragment.
     */
    void closeFragment();

    /**
     * The 'apply' button was pressed.
     * Save final number and provide callback to activity.
     */
    void applyNumber(int number);

    /**
     * The 'plus' button was pressed.
     * Increment the current number value.
     */
    void incrementNumber();

    /**
     * The 'minus' button was pressed.
     * Decrement the current number value.
     */
    void decrementNumber();
}
