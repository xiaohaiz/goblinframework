package org.goblinframework.core.serialization.fst

import org.goblinframework.core.serialization.Serializer
import org.goblinframework.core.serialization.SerializerMode
import java.io.InputStream
import java.io.OutputStream

class FstSerializer : Serializer {

  override fun mode(): SerializerMode {
    return SerializerMode.FST
  }

  override fun serialize(obj: Any, outStream: OutputStream) {
    val configuration = FstConfigurationFactory.configuration
    val fo = configuration.getObjectOutput(outStream)
    fo.writeObject(obj)
    fo.flush()
  }

  override fun serialize(obj: Any): ByteArray {
    val configuration = FstConfigurationFactory.configuration
    return configuration.asByteArray(obj)
  }

  override fun deserialize(inStream: InputStream): Any {
    val configuration = FstConfigurationFactory.configuration
    val fi = configuration.getObjectInput(inStream)
    return fi.readObject()
  }

  override fun deserialize(bs: ByteArray): Any {
    val configuration = FstConfigurationFactory.configuration
    return configuration.asObject(bs)
  }
}