package org.goblinframework.serialization.core.manager

import org.goblinframework.api.serialization.Serializer
import org.goblinframework.core.management.GoblinManagedBean
import org.goblinframework.core.management.GoblinManagedObject
import org.goblinframework.core.util.GoblinServiceLoader

@GoblinManagedBean("SERIALIZATION")
class SerializerManager private constructor() : GoblinManagedObject(), SerializerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = SerializerManager()
  }

  private val buffer = mutableMapOf<Byte, SerializerImpl>()

  init {
    GoblinServiceLoader.installedList(Serializer::class.java).forEach {
      val id = it.id()
      buffer[id]?.run {
        throw UnsupportedOperationException("Duplicated serializer not allowed: $id")
      }
      buffer[id] = SerializerImpl(it)
    }
  }

  fun initialize() {}

  fun close() {
    unregisterIfNecessary()
    buffer.values.forEach { it.unregisterIfNecessary() }
  }

  override fun getSerializerList(): Array<SerializerMXBean> {
    return buffer.values.sortedBy { it.id() }.toTypedArray()
  }
}