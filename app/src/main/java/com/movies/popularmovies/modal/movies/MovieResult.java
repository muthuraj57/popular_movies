package com.movies.popularmovies.modal.movies;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muthu-3955 on 08/10/16.
 */
public class MovieResult {

    private static MovieResult instance = new MovieResult();

    private MovieResult() {
    }

    ;

    public static MovieResult getInstance() {
        if (instance == null) {
            instance = new MovieResult();
        }
        return instance;
    }

    public static MovieResult setInstance(MovieResult movieResult) {

        /*Initialize current instance
        * */
        getInstance();

        /*
        * If already data present, append the incoming data
        * */
        if (instance.results == null || instance.results.isEmpty()/* && instance.page > movieResult.pages*/) {
            instance.results = new ArrayList<>();
        }
        Log.d("MovieData", "setInstance: ");
        for (Result result : movieResult.getResults()) {
            if (!instance.results.contains(result)){
                instance.results.add(result);
            }
        }
        //instance.results.addAll(movieResult.getResults());

        /*
        * Update page number, total pages and total results
        * */
        instance.page = movieResult.page;
        instance.total_pages = movieResult.total_pages;
        instance.total_results = movieResult.total_results;
        return instance;
    }

    private int page;
    private int total_results;
    private int total_pages;
    private List<Result> results;

    public int getPage() {
        return page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public List<Result> getResults() {
        return results;
    }



}
