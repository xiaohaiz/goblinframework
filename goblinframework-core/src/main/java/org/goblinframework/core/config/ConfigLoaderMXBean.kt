package org.goblinframework.core.config

import java.lang.management.PlatformManagedObject

interface ConfigLoaderMXBean : PlatformManagedObject {

  fun getConfigLocationScanner(): ConfigLocationScannerMXBean

  fun getMappingLocationScanner(): MappingLocationScannerMXBean

  fun getApplicationName(): String

}