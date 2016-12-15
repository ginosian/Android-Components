package com.margin.components.models;

import android.os.Parcelable;

/**
 * Created on Feb 29, 2016.
 *
 * @author Marta.Ginosyan
 */
public interface IListItem extends Parcelable {

    /**
     * This parameter will be used in list item to show the title
     *
     * @return the title of the list item
     */
    String getTitle();

    /**
     * This parameter will be used in list item to show the subtext
     *
     * @return the subtext of the list item
     */
    String getSubtext();
}
