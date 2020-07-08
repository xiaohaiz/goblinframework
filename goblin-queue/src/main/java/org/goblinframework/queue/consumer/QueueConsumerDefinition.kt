package org.goblinframework.queue.consumer

import org.goblinframework.queue.QueueLocation

class QueueConsumerDefinition constructor(
    val location: QueueLocation,
    val maxConcurrentConsumers: Int,
    val maxPermits: Int,
    val group: String) {

  override fun toString(): String {
    return "[location=${location}, " +
        "group=$group, " +
        "maxConcurrentConsumers=${maxConcurrentConsumers}, " +
        "maxPermits=${maxPermits}]"
  }
}