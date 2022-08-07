package com.example.tmdb_isnhorts.Service;

import com.example.tmdb_isnhorts.Model.Info;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetMoviesService {



    @GET("movie/popular")
    Call<Info> getMovies(@Query("api_key")String api_key);

    @GET("search/movie")
    Call<Info> getSearchedMovies(@Query("api_key")String api_key, @Query("query") String query);

    @GET("movie/now_playing")
    Call<Info> getNowPlayingMovies(@Query("api_key")String api_key);
}
