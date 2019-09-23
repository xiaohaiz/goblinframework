package org.goblinframework.registry.zookeeper.client

import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.ByteBufUtil
import org.I0Itec.zkclient.exception.ZkMarshallingError
import org.I0Itec.zkclient.serialize.ZkSerializer
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.serialization.Serializer
import org.goblinframework.core.transcoder.TranscoderUtils
import java.io.ByteArrayInputStream

@GoblinManagedBean(type = "registry.zookeeper")
class ZkTranscoder internal constructor(private val serializer: Serializer)
  : GoblinManagedObject(), ZkTranscoderMXBean, ZkSerializer {

  override fun serialize(data: Any): ByteArray {
    val input = if (data is String) return data.toByteArray(Charsets.UTF_8) else data
    val buf = ByteBufAllocator.DEFAULT.buffer()
    try {
      ByteBufOutputStream(buf).use {
        val transcoder = TranscoderUtils.encoder().serializer(serializer).buildTranscoder()
        transcoder.encode(it, input)
      }
      return ByteBufUtil.getBytes(buf)
    } catch (ex: Exception) {
      throw ZkMarshallingError(ex)
    } finally {
      buf.release()
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