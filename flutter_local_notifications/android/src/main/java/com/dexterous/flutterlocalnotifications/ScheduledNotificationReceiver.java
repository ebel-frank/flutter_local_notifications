package com.dexterous.flutterlocalnotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Keep;

import com.dexterous.flutterlocalnotifications.models.NotificationDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/** Created by michaelbui on 24/3/18. Edited by andy-ife on 15/10/25 */
@Keep
public class ScheduledNotificationReceiver extends BroadcastReceiver {

  private static final String TAG = "ScheduledNotifReceiver";

  @Override
  @SuppressWarnings("deprecation")
  public void onReceive(final Context context, Intent intent) {
    String notificationDetailsJson =
            intent.getStringExtra(FlutterLocalNotificationsPlugin.NOTIFICATION_DETAILS);

    Intent serviceIntent = new Intent(context, ForegroundService.class);

    serviceIntent.putExtra(
            ForegroundServiceStartParameter.EXTRA,
            new ForegroundServiceStartParameter(
                    null,
                    notificationDetailsJson,
                    ForegroundService.START_STICKY_COMPATIBILITY,
                    null)
    );

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
      context.startForegroundService(serviceIntent);
    } else {
      context.startService(serviceIntent);
    }
  }
}
