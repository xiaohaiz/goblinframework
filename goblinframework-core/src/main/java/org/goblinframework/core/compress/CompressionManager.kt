package org.goblinframework.core.compress

import org.goblinframework.api.compression.Compression
import org.goblinframework.api.compression.Compressor
import java.util.*

class CompressionManager private constructor() {

  companion object {
    val instance = CompressionManager()
  }

  private val buffer = EnumMap<Compressor, Compression>(Compressor::class.java)

  init {
    buffer[Compressor.BZIP2] = CompressionImpl(Compressor.BZIP2)
    buffer[Compressor.GZIP] = CompressionImpl(Compressor.GZIP)
  }

  fun getCompression(compressor: Compressor): Compression {
    return buffer[compressor]!!
  }
}
