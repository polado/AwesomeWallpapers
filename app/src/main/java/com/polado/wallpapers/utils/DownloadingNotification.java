package com.polado.wallpapers.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

/**
 * Created by PolaDo on 12/19/2017.
 */

public class DownloadingNotification {
    Context context;

    public DownloadingNotification(Context context) {
        this.context = context;
    }

    public void createNotification(String msg, int id) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(context.getResources().getIdentifier("ic_bottomnav_new", "drawable", context.getPackageName()))
                        .setContentTitle("Downloading Photo")
                        .setContentText("Downloading " + msg);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        assert mNotificationManager != null;
        mNotificationManager.notify(id, mBuilder.build());
    }

    public void updateNotification(String msg, int id) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(context.getResources().getIdentifier("ic_bottomnav_new", "drawable", context.getPackageName()))
                        .setContentTitle("Photo is Downloaded")
                        .setContentText(msg + " is Downloaded");

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        assert mNotificationManager != null;
        mNotificationManager.notify(id, mBuilder.build());
    }
}
