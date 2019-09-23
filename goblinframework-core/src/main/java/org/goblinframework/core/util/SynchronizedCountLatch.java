package org.goblinframework.core.util;

import org.goblinframework.core.exception.GoblinInterruptedException;
import org.goblinframework.core.exception.GoblinTimeoutException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

final public class SynchronizedCountLatch {

  private final AtomicInteger count;

  public SynchronizedCountLatch() {
    this(0);
  }

  public SynchronizedCountLatch(int initialValue) {
    count = new AtomicInteger(initialValue);
  }

  public int getCount() {
    return count.get();
  }

  public void countUp() {
    synchronized (count) {
      count.incrementAndGet();
      count.notifyAll();
    }
  }

  public void countDown() {
    synchronized (count) {
      if (count.get() > 0) {
        count.decrementAndGet();
      }
      count.notifyAll();
    }
  }

  public void await() throws InterruptedException {
    synchronized (count) {
      while (count.get() != 0) {
        count.wait();
      }
    }
  }

  public void await(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
    long max = unit.toMillis(timeout);
    long start = System.currentTimeMillis();
    long current = start;
    synchronized (count) {
      while (count.get() != 0) {
        count.wait(max - (current - start));
        current = System.currentTimeMillis();
        if (current - start >= max) {
          throw new TimeoutException();
        }
      }
    }
  }

  public void awaitUninterruptibly() {
    try {
      await();
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    }
  }

  public void awaitUninterruptibly(long timeout, TimeUnit unit) {
    try {
      await(timeout, unit);
    } catch (InterruptedException ex) {
      throw new GoblinInterruptedException(ex);
    } catch (TimeoutException ex) {
      throw new GoblinTimeoutException(ex);
    }
  }

  @Override
  public String toString() {
    return getClass().getName() + "[count=" + count.get() + "]";
  }
}
