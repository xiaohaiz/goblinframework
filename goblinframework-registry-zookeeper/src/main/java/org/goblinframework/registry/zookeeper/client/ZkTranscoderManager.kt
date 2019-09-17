package org.goblinframework.registry.zookeeper.client

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.serialization.SerializerManager
import org.goblinframework.core.serialization.SerializerMode
import java.util.*

@Singleton
@GoblinManagedBean("REGISTRY.ZOOKEEPER")
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

  fun destroy() {
    unregisterIfNecessary()
    transcoders.values.forEach { it.close() }
  }
}