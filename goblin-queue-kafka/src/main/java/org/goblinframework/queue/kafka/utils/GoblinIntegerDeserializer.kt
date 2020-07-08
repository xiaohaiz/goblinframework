package org.goblinframework.queue.kafka.utils

import org.apache.kafka.common.serialization.Deserializer
import org.slf4j.LoggerFactory

class GoblinIntegerDeserializer : Deserializer<Int?> {
  override fun configure(configs: Map<String?, *>?, isKey: Boolean) {}
  override fun deserialize(topic: String, data: ByteArray): Int? {
    if (data.size != 4) {
      logger.error("deserialize failed for topic {}, data {}", topic, data)
      return null
    }
    var value = 0
    for (b in data) {
      value = value shl 8
      value = value or (b.toInt() and 0xFF)
    }
    return value
  }

  override fun close() {}

  companion object {
    private val logger = LoggerFactory.getLogger(GoblinIntegerDeserializer::class.java)
  }
}