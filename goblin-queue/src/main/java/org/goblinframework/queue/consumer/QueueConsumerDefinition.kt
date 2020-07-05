package org.goblinframework.queue.consumer

import org.goblinframework.queue.QueueLocation

class QueueConsumerDefinition constructor(
    val location: QueueLocation,
    val maxConcurrentConsumers: Int,
    val maxPermits: Int) {

  override fun toString(): String {
    return "[location=${location}, " +
        "maxConcurrentConsumers=${maxConcurrentConsumers}, " +
        "maxPermits=${maxPermits}]"
  }
}