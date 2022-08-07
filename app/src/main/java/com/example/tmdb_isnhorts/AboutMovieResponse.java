package com.example.tmdb_isnhorts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AboutMovieResponse {

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("title")
    private String title;

    @SerializedName("vote_average")
    private Double voteAverage;


    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public String toString() {
        return "AboutMovieResponse{" +
                "overview='" + overview + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
