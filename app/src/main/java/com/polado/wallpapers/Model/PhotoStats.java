package com.polado.wallpapers.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PolaDo on 11/19/2017.
 */

public class PhotoStats implements Parcelable {
    @SerializedName("id")
    private String id;

    @SerializedName("downloads")
    private Downloads downloads;

    @SerializedName("likes")
    private Likes likes;

    @SerializedName("views")
    private Views views;


    public static final Creator<PhotoStats> CREATOR = new Creator<PhotoStats>() {
        @SuppressWarnings({
                "unchecked"
        })

        public PhotoStats createFromParcel(Parcel in) {
            PhotoStats instance = new PhotoStats();

            instance.id = (String) in.readValue(String.class.getClassLoader());
            instance.downloads = (Downloads) in.readValue(Downloads.class.getClassLoader());
            instance.likes = (Likes) in.readValue(Likes.class.getClassLoader());
            instance.views = (Views) in.readValue(Views.class.getClassLoader());

            return instance;
        }

        @Override
        public PhotoStats[] newArray(int size) {
            return new PhotoStats[size];
        }
    };

    public PhotoStats() {
    }

    public PhotoStats(String id, Downloads downloads, Likes likes, Views views) {
        this.id = id;
        this.downloads = downloads;
        this.likes = likes;
        this.views = views;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Downloads getDownloads() {
        return downloads;
    }

    public void setDownloads(Downloads downloads) {
        this.downloads = downloads;
    }

    public Likes getLikes() {
        return likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public Views getViews() {
        return views;
    }

    public void setViews(Views views) {
        this.views = views;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(downloads);
        dest.writeValue(likes);
        dest.writeValue(views);
    }

    public class Downloads {
        @SerializedName("total")
        private Integer total;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
    }

    public class Likes {
        @SerializedName("total")
        private Integer total;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
    }

    public class Views {
        @SerializedName("total")
        private Integer total;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
    }
}
