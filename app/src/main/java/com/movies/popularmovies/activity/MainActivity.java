package com.movies.popularmovies.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.movies.popularmovies.R;
import com.movies.popularmovies.adapter.MovieAdapter;
import com.movies.popularmovies.databinding.ActivityMainBinding;
import com.movies.popularmovies.modal.movies.MovieData;
import com.movies.popularmovies.modal.movies.MovieResult;
import com.movies.popularmovies.util.GeneralUtils;
import com.movies.popularmovies.util.GenerateUrl;
import com.movies.popularmovies.util.RequestProcessor;
import com.movies.popularmovies.util.RequestProcessorListener;

public class MainActivity extends AppCompatActivity {

    MovieAdapter movieAdapter;
    private int viewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding activityMain = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewType = MovieAdapter.GRID;
        setSupportActionBar(activityMain.toolbar);
        if (!GeneralUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }
        RequestProcessor requestProcessor = new RequestProcessor(this, Request.Method.GET);
        requestProcessor.setListener(new RequestProcessorListener() {
            @Override
            public void onSuccess(String response) {
                MovieResult movieResult = new Gson().fromJson(response, MovieResult.class);
                MovieData.getInstance().addMovieResult(movieResult);
                movieAdapter = new MovieAdapter(MainActivity.this, MovieAdapter.LIST);
                activityMain.recyclerView.setAdapter(movieAdapter);
                activityMain.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
        requestProcessor.execute(GenerateUrl.getDiscoverMovieUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewType == MovieAdapter.LIST){
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
            switch (item.getItemId()){
                case R.id.grid_view:
                    movieAdapter.setViewType(MovieAdapter.GRID);
                    invalidateMenu(MovieAdapter.LIST);
                    return true;
                case R.id.list_view:
                    movieAdapter.setViewType(MovieAdapter.LIST);
                    invalidateMenu(MovieAdapter.GRID);
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void invalidateMenu(int viewType){
        if (this.viewType != viewType){
            this.viewType = viewType;
            invalidateOptionsMenu();
        }
    }
}
