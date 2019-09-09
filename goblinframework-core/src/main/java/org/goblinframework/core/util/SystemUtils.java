package org.goblinframework.core.util;

abstract public class SystemUtils extends org.apache.commons.lang3.SystemUtils {

  public static int estimateThreads() {
    int processors = Runtime.getRuntime().availableProcessors();
    return Math.max(processors, 2);
  }
}
