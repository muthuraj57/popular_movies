package com.movies.popularmovies.modal.movies;

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

    public static void setInstance(MovieResult movieResult) {
        getInstance();
        if (instance.results == null || instance.results.isEmpty() && instance.page < movieResult.page) {
            instance.results = new ArrayList<>();
        }
        instance.page = movieResult.page;
        instance.total_pages = movieResult.total_pages;
        instance.total_results = movieResult.total_results;
        instance.results.addAll(movieResult.getResults());
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
