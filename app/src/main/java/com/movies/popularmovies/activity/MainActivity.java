package com.movies.popularmovies.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.movies.popularmovies.R;
import com.movies.popularmovies.adapter.MovieAdapter;
import com.movies.popularmovies.databinding.ActivityMainBinding;
import com.movies.popularmovies.modal.movies.MovieResult;
import com.movies.popularmovies.util.EndlessRecyclerViewScrollListener;
import com.movies.popularmovies.util.GenerateUrl;
import com.movies.popularmovies.util.PreferenceUtil;
import com.movies.popularmovies.util.RequestProcessor;
import com.movies.popularmovies.util.RequestProcessorListener;
import com.movies.popularmovies.util.Util;

public class MainActivity extends AppCompatActivity {

    private int curFilter = GenerateUrl.DISCOVER;
    private ActivityMainBinding activityMain;
    private MovieAdapter movieAdapter;
    private int menuViewType;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private PopupMenu menu;

    private RequestProcessor requestProcessor;

    private static final String TAG = "MovieData";

    /*
    * To save scroll position to retain the position on changing view
    * */
    private int gridScrollPosition;
    private int listScrollPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMain = DataBindingUtil.setContentView(this, R.layout.activity_main);

        menuViewType = MovieAdapter.GRID;
        setSupportActionBar(activityMain.toolbar);
        if (!Util.isNetworkAvailable(this) && PreferenceUtil.getData(this) == null) {
            clearLoader();
            Snackbar.make(activityMain.getRoot(), MainActivity.this.getString(R.string.no_internet),
                    Snackbar.LENGTH_LONG).show();
            return;
        }
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2);
        requestProcessor = new RequestProcessor(this, Request.Method.GET);

        setScrollListener();
        getData();

        activityMain.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                /*
                * If no network available, show error and return
                * */
                if (!Util.isNetworkAvailable(MainActivity.this) && PreferenceUtil.getData(MainActivity.this) == null) {
                    activityMain.swipeToRefresh.setRefreshing(false);
                    Snackbar.make(activityMain.getRoot(), MainActivity.this.getString(R.string.no_internet),
                            Snackbar.LENGTH_LONG).show();
                    return;
                }

                /*
                * If network is available, refresh the data and stop the refresh animation
                * */
                refreshData();
                activityMain.swipeToRefresh.setRefreshing(false);
            }
        });
    }

    private void refreshData() {
        requestProcessor.setListener(new RequestProcessorListener() {
            @Override
            public void onSuccess(String response) {
                switch (curFilter){
                    case GenerateUrl.DISCOVER:
                        PreferenceUtil.storeData(MainActivity.this, response);
                        break;
                    case GenerateUrl.TOP_RATED:
                        PreferenceUtil.storeTopRatedData(MainActivity.this, response);
                        break;
                    case GenerateUrl.MOST_POPULAR:
                        PreferenceUtil.storePopularData(MainActivity.this, response);
                        break;
                }
                /*
                * If adapter is null, it should be initialized
                * */
                if (movieAdapter == null) {
                    setAdapter(response);
                }

                /*
                * If adapter already present, it is now reloaded, so notify the adapter
                * */
                else {
                    MovieResult.setInstance(new Gson().fromJson(response, MovieResult.class));
                    movieAdapter.notifyDataChanged();
                }
                clearLoader();
            }

            @Override
            public void onLoading() {
                setLoader();
            }

            @Override
            public void onError(VolleyError error) {
                Snackbar.make(activityMain.getRoot(), MainActivity.this.getString(R.string.something_wrong),
                        Snackbar.LENGTH_LONG).show();
            }
        });
        requestProcessor.execute(GenerateUrl.getDiscoverMovieUrl(curFilter));
    }

    private void setScrollListener() {
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public int getFooterViewType(int defaultNoFooterViewType) {
                return -1;
            }

            @Override
            public void onLoadMore(final int page, final int totalItemsCount) {
                if (movieAdapter.getItemCount() >= page * 20) {
                    return;
                }

                /*
                * If all the pages are loaded, show message and return
                * */
                if (page > MovieResult.getInstance().getTotal_pages()) {
                    Snackbar.make(activityMain.getRoot(), MainActivity.this.getString(R.string.all_loaded),
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                requestProcessor.setListener(new RequestProcessorListener() {
                    @Override
                    public void onSuccess(String response) {
                        if (movieAdapter.getItemCount() >= page * 20) {
                            return;
                        }
                        MovieResult.addResults(new Gson().fromJson(response, MovieResult.class));
                        PreferenceUtil.storeData(MainActivity.this, new Gson().toJson(MovieResult.getInstance()));
                    }

                    @Override
                    public void onLoading() {
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
                requestProcessor.execute(GenerateUrl.getDiscoverMovieUrl(page, curFilter));
            }
        };
    }

    private void getData() {
        String movieData = PreferenceUtil.getData(this);
        if (movieData == null) {
            refreshData();
        } else {
            clearLoader();
            setAdapter(movieData);
        }
    }

    private void setAdapter(String response) {
        MovieResult.setInstance(new Gson().fromJson(response, MovieResult.class));
        movieAdapter = new MovieAdapter(MainActivity.this, MovieAdapter.LIST, activityMain.recyclerView);
        activityMain.recyclerView.setAdapter(movieAdapter);
        activityMain.recyclerView.setLayoutManager(linearLayoutManager);
        activityMain.recyclerView.clearOnScrollListeners();
        activityMain.recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuViewType == MovieAdapter.LIST) {
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
                    scrollListener.changeLayoutManager(gridLayoutManager);
                    return true;
                case R.id.list_view:
                    changeView(MovieAdapter.LIST);
                    invalidateMenu(MovieAdapter.GRID);
                    scrollListener.changeLayoutManager(linearLayoutManager);
                    return true;
                case R.id.sort:
                    if (menu == null) {
                        menu = new PopupMenu(this, findViewById(R.id.sort));
                        menu.inflate(R.menu.sort_popup);
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.most_popular:
                                        curFilter = GenerateUrl.MOST_POPULAR;
                                        refreshData();
                                        break;
                                    case R.id.top_rated:
                                        curFilter = GenerateUrl.TOP_RATED;
                                        refreshData();
                                        break;
                                }
                                return true;
                            }
                        });
                    }
                    menu.show();
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
        if (this.menuViewType != viewType) {
            this.menuViewType = viewType;
            invalidateOptionsMenu();
        }
    }

    private void setLoader() {
        activityMain.progressBar.setVisibility(View.VISIBLE);
        activityMain.recyclerView.setVisibility(View.INVISIBLE);
    }

    private void clearLoader() {
        activityMain.progressBar.setVisibility(View.INVISIBLE);
        activityMain.recyclerView.setVisibility(View.VISIBLE);
    }
}
