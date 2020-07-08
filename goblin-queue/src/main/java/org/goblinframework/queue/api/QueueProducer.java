package org.goblinframework.queue.api;

import org.goblinframework.queue.SendResultFuture;

public interface QueueProducer {
  void send(byte[] data);

  SendResultFuture sendAsync(byte[] data);
}
