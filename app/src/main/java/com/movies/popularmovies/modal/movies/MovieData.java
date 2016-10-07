package com.movies.popularmovies.modal.movies;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muthu-3955 on 08/10/16.
 */
public class MovieData {
    private static MovieData ourInstance = new MovieData();

    public static MovieData getInstance() {
        return ourInstance;
    }

    private MovieData() {
        movieResults = new ArrayList<>();
    }

    private List<MovieResult> movieResults;

    public List<MovieResult> getMovieResults() {
        return movieResults;
    }
    public void addMovieResult(MovieResult movieResult){
        movieResults.add(movieResult);
    }
}
