package com.margin.components.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.margin.components.R;
import com.margin.components.listeners.OnNumSelectedListener;
import com.margin.components.presenters.INumSelectorPresenter;
import com.margin.components.presenters.NumSelectorPresenter;
import com.margin.components.views.INumSelectorView;

/**
 * Created on Feb 22, 2016.
 *
 * @author Marta.Ginosyan
 */
public class NumSelectorFragment extends Fragment implements INumSelectorView {

    private static final String DEFAULT_VALUE = "default_value";
    private static final String MAX_VALUE = "max_value";
    private static final String MIN_VALUE = "min_value";
    private static final String MESSAGE_TEXT = "message_text";
    private INumSelectorPresenter mNumSelectorPresenter;
    private OnNumSelectedListener mOnNumSelectedListener;
    private int mDefaultValue = 0;
    private int mMaxValue = 100;
    private int mMinValue = 0;
    private String mMessageText;

    private NumberPicker mNumberPicker;

    /**
     * Create a new instance of NumSelectorFragment, initialized to show the number selector
     * widget with values and messageText
     */
    public static NumSelectorFragment newInstance(int defaultValue, int maxValue, int minValue,
                                                  String messageText) {
        Bundle arguments = new Bundle();
        arguments.putInt(DEFAULT_VALUE, defaultValue);
        arguments.putInt(MAX_VALUE, maxValue);
        arguments.putInt(MIN_VALUE, minValue);
        arguments.putString(MESSAGE_TEXT, messageText);
        NumSelectorFragment fragment = new NumSelectorFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnNumSelectedListener = (OnNumSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement OnNumSelectedListener!");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDefaultValue = getArguments().getInt(DEFAULT_VALUE);
            mMaxValue = getArguments().getInt(MAX_VALUE);
            mMinValue = getArguments().getInt(MIN_VALUE);
            mMessageText = getArguments().getString(MESSAGE_TEXT);
            if (mMinValue > mDefaultValue || mMinValue > mMaxValue || mDefaultValue > mMaxValue) {
                throw new IllegalArgumentException("Incorrect input values! Values should be: " +
                        "minValue <= defaultValue <= maxValue!");
            }
        } else {
            mMessageText = getString(R.string.info_message_pieces);
        }
        mNumSelectorPresenter = new NumSelectorPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_num_selector, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mNumberPicker.setMinValue(mMinValue);
        mNumberPicker.setMaxValue(mMaxValue);
        mNumberPicker.setValue(mDefaultValue);
        mNumberPicker.setWrapSelectorWheel(false);
        View applyButton = view.findViewById(R.id.apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumSelectorPresenter.onApplyButtonPressed(mNumberPicker.getValue());
            }
        });
        View cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumSelectorPresenter.onCancelButtonPressed();
            }
        });
        View plusButton = view.findViewById(R.id.plus_button);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumSelectorPresenter.onPlusButtonPressed();
            }
        });
        View minusButton = view.findViewById(R.id.minus_button);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumSelectorPresenter.onMinusButtonPressed();
            }
        });
        ((TextView) view.findViewById(R.id.message_text)).setText(mMessageText);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // We should set all listeners in null to avoid memory leaks
        mOnNumSelectedListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNumSelectorPresenter.onDestroy();
    }

    @Override
    public void closeFragment() {
        getActivity().onBackPressed();
    }

    @Override
    public void applyNumber(int number) {
        if (mOnNumSelectedListener != null) {
            mOnNumSelectedListener.onNumSelected(number);
        }
        closeFragment();
    }

    @Override
    public void incrementNumber() {
        mNumberPicker.setValue(mNumberPicker.getValue() + 1);
    }

    @Override
    public void decrementNumber() {
        mNumberPicker.setValue(mNumberPicker.getValue() - 1);
    }
}
