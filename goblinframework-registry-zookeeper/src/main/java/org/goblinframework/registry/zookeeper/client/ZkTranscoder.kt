package org.goblinframework.registry.zookeeper.client

import org.I0Itec.zkclient.exception.ZkMarshallingError
import org.I0Itec.zkclient.serialize.ZkSerializer
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.serialization.Serializer
import org.goblinframework.core.transcoder.TranscoderUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@GoblinManagedBean("REGISTRY.ZOOKEEPER")
class ZkTranscoder internal constructor(private val serializer: Serializer)
  : GoblinManagedObject(), ZkSerializer {

  override fun serialize(data: Any): ByteArray {
    try {
      return ByteArrayOutputStream(512).use {
        val transcoder = TranscoderUtils.encoder().serializer(serializer).buildTranscoder()
        transcoder.encode(it, data)
        it.toByteArray()
      }
    } catch (ex: Exception) {
      throw ZkMarshallingError(ex)
    }
  }

  override fun deserialize(bytes: ByteArray): Any {
    try {
      return ByteArrayInputStream(bytes).use {
        TranscoderUtils.decode(it).result
      }
    } catch (ex: Exception) {
      throw ZkMarshallingError(ex)
    }
  }

}