package org.goblinframework.registry.zookeeper.client

import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.ByteBufUtil
import org.I0Itec.zkclient.exception.ZkMarshallingError
import org.I0Itec.zkclient.serialize.ZkSerializer
import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject
import org.goblinframework.core.serialization.Serializer
import org.goblinframework.core.transcoder.Transcoder
import org.goblinframework.core.transcoder.TranscoderSetting
import java.io.ByteArrayInputStream

@GoblinManagedBean(type = "registry.zookeeper")
class ZkTranscoder internal constructor(private val serializer: Serializer)
  : GoblinManagedObject(), ZkTranscoderMXBean, ZkSerializer {

  override fun serialize(data: Any): ByteArray {
    val buf = ByteBufAllocator.DEFAULT.buffer()
    try {
      ByteBufOutputStream(buf).use {
        val transcoder = TranscoderSetting.builder().serializer(serializer).build().transcoder()
        transcoder.encode(it, data)
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
        Transcoder.decode(it).result
      }
    } catch (ex: Exception) {
      throw ZkMarshallingError(ex)
    }
  }

}