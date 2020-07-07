package org.goblinframework.queue.api;

import java.lang.management.PlatformManagedObject;

public interface QueueConsumerMXBean extends PlatformManagedObject {

  String getConsumerType();

  String getLocation();

  int getMaxConcurrentConsumers();

  int getMaxPermits();

  String getGroup();

  long getFetched();

  long getTransformed();

  long getDiscarded();

  long getPublished();

  long getReceived();

  long getHandled();

  long getSuccess();

  long getFailure();
}
