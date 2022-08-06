package com.example.tmdb_isnhorts.View;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding activityMainBinding;
    MovieViewModel movieViewModel;
    ArrayList<Result> resultArrayList= new ArrayList<>();
    ArrayList<Result> offlinemovies = new ArrayList<>();

    RecyclerView favouriteMovieRecyclerView;
    EditText searchEditText;
    Button searchButton;
    MovieAdapter movieAdapter;
   SwipeRefreshLayout swipeRefreshLayout;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        searchEditText = activityMainBinding.searchEditText;
        searchButton = activityMainBinding.searchButton;

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo!=null) {

                    searchEditText.setText("");
                    getPopularMovies();
                    swipeRefreshLayout.setRefreshing(false);

                } else {

                    searchEditText.setText("");
                    getDatabaseMovies();
                    swipeRefreshLayout.setRefreshing(false);

                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo!=null) {

            getPopularMovies();

        } else {

            getDatabaseMovies();
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(searchEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Search field is empty", Toast.LENGTH_LONG).show();
                }else{
                    getSearchedMovies(searchEditText.getText().toString().trim());
                }
            }
        });
    }

    private void getDatabaseMovies() {

        movieViewModel.getMoviesfromDB().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                offlinemovies = (ArrayList<Result>) results;

                showOfflineData();

            }
        });
    }

    private void showOfflineData() {

        favouriteMovieRecyclerView = activityMainBinding.popularMovieRecyclerView;

        movieAdapter = new MovieAdapter(this, offlinemovies);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            favouriteMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {

            favouriteMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        favouriteMovieRecyclerView.setItemAnimator(new DefaultItemAnimator());
        favouriteMovieRecyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();

    }

    private void getPopularMovies() {

        movieViewModel.getMoviesFromAPI().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {

                resultArrayList = (ArrayList<Result>) results;
                for (Result rs: resultArrayList) {

                    movieViewModel.addMoviesinDb(rs);



                }

                Log.i("TAG", "onChanged: " + resultArrayList);
                showRecyclerView();

            }
        });
    }

    private void getSearchedMovies(String searchQuery) {

        movieViewModel.getSearchedMoviesFromAPI(searchQuery).observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {

                resultArrayList = (ArrayList<Result>) results;
                for (Result rs: resultArrayList) {

                    movieViewModel.addMoviesinDb(rs);

                }

                Log.i("TAG", "onChanged: seacrh result " + resultArrayList);
                showRecyclerView();

            }
        });

    }

    private void showRecyclerView() {

        favouriteMovieRecyclerView = activityMainBinding.popularMovieRecyclerView;
        movieAdapter = new MovieAdapter(this, resultArrayList);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            favouriteMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {


            favouriteMovieRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));


        }

        favouriteMovieRecyclerView.setItemAnimator(new DefaultItemAnimator());
        favouriteMovieRecyclerView.setAdapter(movieAdapter);
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