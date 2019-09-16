package org.goblinframework.core.config

import java.lang.management.PlatformManagedObject

interface ConfigLocationScannerMXBean : PlatformManagedObject {

  fun getConfigPath(): String?

  fun getFoundInFileSystem(): Boolean

  fun getCandidatePathList(): Array<String>

}