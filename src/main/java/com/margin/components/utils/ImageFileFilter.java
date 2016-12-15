package com.margin.components.utils;

import java.io.File;
import java.io.FileFilter;

/**
 * Filters directory for .jpg and .png files.
 * <p>
 * Created on 3/25/2016.
 *
 * @author Marta.Ginosyan
 */
public class ImageFileFilter implements FileFilter {

    private static ImageFileFilter sImageFileFilter;

    private ImageFileFilter() {
    }

    public static ImageFileFilter getInstance() {
        if (null == sImageFileFilter) sImageFileFilter = new ImageFileFilter();
        return sImageFileFilter;
    }

    @Override
    public boolean accept(File file) {
        return file.isDirectory() || isImageFile(file.getAbsolutePath());
    }

    private boolean isImageFile(String filePath) {
        return filePath.endsWith(".jpg") || filePath.endsWith(".png");
    }
}