package com.example.tmdb_isnhorts.Repository;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tmdb_isnhorts.AboutMovieResponse;
import com.example.tmdb_isnhorts.FavoriteMovies.FavMovie;
import com.example.tmdb_isnhorts.FavoriteMovies.FavoriteDatabase;
import com.example.tmdb_isnhorts.Favorites;
import com.example.tmdb_isnhorts.Model.Info;
import com.example.tmdb_isnhorts.Model.MovieDao;
import com.example.tmdb_isnhorts.Model.MovieDatabase;

import com.example.tmdb_isnhorts.R;
import com.example.tmdb_isnhorts.Result;
import com.example.tmdb_isnhorts.Service.GetMoviesService;
import com.example.tmdb_isnhorts.Service.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    ArrayList<Result> resultArrayList = new ArrayList<>();
    AboutMovieResponse aboutMovieResponse;
    MutableLiveData<List<Result>> getMutableLiveData = new MutableLiveData<>();
    MutableLiveData<AboutMovieResponse> getMutableMovieData = new MutableLiveData<>();
    MovieDao movieDao;
    FavMovie favDao;
    Application application;


    public Repository(Application application) {

        this.application = application;
        MovieDatabase movieDatabase = MovieDatabase.getInstance(application);
        FavoriteDatabase favoriteDatabase = FavoriteDatabase.getInstance(application);
        favDao = favoriteDatabase.getFavDao();
        movieDao = movieDatabase.getMovieDao();

    }

    public MutableLiveData<List<Result>> GetMutableMovies() {


        GetMoviesService getMoviesService = RetrofitInstance.getMoviesService();
        Call<Info> call = getMoviesService.getMovies(application.getApplicationContext().
                getString(R.string.api_key));


        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(Call<Info> call, Response<Info> response) {

                Info info = response.body();

                if (info != null && info.getResults() != null) {

                    resultArrayList.clear();
                    resultArrayList = (ArrayList<Result>) info.getResults();
                    getMutableLiveData.setValue(resultArrayList);

                }

            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                Log.i("TAG", "GetMutableMovies onFailure: " + t.getMessage());
            }
        });

        return getMutableLiveData;
    }

    public MutableLiveData<List<Result>> GetSearchedMutableMovies(String searchedQuery) {


        GetMoviesService getMoviesService = RetrofitInstance.getMoviesService();
        Call<Info> call = getMoviesService.getSearchedMovies(application.getApplicationContext().
                getString(R.string.api_key), searchedQuery);


        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(Call<Info> call, Response<Info> response) {

                Info info = response.body();

                if (info != null && info.getResults() != null) {

                    resultArrayList = (ArrayList<Result>) info.getResults();
                    getMutableLiveData.setValue(resultArrayList);

                }

            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                Log.i("TAG", "GetSearchedMutableMovies onFailure: " + t.getMessage());
            }
        });

        return getMutableLiveData;
    }

    public MutableLiveData<List<Result>> GetNowPlayingMutableMovies() {


        GetMoviesService getMoviesService = RetrofitInstance.getMoviesService();
        Call<Info> call = getMoviesService.getNowPlayingMovies(application.getApplicationContext().
                getString(R.string.api_key));


        call.enqueue(new Callback<Info>() {
            @Override
            public void onResponse(Call<Info> call, Response<Info> response) {

                Info info = response.body();

                if (info != null && info.getResults() != null) {

                    resultArrayList = (ArrayList<Result>) info.getResults();
                    getMutableLiveData.setValue(resultArrayList);

                }

            }

            @Override
            public void onFailure(Call<Info> call, Throwable t) {
                Log.i("TAG", "GetNowPlayingMutableMovies onFailure: " + t.getMessage());
            }
        });

        return getMutableLiveData;
    }

    public MutableLiveData<AboutMovieResponse> GetMutableMovieById(String id) {


        GetMoviesService getMoviesService = RetrofitInstance.getMoviesService();
        Call<AboutMovieResponse> call = getMoviesService.getMovieById(id, application.getApplicationContext().
                getString(R.string.api_key));

        call.enqueue(new Callback<AboutMovieResponse>() {
            @Override
            public void onResponse(Call<AboutMovieResponse> call, Response<AboutMovieResponse> response) {

                AboutMovieResponse info = response.body();

                if (info != null) {

                    aboutMovieResponse = info;
                    getMutableMovieData.setValue(aboutMovieResponse);

                }
            }

            @Override
            public void onFailure(Call<AboutMovieResponse> call, Throwable t) {

                Log.i("TAG", "GetMutableMovieById onFailure: " + t.getMessage());

            }
        });


        return getMutableMovieData;
    }

    public LiveData<List<Result>> getMoviesfromDB() {
        return movieDao.getDbmovies();
    }

    public LiveData<List<Favorites>> getFav() {

        return favDao.getFavMovies();
    }

    public void addMovies(Result result) {

        new AddMoviesAsync(movieDao).execute(result);

    }

    public static class AddMoviesAsync extends AsyncTask<Result, Void, Void> {

        MovieDao movieDao;

        public AddMoviesAsync(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Result... results) {

            movieDao.addMovies(results[0]);
            return null;
        }
    }


    public void addFav(Favorites favorites) {

        new AddFav(favDao).execute(favorites);

    }

    public void deleteFav() {
        new DeleteFav(favDao).execute();
    }


    public static class AddFav extends AsyncTask<Favorites, Void, Void> {

        FavMovie favdao;

        public AddFav(FavMovie favdao) {
            this.favdao = favdao;
        }

        @Override
        protected Void doInBackground(Favorites... favorites) {

            favdao.addFavorite(favorites[0]);
            return null;
        }
    }

    public static class DeleteFav extends AsyncTask<Favorites, Void, Void> {

        FavMovie favdao;

        public DeleteFav(FavMovie favdao) {
            this.favdao = favdao;
        }

        @Override
        protected Void doInBackground(Favorites... favorites) {

            favdao.deleteAllFromTable();
            return null;
        }
    }
}
