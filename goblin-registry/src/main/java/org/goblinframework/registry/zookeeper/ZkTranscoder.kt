package org.goblinframework.registry.zookeeper

import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.ByteBufOutputStream
import io.netty.buffer.ByteBufUtil
import io.netty.util.ReferenceCountUtil
import org.I0Itec.zkclient.exception.ZkMarshallingError
import org.I0Itec.zkclient.serialize.ZkSerializer
import org.goblinframework.core.serialization.Serializer
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.transcoder.Transcoder
import org.goblinframework.core.transcoder.TranscoderSetting

@GoblinManagedBean(type = "Registry")
class ZkTranscoder internal constructor(serializer: Serializer)
  : GoblinManagedObject(), ZkTranscoderMXBean, ZkSerializer {

  private val transcoder: Transcoder

  init {
    val setting = TranscoderSetting.builder().serializer(serializer).build()
    transcoder = setting.transcoder()
  }

  override fun serialize(data: Any): ByteArray {
    val buf = ByteBufAllocator.DEFAULT.buffer()
    try {
      ByteBufOutputStream(buf).use {
        transcoder.encode(it, data)
      }
      return ByteBufUtil.getBytes(buf)
    } catch (ex: Exception) {
      throw ZkMarshallingError(ex)
    } finally {
      ReferenceCountUtil.release(buf)
    }
  }

  override fun deserialize(bytes: ByteArray): Any {
    val buf = ByteBufAllocator.DEFAULT.buffer()
    try {
      buf.writeBytes(bytes)
      return ByteBufInputStream(buf).use {
        Transcoder.decode(it).result
      }
    } catch (ex: Exception) {
      throw ZkMarshallingError(ex)
    } finally {
      ReferenceCountUtil.release(buf)
    }
  }

}