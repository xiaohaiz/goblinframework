package org.goblinframework.core.serialization

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.serialization.fst.FstSerializer
import org.goblinframework.core.serialization.hessian.Hessian2Serializer
import org.goblinframework.core.serialization.java.JavaSerializer
import java.util.*

@GoblinManagedBean("CORE")
class SerializerManager private constructor() : GoblinManagedObject(), SerializerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = SerializerManager()
  }

  private val buffer = EnumMap<SerializerMode, SerializerImpl>(SerializerMode::class.java)

  init {
    buffer[SerializerMode.JAVA] = SerializerImpl(JavaSerializer())
    buffer[SerializerMode.FST] = SerializerImpl(FstSerializer())
    buffer[SerializerMode.HESSIAN2] = SerializerImpl(Hessian2Serializer())
  }

  fun getSerializer(id: Byte): Serializer? {
    val mode = SerializerMode.parse(id) ?: return null
    return getSerializer(mode)
  }

  fun getSerializer(mode: SerializerMode): Serializer? {
    return buffer[mode]
  }

  fun close() {
    unregisterIfNecessary()
    buffer.values.forEach { it.close() }
  }

  override fun getSerializerList(): Array<SerializerMXBean> {
    return buffer.values.toTypedArray()
  }
}