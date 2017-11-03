package com.vietanhs0817.faceidentify.network;

import android.content.Context;

import com.vietanhs0817.faceidentify.R;
import com.vietanhs0817.faceidentify.model.Actor;
import com.vietanhs0817.faceidentify.model.Movie;
import com.vietanhs0817.faceidentify.model.MovieList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by YukiNoHara on 11/1/2017.
 */

public class NetworkManagement {
    public static void getActors(Context context, String name, final Callback<Actor> callback) {
        IActor iActor = Client.getInstance(context.getResources().getString(R.string.base_url_imdb)).create(IActor.class);
        Call<List<Actor>> call = iActor.getActors(name);
        call.enqueue(new retrofit2.Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
                if (response.isSuccessful()) {
                    List<Actor> list = response.body();
                    callback.onSuccess(list.get(0));
                }
            }

            @Override
            public void onFailure(Call<List<Actor>> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void getMovie(Context context, String id, final Callback<Movie> callback) {
        IMovie iMovie = Client.getInstance(context.getResources().getString(R.string.base_url_themoviedb)).create(IMovie.class);
        Call<MovieList> call = iMovie.getMovieList(id, context.getResources().getString(R.string.api_key), "imdb_id");
        call.enqueue(new retrofit2.Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful()) {
                    MovieList movieList = response.body();
                    Movie movie = null;
                    if(!movieList.getMovieResults().isEmpty()){
                        movie = movieList.getMovieResults().get(0);
                    } else if(!movieList.getTvResults().isEmpty()){
                        movie = movieList.getTvResults().get(0);
                    } else if(!movieList.getTvEpisodeResults().isEmpty()){
                        movie = movieList.getTvEpisodeResults().get(0);
                    } else if(!movieList.getTvSeasonResults().isEmpty()){
                        movie = movieList.getTvSeasonResults().get(0);
                    }
                    callback.onSuccess(movie);
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                callback.onFailed();
            }
        });
    }
}
