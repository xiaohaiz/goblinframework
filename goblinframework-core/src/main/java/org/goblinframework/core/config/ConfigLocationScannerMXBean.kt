package org.goblinframework.core.config

import java.lang.management.PlatformManagedObject

interface ConfigLocationScannerMXBean : PlatformManagedObject {

  fun getConfigPath(): String

  fun getAvailable(): Boolean

  fun getFoundInFileSystem(): Boolean

  fun getCandidatePathList(): Array<String>

}