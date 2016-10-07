/* $Id$ */
package com.movies.popularmovies.modal.movies;

import java.util.List;

/**
 * Created by muthu-3955 on 08/10/16.
 */
public class Result {

    /*
    * poster_path: "/xiQKxPbn3LyVqI3cBtdyp4XIdLx.jpg",
adult: false,
overview: "Anna is a young costume designer, focused on her job and wary of getting caught in romantic relationships. She has just found a new apartment, and is tempted to let her latest boyfriend, Frank, move in with her. Instead, she finds a tenant: The flamboyant, fun-loving Camilla, who shares Anna’s views on love and commitment. For both of them, it’s all about fun.",
release_date: "2005-11-24",
genre_ids: [
35,
18,
10749
],
id: 16048,
original_title: "All About Anna",
original_language: "en",
title: "All About Anna",
backdrop_path: "/g8Zl2fGVvF6O8UGUAd85Eoh3EFY.jpg",
popularity: 6.659356,
vote_count: 25,
video: false,
vote_average: 2.42
*/
    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private List<Integer> genre_ids;
    private String id;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private double popularity;
    private int vote_count;
    private boolean video;
    private float vote_average;

    public String getPoster_path() {
        return poster_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public String getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public float getVote_average() {
        return vote_average;
    }
}
