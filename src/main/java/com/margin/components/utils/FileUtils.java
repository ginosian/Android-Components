package com.margin.components.utils;

import android.text.TextUtils;

import java.io.File;

/**
 * A utility class for manipulating with {@link File}s.
 * <p>
 * Created on April 01, 2016.
 *
 * @author Marta.Ginosyan
 */
public class FileUtils {

    /**
     * Recursively removes directory and it's child directories.
     *
     * @param fileOrDirectory File or directory to be removed from file system.
     * @return boolean, indicating whether operation succeeded gracefully.
     */
    public static boolean deleteDirectory(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteDirectory(child);

        return fileOrDirectory.delete();
    }

    /**
     * Recursively removes directory and it's child directories.
     *
     * @param fileOrDirectory File or directory to be removed from file system.
     * @return boolean, indicating whether operation succeeded gracefully.
     */
    public static boolean deleteDirectory(String fileOrDirectory) {
        return !TextUtils.isEmpty(fileOrDirectory) && deleteDirectory(new File(fileOrDirectory));
    }
}
