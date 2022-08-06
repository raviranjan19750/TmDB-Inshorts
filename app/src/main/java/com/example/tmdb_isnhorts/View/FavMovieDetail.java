package com.example.tmdb_isnhorts.View;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.tmdb_isnhorts.Favorites;
import com.example.tmdb_isnhorts.R;
import com.example.tmdb_isnhorts.databinding.ActivityFavMovieDetailBinding;

public class FavMovieDetail extends AppCompatActivity {

    Favorites favorites;
    ActivityFavMovieDetailBinding activityFavoriteBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        activityFavoriteBinding  = DataBindingUtil.setContentView(this, R.layout.activity_fav_movie_detail);


        favorites = getIntent().getParcelableExtra("favresult");
        String moviename =  favorites.getOriginalTitle();

        getSupportActionBar().setTitle(moviename);

        activityFavoriteBinding.setFavorites(favorites);








    }
}