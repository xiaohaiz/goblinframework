package org.goblinframework.core.compression

import java.lang.management.PlatformManagedObject

interface CompressorMXBean : PlatformManagedObject {

  fun getMode(): CompressorMode

}