package com.polado.wallpapers.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PolaDo on 11/18/2017.
 */

public class Links {
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
}
