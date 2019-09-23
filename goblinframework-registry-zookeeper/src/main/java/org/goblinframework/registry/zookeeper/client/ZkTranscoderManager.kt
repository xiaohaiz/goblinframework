package org.goblinframework.registry.zookeeper.client

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.serialization.SerializerMode
import java.util.*

@Singleton
@GoblinManagedBean(type = "registry.zookeeper")
class ZkTranscoderManager private constructor()
  : GoblinManagedObject(), ZkTranscoderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = ZkTranscoderManager()
  }

  private val transcoders = EnumMap<SerializerMode, ZkTranscoder>(SerializerMode::class.java)

  init {
    val serializerManager = SerializerManager.INSTANCE
    SerializerMode.values()
        .map { serializerManager.getSerializer(it) }
        .forEach { transcoders[it.mode()] = ZkTranscoder(it) }
  }

  fun getTranscoder(mode: SerializerMode): ZkTranscoder {
    return transcoders[mode]!!
  }

  override fun disposeBean() {
    transcoders.values.forEach { it.dispose() }
  }
}