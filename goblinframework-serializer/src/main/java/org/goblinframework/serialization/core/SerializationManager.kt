package org.goblinframework.serialization.core

import org.goblinframework.api.serialization.Serialization
import org.goblinframework.api.serialization.Serializer
import org.goblinframework.core.management.GoblinManagedBean
import org.goblinframework.core.management.GoblinManagedObject
import org.goblinframework.core.util.GoblinServiceLoader
import java.util.*

@GoblinManagedBean("SERIALIZATION")
class SerializationManager private constructor() : GoblinManagedObject(), SerializationManagerMXBean {

  companion object {
    @JvmField val INSTANCE = SerializationManager()
  }

  private val buffer = EnumMap<Serializer, SerializationDelegator>(Serializer::class.java)

  init {
    GoblinServiceLoader.installedList(Serialization::class.java).forEach {
      buffer.put(it.serializer, SerializationDelegator(it))?.run {
        throw UnsupportedOperationException("Duplicated serializer not allowed")
      }
    }
  }

  fun getSerialization(serializer: Serializer): Serialization? {
    return buffer[serializer]
  }

  fun close() {
    unregisterIfNecessary()
    buffer.values.forEach { it.unregisterIfNecessary() }
  }

  override fun getSerializationList(): Array<SerializationMXBean> {
    return buffer.values.sortedBy { it.serializer }.toTypedArray()
  }
}