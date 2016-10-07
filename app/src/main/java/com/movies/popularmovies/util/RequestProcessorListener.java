/* $Id$ */
package com.movies.popularmovies.util;

import com.android.volley.VolleyError;

/**
 * Created by muthu-3955 on 08/10/16.
 */
public interface RequestProcessorListener {

    void onSuccess(String response);

    void onLoading();

    void onError(VolleyError error);

    abstract class SimpleRequestProcessor implements RequestProcessorListener {
        @Override
        public void onError(VolleyError error) {
            //some error occurred
        }

    }
}
