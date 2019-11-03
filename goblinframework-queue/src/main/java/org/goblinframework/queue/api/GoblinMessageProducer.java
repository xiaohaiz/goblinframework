package org.goblinframework.queue.api;

import org.goblinframework.queue.GoblinMessage;

public interface GoblinMessageProducer extends QueueProducer {

  void send(GoblinMessage message);
}
