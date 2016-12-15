package com.margin.components.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.margin.components.R;
import com.margin.components.adapters.ListItemAdapter;
import com.margin.components.models.IListItem;
import com.margin.components.views.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created on Feb 29, 2016.
 *
 * @author Marta.Ginosyan
 */
public class ListFragment<T extends IListItem> extends Fragment {

    private static final String TAG = ListFragment.class.getSimpleName();

    private static final String ITEMS = "items";
    private static final String LAYOUT_RESOURCE_ID = "layout_resource_id";

    private List<T> mItems;

    private OnItemClickListener<T> mOnItemClickListener;

    private RecyclerView.Adapter mAdapter;

    private int mLayoutResourceId = -1;

    /**
     * Create a new instance of ListFragment, initialized to use list of items 'items'.
     */
    public static <T extends IListItem> ListFragment<T> newInstance(Collection<T> items) {
        return newInstance(items, -1);
    }

    /**
     * Create a new instance of ListFragment, initialized to use list of items 'items',
     * and use layoutResourceId for the list item layout 'layoutResourceId'.
     * The layout <b>must</b> contain:
     * <pre>
     * ...
     * &lt;ViewGroup
     *     android:id="@+id/item_layout"
     *     .../&gt;
     *      ...
     *      &lt;TextView
     *          android:id="@+id/title"
     *          .../&gt;
     *      ...
     *      &lt;TextView
     *          android:id="@+id/subtext"
     *          .../&gt;
     *      ...
     * &lt;/ViewGroup&gt;
     * ...
     * </pre>
     */
    public static <T extends IListItem> ListFragment<T> newInstance(Collection<T> items,
                                                                    int layoutResourceId) {
        ListFragment<T> fragment = new ListFragment<>();
        fragment.setArguments(createArguments(items, layoutResourceId));
        return fragment;
    }

    /**
     * Create common arguments for all list fragments.
     */
    protected static <T extends IListItem> Bundle createArguments(Collection<T> items,
                                                                  int layoutResourceId) {
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(ITEMS, new ArrayList<>(items));
        if (layoutResourceId != -1) {
            arguments.putInt(LAYOUT_RESOURCE_ID, layoutResourceId);
        }
        return arguments;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            if (context instanceof OnItemClickListener) {
                mOnItemClickListener = (OnItemClickListener) context;
            } else {
                mOnItemClickListener = (OnItemClickListener) getParentFragment();
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Parent must implement OnItemClickListener<T>!");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItems = new ArrayList<>();
        if (getArguments() != null) {
            ArrayList<Parcelable> items = getArguments().getParcelableArrayList(ITEMS);
            if (items != null) {
                for (Parcelable item : items) {
                    mItems.add((T) item);
                }
            }
            if (getArguments().containsKey(LAYOUT_RESOURCE_ID)) {
                mLayoutResourceId = getArguments().getInt(LAYOUT_RESOURCE_ID);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_component, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnItemClickListener = null;
    }

    private void initRecyclerView(View root) {
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.list_view);
        setAdapter(recyclerView, mItems, mOnItemClickListener, mLayoutResourceId);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
    }

    /**
     * Refresh recyclerView after data was changed
     */
    public void updateList() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Override and set custom adapter here
     */
    protected void setAdapter(RecyclerView recyclerView, List<T> items,
                              OnItemClickListener<T> onItemClickListener, int layoutResourceId) {
        mAdapter = new ListItemAdapter<>(items, onItemClickListener, layoutResourceId);
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * This interface must be implemented by parent that contain this
     * fragment to add action on item clicked event.
     */
    public interface OnItemClickListener<T> {

        /**
         * Callback to parent, when user pressed item in list
         */
        void onItemClick(T item);
    }
}
