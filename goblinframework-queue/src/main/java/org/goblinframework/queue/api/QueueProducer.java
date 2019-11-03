package org.goblinframework.queue.api;

public interface QueueProducer {
  void send(byte[] data);
}
