package com.example.tmdb_isnhorts.FavoriteMovies;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tmdb_isnhorts.Favorites;

import java.util.List;

@Dao
public interface FavMovie {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFavorite(Favorites favorites);

    @Query("select * from favmovies")
    public LiveData<List<Favorites>> getFavMovies();

    @Query("DELETE FROM favmovies")
    void deleteAllFromTable();
}
