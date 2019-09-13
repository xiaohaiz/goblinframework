package org.goblinframework.core.util;

import org.goblinframework.core.exception.GoblinInterruptedException;

abstract public class ThreadUtils extends org.apache.commons.lang3.ThreadUtils {

  public static void joinCurrentThread() {
    try {
      Thread.currentThread().join();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    }
  }

}
