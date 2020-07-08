package org.goblinframework.queue.consumer.builder

import java.lang.management.PlatformManagedObject

interface QueueConsumerBuilderManagerMXBean : PlatformManagedObject {
  fun getQueueConsumerBuilderList(): Array<QueueConsumerBuilderMXBean>
}