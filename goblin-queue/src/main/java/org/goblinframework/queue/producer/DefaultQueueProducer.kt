package org.goblinframework.queue.producer

import org.goblinframework.queue.SendResultFuture
import org.goblinframework.queue.api.QueueProducer

class DefaultQueueProducer : QueueProducer {
  override fun sendAsync(data: ByteArray?): SendResultFuture {
    TODO("Not yet implemented")
  }

  override fun send(data: ByteArray?) {
    TODO("Not yet implemented")
  }

}