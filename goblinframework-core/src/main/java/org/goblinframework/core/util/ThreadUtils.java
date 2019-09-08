package org.goblinframework.core.util;

final public class ThreadUtils extends org.apache.commons.lang3.ThreadUtils {

  public static int estimateThreads() {
    int processors = Runtime.getRuntime().availableProcessors();
    return Math.max(processors, 2);
  }

}
