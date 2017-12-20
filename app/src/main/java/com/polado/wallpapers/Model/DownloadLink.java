package com.polado.wallpapers.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PolaDo on 12/18/2017.
 */

public class DownloadLink {
    @SerializedName("url")
    private String url;

    public DownloadLink(String url) {
        this.url = url;
    }

    public DownloadLink() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
