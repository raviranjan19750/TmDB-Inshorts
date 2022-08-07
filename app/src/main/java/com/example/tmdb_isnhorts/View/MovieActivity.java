package com.example.tmdb_isnhorts.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tmdb_isnhorts.AboutMovieResponse;
import com.example.tmdb_isnhorts.Favorites;
import com.example.tmdb_isnhorts.R;
import com.example.tmdb_isnhorts.Result;
import com.example.tmdb_isnhorts.ViewModel.MovieViewModel;
import com.example.tmdb_isnhorts.databinding.ActivityMovieBinding;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {


    ActivityMovieBinding activityMovieBinding;
    ImageView favButtonImageView, shareButtonImageView;
    Favorites favorites;
    MovieViewModel movieViewModel;
    AboutMovieResponse aboutMovieResponse;

    String id;
    String posterpath;
    String titleofmovie, overrivew;
    double rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        activityMovieBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);

        favButtonImageView = findViewById(R.id.favButtonImageView);
        shareButtonImageView = findViewById(R.id.shareButtonImageView);


        if(getIntent() != null && getIntent().getStringExtra("id") != null) {

            id = getIntent().getStringExtra("id");
            fetchMovieDetails(id);

        }




        favButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                movieViewModel.adddFav(favorites);
                Toast.makeText(MovieActivity.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                Intent newintent = new Intent(MovieActivity.this, com.example.tmdb_isnhorts.View.FavoriteActivity.class);
                startActivity(newintent);
            }
        });

        shareButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://www.tmdbinshorts.com/" + id;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });


    }

    private void fetchMovieDetails(String id) {

        movieViewModel.getMutableMovieById(id).observe(this, new Observer<AboutMovieResponse>() {
            @Override
            public void onChanged(AboutMovieResponse response) {
                aboutMovieResponse =  response;
                showAboutMovieLayout();
            }
        });

    }

    private void showAboutMovieLayout() {

        activityMovieBinding.setAboutMovieResponse(aboutMovieResponse);
        if(aboutMovieResponse != null && aboutMovieResponse.getTitle() != null) {
            getSupportActionBar().setTitle(aboutMovieResponse.getTitle().toString());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            favorites = new Favorites(aboutMovieResponse.getPosterPath(), aboutMovieResponse.getTitle(), aboutMovieResponse.getOverview(), aboutMovieResponse.getVoteAverage());
        }



    }
}