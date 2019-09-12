package org.goblinframework.core.serialization.java

import org.apache.commons.lang3.SerializationUtils
import org.goblinframework.core.serialization.Serializer
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable

class JavaSerializer : Serializer {

  override fun id(): Byte {
    return Serializer.JAVA
  }

  override fun serialize(obj: Any, outStream: OutputStream) {
    if (obj !is Serializable) {
      throw UnsupportedOperationException("Serializable is required")
    }
    SerializationUtils.serialize(obj, outStream)
  }

  override fun serialize(obj: Any): ByteArray {
    if (obj !is Serializable) {
      throw UnsupportedOperationException("Serializable is required")
    }
    return SerializationUtils.serialize(obj)
  }

  override fun deserialize(inStream: InputStream): Any {
    return SerializationUtils.deserialize(inStream)
  }

  override fun deserialize(bs: ByteArray): Any {
    return SerializationUtils.deserialize(bs)
  }
}