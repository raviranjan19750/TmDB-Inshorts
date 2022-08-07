package com.example.tmdb_isnhorts.View;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tmdb_isnhorts.Adapter.MovieAdapter;
import com.example.tmdb_isnhorts.R;
import com.example.tmdb_isnhorts.Result;
import com.example.tmdb_isnhorts.ViewModel.MovieViewModel;
import com.example.tmdb_isnhorts.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    ActivityMainBinding activityMainBinding;
    MovieViewModel movieViewModel;
    ArrayList<Result> resultArrayList = new ArrayList<>();
    ArrayList<Result> nowPlayingMovieArrayList = new ArrayList<>();
    ArrayList<Result> searchedMovieArrayList = new ArrayList<>();
    ArrayList<Result> offlinemovies = new ArrayList<>();

    RecyclerView popularMovieRecyclerView;
    RecyclerView nowPlayingMovieRecyclerView;
    RecyclerView searchedMovieRecyclerView;
    LinearLayout popularMoviesLinearLayout, nowPlayingMoviesLinearLayout, searchedMoviesLinearLayout;
    TextView popularMoviesTextView;
    MovieAdapter movieAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;
    ConnectivityManager connectivityManager;

    private CompositeDisposable disposables = new CompositeDisposable();
    private long timeSinceLastRequest; // for log printouts only. Not part of logic.


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        searchView = findViewById(R.id.searchView);


        popularMovieRecyclerView = activityMainBinding.popularMovieRecyclerView;
        popularMoviesLinearLayout = activityMainBinding.popularMoviesLinearLayout;

        nowPlayingMovieRecyclerView = activityMainBinding.nowPlayingMovieRecyclerView;
        nowPlayingMoviesLinearLayout = activityMainBinding.nowPlayingMoviesLinearLayout;

        searchedMovieRecyclerView = activityMainBinding.searchedMovieRecyclerView;
        searchedMoviesLinearLayout = activityMainBinding.searchedMoviesLinearLayout;

        timeSinceLastRequest = System.currentTimeMillis();


        Observable<String> observableQueryText = Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                        // Listen for text input into the SearchView
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                if (!emitter.isDisposed()) {
                                    emitter.onNext(newText); // Pass the query to the emitter
                                }
                                return false;
                            }
                        });
                    }
                })
                .debounce(3000, TimeUnit.MILLISECONDS) // Apply Debounce() operator to limit requests
                .subscribeOn(Schedulers.io());

        // Subscribe an Observer
        observableQueryText.subscribe(new io.reactivex.rxjava3.core.Observer<String>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                disposables.add(d);
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                Log.d(TAG, "onNext: time  since last request: " + (System.currentTimeMillis() - timeSinceLastRequest));
                Log.d(TAG, "onNext: search query: " + s);
                timeSinceLastRequest = System.currentTimeMillis();


                // method for sending a request to the server
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {

                        if (s.isEmpty()) {
                            fetchMoviesData();
                        }else {
                            getSearchedMovies(s);
                        }

                    }
                });

            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchMoviesData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        fetchMoviesData();

    }

    public NetworkInfo getNetWorkInfo() {
         return connectivityManager.getActiveNetworkInfo();
    }
    private void fetchMoviesData() {

        if (getNetWorkInfo()!=null) {

            getPopularMovies();

            getNowPlayingMovies();

        } else {

            getDatabaseMovies();
        }
    }

    private void getDatabaseMovies() {

        movieViewModel.getMoviesfromDB().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                Log.i("TAG", "onChanged: inside getDatabaseMovies ");
                offlinemovies = (ArrayList<Result>) results;

                if(getNetWorkInfo() == null) {
                    showOfflineData();
                }


            }
        });
    }

    private void showOfflineData() {

        Log.i("TAG", "onChanged: inside showOfflineData ");
        popularMovieRecyclerView = activityMainBinding.popularMovieRecyclerView;
        popularMoviesLinearLayout.setVisibility(View.VISIBLE);
        nowPlayingMoviesLinearLayout.setVisibility(View.GONE);
        searchedMoviesLinearLayout.setVisibility(View.GONE);

        popularMoviesTextView = activityMainBinding.popularMoviesTextView;
        popularMoviesTextView.setText("Offline Movies");

        movieAdapter = new MovieAdapter(this, offlinemovies);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            popularMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {

            popularMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        popularMovieRecyclerView.setItemAnimator(new DefaultItemAnimator());
        popularMovieRecyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

    }

    private void getPopularMovies() {

        Log.i("TAG", "onChanged: inside getPopularMovies ");
        movieViewModel.getMoviesFromAPI().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {

                resultArrayList = (ArrayList<Result>) results;
                Log.i("TAG", "onChanged: inside getPopularMovies size  " + resultArrayList.size());
                for (Result rs : resultArrayList) {

                    movieViewModel.addMoviesinDb(rs);

                }

                Log.i("TAG", "onChanged: getPopularMovies " + resultArrayList);
                showPopularMoviesLinearLayout();

            }
        });
        Log.i("TAG", "onChanged: outside getPopularMovies ");
    }

    private void getNowPlayingMovies() {

        Log.i("TAG", "onChanged: inside getNowPlayingMovies ");
        movieViewModel.getNowPlayingMoviesFromAPI().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {

                nowPlayingMovieArrayList = (ArrayList<Result>) results;
                for (Result rs : nowPlayingMovieArrayList) {

                    //movieViewModel.addMoviesinDb(rs);

                }

                Log.i("TAG", "onChanged: getNowPlayingMovies " + nowPlayingMovieArrayList);
                showNowPlayingMoviesLinearLayout();

            }
        });
        Log.i("TAG", "onChanged: outside getNowPlayingMovies ");
    }

    private void getSearchedMovies(String searchQuery) {

        Log.i("TAG", "onChanged: inside getSearchedMovies ");

        movieViewModel.getSearchedMoviesFromAPI(searchQuery).observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {

                searchedMovieArrayList = (ArrayList<Result>) results;

                Log.i("TAG", "onChanged: seacrh result " + searchedMovieArrayList);
                showSearchedMoviesLinearLayout();

            }
        });

        Log.i("TAG", "onChanged: outside getSearchedMovies ");

    }

    private void showPopularMoviesLinearLayout() {

        popularMoviesLinearLayout.setVisibility(View.VISIBLE);
        searchedMoviesLinearLayout.setVisibility(View.GONE);

        popularMoviesTextView = activityMainBinding.popularMoviesTextView;
        popularMoviesTextView.setText("Popular Movies");

        movieAdapter = new MovieAdapter(this, resultArrayList);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            popularMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        } else {
            popularMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        }

        popularMovieRecyclerView.setItemAnimator(new DefaultItemAnimator());
        popularMovieRecyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

    }

    private void showNowPlayingMoviesLinearLayout() {

        nowPlayingMoviesLinearLayout.setVisibility(View.VISIBLE);
        searchedMoviesLinearLayout.setVisibility(View.GONE);

        movieAdapter = new MovieAdapter(this, nowPlayingMovieArrayList);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            nowPlayingMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        } else {
            nowPlayingMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        }

        nowPlayingMovieRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nowPlayingMovieRecyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

    }

    private void showSearchedMoviesLinearLayout() {

        nowPlayingMoviesLinearLayout.setVisibility(View.GONE);
        popularMoviesLinearLayout.setVisibility(View.GONE);
        searchedMoviesLinearLayout.setVisibility(View.VISIBLE);

        movieAdapter = new MovieAdapter(this, searchedMovieArrayList);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            searchedMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        } else {
            searchedMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        }

        searchedMovieRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchedMovieRecyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.projectLink) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));

        } else if (item.getItemId() == R.id.favActivity) {
            startActivity(new Intent(MainActivity.this, FavoriteActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
}