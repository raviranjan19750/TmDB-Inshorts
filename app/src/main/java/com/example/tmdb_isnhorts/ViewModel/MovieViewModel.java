package com.example.tmdb_isnhorts.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tmdb_isnhorts.Favorites;
import com.example.tmdb_isnhorts.Repository.Repository;
import com.example.tmdb_isnhorts.Result;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {


    Repository repository;


    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);

    }

    public LiveData<List<Result>> getMoviesFromAPI(){
        return repository.GetMutableMovies();

    }

    public MutableLiveData<List<Result>> getSearchedMoviesFromAPI(String searchedQuery){
        return repository.GetSearchedMutableMovies(searchedQuery);

    }

    public MutableLiveData<List<Result>> getNowPlayingMoviesFromAPI(){
        return repository.GetNowPlayingMutableMovies();

    }

    public void addMoviesinDb(Result result) {
        repository.addMovies(result);
    }

    public LiveData<List<Result>> getMoviesfromDB() {
        return repository.getMoviesfromDB();
    }

    public void adddFav(Favorites favorites) {
        repository.addFav(favorites);
    }

    public LiveData<List<Favorites>> getFavFromDb() {
        return repository.getFav();
    }
}
