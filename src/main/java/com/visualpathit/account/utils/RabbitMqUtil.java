package com.visualpathit.account.utils;

import com.visualpathit.account.beans.Components;

public class RabbitMqUtil {

  private static Components object;

  public static void setComponents(Components components) {
    object = components;
  }

  public static void clear() {
    object = null;
  }

  public static String getRabbitMqHost() {
    return object != null ? object.getRabbitMqHost() : null;
  }

  public static String getRabbitMqPort() {
    return object != null ? object.getRabbitMqPort() : null;
  }

  public static String getRabbitMqUser() {
    return object != null ? object.getRabbitMqUser() : null;
  }

  public static String getRabbitMqPassword() {
    return object != null ? object.getRabbitMqPassword() : null;
  }

  public static boolean isConfigured() {
    return object != null;
  }
}