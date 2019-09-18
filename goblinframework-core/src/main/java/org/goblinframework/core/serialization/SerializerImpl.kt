package org.goblinframework.core.serialization

import org.goblinframework.core.exception.GoblinSerializationException
import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.util.StopWatch

import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.atomic.LongAdder

@GoblinManagedBean(type = "CORE", name = "Serializer")
internal class SerializerImpl(private val serializer: Serializer)
  : GoblinManagedObject(), Serializer, SerializerMXBean {

  private val watch = StopWatch()
  private val serializeCount = LongAdder()
  private val serializeExceptionCount = LongAdder()
  private val deserializeCount = LongAdder()
  private val deserializeExceptionCount = LongAdder()

  override fun mode(): SerializerMode {
    return serializer.mode()
  }

  override fun serialize(obj: Any, outStream: OutputStream) {
    try {
      serializer.serialize(obj, outStream)
    } catch (ex: GoblinSerializationException) {
      serializeExceptionCount.increment()
      throw ex
    } finally {
      serializeCount.increment()
    }
  }

  override fun serialize(obj: Any): ByteArray {
    try {
      return serializer.serialize(obj)
    } catch (ex: GoblinSerializationException) {
      serializeExceptionCount.increment()
      throw ex
    } finally {
      serializeCount.increment()
    }
  }

  override fun deserialize(inStream: InputStream): Any {
    try {
      return serializer.deserialize(inStream)
    } catch (ex: GoblinSerializationException) {
      deserializeExceptionCount.increment()
      throw ex
    } finally {
      deserializeCount.increment()
    }
  }

  override fun deserialize(bs: ByteArray): Any {
    try {
      return serializer.deserialize(bs)
    } catch (ex: GoblinSerializationException) {
      deserializeExceptionCount.increment()
      throw ex
    } finally {
      deserializeCount.increment()
    }
  }

  override fun getUpTime(): String {
    return watch.toString()
  }

  override fun getMode(): SerializerMode {
    return mode()
  }

  override fun getSerializeCount(): Long {
    return serializeCount.sum()
  }

  override fun getSerializeExceptionCount(): Long {
    return serializeExceptionCount.sum()
  }

  override fun getDeserializeCount(): Long {
    return deserializeCount.sum()
  }

  override fun getDeserializeExceptionCount(): Long {
    return deserializeExceptionCount.sum()
  }

  override fun disposeBean() {
    watch.stop()
  }
}
