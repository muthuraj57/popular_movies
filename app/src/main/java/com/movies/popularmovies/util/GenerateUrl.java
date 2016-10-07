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
        return "http://image.tmdb.org/t/p/w342/" + imageUrl;
    }
}
