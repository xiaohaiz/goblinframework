package org.goblinframework.api.service;

import org.goblinframework.api.common.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
final class ObjectNameGenerator {

  private static final String DOMAIN = "org.goblinframework";

  private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private static final Map<Tuple, AtomicInteger> buffer = new HashMap<>();

  @NotNull
  static ObjectName generate(@NotNull String type, @NotNull String name) {
    AtomicInteger counter = getCounter(new Tuple(type, name));
    long next = counter.getAndIncrement();
    String n = (next > 0) ? (name + "_" + next) : name;
    try {
      return new ObjectName(DOMAIN + ":type=" + type + ",name=" + n);
    } catch (MalformedObjectNameException ex) {
      throw new GoblinServiceException(ex);
    }
  }

  private static AtomicInteger getCounter(Tuple id) {
    lock.readLock().lock();
    try {
      AtomicInteger i = buffer.get(id);
      if (i != null) return i;
    } finally {
      lock.readLock().unlock();
    }
    lock.writeLock().lock();
    try {
      return buffer.computeIfAbsent(id, _t -> new AtomicInteger());
    } finally {
      lock.writeLock().unlock();
    }
  }

  private static class Tuple {
    @NotNull private final String type;
    @NotNull private final String name;

    private Tuple(@NotNull String type, @NotNull String name) {
      this.type = type;
      this.name = name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Tuple tuple = (Tuple) o;
      return type.equals(tuple.type) && name.equals(tuple.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(type, name);
    }
  }
}
