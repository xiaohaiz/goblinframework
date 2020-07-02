package org.goblinframework.queue.api;

import org.goblinframework.api.concurrent.GoblinFuture;
import org.goblinframework.queue.GoblinMessage;

public interface QueueMessageProducer {

  void send(GoblinMessage message);

  GoblinFuture sendAsync(GoblinMessage message);
}
