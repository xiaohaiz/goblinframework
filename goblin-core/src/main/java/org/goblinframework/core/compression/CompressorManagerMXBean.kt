package org.goblinframework.core.compression

import java.lang.management.PlatformManagedObject

interface CompressorManagerMXBean : PlatformManagedObject {

  fun getCompressorList(): Array<CompressorMXBean>

}