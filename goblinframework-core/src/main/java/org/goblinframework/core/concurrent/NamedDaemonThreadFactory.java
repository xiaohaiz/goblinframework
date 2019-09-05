package org.goblinframework.core.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

final public class NamedDaemonThreadFactory implements ThreadFactory {

  private static final Map<String, NamedDaemonThreadFactory> buffer = new ConcurrentHashMap<>();

  public static NamedDaemonThreadFactory getInstance(@NotNull String name) {
    return buffer.computeIfAbsent(name, NamedDaemonThreadFactory::new);
  }

  private final String name;
  private final AtomicLong counter = new AtomicLong(0);

  private NamedDaemonThreadFactory(@NotNull String name) {
    this.name = name;
  }

  @Override
  public Thread newThread(@NotNull Runnable r) {
    Thread thread = new Thread(r, name + "-" + counter.incrementAndGet());
    thread.setDaemon(true);
    return thread;
  }
}
