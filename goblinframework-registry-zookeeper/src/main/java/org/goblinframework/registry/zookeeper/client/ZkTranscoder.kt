package org.goblinframework.registry.zookeeper.client

import org.I0Itec.zkclient.exception.ZkMarshallingError
import org.I0Itec.zkclient.serialize.ZkSerializer
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.serialization.Serializer
import org.goblinframework.core.transcoder.Transcoder1
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@GoblinManagedBean("REGISTRY.ZOOKEEPER")
class ZkTranscoder internal constructor(private val serializer: Serializer)
  : GoblinManagedObject(), ZkSerializer {

  override fun serialize(data: Any): ByteArray {
    try {
      return ByteArrayOutputStream(512).use {
        Transcoder1.encode(it, data, serializer)
        it.toByteArray()
      }
    } catch (ex: Exception) {
      throw ZkMarshallingError(ex)
    }
  }

  override fun deserialize(bytes: ByteArray): Any {
    try {
      return ByteArrayInputStream(bytes).use {
        Transcoder1.decode(it).decoded
      }
    } catch (ex: Exception) {
      throw ZkMarshallingError(ex)
    }
  }

  internal fun close() {
    unregisterIfNecessary()
  }
}