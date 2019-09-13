package org.goblinframework.transport.core.codec

import org.goblinframework.core.transcoder.DecodedObject
import org.goblinframework.core.util.JsonUtils
import org.goblinframework.transport.core.protocol.*
import org.slf4j.LoggerFactory
import org.springframework.core.convert.converter.Converter

class DecodedObjectToTransportMessage : Converter<DecodedObject, TransportMessage> {

  companion object {
    private val logger = LoggerFactory.getLogger(DecodedObjectToTransportMessage::class.java)

    private val buffer = mutableMapOf<Byte, Class<*>>()

    init {
      buffer[1] = HandshakeRequest::class.java
      buffer[-1] = HandshakeResponse::class.java
      buffer[2] = HeartbeatRequest::class.java
      buffer[-2] = HeartbeatResponse::class.java
      buffer[3] = ShutdownRequest::class.java
      buffer[4] = TransportRequest::class.java
      buffer[-4] = TransportResponse::class.java
    }
  }

  override fun convert(source: DecodedObject): TransportMessage? {
    return if (source.serializer == 0.toByte()) {
      try {
        transform(source.decoded as String)
      } catch (ex: Exception) {
        logger.error("Exception raised when transforming decoded object", ex)
        TransportMessage.unrecognized()
      }
    } else {
      TransportMessage().also {
        it.message = source.decoded
        it.serializer = source.serializer
      }
    }
  }

  private fun transform(s: String): TransportMessage {
    val mapper = JsonUtils.createObjectMapper()
    val root = mapper.readTree(s)
    if (!root.isObject) {
      logger.error("Unrecognized transport message received: $s")
      return TransportMessage.unrecognized()
    }
    val idNode = root.get("id")
    if (idNode == null || !idNode.isNumber) {
      logger.error("Unrecognized transport message received: $s")
      return TransportMessage.unrecognized()
    }
    val id = idNode.numberValue().toByte()
    val clazz = buffer[id]
    if (clazz == null) {
      logger.error("Unrecognized id in transport message: $s")
      return TransportMessage.unrecognized()
    }
    val parsed = mapper.readValue(root.traverse(), clazz)
    return TransportMessage(parsed, 0)
  }
}
