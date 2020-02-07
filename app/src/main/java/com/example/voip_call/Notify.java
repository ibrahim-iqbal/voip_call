package com.example.voip_call;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class Notify extends Application {
    public static final String CHANNEL1 = "1";

    @Override
    public void onCreate() {
        super.onCreate();

        createnotification();
    }

    public void createnotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL1, "CHANNEL_1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("Sent from channel_1 ");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel1);
        }
    }
}
