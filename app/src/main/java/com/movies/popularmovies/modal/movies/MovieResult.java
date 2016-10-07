package com.movies.popularmovies.modal.movies;

import java.util.List;

/**
 * Created by muthu-3955 on 08/10/16.
 */
public class MovieResult {

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
