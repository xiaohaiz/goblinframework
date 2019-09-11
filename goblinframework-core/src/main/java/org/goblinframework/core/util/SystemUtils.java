package org.goblinframework.core.util;

import org.apache.commons.lang3.Validate;

abstract public class SystemUtils extends org.apache.commons.lang3.SystemUtils {

  public static int estimateThreads() {
    int processors = Runtime.getRuntime().availableProcessors();
    return Math.max(processors, 2);
  }

  public static int estimateThreads(int threads) {
    Validate.isTrue(threads >= 0);
    return threads == 0 ? estimateThreads() : threads;
  }
}
