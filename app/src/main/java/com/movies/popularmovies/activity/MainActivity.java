package com.movies.popularmovies.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.movies.popularmovies.R;
import com.movies.popularmovies.adapter.MovieBackdropAdapter;
import com.movies.popularmovies.databinding.ActivityMainBinding;
import com.movies.popularmovies.modal.movies.MovieData;
import com.movies.popularmovies.modal.movies.MovieResult;
import com.movies.popularmovies.util.GeneralUtils;
import com.movies.popularmovies.util.GenerateUrl;
import com.movies.popularmovies.util.RequestProcessor;
import com.movies.popularmovies.util.RequestProcessorListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding activityMain = DataBindingUtil.setContentView(this, R.layout.activity_main);

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
                activityMain.recyclerView.setAdapter(new MovieBackdropAdapter(MainActivity.this));
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
}
