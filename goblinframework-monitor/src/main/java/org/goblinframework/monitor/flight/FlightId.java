package org.goblinframework.monitor.flight;

import org.goblinframework.api.common.ReferenceCount;
import org.goblinframework.core.util.RandomUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class FlightId implements org.goblinframework.core.monitor.FlightId, ReferenceCount {

  private final String id = RandomUtils.nextObjectId();
  private final AtomicInteger referenceCount = new AtomicInteger();

  FlightId() {
  }

  @NotNull
  @Override
  public String id() {
    return id;
  }

  @Override
  public int count() {
    return referenceCount.get();
  }

  @Override
  public void retain() {
    referenceCount.incrementAndGet();
  }

  @Override
  public boolean release() {
    return referenceCount.decrementAndGet() <= 0;
  }

  @Override
  public void retain(int increment) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean release(int decrement) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FlightId flightId = (FlightId) o;
    return id.equals(flightId.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
