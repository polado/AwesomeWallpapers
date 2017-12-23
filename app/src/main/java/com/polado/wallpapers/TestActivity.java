package com.polado.wallpapers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.polado.wallpapers.Model.Photo;
import com.polado.wallpapers.rest.UnsplashApi;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    String clientID = "51e5db65183a9b48b04f0bea91ec5f7fc84248245f96629fd9639e4e30828023";
    ImagesAdapter2 imagesAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        UnsplashApi unsplashApi = new UnsplashApi();
        unsplashApi.getPhotosList(1, 5, "latest", new UnsplashApi.OnPhotosLoadedListener() {
            @Override
            public void onLoaded(ArrayList<Photo> photos) {
                imagesAdapter = new ImagesAdapter2(TestActivity.this, photos);
                recyclerView = (RecyclerView) findViewById(R.id.test_images_rv);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TestActivity.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(imagesAdapter);
            }

            @Override
            public void onFailure(String error) {

            }
        });

        final SwipyRefreshLayout swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.test_swipe_refresh);
        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Log.d("TestActivity", "Refresh triggered at "
                        + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
                swipyRefreshLayout.setRefreshing((direction != SwipyRefreshLayoutDirection.TOP));
            }
        });

//        PhotoEndPoint photoEndPoint = ApiClient.getClient().create(PhotoEndPoint.class);
//        Call<ArrayList<Photo>> call = photoEndPoint.getRandomPhotos(5);
//        call.enqueue(new Callback<ArrayList<Photo>> (){
//
//            @Override
//            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
//                ArrayList<Photo> photosList = response.body();
//
//                photosAdapter = new ImagesAdapter2(TestActivity.this, photosList);
//                recyclerView = (RecyclerView) findViewById(R.id.test_images_rv);
//
//                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TestActivity.this);
//                recyclerView.setLayoutManager(mLayoutManager);
//                recyclerView.setAdapter(photosAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
//
//            }
//        });
    }
}
