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
}
