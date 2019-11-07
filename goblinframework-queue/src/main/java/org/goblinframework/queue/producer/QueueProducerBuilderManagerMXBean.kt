package org.goblinframework.queue.producer

import java.lang.management.PlatformManagedObject

interface QueueProducerBuilderManagerMXBean : PlatformManagedObject {
  fun getQueueProducerBuilderList(): Array<QueueProducerBuilderMXBean>
}