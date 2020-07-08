package org.goblinframework.queue.api;

import org.goblinframework.queue.GoblinMessage;

public interface QueueMessageListener {

  void handle(GoblinMessage message);
}
