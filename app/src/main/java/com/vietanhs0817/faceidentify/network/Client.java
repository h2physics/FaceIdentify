package com.vietanhs0817.faceidentify.network;

import android.content.Context;

import com.vietanhs0817.faceidentify.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YukiNoHara on 11/1/2017.
 */

public class Client {
    private static Retrofit retrofit;
    private static Client instance;
    public static Client getInstance(){
        if (instance == null){
            instance = new Client();
        }
        return instance;
    }

    private Client(){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        });
        if(BuildConfig.DEBUG){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }
        retrofit = new Retrofit.Builder()
                .baseUrl("http://www.theimdbapi.org/api/find/")
                .addConverterFactory(GsonConverterFactory.create()).callFactory(httpClientBuilder.build()).build();
    }
    public <T> T create (final  Class<T> service){
        return retrofit.create(service);
    }
}
