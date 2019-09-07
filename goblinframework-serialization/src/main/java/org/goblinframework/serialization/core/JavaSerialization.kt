package org.goblinframework.serialization.core

import org.apache.commons.lang3.SerializationUtils
import org.goblinframework.api.serialization.Serialization
import org.goblinframework.api.serialization.Serializer0
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable

class JavaSerialization : Serialization {

  override fun getSerializer(): Serializer0 {
    return Serializer0.JAVA
  }

  override fun serialize(obj: Any, outStream: OutputStream) {
    if (obj !is Serializable) {
      throw IllegalArgumentException("Serializable object required")
    }
    SerializationUtils.serialize(obj, outStream)
  }

  override fun deserialize(inStream: InputStream): Any {
    return SerializationUtils.deserialize<Any>(inStream)
  }
}