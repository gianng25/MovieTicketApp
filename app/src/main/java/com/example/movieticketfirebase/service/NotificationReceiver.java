package com.example.movieticketfirebase.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.movieticketfirebase.R;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "movie_reminder_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String movieTitle = intent.getStringExtra("movieTitle");
        String showtime = intent.getStringExtra("showtime");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Nhắc nhở xem phim",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_ticket)
                .setContentTitle("Sắp đến giờ xem phim!")
                .setContentText("Phim " + movieTitle + " sẽ bắt đầu vào lúc " + showtime)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
