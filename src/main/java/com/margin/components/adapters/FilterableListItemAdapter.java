package com.margin.components.adapters;

import com.margin.components.fragments.ListFragment.OnItemClickListener;
import com.margin.components.models.IListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Mar 23, 2016.
 *
 * @author Marta.Ginosyan
 */
public class FilterableListItemAdapter<T extends IListItem> extends ListItemAdapter<T> {

    private List<T> mItems;

    /**
     * Create FilterableListItemAdapter with list of items and onItemClickListener
     */
    public FilterableListItemAdapter(List<T> items, OnItemClickListener<T> onItemClickListener) {
        super(items, onItemClickListener);
        mItems = items;
    }

    /**
     * Create FilterableListItemAdapter with list of items, onItemClickListener and
     * layoutResourceId.
     */
    public FilterableListItemAdapter(List<T> items, OnItemClickListener<T> onItemClickListener,
                                     int layoutResourceId) {
        super(items, onItemClickListener, layoutResourceId);
        mItems = items;
    }

    /**
     * Remove item with standard animation
     */
    public T removeItem(int position) {
        T item = mItems.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    /**
     * Add item with standard animation
     */
    public void addItem(int position, T item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * Move item with standard animation
     */
    public void moveItem(int fromPosition, int toPosition) {
        T item = mItems.remove(fromPosition);
        mItems.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * Apply all change animations in adapter
     */
    public void animateTo(List<T> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    /**
     * Filter the adapter with 'items' and 'query'
     */
    public List<T> filter(List<T> items, String query) {
        query = query.toLowerCase();
        List<T> filteredItems = new ArrayList<>();
        for (T item : items) {
            String title = item.getTitle().toLowerCase();
            String subText = item.getSubtext().toLowerCase();
            if (title.contains(query) || subText.contains(query)) {
                filteredItems.add(item);
            }
        }
        animateTo(filteredItems);
        return filteredItems;
    }

    /**
     * Apply remove animations
     */
    private void applyAndAnimateRemovals(List<T> newItems) {
        for (int i = mItems.size() - 1; i >= 0; i--) {
            T item = mItems.get(i);
            if (!newItems.contains(item)) {
                removeItem(i);
            }
        }
    }

    /**
     * Apply add animations
     */
    private void applyAndAnimateAdditions(List<T> newItems) {
        for (int i = 0, count = newItems.size(); i < count; i++) {
            T item = newItems.get(i);
            if (!mItems.contains(item)) {
                addItem(i, item);
            }
        }
    }

    /**
     * Apply move animations
     */
    private void applyAndAnimateMovedItems(List<T> newItems) {
        for (int toPosition = newItems.size() - 1; toPosition >= 0; toPosition--) {
            T item = newItems.get(toPosition);
            int fromPosition = newItems.indexOf(item);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
