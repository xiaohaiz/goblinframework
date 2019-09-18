package org.goblinframework.core.compression

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import java.util.*

@GoblinManagedBean("CORE")
class CompressorManager private constructor() : GoblinManagedObject(), CompressorManagerMXBean {

  companion object {
    @JvmField val INSTANCE = CompressorManager()
  }

  private val buffer = EnumMap<CompressorMode, CompressorImpl>(CompressorMode::class.java)

  init {
    for (mode in CompressorMode.values()) {
      buffer[mode] = CompressorImpl(mode)
    }
  }

  fun getCompressor(id: Byte): Compressor? {
    val mode = CompressorMode.resolve(id) ?: return null
    return getCompressor(mode)
  }

  fun getCompressor(compressor: CompressorMode): Compressor {
    return buffer[compressor]!!
  }

  override fun getCompressorList(): Array<CompressorMXBean> {
    return buffer.values.sortedBy { it.mode() }.toTypedArray()
  }

  override fun disposeBean() {
    buffer.values.forEach { it.dispose() }
  }
}
