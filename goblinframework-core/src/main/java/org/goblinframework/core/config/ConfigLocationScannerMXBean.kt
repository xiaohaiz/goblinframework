package org.goblinframework.core.config

import java.lang.management.PlatformManagedObject

interface ConfigLocationScannerMXBean : PlatformManagedObject {

  fun getConfigFile(): String

  fun getConfigPath(): String

  fun getConfigPathUrl(): String?

  fun getFoundInFileSystem(): Boolean

  fun getCandidatePathList(): Array<String>

}