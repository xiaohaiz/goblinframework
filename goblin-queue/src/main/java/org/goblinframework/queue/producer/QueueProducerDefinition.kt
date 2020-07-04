package org.goblinframework.queue.producer

import org.goblinframework.queue.QueueLocation

class QueueProducerDefinition(val location: QueueLocation) {
  override fun toString(): String {
    return location.toString()
  }
}