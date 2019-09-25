package org.goblinframework.core.config

import org.goblinframework.api.system.RuntimeMode
import java.lang.management.PlatformManagedObject

interface ConfigManagerMXBean : PlatformManagedObject {

  fun getConfigLocationScanner(): ConfigLocationScannerMXBean

  fun getMappingLocationScanner(): MappingLocationScannerMXBean

  fun getApplicationName(): String

  fun getRuntimeMode(): RuntimeMode

}