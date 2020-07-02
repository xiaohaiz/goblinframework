package org.goblinframework.queue.api;

import org.goblinframework.api.concurrent.GoblinFuture;

public interface QueueProducer {
  void send(byte[] data);

  GoblinFuture sendAsync(byte[] data);
}
