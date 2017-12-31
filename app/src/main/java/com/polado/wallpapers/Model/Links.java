package com.polado.wallpapers.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PolaDo on 11/18/2017.
 */

public class Links implements Parcelable {
    public final static Parcelable.Creator<Links> CREATOR = new Creator<Links>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Links createFromParcel(Parcel in) {
            Links instance = new Links();
            instance.self = ((String) in.readValue((String.class.getClassLoader())));
            instance.html = ((String) in.readValue((String.class.getClassLoader())));
            instance.photos = ((String) in.readValue((String.class.getClassLoader())));
            instance.likes = ((String) in.readValue((String.class.getClassLoader())));
            instance.portfolio = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Links[] newArray(int size) {
            return (new Links[size]);
        }

    };
    @SerializedName("self")
    private String self;
    @SerializedName("html")
    private String html;
    @SerializedName("photos")
    private String photos;
    @SerializedName("likes")
    private String likes;
    @SerializedName("portfolio")
    private String portfolio;

    public Links() {
    }

    public Links(String self, String html, String photos, String likes, String portfolio) {
        this.self = self;
        this.html = html;
        this.photos = photos;
        this.likes = likes;
        this.portfolio = portfolio;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(self);
        dest.writeValue(html);
        dest.writeValue(photos);
        dest.writeValue(likes);
        dest.writeValue(portfolio);
    }

    public int describeContents() {
        return 0;
    }
}
