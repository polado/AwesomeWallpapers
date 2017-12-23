package com.polado.wallpapers.rest;

import com.polado.wallpapers.Model.Collection;
import com.polado.wallpapers.Model.Photo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by PolaDo on 12/20/2017.
 */

public interface CollectionEndPoint {
    @GET("/collections")
    Call<ArrayList<Collection>> getCollections(@Query("page") Integer page,
                                               @Query("per_page") Integer perPage);

    @GET("collections/featured")
    Call<ArrayList<Collection>> getFeaturedCollections(@Query("page") Integer page,
                                                       @Query("per_page") Integer perPage);

    @GET("collections/curated")
    Call<ArrayList<Collection>> getCuratedCollections(@Query("page") Integer page,
                                                      @Query("per_page") Integer perPage);

    @GET("collections/{id}")
    Call<Collection> getCollection(@Path("id") String id);

    @GET("collections/curated/{id}")
    Call<Collection> getCuratedCollection(@Path("id") String id);

    @GET("collections/{id}/photos")
    Call<ArrayList<Photo>> getCollectionPhotos(@Path("id") String id, @Query("page") Integer page, @Query("per_page") Integer perPage);

    @GET("collections/curated/{id}/photos")
    Call<ArrayList<Photo>> getCuratedCollectionPhotos(@Path("id") String id, @Query("page") Integer page, @Query("per_page") Integer perPage);

    @GET("collections/{id}/related")
    Call<ArrayList<Collection>> getRelatedCollections(@Path("id") String id);
}
