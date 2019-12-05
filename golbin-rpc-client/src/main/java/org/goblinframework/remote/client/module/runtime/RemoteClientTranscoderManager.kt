package org.goblinframework.remote.client.module.runtime

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.transcoder.Transcoder
import org.goblinframework.core.transcoder.TranscoderSetting
import org.goblinframework.remote.client.module.config.RemoteClientConfigManager
import java.util.*

@Singleton
@GoblinManagedBean("RemoteClient")
class RemoteClientTranscoderManager private constructor()
  : GoblinManagedObject(), RemoteClientTranscoderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteClientTranscoderManager()
  }

  private val buffer = EnumMap<SerializerMode, Transcoder>(SerializerMode::class.java)

  override fun initializeBean() {
    val clientConfig = RemoteClientConfigManager.INSTANCE.getRemoteClientConfig()
    SerializerMode.values().forEach {
      val transcoder = TranscoderSetting.builder()
          .serializer(it)
          .compressor(clientConfig.getCompressor())
          .compressionThreshold(clientConfig.getCompressionThreshold())
          .build()
          .transcoder()
      buffer[it] = transcoder
    }
  }

  fun getTranscoder(serializer: SerializerMode): Transcoder {
    check(buffer.isNotEmpty()) { "Initialization is required" }
    return buffer[serializer]!!
  }
}