package com.margin.components.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.margin.components.R;
import com.margin.components.fragments.ListFragment.OnItemClickListener;
import com.margin.components.models.IListItem;

import java.util.List;

/**
 * Created on Feb 29, 2016.
 *
 * @author Marta.Ginosyan
 */
public class ListItemAdapter<T extends IListItem> extends
        RecyclerView.Adapter<ListItemAdapter.ItemHolder> {

    private List<T> mItems;

    private OnItemClickListener<T> mOnItemClickListener;

    private int mLayoutResourceId = -1;

    /**
     * Create ListItemAdapter with list of items and onItemClickListener
     */
    public ListItemAdapter(List<T> items, OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        mItems = items;
    }

    /**
     * Create ListItemAdapter with list of items, onItemClickListener and
     * layoutResourceId.
     */
    public ListItemAdapter(List<T> items, OnItemClickListener<T> onItemClickListener,
                           int layoutResourceId) {
        this(items, onItemClickListener);
        mLayoutResourceId = layoutResourceId;
    }

    @Override
    public ListItemAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(mLayoutResourceId != -1 ? mLayoutResourceId :
                        R.layout.list_component_item, parent, false);
        return new ListItemAdapter.ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ListItemAdapter.ItemHolder holder, int position) {
        if (holder.title != null) {
            holder.title.setText(mItems.get(position).getTitle());
        }
        if (holder.subtext != null) {
            holder.subtext.setText(mItems.get(position).getSubtext());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView subtext;

        public ItemHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            subtext = (TextView) itemView.findViewById(R.id.subtext);
            View itemLayout = itemView.findViewById(R.id.item_layout);
            if (itemLayout != null) {
                itemLayout.setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (mOnItemClickListener != null) {
                                    mOnItemClickListener.onItemClick(mItems
                                            .get(ItemHolder.this.getAdapterPosition()));
                                }
                            }
                        });
            }
        }
    }
}