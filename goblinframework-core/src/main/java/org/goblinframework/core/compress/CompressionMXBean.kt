package org.goblinframework.core.compress

import org.goblinframework.api.compression.Compressor
import java.lang.management.PlatformManagedObject

interface CompressionMXBean : PlatformManagedObject {

  fun getCompressor(): Compressor

}