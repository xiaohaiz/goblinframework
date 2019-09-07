package org.goblinframework.core.compression

enum class CompressorMode(val id: Byte, val algorithm: String) {

  BZIP2(1.toByte(), "bzip2"),
  GZIP(2.toByte(), "gz"),
  DEFLATE(3.toByte(), "deflate")

}
