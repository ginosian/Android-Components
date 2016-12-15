package com.margin.components.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.margin.components.R;
import com.margin.components.adapters.FilterableListItemAdapter;
import com.margin.components.models.IListItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created on Mar 23, 2016.
 *
 * @author Marta.Ginosyan
 */
public class FilterableListFragment<T extends IListItem> extends ListFragment<T>
        implements SearchView.OnQueryTextListener {

    private List<T> mItems;
    private FilterableListItemAdapter<T> mAdapter;
    private RecyclerView mRecyclerView;

    /**
     * Create a new instance of FilterableListFragment, initialized to
     * show the list of items 'items'.
     */
    public static <T extends IListItem> FilterableListFragment<T> newInstance(Collection<T> items) {
        FilterableListFragment<T> fragment = new FilterableListFragment<>();
        fragment.setArguments(createArguments(items, -1));
        return fragment;
    }

    /**
     * Create a new instance of FilterableListFragment, initialized to
     * show the list of items 'items' and use layoutResourceId for the
     * list item layout 'layoutResourceId'.
     */
    public static <T extends IListItem> FilterableListFragment<T> newInstance(Collection<T> items,
                                                                              int layoutResourceId) {
        FilterableListFragment<T> fragment = new FilterableListFragment<>();
        fragment.setArguments(createArguments(items, layoutResourceId));
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.filterable_list_fragment_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    protected void setAdapter(RecyclerView recyclerView, List<T> items,
                              OnItemClickListener<T> onItemClickListener, int layoutResourceId) {
        mItems = items;
        mRecyclerView = recyclerView;
        mAdapter = new FilterableListItemAdapter<>(new ArrayList<>(items), onItemClickListener,
                layoutResourceId);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.filter(mItems, newText);
        mRecyclerView.scrollToPosition(0);
        return true;
    }
}
