package org.goblinframework.queue.api;

public interface QueueListener {

  void handle(byte[] message);
}
