package com.example.tmdb_isnhorts.View;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tmdb_isnhorts.FavoriteMovies.FavoriteMovieAdapter;
import com.example.tmdb_isnhorts.Favorites;
import com.example.tmdb_isnhorts.R;
import com.example.tmdb_isnhorts.ViewModel.MovieViewModel;
import com.example.tmdb_isnhorts.databinding.ActivityFavoriteBinding;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {


    FavoriteMovieAdapter mAdapter;
    MovieViewModel viewModel;
    RecyclerView recyclerView;
    ArrayList<Favorites> favoritesArrayList = new ArrayList<>();
    ActivityFavoriteBinding favoriteBinding;
    LinearLayout emptyStateLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        getSupportActionBar().setTitle("Favourites");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        favoriteBinding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);

        emptyStateLinearLayout = favoriteBinding.emptyStateLinearLayout;



        viewModel.getFavFromDb().observe(this, new Observer<List<Favorites>>() {
            @Override
            public void onChanged(List<Favorites> favorites) {

                favoritesArrayList = (ArrayList<Favorites>) favorites;

                showRecyclerView();
            }
        });


    }

    private void showRecyclerView() {

        recyclerView = favoriteBinding.favouriteMovieRecyclerView;
        mAdapter = new FavoriteMovieAdapter(this, favoritesArrayList);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        if(favoritesArrayList != null && favoritesArrayList.size() == 0) {
            emptyStateLinearLayout.setVisibility(View.VISIBLE);
        }else{
            emptyStateLinearLayout.setVisibility(View.GONE);
        }
    }
}