package com.margin.components.interfaces;

/**
 * Created on Mar 30, 2016.
 *
 * @author Marta.Ginosyan
 */
public interface INavigable {

    /**
     * @return True if item can move to the next page, False otherwise
     */
    boolean canMoveNext();

    /**
     * @return True if item can move to the previous page, False otherwise
     */
    boolean canMovePrev();
}
