package com.polado.wallpapers.utils;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class DownloadPhotoService extends Service {
    private Context context;
    private String msg;
    private int id;
    private boolean action;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("SERVICE", "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SERVICE", "onStartCommand");
        context = this;
        msg = intent.getStringExtra("MSG");
        id = intent.getIntExtra("ID", 0);
        action = intent.getBooleanExtra("ACTION", true);

        startNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startNotification() {
        Log.i("SERVICE", "startNotification");
        if (action)
            createNotification(msg, id);
        else
            updateNotification(msg, id);
    }

    private void createNotification(String msg, int id) {
        Log.i("SERVICE", "createNotification");
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(context.getResources().getIdentifier("ic_bottomnav_new", "drawable", context.getPackageName()))
                        .setContentTitle("Downloading Photo")
                        .setContentText("Downloading " + msg)
//                        .addAction(android.R.drawable.ic_delete, "Cancel", PendingIntent.getActivity(
//                                context,
//                                0,
//                                new Intent(context, Home.class),
//                                PendingIntent.FLAG_UPDATE_CURRENT
//                        ))
                        .setProgress(0, 0, true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        assert mNotificationManager != null;
        mNotificationManager.notify(id, mBuilder.build());
    }

    private void updateNotification(String msg, int id) {
        Log.i("SERVICE", "updateNotification");
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(context.getResources().getIdentifier("ic_bottomnav_new", "drawable", context.getPackageName()))
                        .setContentTitle("Photo is Downloaded")
                        .setContentText(msg + " is Downloaded");

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        assert mNotificationManager != null;
        mNotificationManager.notify(id, mBuilder.build());

//        stopSelf();
    }

    @Override
    public void onCreate() {
        Log.i("SERVICE", "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i("SERVICE", "onDestroy");
        super.onDestroy();
    }
}
