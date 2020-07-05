package org.goblinframework.queue.consumer

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.queue.api.QueueConsumer
import org.goblinframework.queue.api.QueueConsumerMXBean
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read

@Singleton
class QueueConsumerManager : GoblinManagedObject(), QueueConsumerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = QueueConsumerManager()
  }

  private val locker = ReentrantReadWriteLock()
  private val consumers = mutableListOf<QueueConsumer>()

  override fun getConsumers(): Array<QueueConsumerMXBean> {
    locker.read {
      return consumers.map { it as QueueConsumerMXBean }.toTypedArray()
    }
  }

  fun register(consumer: QueueConsumer) {

  }

}