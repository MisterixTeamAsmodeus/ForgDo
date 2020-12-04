package com.itschool.buzuverov.forgdo.Model.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper helper = new NotificationHelper(context);
        helper.getManager().notify(0, helper.showNotification(intent.getExtras().get("title").toString(), intent.getExtras().get("description").toString()));
    }
}
