package com.dexterous.flutterlocalnotifications;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

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

    if (parameter == null) {
      Log.e("ForegroundService", "No parameter found, stopping service");
      stopSelf(startId);
      return START_NOT_STICKY;
    }

    Notification notification =
        FlutterLocalNotificationsPlugin.createNotification(this, parameter.notificationData);
    if (parameter.foregroundServiceTypes != null
        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      startForeground(
          parameter.notificationData.id,
          notification,
          orCombineFlags(parameter.foregroundServiceTypes));
    } else {
      startForeground(parameter.notificationData.id, notification);
    }
    new Thread(() -> {
      try {
        FlutterLocalNotificationsPlugin.showNotification(this, parameter.notificationData);
        FlutterLocalNotificationsPlugin.scheduleNextNotification(this, parameter.notificationData);
      } catch (Exception e) {
        Log.e("ForegroundService", "Error handling notification", e);
      } finally {
        stopSelf(startId);
      }
    }).start();
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
