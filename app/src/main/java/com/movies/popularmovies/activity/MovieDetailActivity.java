/* $Id$ */
package com.movies.popularmovies.activity;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;

import com.movies.popularmovies.R;
import com.movies.popularmovies.databinding.MovieDetailBinding;
import com.movies.popularmovies.modal.movies.MovieResult;
import com.movies.popularmovies.modal.movies.Result;
import com.movies.popularmovies.util.GenerateUrl;
import com.squareup.picasso.Picasso;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition));
        }

        result = MovieResult.getInstance().getResults().get(index);

        Picasso.with(this)
                .load(GenerateUrl.getBackdropImageUrl(result.getBackdrop_path()))
                .into(movieDetail.movieImage);
    }
}
