package com.vietanhs0817.faceidentify.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YukiNoHara on 11/3/2017.
 */

public class MovieList {
    @SerializedName("movie_results")
    @Expose
    private List<Movie> movieResults;
    @SerializedName("tv_results")
    @Expose
    private List<Movie> tvResults;
    @SerializedName("tv_episode_results")
    @Expose
    private List<Movie> tvEpisodeResults;
    @SerializedName("tv_season_results")
    @Expose
    private List<Movie> tvSeasonResults;

    public List<Movie> getMovieResults() {
        return movieResults;
    }

    public List<Movie> getTvResults() {
        return tvResults;
    }

    public List<Movie> getTvEpisodeResults() {
        return tvEpisodeResults;
    }

    public List<Movie> getTvSeasonResults() {
        return tvSeasonResults;
    }
}
