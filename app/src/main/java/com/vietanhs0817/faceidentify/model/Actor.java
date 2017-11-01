package com.vietanhs0817.faceidentify.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YukiNoHara on 11/1/2017.
 */

public class Actor {
    @SerializedName("title")
    @Expose
    private String mName;
    @SerializedName("image")
    @Expose
    private Image mImage;
    @SerializedName("type")
    @Expose
    private List<String> mTypes;
    @SerializedName("description")
    @Expose
    private String mDescription;
    @SerializedName("birthday")
    @Expose
    private String mBirthday;
    @SerializedName("birthplace")
    @Expose
    private String mBirthPlace;
    @SerializedName("filmography")
    @Expose
    private Filmography mFilms;

    public String getName() {
        return mName;
    }

    public Image getImage() {
        return mImage;
    }

    public List<String> getTypes() {
        return mTypes;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getBirthday() {
        return mBirthday;
    }

    public String getBirthPlace() {
        return mBirthPlace;
    }

    public Filmography getFilms() {
        return mFilms;
    }

    public class Image {
        @SerializedName("poster")
        private String mPoster;

        public String getPoster() {
            return mPoster;
        }
    }

    public class Filmography {
        @SerializedName("self")
        @Expose
        private List<Film> list;

        public List<Film> getList() {
            return list;
        }
    }

    public class Film {
        @SerializedName("url")
        @Expose
        private String mUrl;
        @SerializedName("year")
        @Expose
        private String mYear;
        @SerializedName("title")
        @Expose
        private String mTitle;

        public String getUrl() {
            return mUrl;
        }

        public String getYear() {
            return mYear;
        }

        public String getTitle() {
            return mTitle;
        }

        @Override
        public String toString() {
            return "Film{" +
                    "mUrl='" + mUrl + '\'' +
                    ", mYear='" + mYear + '\'' +
                    ", mTitle='" + mTitle + '\'' +
                    '}';
        }
    }
}
