package org.goblinframework.core.compression

enum class CompressorMode(val id: Byte, val algorithm: String) {

  BZIP2(1.toByte(), "bzip2"),
  GZIP(2.toByte(), "gz"),
  DEFLATE(3.toByte(), "deflate");

  companion object {
    private val modes = mutableMapOf<Byte, CompressorMode>()

    init {
      for (it in values()) {
        modes[it.id] = it
      }
    }

    fun mode(id: Byte): CompressorMode? {
      return modes[id]
    }
  }
}
