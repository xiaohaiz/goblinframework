package org.goblinframework.queue.producer

import org.goblinframework.queue.GoblinMessageSerializer
import org.goblinframework.queue.QueueLocation

class QueueProducerDefinition(val location: QueueLocation, val serializer: GoblinMessageSerializer) {

  override fun toString(): String {
    return location.toString()
  }
}