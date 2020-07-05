package org.goblinframework.queue.api;

import org.goblinframework.queue.QueueSystem;

import java.lang.management.PlatformManagedObject;

public interface QueueConsumerMXBean extends PlatformManagedObject {

  QueueSystem getQueueSystem();

  String getConnectionName();

  String getQueueName();

  long getFetched();

  long getTransformed();

  long getDiscarded();

  long getPublished();

  long getReceived();

  long getHandled();

  long getSuccess();

  long getFailure();
}
