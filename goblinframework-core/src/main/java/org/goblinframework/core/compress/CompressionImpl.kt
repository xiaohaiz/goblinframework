package org.goblinframework.core.compress

import org.goblinframework.api.compression.Compression
import org.goblinframework.api.compression.Compressor
import java.io.InputStream
import java.io.OutputStream

class CompressionImpl
internal constructor(private val compressor: Compressor) : Compression {

  override fun getCompressor(): Compressor {
    return compressor
  }

  override fun compress(inStream: InputStream, outStream: OutputStream) {
    CompressionUtils.compress(compressor, inStream, outStream)
  }

  override fun decompress(inStream: InputStream, outStream: OutputStream) {
    CompressionUtils.decompress(compressor, inStream, outStream)
  }
}