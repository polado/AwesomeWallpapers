package com.polado.wallpapers.rest;

import java.util.ArrayList;

import com.polado.wallpapers.Model.Photo;
import com.polado.wallpapers.Model.PhotoStats;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by PolaDo on 11/18/2017.
 */

public interface PhotoEndPoint {
    @GET("/photos")
    Call<ArrayList<Photo>> getPhotosList(@Query("page") Integer page,
                                         @Query("per_page") Integer perPage,
                                         @Query("order_by") String orderBy);

    @GET("/photos/curated")
    Call<ArrayList<Photo>> getCuratedPhotosList(@Query("page") Integer page,
                                         @Query("per_page") Integer perPage,
                                         @Query("order_by") String orderBy);

    @GET("/photos/{id}")
    Call<Photo> getPhotoByID(@Path("id") String id, @Query("w") Integer width, @Query("h") Integer height);

    @GET("/photos/random")
    Call<Photo> getRandomPhoto();

    @GET("/photos/random")
    Call<ArrayList<Photo>> getRandomPhotos(@Query("count") Integer count);

    @GET("/photos/{id}/statistics")
    Call<PhotoStats> getPhotoStats(@Path("id") String id);

    @POST("/photos/{id}/like")
    Call<String> setLike(@Path("id") String id);

}
