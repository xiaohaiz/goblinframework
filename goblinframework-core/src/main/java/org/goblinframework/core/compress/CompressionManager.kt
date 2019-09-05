package org.goblinframework.core.compress

import org.goblinframework.api.compression.Compression
import org.goblinframework.api.compression.Compressor
import org.goblinframework.core.management.GoblinManagedBean
import org.goblinframework.core.management.GoblinManagedObject
import java.util.*

@GoblinManagedBean("CORE")
class CompressionManager private constructor()
  : GoblinManagedObject(), CompressionManagerMXBean {

  companion object {
    @JvmField
    val INSTANCE = CompressionManager()
  }

  private val buffer = EnumMap<Compressor, CompressionImpl>(Compressor::class.java)

  init {
    for (compressor in Compressor.values()) {
      buffer[compressor] = CompressionImpl(compressor)
    }
  }

  fun getCompression(compressor: Compressor): Compression {
    return buffer[compressor]!!
  }

  override fun getCompressionList(): Array<CompressionMXBean> {
    return buffer.values.sortedBy { it.compressor }.toTypedArray()
  }
}
