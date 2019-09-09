package org.goblinframework.serialization.core.manager

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.GoblinServiceLoader
import org.goblinframework.serialization.core.Serializer

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

  fun getSerializer(id: Byte): Serializer? {
    return buffer[id]
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