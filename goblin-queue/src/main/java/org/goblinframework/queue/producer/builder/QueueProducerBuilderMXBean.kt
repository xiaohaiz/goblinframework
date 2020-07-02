package org.goblinframework.queue.producer.builder

import org.goblinframework.queue.QueueSystem
import org.goblinframework.queue.api.QueueProducerMXBean
import java.lang.management.PlatformManagedObject

interface QueueProducerBuilderMXBean : PlatformManagedObject {

  fun getSystem(): QueueSystem

  fun getProducerList(): Array<QueueProducerMXBean>
}