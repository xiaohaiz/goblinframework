package org.goblinframework.core.compress

import org.goblinframework.api.compression.Compression
import org.goblinframework.api.compression.Compressor
import org.goblinframework.core.management.GoblinManagedBean
import org.goblinframework.core.management.GoblinManagedObject
import java.io.InputStream
import java.io.OutputStream

@GoblinManagedBean("CORE", "Compression")
class CompressionImpl
internal constructor(private val compressor: Compressor)
  : GoblinManagedObject(), Compression, CompressionMXBean {

  override fun getCompressor(): Compressor {
    return compressor
  }

  override fun compress(inStream: InputStream, outStream: OutputStream) {
    CompressionUtils.compress(compressor, inStream, outStream)
  }

  override fun decompress(inStream: InputStream, outStream: OutputStream) {
    CompressionUtils.decompress(compressor, inStream, outStream)
  }

  internal fun close() {
    unregisterIfNecessary()
  }
}