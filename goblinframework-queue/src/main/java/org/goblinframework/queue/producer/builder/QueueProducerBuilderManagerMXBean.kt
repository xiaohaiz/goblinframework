package org.goblinframework.queue.producer.builder

import java.lang.management.PlatformManagedObject

interface QueueProducerBuilderManagerMXBean : PlatformManagedObject {
  fun getQueueProducerBuilderList(): Array<QueueProducerBuilderMXBean>
}