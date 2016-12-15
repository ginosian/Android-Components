package com.margin.components.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.margin.components.R;
import com.margin.components.interfaces.INavigable;
import com.margin.components.views.StepperViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Mar 29, 2016.
 *
 * @author Marta.Ginosyan
 */
public abstract class StepperFragment extends Fragment {

    private StepperViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private ImageView[] mDots;
    private ViewGroup mDotsContainer;
    private View mBackButton;
    private View mNextButton;
    private View mNextButtonArrow;
    private TextView mNextButtonText;

    /**
     * Create custom implementation of StepperPagerAdapter.
     * All fragments inside adapter must implement {@link INavigable}
     */
    protected abstract StepperPagerAdapter getPagerAdapter();

    /**
     * Do actions when another page was selected
     */
    protected abstract void onPageChanged(int position);

    /**
     * Invokes only if we reached the last {@link INavigable} fragment
     * and it can move next
     */
    protected abstract void onDonePressed();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stepper, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (StepperViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = getPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new PageChangeListener());
        mViewPager.setPagingEnabled(false); //disable touch events
        mBackButton = view.findViewById(R.id.button_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() > 0) {
                    INavigable fragment = getNavigableFragment(mViewPager.getCurrentItem());
                    if (fragment.canMovePrev()) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                    }
                }
            }
        });
        mNextButton = view.findViewById(R.id.button_next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() < mPagerAdapter.getCount()) {
                    INavigable fragment = getNavigableFragment(mViewPager.getCurrentItem());
                    if (fragment.canMoveNext()) {
                        if (mViewPager.getCurrentItem() == mPagerAdapter.getCount() - 1) {
                            onDonePressed();
                        } else {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    }
                }
            }
        });
        mNextButtonText = (TextView) mNextButton.findViewById(R.id.text);
        mNextButtonArrow = mNextButton.findViewById(R.id.arrow);
        mDotsContainer = (ViewGroup) view.findViewById(R.id.dots_container);
        prepareDotsIndicators();
        updateNextButton(mViewPager.getCurrentItem());
    }

    /**
     * Check if current fragment implements {@link INavigable}
     *
     * @return navigable fragment from adapter with selected position
     */
    private INavigable getNavigableFragment(int position) {
        try {
            return (INavigable) mPagerAdapter.instantiateItem(mViewPager, position);
        } catch (ClassCastException e) {
            throw new ClassCastException("All fragments inside viewPager" +
                    " must implement INavigable!");
        }
    }

    /**
     * Prepare and show all dots view indicators
     */
    private void prepareDotsIndicators() {
        mDots = new ImageView[mPagerAdapter.getCount()];
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new ImageView(getContext());
            mDots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable
                    .stepper_item_unselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            int margin = (int) getResources().getDimension(R.dimen.spacing_tiny);

            params.setMargins(margin, 0, margin, 0);

            mDotsContainer.addView(mDots[i], params);
        }

        if (mDots.length > 0) {
            mDots[mViewPager.getCurrentItem()].setImageDrawable(ContextCompat.getDrawable(
                    getContext(), R.drawable.stepper_item_selected));
        }
    }

    /**
     * Update Back button with selected position
     */
    private void updateBackButton(int position) {
        if (position == 0) {
            mBackButton.setVisibility(View.GONE);
        } else {
            mBackButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Update Next button with selected position
     */
    private void updateNextButton(int position) {
        if (position == mDots.length - 1) {
            mNextButtonText.setText(getString(R.string.button_name_done));
        } else {
            mNextButtonText.setText(getString(R.string.button_name_next));
        }
    }

    /**
     * Make necessary updates when fragments have been added
     */
    private void onAddFragmentsAction() {
        mPagerAdapter.notifyDataSetChanged();
        mDotsContainer.removeAllViews();
        prepareDotsIndicators();
        updateBackButton(mViewPager.getCurrentItem());
        updateNextButton(mViewPager.getCurrentItem());
    }

    /**
     * Custom {@link FragmentStatePagerAdapter} implementation,
     * this class keeps fragmentsList inside and update item with this list
     */
    public class StepperPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public StepperPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        /**
         * Add fragment dynamically
         */
        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
            onAddFragmentsAction();
        }

        /**
         * Add fragments dynamically
         */
        public void addFragments(List<Fragment> fragments) {
            mFragmentList.addAll(fragments);
            onAddFragmentsAction();
        }
    }

    /**
     * Callback class for responding to changing state of the selected page.
     */
    private class PageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            updateBackButton(position);
            updateNextButton(position);
            for (ImageView dot : mDots) {
                dot.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable
                        .stepper_item_unselected));
            }
            mDots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable
                    .stepper_item_selected));
            onPageChanged(position);
        }
    }
}
