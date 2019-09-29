package org.goblinframework.core.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GoblinFluxDataFeeder<E> {

  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private final List<DataListener<E>> listeners = new ArrayList<>();

  public void onData(E data) {
    List<DataListener<E>> list;
    lock.readLock().lock();
    try {
      list = new ArrayList<>(listeners);
    } finally {
      lock.readLock().unlock();
    }
    list.forEach(e -> e.onData(data));
  }

  public void complete() {
    List<DataListener<E>> list;
    lock.readLock().lock();
    try {
      list = new ArrayList<>(listeners);
    } finally {
      lock.readLock().unlock();
    }
    list.forEach(DataListener::complete);
  }

  void addListener(@NotNull DataListener<E> listener) {
    lock.writeLock().lock();
    try {
      listeners.add(listener);
    } finally {
      lock.writeLock().unlock();
    }
  }

  void removeListener(@NotNull DataListener<E> listener) {
    lock.writeLock().lock();
    try {
      listeners.removeIf(e -> e == listener);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public interface DataListener<E> {

    void onData(E data);

    void complete();

  }
}
