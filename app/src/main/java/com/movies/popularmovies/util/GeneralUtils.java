/* $Id$ */
package com.movies.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by muthu-3955 on 08/10/16.
 */
public class GeneralUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
