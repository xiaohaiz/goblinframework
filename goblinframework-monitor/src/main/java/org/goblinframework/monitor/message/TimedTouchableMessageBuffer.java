package org.goblinframework.monitor.message;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TimedTouchableMessageBuffer<E extends TouchableMessage> {

  private final Map<Long, Map<Long, E>> buffer = new ConcurrentHashMap<>(3);
  private final TouchableMessageFactory<E> touchableMessageFactory;

  public TimedTouchableMessageBuffer(@NotNull TouchableMessageFactory<E> touchableMessageFactory) {
    this.touchableMessageFactory = touchableMessageFactory;
    this.initialize();
    TimedTouchableMessageBufferManager manager = TimedTouchableMessageBufferManager.INSTANCE;
    manager.registerAction(() -> {
      long epochSecond = Instant.now().getEpochSecond();
      long epochHour = epochSecond / 3600;

      Set<Long> expired = buffer.keySet().stream()
          .filter(e -> Math.abs(e - epochHour) >= 2)
          .collect(Collectors.toSet());

      expired.forEach(buffer::remove);

      long nextHour = epochHour + 1;
      if (!buffer.containsKey(nextHour)) {
        buffer.put(nextHour, initializeMap(nextHour));
      }
    });
  }

  public E getAndTouch(long epochSecond) {
    long epochHour = epochSecond / 3600;
    Map<Long, E> map = buffer.get(epochHour);
    if (map == null) {
      return null;
    }
    E message = map.get(epochSecond);
    if (message != null) {
      message.touch();
    }
    return message;
  }

  public synchronized Map<Long, E> drainBefore(long epochSecond) {
    Map<Long, E> result = new HashMap<>();
    long epochHour = epochSecond / 3600;
    new LinkedHashSet<>(buffer.keySet()).stream()
        .mapToLong(e -> e)
        .filter(e -> e <= epochHour)
        .forEach(e -> {
          Map<Long, E> map = buffer.get(e);
          if (map != null && !map.isEmpty()) {
            for (Iterator<Map.Entry<Long, E>> it = map.entrySet().iterator(); it.hasNext(); ) {
              Map.Entry<Long, E> entry = it.next();
              if (entry.getKey() > epochSecond) {
                continue;
              }
              E message = entry.getValue();
              if (message.isTouched()) {
                result.put(entry.getKey(), message);
              }
              it.remove();
            }
          }
        });
    return result;
  }

  private void initialize() {
    Instant current = Instant.now();
    long epochSecond = current.getEpochSecond();
    long epochHour = epochSecond / 3600;
    for (long l = epochHour - 1; l <= epochHour + 1; l++) {
      buffer.put(l, initializeMap(l));
    }
  }

  private Map<Long, E> initializeMap(long epochHour) {
    Map<Long, E> map = new LinkedHashMap<>(3600);
    long s = epochHour * 3600;
    for (int i = 0; i < 3600; i++) {
      map.put(s + i, touchableMessageFactory.newInstance());
    }
    return map;
  }
}
