package com.polado.wallpapers.rest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import com.polado.wallpapers.Model.DownloadLink;
import com.polado.wallpapers.Model.Photo;
import com.polado.wallpapers.Model.PhotoStats;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PolaDo on 11/18/2017.
 */

public class UnsplashApi {

    private PhotoEndPoint photoEndPoint;

    public UnsplashApi() {
        photoEndPoint = ApiClient.getClient().create(PhotoEndPoint.class);
    }

    public void getPhotosList(Integer page, Integer perPage, String order, final OnPhotosLoadedListener listener) {
        Call<ArrayList<Photo>> call = photoEndPoint.getPhotosList(page, perPage, order);
        call.enqueue(getPhotosCallback(listener));
    }

    public void getCuratedPhotosList(Integer page, Integer perPage, String order, final OnPhotosLoadedListener listener) {
        Call<ArrayList<Photo>> call = photoEndPoint.getCuratedPhotosList(page, perPage, order);
        call.enqueue(getPhotosCallback(listener));
    }

    public void getPhotoByID(@NonNull String id, @Nullable Integer width, @Nullable Integer height, final OnPhotoLoadedListener listener) {
        Call<Photo> call = photoEndPoint.getPhotoByID(id, width, height);
        call.enqueue(getPhotoCallback(listener));
    }

    public void getRandomPhoto(final OnPhotoLoadedListener listener) {
        Call<Photo> call = photoEndPoint.getRandomPhoto();
        call.enqueue(getPhotoCallback(listener));
    }

    public void getRandomPhotos(Integer count, final OnPhotosLoadedListener listener) {
        Call<ArrayList<Photo>> call = photoEndPoint.getRandomPhotos(count);
        call.enqueue(getPhotosCallback(listener));
    }

    public void getPhotoStats(@NonNull String id, final OnPhotoStatsLoadedListener listener) {
        Call<PhotoStats> call = photoEndPoint.getPhotoStats(id);
        call.enqueue(getPhotoStatsCallback(listener));
    }

    public void getPhotoDownloadLink(@NonNull String id, final OnLinkLoadedListener listener) {
        Call<DownloadLink> call = photoEndPoint.getPhotoDownloadLink(id);
        call.enqueue(getPhotoDownloadLinkCallback(listener));
    }

    public void setLike(@NonNull String id, final OnPhotoLikedListener listener) {
        Log.i("liked", "setlike api");
        Call<String> call = photoEndPoint.setLike(id);
        call.enqueue(setLikeCallback(listener));
    }

    private Callback<Photo> getPhotoCallback(final OnPhotoLoadedListener listener) {
        return new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Photo photo = response.body();
                    listener.onLoaded(photo);
                } else if (statusCode == 401) {
                    listener.onFailure("Unauthorized, Check your client ID");
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        };
    }

    private Callback<ArrayList<Photo>> getPhotosCallback(final OnPhotosLoadedListener listener) {
        return new Callback<ArrayList<Photo>>() {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    ArrayList<Photo> photos = response.body();
                    listener.onLoaded(photos);
                } else if (statusCode == 400) {
                    listener.onFailure("Unauthorized, Check your client ID");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        };
    }

    private Callback<PhotoStats> getPhotoStatsCallback(final OnPhotoStatsLoadedListener listener) {
        return new Callback<PhotoStats>() {
            @Override
            public void onResponse(Call<PhotoStats> call, Response<PhotoStats> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    PhotoStats stats = response.body();
                    listener.onLoaded(stats);
                } else if (statusCode == 400) {
                    listener.onFailure("Unauthorized, Check your client ID");
                }
            }

            @Override
            public void onFailure(Call<PhotoStats> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        };
    }

    private Callback<DownloadLink> getPhotoDownloadLinkCallback(final OnLinkLoadedListener listener) {
        return new Callback<DownloadLink>() {
            @Override
            public void onResponse(Call<DownloadLink> call, Response<DownloadLink> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    listener.onLoaded(response.body());
                } else if (statusCode == 401) {
                    listener.onFailure("Unauthorized, Check your client ID");
                }
            }

            @Override
            public void onFailure(Call<DownloadLink> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        };
    }

    private Callback<String> setLikeCallback(final OnPhotoLikedListener listener) {
        return new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("liked", "setlike api callback response " + response.toString());
                int statusCode = response.code();
                if (statusCode == 200) {
                    listener.onLiked(response.toString());
                } else if (statusCode == 400 || statusCode == 401) {
                    listener.onFailure("Unauthorized, Check your client ID");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("liked", "setlike api callback fail");
                listener.onFailure(t.getMessage());
            }
        };
    }

    public interface OnPhotoLoadedListener {
        void onLoaded(Photo photo);

        void onFailure(String error);
    }

    public interface OnPhotosLoadedListener {
        void onLoaded(ArrayList<Photo> photos);

        void onFailure(String error);
    }

    public interface OnPhotoStatsLoadedListener {
        void onLoaded(PhotoStats stats);

        void onFailure(String error);
    }

    public interface OnLinkLoadedListener {

        void onLoaded(DownloadLink downloadLink);

        void onFailure(String error);
    }

    public interface OnPhotoLikedListener {
        void onLiked(String msg);

        void onFailure(String error);
    }
}
