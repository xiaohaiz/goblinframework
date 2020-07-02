package org.goblinframework.queue.consumer;

import org.goblinframework.queue.GoblinMessage;

public interface QueueMessageListener {

  void handle(GoblinMessage message);
}
