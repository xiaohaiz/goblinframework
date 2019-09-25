package org.goblinframework.core.config

import java.lang.management.PlatformManagedObject

interface ConfigManagerMXBean : PlatformManagedObject {

  fun getConfigLocationScanner(): ConfigLocationScannerMXBean

  fun getMappingLocationScanner(): MappingLocationScannerMXBean

  fun getApplicationName(): String

}