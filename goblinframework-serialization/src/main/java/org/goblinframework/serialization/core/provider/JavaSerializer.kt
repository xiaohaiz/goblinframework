package org.goblinframework.serialization.core.provider

import org.apache.commons.lang3.SerializationUtils
import org.goblinframework.serialization.core.Serializer
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

  override fun deserialize(inStream: InputStream): Any {
    return SerializationUtils.deserialize(inStream)
  }
}