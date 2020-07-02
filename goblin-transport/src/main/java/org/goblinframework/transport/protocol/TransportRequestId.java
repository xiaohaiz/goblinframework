package org.goblinframework.transport.protocol;

import java.util.concurrent.atomic.AtomicLong;

final public class TransportRequestId extends AtomicLong {

  private static final TransportRequestId instance = new TransportRequestId();

  private TransportRequestId() {
    super(0);
  }

  public static long nextId() {
    return instance.getAndIncrement();
  }
}
