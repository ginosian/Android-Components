package com.margin.components.presenters;

import com.margin.components.views.INumSelectorView;

/**
 * Created on Feb 22, 2016.
 *
 * @author Marta.Ginosyan
 */
public class NumSelectorPresenter implements INumSelectorPresenter {

    private INumSelectorView mNumSelectorView;

    public NumSelectorPresenter(INumSelectorView numSelectorView) {
        mNumSelectorView = numSelectorView;
    }

    @Override
    public void onApplyButtonPressed(int currentValue) {
        if (mNumSelectorView != null) {
            mNumSelectorView.applyNumber(currentValue);
        }
    }

    @Override
    public void onCancelButtonPressed() {
        if (mNumSelectorView != null) {
            mNumSelectorView.closeFragment();
        }
    }

    @Override
    public void onPlusButtonPressed() {
        if (mNumSelectorView != null) {
            mNumSelectorView.incrementNumber();
        }
    }

    @Override
    public void onMinusButtonPressed() {
        if (mNumSelectorView != null) {
            mNumSelectorView.decrementNumber();
        }
    }

    @Override
    public void onDestroy() {
        mNumSelectorView = null;
    }
}
