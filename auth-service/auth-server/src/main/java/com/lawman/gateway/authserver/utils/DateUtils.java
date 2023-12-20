package com.lawman.gateway.authserver.utils;

import java.time.LocalDate;

public interface DateUtils {
  static String getCurrentDateString() {
    return LocalDate.now().toString();
  }
}
