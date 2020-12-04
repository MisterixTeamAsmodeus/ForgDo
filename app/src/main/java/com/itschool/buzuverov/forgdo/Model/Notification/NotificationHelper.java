package com.itschool.buzuverov.forgdo.Model.Notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.itschool.buzuverov.forgdo.Activity.MainActivity;
import com.itschool.buzuverov.forgdo.R;

public class NotificationHelper {
    public static final String channelID = "!ForgDo ID";
    public static final String channelName = "!ForgDo Notification";
    private NotificationManager mManager;
    private Context context;

    public NotificationHelper(Context context) {
        super();
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public Notification showNotification(String title, String description) {
        if (description.trim().length() == 0) {
            description = title;
            title = context.getString(R.string.reminder_task);
        }
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, channelID).
                        setContentTitle(title).
                        setContentText(description).
                        setSmallIcon(R.drawable.ic_stat_name).
                        setVibrate(new long[]{200, 500}).
                        setLights(Color.YELLOW, 500, 5000).
                        setVisibility(NotificationCompat.VISIBILITY_PUBLIC).
                        setContentIntent(contentIntent).
                        setPriority(NotificationCompat.PRIORITY_DEFAULT).
                        setAutoCancel(true);
        try {
            Ringtone r = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }
}
