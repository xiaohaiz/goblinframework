package org.goblinframework.core.serialization

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject

import java.io.InputStream
import java.io.OutputStream

@GoblinManagedBean(type = "CORE", name = "Serializer")
internal class SerializerImpl(private val serializer: Serializer)
  : GoblinManagedObject(), Serializer, SerializerMXBean {

  override fun id(): Byte {
    return serializer.id()
  }

  override fun serialize(obj: Any, outStream: OutputStream) {
    serializer.serialize(obj, outStream)
  }

  override fun serialize(obj: Any): ByteArray {
    return serializer.serialize(obj)
  }

  override fun deserialize(inStream: InputStream): Any {
    return serializer.deserialize(inStream)
  }

  override fun deserialize(bs: ByteArray): Any {
    return serializer.deserialize(bs)
  }

  override fun getId(): Byte {
    return id()
  }

  fun close() {
    unregisterIfNecessary()
  }
}
