package org.goblinframework.remote.server.module.runtime

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.serialization.SerializerMode
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.core.transcoder.Transcoder
import org.goblinframework.core.transcoder.TranscoderSetting
import org.goblinframework.remote.server.module.config.RemoteServerConfigManager
import java.util.*

@Singleton
@GoblinManagedBean("RemoteServer")
class RemoteServerTranscoderManager private constructor()
  : GoblinManagedObject(), RemoteServerTranscoderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RemoteServerTranscoderManager()
  }

  private val buffer = EnumMap<SerializerMode, Transcoder>(SerializerMode::class.java)

  override fun initializeBean() {
    val serverConfig = RemoteServerConfigManager.INSTANCE.getRemoteServerConfig()
    SerializerMode.values().forEach {
      val transcoder = TranscoderSetting.builder()
          .serializer(it)
          .compressor(serverConfig.getCompressor())
          .compressionThreshold(serverConfig.getCompressionThreshold())
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