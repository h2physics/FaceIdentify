package com.vietanhs0817.faceidentify.network;

import com.vietanhs0817.faceidentify.model.Actor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by YukiNoHara on 11/1/2017.
 */

public interface IActor {
    @GET("person?")
    Call<List<Actor>> getActors(@Query("name") String name);
}
