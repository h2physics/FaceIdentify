package com.vietanhs0817.faceidentify.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by YukiNoHara on 11/3/2017.
 */

public class Movie {
    @SerializedName("poster_path")
    @Expose
    private String mPosterPath;

    public String getPosterPath() {
        return mPosterPath;
    }
}
