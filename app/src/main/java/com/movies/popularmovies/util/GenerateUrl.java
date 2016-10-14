/* $Id$ */
package com.movies.popularmovies.util;

import android.net.Uri;
import android.util.Log;

/**
 * Created by muthu-3955 on 07/10/16.
 */
public class GenerateUrl {
    public static final String TAG = "GenerateUrl";
    private static final String API_KEY = "7d0900783de7f8c19a366a3b17bd0083";
    public static final String API_READ_ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3ZDA5MDA3ODNkZTdmOGMxOWEzNjZhM2IxN2JkMDA4MyIsInN1YiI6IjU3ZjdkNmJiYzNhMzY4MjNkMTAwMjU1ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JXN-ttLfk6dc6Rd4_v0EGkvg23xIOu0xatZqHVmaYEs";

    public static final String URL_SCHEME = "https";
    public static final String URL_BASE = "api.themoviedb.org";

    public static String getDiscoverMovieUrl() {
        Uri uri = new Uri.Builder().scheme(URL_SCHEME)
                .encodedAuthority(URL_BASE)
                .appendEncodedPath("3")
                .appendEncodedPath("discover")
                .appendEncodedPath("movie")
                .appendQueryParameter("api_key", API_KEY)
                .build();
        Log.d(TAG, "getDiscoverMovieUrl: " + uri.toString());
        return uri.toString();
    }

    public static String getBackdropImageUrl(String imageUrl) {
        return "http://image.tmdb.org/t/p/w500/" + imageUrl;
    }

    /*
    * Grid image url
    * Set width of image based on display width.
    **/
    public static String getGridItemImageUrl(int screenWidth, String imageUrl) {
        String widthPath = "w92";

        /*
        *  Here, we show two items per row.
        *  Hence we divide screen width by 2 to get single item width
        *  */
        screenWidth /= 2;

        if (screenWidth >= 780) {
            widthPath = "w780";
        } else if (screenWidth >= 500) {
            widthPath = "w500";
        } else if (screenWidth >= 342) {
            widthPath = "w342";
        } else if (screenWidth >= 185) {
            widthPath = "w185";
        } else if (screenWidth >= 154) {
            widthPath = "w154";
        }
        return "http://image.tmdb.org/t/p/" + widthPath + "/" + imageUrl;
    }
}
