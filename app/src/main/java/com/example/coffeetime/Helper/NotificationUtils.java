package com.example.coffeetime.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.example.coffeetime.R;

public class NotificationUtils {

    private static final String CHANNEL_ID = "my_channel_id";
    private static final CharSequence CHANNEL_NAME = "My Channel";
    private static final String CHANNEL_DESCRIPTION = "My Channel Description";

    public static void sendNotification(Context context, String title, String body) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(context);
        }

        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_coffee_msg);

        notificationManager.notify(0, builder.build());
    }
}