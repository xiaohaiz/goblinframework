package org.goblinframework.queue.kafka.client

import java.lang.management.PlatformManagedObject

interface KafkaQueueProducerClientMXBean : PlatformManagedObject {

  fun getConnection(): String

  fun shutdown()
}