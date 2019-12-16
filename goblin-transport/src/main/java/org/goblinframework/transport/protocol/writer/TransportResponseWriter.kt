package org.goblinframework.transport.protocol.writer

import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.core.CompressorMode
import org.goblinframework.core.compression.CompressorManager
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.transport.protocol.TransportResponse
import org.goblinframework.transport.protocol.TransportResponseException
import org.goblinframework.transport.protocol.reader.TransportRequestReader
import java.util.*

@ThreadSafe
class TransportResponseWriter(reader: TransportRequestReader) {

  private val response: TransportResponse

  init {
    val request = reader.request()
    response = TransportResponse()
    response.requestId = request.requestId
    response.requestCreateTime = request.requestCreateTime
    response.responseCreateTime = System.currentTimeMillis()
    response.code = 0
  }

  @Synchronized
  fun response(): TransportResponse {
    return response
  }

  @Synchronized
  fun reset() {
    response.code = 0
    response.compressor = 0
    response.serializer = 0
    response.payload = null
    response.hasPayload = false
    response.rawPayload = false
    response.extensions = null
  }

  @Synchronized
  fun writeException(tme: TransportResponseException?) {
    if (tme == null) {
      return
    }
    reset()
    if (response.extensions == null) {
      response.extensions = LinkedHashMap()
    }
    response.code = tme.code.id
    val cause = tme.cause
    if (cause != null) {
      response.extensions["EXCEPTION_CAUSE"] = cause.javaClass.name
    }
    val message = tme.message
    if (message != null) {
      response.extensions["EXCEPTION_MESSAGE"] = message
    }
  }

  @Synchronized
  fun writePayload(payload: Any?) {
    if (payload == null) {
      return
    }
    check(!response.hasPayload) { "Payload already set, try reset if required" }
    response.hasPayload = true
    when (payload) {
      is ByteArray -> {
        response.payload = payload
        response.rawPayload = true
      }
      is String -> {
        response.payload = payload.toByteArray(Charsets.UTF_8)
      }
      else -> {
        response.serializer = 2
        val serializer = SerializerManager.INSTANCE.getSerializer(2)!!
        response.payload = serializer.serialize(payload)
      }
    }
    compressIfNecessary()
  }

  private fun compressIfNecessary() {
    val size = response.payload.size
    if (size >= 4194304) {
      val compressor = CompressorManager.INSTANCE.getCompressor(CompressorMode.GZIP)
      val compressed = compressor.compress(response.payload)
      if (compressed.size < size) {
        response.compressor = CompressorMode.GZIP.id
        response.payload = compressed
      }
    }
  }
}