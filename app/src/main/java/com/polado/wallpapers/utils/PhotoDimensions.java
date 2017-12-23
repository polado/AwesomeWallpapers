package com.polado.wallpapers.utils;

import android.app.Activity;
import android.content.Context;

import com.polado.wallpapers.Model.Photo;

/**
 * Created by pola on 22/12/17.
 */

public class PhotoDimensions {
    private Context context;
    private Photo photo;

    public PhotoDimensions(Context context, Photo photo) {
        this.context = context;
        this.photo = photo;
    }

    public int getHeight() {
        int screenWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();

        return screenWidth * photo.getHeight() / photo.getWidth();
    }
}
