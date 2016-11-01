/* $Id$ */
package com.movies.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by muthu-3955 on 14/10/16.
 */
public class PreferenceUtil {
    private static final String FILE_NAME = "Movie data";
    private static final String KEY = "Data";
    private static final String POPULAR = "Popular";
    private static final String TOP_RATED = "Top_rated";

    public static void storeData(Context context, String response) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY, response);
        editor.apply();
    }

    public static String getData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        if (sharedPreferences.contains(KEY)) {
            return sharedPreferences.getString(KEY, null);
        }
        return null;
    }

    public static void storePopularData(Context context, String response) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString(POPULAR, response);
        editor.apply();
    }

    public static String getPopularData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        if (sharedPreferences.contains(POPULAR)) {
            return sharedPreferences.getString(POPULAR, null);
        }
        return null;
    }

    public static void storeTopRatedData(Context context, String response) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString(TOP_RATED, response);
        editor.apply();
    }

    public static String getTopRatedData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        if (sharedPreferences.contains(TOP_RATED)) {
            return sharedPreferences.getString(TOP_RATED, null);
        }
        return null;
    }
}
