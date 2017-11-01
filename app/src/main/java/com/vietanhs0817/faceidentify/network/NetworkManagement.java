package com.vietanhs0817.faceidentify.network;

import com.vietanhs0817.faceidentify.model.Actor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by YukiNoHara on 11/1/2017.
 */

public class NetworkManagement {
    public static void getActors(String name, final Callback callback){
        IActor iActor = Client.getInstance().create(IActor.class);
        Call<List<Actor>> call = iActor.getActors(name);
        call.enqueue(new retrofit2.Callback<List<Actor>>() {
            @Override
            public void onResponse(Call<List<Actor>> call, Response<List<Actor>> response) {
                if (response.isSuccessful()){
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
}
