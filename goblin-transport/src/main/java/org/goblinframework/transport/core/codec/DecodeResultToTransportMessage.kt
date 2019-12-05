package org.goblinframework.transport.core.codec

import com.fasterxml.jackson.databind.JsonNode
import org.goblinframework.core.transcoder.DecodeResult
import org.goblinframework.core.transcoder.GoblinTranscoderException
import org.goblinframework.core.util.JsonUtils
import org.goblinframework.transport.core.protocol.*
import org.springframework.core.convert.converter.Converter

class DecodeResultToTransportMessage : Converter<DecodeResult, TransportMessage> {


  companion object {
    private val map = mutableMapOf<String, Class<*>>()

    init {
      map["1"] = HandshakeRequest::class.java
      map["-1"] = HandshakeResponse::class.java
      map["2"] = HeartbeatRequest::class.java
      map["-2"] = HeartbeatResponse::class.java
      map["3"] = ShutdownRequest::class.java
      map["4"] = TransportRequest::class.java
      map["-4"] = TransportResponse::class.java
    }
  }

  override fun convert(source: DecodeResult): TransportMessage? {
    return if (source.serializer == 0.toByte()) {
      try {
        TransportMessage(mapTo(source), 0)
      } catch (ex: Exception) {
        TransportMessage.unrecognized()
      }
    } else {
      TransportMessage().also {
        it.message = source.result
        it.serializer = source.serializer
      }
    }
  }

  private fun mapTo(source: DecodeResult): Any {
    val mapper = JsonUtils.getDefaultObjectMapper()
    var root: JsonNode? = null
    try {
      if (source.result is ByteArray) {
        root = mapper.readTree(source.result as ByteArray)
      } else if (source.result is String) {
        root = mapper.readTree(source.result as String)
      }
    } catch (ignore: Exception) {
    }

    if (root == null || !root.isObject) {
      throw GoblinTranscoderException("Decoded object is not JSON object")
    }
    val idNode = root.get("_id")
    if (idNode == null || idNode.isNull) {
      throw GoblinTranscoderException("Decoded object has no valid _id field")
    }
    val id = idNode.asText()
    val type = map[id]
        ?: throw GoblinTranscoderException("Decoded object [$id] not registered")
    try {
      return mapper.readValue(root.traverse(), type)
    } catch (ex: Exception) {
      throw GoblinTranscoderException(ex)
    }

  }
}
