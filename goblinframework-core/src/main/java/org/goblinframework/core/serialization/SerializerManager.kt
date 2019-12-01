package org.goblinframework.core.serialization

import org.goblinframework.api.core.SerializerMode
import org.goblinframework.core.serialization.fst.FstSerializer
import org.goblinframework.core.serialization.hessian.Hessian2Serializer
import org.goblinframework.core.serialization.java.JavaSerializer
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import java.util.*

@GoblinManagedBean(type = "core")
class SerializerManager private constructor() : GoblinManagedObject(), SerializerManagerMXBean {

  companion object {
    @JvmField val INSTANCE = SerializerManager()
  }

  private val buffer = EnumMap<SerializerMode, SerializerImpl>(SerializerMode::class.java)

  init {
    buffer[SerializerMode.JAVA] = SerializerImpl(JavaSerializer.INSTANCE)
    buffer[SerializerMode.FST] = SerializerImpl(FstSerializer.INSTANCE)
    buffer[SerializerMode.HESSIAN2] = SerializerImpl(Hessian2Serializer.INSTANCE)
  }

  fun getSerializer(id: Byte): Serializer? {
    val mode = SerializerMode.parse(id) ?: return null
    return getSerializer(mode)
  }

  fun getSerializer(mode: SerializerMode): Serializer {
    return buffer[mode]!!
  }

  override fun disposeBean() {
    buffer.values.forEach { it.dispose() }
  }

  override fun getSerializerList(): Array<SerializerMXBean> {
    return buffer.values.toTypedArray()
  }
}