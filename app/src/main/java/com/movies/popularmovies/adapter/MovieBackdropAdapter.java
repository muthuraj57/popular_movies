/* $Id$ */
package com.movies.popularmovies.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.movies.popularmovies.R;
import com.movies.popularmovies.databinding.MovieOverviewBackdropBinding;
import com.movies.popularmovies.modal.movies.MovieData;
import com.movies.popularmovies.modal.movies.MovieResult;
import com.movies.popularmovies.modal.movies.Result;
import com.movies.popularmovies.util.GenerateUrl;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by muthu-3955 on 08/10/16.
 */
public class MovieBackdropAdapter extends RecyclerView.Adapter<MovieBackdropAdapter.MovieHolder>{

    private Context context;
    private List<Result> results;
    private LayoutInflater inflater;

    public MovieBackdropAdapter(Context context) {
        this.context = context;

        inflater = LayoutInflater.from(context);
        List<MovieResult> movieResults = MovieData.getInstance().getMovieResults();
        if (movieResults != null && !movieResults.isEmpty()) {
            results = movieResults.get(0).getResults();
        }
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieHolder((MovieOverviewBackdropBinding) DataBindingUtil.inflate(inflater, R.layout.movie_overview_backdrop,
                parent, false));
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Result result = results.get(position);
        holder.movieOverview.movieName.setText(result.getTitle());
        Picasso.with(context)
                .load(GenerateUrl.getBackdropImageUrl(result.getBackdrop_path()))
                .into(holder.movieOverview.coverImage);
        holder.movieOverview.rating.setText(Float.toString(result.getVote_average()));
        holder.movieOverview.year.setText(result.getRelease_date().substring(0, 4));
    }

    @Override
    public int getItemCount() {
        return results == null ? 0 : results.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder{
        MovieOverviewBackdropBinding movieOverview;
        public MovieHolder(MovieOverviewBackdropBinding movieOverview) {
            super(movieOverview.getRoot());
            this.movieOverview = movieOverview;
        }
    }
}
