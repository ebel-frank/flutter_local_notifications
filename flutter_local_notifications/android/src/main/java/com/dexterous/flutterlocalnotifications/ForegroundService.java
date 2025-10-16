package com.dexterous.flutterlocalnotifications;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.dexterous.flutterlocalnotifications.models.NotificationDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ForegroundService extends Service {

  @Override
  @SuppressWarnings("deprecation")
  public int onStartCommand(Intent intent, int flags, int startId) {
    ForegroundServiceStartParameter parameter;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
      parameter =
          (ForegroundServiceStartParameter)
              intent.getSerializableExtra(
                  ForegroundServiceStartParameter.EXTRA, ForegroundServiceStartParameter.class);
    } else {
      parameter =
          (ForegroundServiceStartParameter)
              intent.getSerializableExtra(ForegroundServiceStartParameter.EXTRA);
    }
    NotificationDetails notificationDetails;

    if(parameter.notificationData == null){
      Gson gson = FlutterLocalNotificationsPlugin.buildGson();
      Type type = new TypeToken<NotificationDetails>() {}.getType();
      notificationDetails = gson.fromJson(parameter.notificationJson, type);
    } else {
      notificationDetails = parameter.notificationData;
    }

    Notification notification =
        FlutterLocalNotificationsPlugin.createNotification(this, notificationDetails);
    if (parameter.foregroundServiceTypes != null
        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      startForeground(
              notificationDetails.id,
          notification,
          orCombineFlags(parameter.foregroundServiceTypes));
    } else {
      startForeground(notificationDetails.id, notification);
    }

    FlutterLocalNotificationsPlugin.scheduleNextNotification(this, notificationDetails);
    FlutterLocalNotificationsPlugin.notifyAdminOnTimeout(this, notificationDetails);

    return parameter.startMode;
  }

  private static int orCombineFlags(ArrayList<Integer> flags) {
    int flag = flags.get(0);
    for (int i = 1; i < flags.size(); i++) {
      flag |= flags.get(i);
    }
    return flag;
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
