package org.goblinframework.transport.protocol.writer

import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.core.CompressorMode
import org.goblinframework.core.compression.CompressorManager
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.transport.protocol.TransportRequest

@ThreadSafe
class TransportRequestWriter(private val request: TransportRequest) {

  @Synchronized
  fun request(): TransportRequest {
    return request
  }

  @Synchronized
  fun reset() {
    request.compressor = 0
    request.serializer = 0
    request.payload = null
    request.hasPayload = false
    request.rawPayload = false
    request.extensions = null
  }

  @Synchronized
  fun writePayload(payload: Any?) {
    if (payload == null) {
      return
    }
    check(!request.hasPayload) { "Payload already set" }
    request.hasPayload = true
    when (payload) {
      is ByteArray -> {
        request.payload = payload
        request.rawPayload = true
      }
      is String -> {
        request.payload = payload.toByteArray(Charsets.UTF_8)
      }
      else -> {
        request.serializer = 2
        val serializer = SerializerManager.INSTANCE.getSerializer(2)!!
        request.payload = serializer.serialize(payload)
      }
    }
    compressIfNecessary()
  }

  private fun compressIfNecessary() {
    val size = request.payload.size
    if (size >= 4194304) {
      val compressor = CompressorManager.INSTANCE.getCompressor(CompressorMode.GZIP)
      val compressed = compressor.compress(request.payload)
      if (compressed.size < size) {
        request.compressor = CompressorMode.GZIP.id
        request.payload = compressed
      }
    }
  }
}