package org.goblinframework.queue.producer

import org.goblinframework.core.service.GoblinManagedObject
import java.lang.management.PlatformManagedObject

interface QueueProducerBuilderManagerMXBean : PlatformManagedObject {
  fun getQueueProducerBuilderList(): Array<QueueProducerBuilderMXBean>
}