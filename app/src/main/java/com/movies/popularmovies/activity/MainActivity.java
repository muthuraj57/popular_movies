package com.movies.popularmovies.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.movies.popularmovies.R;
import com.movies.popularmovies.adapter.MovieAdapter;
import com.movies.popularmovies.databinding.ActivityMainBinding;
import com.movies.popularmovies.modal.movies.MovieResult;
import com.movies.popularmovies.util.GenerateUrl;
import com.movies.popularmovies.util.PreferenceUtil;
import com.movies.popularmovies.util.RequestProcessor;
import com.movies.popularmovies.util.RequestProcessorListener;
import com.movies.popularmovies.util.Util;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMain;
    private MovieAdapter movieAdapter;
    private int viewType;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    /*
    * To save scroll position to retain the position on changing view
    * */
    private int gridScrollPosition;
    private int listScrollPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMain = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewType = MovieAdapter.GRID;
        setSupportActionBar(activityMain.toolbar);
        if (!Util.isNetworkAvailable(this) && PreferenceUtil.getData(this) == null) {
            clearLoader();
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2);

        getData();
    }

    private void getData() {
        String movieData = PreferenceUtil.getData(this);
        if (movieData == null) {
            RequestProcessor requestProcessor = new RequestProcessor(this, Request.Method.GET);
            requestProcessor.setListener(new RequestProcessorListener() {
                @Override
                public void onSuccess(String response) {
                    PreferenceUtil.storeData(MainActivity.this, response);
                    setAdapter(response);
                    clearLoader();
                }

                @Override
                public void onLoading() {
                    setLoader();
                }

                @Override
                public void onError(VolleyError error) {

                }
            });
            requestProcessor.execute(GenerateUrl.getDiscoverMovieUrl());
        } else {
            clearLoader();
            setAdapter(movieData);
        }
    }

    private void setAdapter(String response) {
        MovieResult.setInstance(new Gson().fromJson(response, MovieResult.class));
        movieAdapter = new MovieAdapter(MainActivity.this, MovieAdapter.LIST);
        activityMain.recyclerView.setAdapter(movieAdapter);
        activityMain.recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewType == MovieAdapter.LIST) {
            getMenuInflater().inflate(R.menu.list_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.grid_menu, menu);
        }
        //getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (movieAdapter != null) {
            switch (item.getItemId()) {
                case R.id.grid_view:
                    changeView(MovieAdapter.GRID);
                    invalidateMenu(MovieAdapter.LIST);
                    return true;
                case R.id.list_view:
                    changeView(MovieAdapter.LIST);
                    invalidateMenu(MovieAdapter.GRID);
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeView(int viewType) {
        if (movieAdapter.getViewType() != viewType) {
            setLayoutManager(viewType);
        }
    }

    private void setLayoutManager(int viewType) {
        switch (viewType) {
            case MovieAdapter.GRID:

                /*
                * Get the list scroll position and save it
                * */
                listScrollPosition = linearLayoutManager.findFirstVisibleItemPosition();

                /*
                * Change the layout and set view type as grid
                * */
                activityMain.recyclerView.setLayoutManager(gridLayoutManager);
                movieAdapter.setViewType(viewType);
                movieAdapter.notifyDataSetChanged();

                /*
                * If grid position already saved, scroll to that position
                * */
                if (gridScrollPosition - 1 >= 0) {
                    activityMain.recyclerView.scrollToPosition(gridScrollPosition);
                }
                break;
            case MovieAdapter.LIST:

                /*
                * Get the list scroll position and save it
                * */
                gridScrollPosition = gridLayoutManager.findFirstVisibleItemPosition();

                /*
                * Change the layout and set view type as list
                * */
                activityMain.recyclerView.setLayoutManager(linearLayoutManager);
                movieAdapter.setViewType(viewType);
                movieAdapter.notifyDataSetChanged();

                /*
                * If list position already saved, scroll to that position
                * */
                if (listScrollPosition - 1 >= 0) {
                    activityMain.recyclerView.scrollToPosition(listScrollPosition);
                }
                break;
        }
    }

    private void invalidateMenu(int viewType) {
        if (this.viewType != viewType) {
            this.viewType = viewType;
            invalidateOptionsMenu();
        }
    }

    private void setLoader() {
        activityMain.progressBar.setVisibility(View.VISIBLE);
        activityMain.scrollView.setVisibility(View.INVISIBLE);
    }

    private void clearLoader() {
        activityMain.progressBar.setVisibility(View.INVISIBLE);
        activityMain.scrollView.setVisibility(View.VISIBLE);
    }
}
