package org.goblinframework.queue.api;

import org.goblinframework.queue.GoblinMessage;
import org.goblinframework.queue.SendResultFuture;

public interface QueueMessageProducer {

  void send(GoblinMessage message);

  SendResultFuture sendAsync(GoblinMessage message);
}
