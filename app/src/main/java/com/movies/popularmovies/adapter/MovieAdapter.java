/* $Id$ */
package com.movies.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.movies.popularmovies.R;
import com.movies.popularmovies.activity.MovieDetailActivity;
import com.movies.popularmovies.databinding.MovieOverviewGridItemBinding;
import com.movies.popularmovies.databinding.MovieOverviewListItemBinding;
import com.movies.popularmovies.modal.movies.MovieResult;
import com.movies.popularmovies.modal.movies.Result;
import com.movies.popularmovies.util.GenerateUrl;
import com.movies.popularmovies.util.Util;
import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
    private DecimalFormat decimalFormat;
    private int screenWidth;

    public MovieAdapter(Context context, int viewType) {
        this.context = context;
        this.viewType = viewType;

        screenWidth = Util.getDisplayMetrics(context).x;
        decimalFormat = new DecimalFormat("#.#");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        inflater = LayoutInflater.from(context);
        results = MovieResult.getInstance().getResults();
//        List<MovieResult> movieResults = MovieData.getInstance().getMovieResults();
//        if (movieResults != null && !movieResults.isEmpty()) {
//            results = movieResults.get(0).getResults();
//        }
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    public int getViewType() {
        return viewType;
    }

    @Override
    public int getItemCount() {
        return results == null ? 0 : results.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LIST:
                return new MovieListItemHolder((MovieOverviewListItemBinding) DataBindingUtil.inflate(inflater, R.layout.movie_overview_list_item,
                        parent, false));
            case GRID:
                return new MovieGridItemHolder((MovieOverviewGridItemBinding) DataBindingUtil.inflate(inflater, R.layout.movie_overview_grid_item,
                        parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Result result = results.get(position);

        if (holder instanceof MovieListItemHolder) {
            setListItem(((MovieListItemHolder) holder).movieOverview, result);
        } else if (holder instanceof MovieGridItemHolder) {
            setGridItem(((MovieGridItemHolder) holder).movieOverview, result);
        }
    }

    private void setGridItem(final MovieOverviewGridItemBinding movieOverview, Result result) {
        movieOverview.getRoot().getLayoutParams().width = screenWidth / 2;
        openDetailActivityOnClick(movieOverview.getRoot(), result);
        Picasso.with(context)
                .load(GenerateUrl.getGridItemImageUrl(screenWidth, result.getPoster_path()))
                .into(movieOverview.coverImage);
        movieOverview.movieName.setText(result.getTitle());
    }

    private void setListItem(MovieOverviewListItemBinding movieOverview, Result result) {
        openDetailActivityOnClick(movieOverview.getRoot(), result);
        movieOverview.movieName.setText(result.getTitle());
        Picasso.with(context)
                .load(GenerateUrl.getBackdropImageUrl(result.getBackdrop_path()))
                .into(movieOverview.coverImage);

        movieOverview.rating.setText(decimalFormat.format(result.getVote_average()));
        movieOverview.year.setText(result.getRelease_date().substring(0, 4));
    }

    private void openDetailActivityOnClick(final View view, final Result result) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("RESULT_KEY", results.indexOf(result));

                if (DataBindingUtil.getBinding(view) instanceof MovieOverviewListItemBinding) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        MovieOverviewListItemBinding listItemBinding = DataBindingUtil.getBinding(v);
                        Pair<View, String> coverImage = Pair.create((View) listItemBinding.coverImage, listItemBinding.coverImage.getTransitionName());
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, coverImage);
                        context.startActivity(intent, optionsCompat.toBundle());
                    }
                } else {
                    context.startActivity(intent);
                }
            }
        });
    }


    /*
    * View Holders
    * */
    public class MovieListItemHolder extends RecyclerView.ViewHolder {
        MovieOverviewListItemBinding movieOverview;

        public MovieListItemHolder(MovieOverviewListItemBinding movieOverview) {
            super(movieOverview.getRoot());
            this.movieOverview = movieOverview;
        }
    }

    public class MovieGridItemHolder extends RecyclerView.ViewHolder {
        MovieOverviewGridItemBinding movieOverview;

        public MovieGridItemHolder(MovieOverviewGridItemBinding movieOverview) {
            super(movieOverview.getRoot());
            this.movieOverview = movieOverview;
        }
    }
}
