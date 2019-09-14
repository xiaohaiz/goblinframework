package org.goblinframework.transport.core.codec

import org.goblinframework.core.transcoder.DecodedObject
import org.springframework.core.convert.converter.Converter

class DecodedObjectToTransportMessage : Converter<DecodedObject, TransportMessage> {

  override fun convert(source: DecodedObject): TransportMessage? {
    return if (source.serializer == 0.toByte()) {
      try {
        TransportMessage(source.mapTo(), 0)
      } catch (ex: Exception) {
        TransportMessage.unrecognized()
      }
    } else {
      TransportMessage().also {
        it.message = source.decoded
        it.serializer = source.serializer
      }
    }
  }
}
