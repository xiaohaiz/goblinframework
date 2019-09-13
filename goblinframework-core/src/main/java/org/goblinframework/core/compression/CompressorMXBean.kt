package org.goblinframework.core.compression

import java.lang.management.PlatformManagedObject

interface CompressorMXBean : PlatformManagedObject {

  fun getUpTime(): String

  fun getMode(): CompressorMode

  fun getCompressCount(): Long

  fun getCompressExceptionCount(): Long

  fun getDecompressCount(): Long

  fun getDecompressExceptionCount(): Long
}