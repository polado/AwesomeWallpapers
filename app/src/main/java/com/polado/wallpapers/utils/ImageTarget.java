package com.polado.wallpapers.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by PolaDo on 11/27/2017.
 */

public class ImageTarget implements Target {
    private final ContentResolver r;
    private final String name;
    private final String desc;

    public ImageTarget(ContentResolver resolver, String name, String desc) {
        this.r = resolver;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Log.i("downlaod", "onBitmapLoaded");
//        ContentResolver r = resolver.get();
        if (r != null)
        {
            Log.i("downlaod", "if");
            MediaStore.Images.Media.insertImage(r, bitmap, name, desc);
        } else {
            Log.i("downlaod", "else");
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

        Log.i("downlaod", "onBitmapFailed");
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

        Log.i("downlaod", "onPrepareLoad");
    }
}
