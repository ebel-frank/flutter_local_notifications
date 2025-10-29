package com.dexterous.flutterlocalnotifications;

import com.dexterous.flutterlocalnotifications.models.NotificationDetails;

import java.io.Serializable;
import java.util.ArrayList;

public class ForegroundServiceStartParameter implements Serializable {
  public static final String EXTRA =
      "com.dexterous.flutterlocalnotifications.ForegroundServiceStartParameter";

  public final NotificationDetails notificationData;
  public final String notificationJson;
  public final int startMode;
  public final ArrayList<Integer> foregroundServiceTypes;

  public ForegroundServiceStartParameter(
      NotificationDetails notificationData,
      String notificationJson,
      int startMode,
      ArrayList<Integer> foregroundServiceTypes) {
    this.notificationData = notificationData;
    this.notificationJson = notificationJson;
    this.startMode = startMode;
    this.foregroundServiceTypes = foregroundServiceTypes;
  }

  @Override
  public String toString() {
    return "ForegroundServiceStartParameter{"
        + "notificationData="
        + notificationData
        + ", notificationJson="
        + notificationJson
        + ", startMode="
        + startMode
        + ", foregroundServiceTypes="
        + foregroundServiceTypes
        + '}';
  }
}
