package org.goblinframework.queue.consumer;

public interface QueueListener {

  void handle(byte[] message);
}
