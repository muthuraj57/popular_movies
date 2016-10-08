/* $Id$ */
package com.movies.popularmovies.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.movies.popularmovies.R;
import com.movies.popularmovies.databinding.MovieDetailBinding;
import com.movies.popularmovies.modal.movies.MovieData;
import com.movies.popularmovies.modal.movies.MovieResult;
import com.movies.popularmovies.modal.movies.Result;
import com.movies.popularmovies.util.GenerateUrl;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by muthu-3955 on 08/10/16.
 */
public class MovieDetailActivity extends AppCompatActivity{

    private MovieDetailBinding movieDetail;
    private Result result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDetail = DataBindingUtil.setContentView(this, R.layout.movie_detail);

        int index = getIntent().getIntExtra("RESULT_KEY", -1);
        if (index == -1){
            return;
        }
        List<MovieResult> movieResults = MovieData.getInstance().getMovieResults();
        if (movieResults == null || movieResults.isEmpty()) {
            return;
        }
        result = movieResults.get(0).getResults().get(index);

        Picasso.with(this)
                .load(GenerateUrl.getBackdropImageUrl(result.getBackdrop_path()))
                .into(movieDetail.movieImage);
    }
}
