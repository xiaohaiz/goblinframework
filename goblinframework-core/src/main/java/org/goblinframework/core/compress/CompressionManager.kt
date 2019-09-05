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
    for (compressor in Compressor.values()) {
      buffer[compressor] = CompressionImpl(compressor)
    }
  }

  fun getCompression(compressor: Compressor): Compression {
    return buffer[compressor]!!
  }
}
