/* $Id$ */
package com.movies.popularmovies.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.movies.popularmovies.R;
import com.movies.popularmovies.databinding.MovieOverviewBackdropBinding;
import com.movies.popularmovies.databinding.MovieOverviewPostcardBinding;
import com.movies.popularmovies.modal.movies.MovieData;
import com.movies.popularmovies.modal.movies.MovieResult;
import com.movies.popularmovies.modal.movies.Result;
import com.movies.popularmovies.util.GenerateUrl;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by muthu-3955 on 08/10/16.
 */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int GRID = 0;
    public static final int LIST = 1;
    private int viewType;
    private Context context;
    private List<Result> results;
    private LayoutInflater inflater;

    public MovieAdapter(Context context, int viewType) {
        this.context = context;
        this.viewType = viewType;

        inflater = LayoutInflater.from(context);
        List<MovieResult> movieResults = MovieData.getInstance().getMovieResults();
        if (movieResults != null && !movieResults.isEmpty()) {
            results = movieResults.get(0).getResults();
        }
    }

    public void setViewType(int viewType) {
        if (this.viewType != viewType) {
            this.viewType = viewType;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    public int getViewType() {
        return viewType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LIST:
                return new MovieHolder((MovieOverviewBackdropBinding) DataBindingUtil.inflate(inflater, R.layout.movie_overview_backdrop,
                        parent, false));
            case GRID:
                return new MoviePostCardHolder((MovieOverviewPostcardBinding) DataBindingUtil.inflate(inflater, R.layout.movie_overview_postcard,
                        parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Result result = results.get(position);

        if (holder instanceof MovieHolder) {
            MovieOverviewBackdropBinding movieOverview = ((MovieHolder) holder).movieOverview;
            movieOverview.movieName.setText(result.getTitle());
            Picasso.with(context)
                    .load(GenerateUrl.getBackdropImageUrl(result.getBackdrop_path()))
                    .into(movieOverview.coverImage);
            movieOverview.rating.setText(Float.toString(result.getVote_average()));
            movieOverview.year.setText(result.getRelease_date().substring(0, 4));
        } else if (holder instanceof MoviePostCardHolder) {
            MovieOverviewPostcardBinding movieOverview = ((MoviePostCardHolder) holder).movieOverview;
            Picasso.with(context)
                    .load(GenerateUrl.getBackdropImageUrl(result.getPoster_path()))
                    .into(movieOverview.coverImage1);
            Picasso.with(context)
                    .load(GenerateUrl.getBackdropImageUrl(result.getPoster_path()))
                    .into(movieOverview.coverImage2);
            movieOverview.movieName1.setText(result.getTitle());
            movieOverview.movieName2.setText(result.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return results == null ? 0 : results.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {
        MovieOverviewBackdropBinding movieOverview;

        public MovieHolder(MovieOverviewBackdropBinding movieOverview) {
            super(movieOverview.getRoot());
            this.movieOverview = movieOverview;
        }
    }

    public class MoviePostCardHolder extends RecyclerView.ViewHolder {
        MovieOverviewPostcardBinding movieOverview;

        public MoviePostCardHolder(MovieOverviewPostcardBinding movieOverview) {
            super(movieOverview.getRoot());
            this.movieOverview = movieOverview;
        }
    }
}
