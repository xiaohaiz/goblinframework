package org.goblinframework.core.config;

import org.goblinframework.api.annotation.ThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@ThreadSafe
final class ConfigListenerManager {

  private final ReentrantLock lock = new ReentrantLock();
  private final List<ConfigListener> listeners = new ArrayList<>();

  ConfigListenerManager() {
  }

  void register(@NotNull ConfigListener listener) {
    lock.lock();
    try {
      listeners.add(listener);
    } finally {
      lock.unlock();
    }
  }

  void onConfigChanged() {
    List<ConfigListener> candidates;
    lock.lock();
    try {
      candidates = new ArrayList<>(listeners);
    } finally {
      lock.unlock();
    }
    candidates.parallelStream().forEach(ConfigListener::onConfigChanged);
  }
}
