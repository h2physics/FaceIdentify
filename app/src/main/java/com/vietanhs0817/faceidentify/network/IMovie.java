package com.vietanhs0817.faceidentify.network;

import com.vietanhs0817.faceidentify.model.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by YukiNoHara on 11/3/2017.
 */

public interface IMovie {
    @GET("find/{id}")
    Call<MovieList> getMovieList(@Path("id") String id,
                                 @Query("api_key") String apiKey,
                                 @Query("external_source") String source);
}
