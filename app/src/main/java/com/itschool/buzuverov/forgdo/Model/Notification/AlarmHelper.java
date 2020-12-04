package com.itschool.buzuverov.forgdo.Model.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

public class AlarmHelper {

    public static final int REQUEST_CODE = 512;

    public static void createReminder(long deadline, Context context, String title, String description) {
        if (new Date().before(new Date(deadline))) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("description", description);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, deadline, pendingIntent);
        }
    }

    public static void cancelReminder(Context context, String title, String description) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
