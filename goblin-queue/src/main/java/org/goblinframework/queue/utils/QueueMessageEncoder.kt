package org.goblinframework.queue.utils

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.util.JsonUtils
import org.goblinframework.queue.GoblinMessage
import org.goblinframework.queue.GoblinMessageSerializer
import org.goblinframework.queue.GoblinQueueException
import java.io.ByteArrayOutputStream

object QueueMessageEncoder {

  private const val HEADER_SIZE = 1

  fun encode(message: GoblinMessage, serializer: GoblinMessageSerializer): ByteArray? {
    val body = when(serializer) {
      GoblinMessageSerializer.JSON -> JsonUtils.toJson(message).toByteArray(Charsets.UTF_8)
      GoblinMessageSerializer.HESSIAN2 -> SerializerManager.INSTANCE.getSerializer(SerializerMode.HESSIAN2).serialize(message)
      GoblinMessageSerializer.FST -> SerializerManager.INSTANCE.getSerializer(SerializerMode.FST).serialize(message)
      GoblinMessageSerializer.JAVA -> SerializerManager.INSTANCE.getSerializer(SerializerMode.JAVA).serialize(message)
    }

    val outStream = ByteArrayOutputStream()

    val header = ByteArray(HEADER_SIZE)
    header[0] = serializer.type

    outStream.write(header)
    outStream.write(body)

    return outStream.toByteArray()
  }

  fun decode(bytes: ByteArray): GoblinMessage? {
    val type = bytes[0]
    val body = bytes.takeLast(bytes.size - 1).toByteArray()

    try {
      return when(GoblinMessageSerializer.safeParse(type) ?: null) {
        GoblinMessageSerializer.JSON -> JsonUtils.asObject(String(body), GoblinMessage::class.java)
        GoblinMessageSerializer.HESSIAN2 -> SerializerManager.INSTANCE.getSerializer(SerializerMode.HESSIAN2).deserialize(body)
        GoblinMessageSerializer.FST -> SerializerManager.INSTANCE.getSerializer(SerializerMode.FST).deserialize(body)
        GoblinMessageSerializer.JAVA -> SerializerManager.INSTANCE.getSerializer(SerializerMode.JAVA).deserialize(body)
        else -> null
      } as GoblinMessage
    } catch (e: Exception) {
      throw GoblinQueueException("Failed to decode to GoblinMessage: [${bytes}]")
    }
  }
}