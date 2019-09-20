package org.goblinframework.core.util;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicPositiveInteger extends Number {
  private static final long serialVersionUID = 3799803626714841859L;

  private final AtomicInteger i;

  public AtomicPositiveInteger() {
    this(0);
  }

  public AtomicPositiveInteger(int initialValue) {
    if (initialValue < 0) {
      throw new IllegalArgumentException();
    }
    i = new AtomicInteger(initialValue);
  }

  public final int getAndIncrement() {
    for (; ; ) {
      int current = i.get();
      int next = (current >= Integer.MAX_VALUE ? 0 : current + 1);
      if (i.compareAndSet(current, next)) {
        return current;
      }
    }
  }

  public final int getAndDecrement() {
    for (; ; ) {
      int current = i.get();
      int next = (current <= 0 ? Integer.MAX_VALUE : current - 1);
      if (i.compareAndSet(current, next)) {
        return current;
      }
    }
  }

  public final int incrementAndGet() {
    for (; ; ) {
      int current = i.get();
      int next = (current >= Integer.MAX_VALUE ? 0 : current + 1);
      if (i.compareAndSet(current, next)) {
        return next;
      }
    }
  }

  public final int decrementAndGet() {
    for (; ; ) {
      int current = i.get();
      int next = (current <= 0 ? Integer.MAX_VALUE : current - 1);
      if (i.compareAndSet(current, next)) {
        return next;
      }
    }
  }

  public final int get() {
    return i.get();
  }

  public final void set(int newValue) {
    if (newValue < 0) {
      throw new IllegalArgumentException();
    }
    i.set(newValue);
  }

  public final int getAndSet(int newValue) {
    if (newValue < 0) {
      throw new IllegalArgumentException();
    }
    return i.getAndSet(newValue);
  }

  public final int getAndAdd(int delta) {
    if (delta < 0) {
      throw new IllegalArgumentException();
    }
    for (; ; ) {
      int current = i.get();
      int next = (current >= Integer.MAX_VALUE - delta + 1 ? delta - 1 : current + delta);
      if (i.compareAndSet(current, next)) {
        return current;
      }
    }
  }

  public final int addAndGet(int delta) {
    if (delta < 0) {
      throw new IllegalArgumentException();
    }
    for (; ; ) {
      int current = i.get();
      int next = (current >= Integer.MAX_VALUE - delta + 1 ? delta - 1 : current + delta);
      if (i.compareAndSet(current, next)) {
        return next;
      }
    }
  }

  public final boolean compareAndSet(int expect, int update) {
    if (update < 0) {
      throw new IllegalArgumentException();
    }
    return i.compareAndSet(expect, update);
  }

  public final boolean weakCompareAndSet(int expect, int update) {
    if (update < 0) {
      throw new IllegalArgumentException();
    }
    return i.weakCompareAndSet(expect, update);
  }

  @Override
  public int intValue() {
    return i.intValue();
  }

  @Override
  public long longValue() {
    return i.longValue();
  }

  @Override
  public float floatValue() {
    return i.floatValue();
  }

  @Override
  public double doubleValue() {
    return i.doubleValue();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AtomicPositiveInteger that = (AtomicPositiveInteger) o;
    return i.equals(that.i);
  }

  @Override
  public int hashCode() {
    return Objects.hash(i);
  }
}
