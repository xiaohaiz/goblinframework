package org.goblinframework.queue.api;

import org.goblinframework.queue.GoblinMessage;

public interface QueueMessageProducer {

  void send(GoblinMessage message);

  void sendAsync(GoblinMessage message);
}
