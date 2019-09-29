package org.goblinframework.core.util;

import org.goblinframework.core.concurrent.GoblinInterruptedException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;

abstract public class ThreadUtils extends org.apache.commons.lang3.ThreadUtils {

  public static void joinCurrentThread() {
    try {
      Thread.currentThread().join();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    }
  }

  public static void awaitUninterruptibly(@NotNull final CountDownLatch latch) {
    try {
      latch.await();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    }
  }
}
