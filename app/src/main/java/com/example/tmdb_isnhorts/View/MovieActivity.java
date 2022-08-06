package com.example.tmdb_isnhorts.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.tmdb_isnhorts.Favorites;
import com.example.tmdb_isnhorts.R;
import com.example.tmdb_isnhorts.Result;
import com.example.tmdb_isnhorts.ViewModel.MovieViewModel;
import com.example.tmdb_isnhorts.databinding.ActivityMovieBinding;

public class MovieActivity extends AppCompatActivity {



    Result result;
    ActivityMovieBinding activityMovieBinding;
    ImageView favButtonImageView;
    Favorites favorites;
    MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);




        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        activityMovieBinding  = DataBindingUtil.setContentView(this, R.layout.activity_movie);


        favButtonImageView = findViewById(R.id.favButtonImageView);

        favButtonImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    result = getIntent().getParcelableExtra("movieresult");

                    String posterpath = result.getPosterPath();
                    String titleofmovie = result.getOriginalTitle();
                    String overrivew = result.getOverview();
                    double rating = result.getVoteAverage();

                    favorites = new Favorites(posterpath, titleofmovie, overrivew, rating);


                    movieViewModel.adddFav(favorites);
                    Toast.makeText(MovieActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                    Intent newintent = new Intent(MovieActivity.this, com.example.tmdb_isnhorts.View.FavoriteActivity.class);
                    startActivity(newintent);
                }
            });


            result = getIntent().getParcelableExtra("movieresult");

            String moviename = result.getOriginalTitle();

        getSupportActionBar().setTitle(moviename);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        activityMovieBinding.setResult(result);




    }
}