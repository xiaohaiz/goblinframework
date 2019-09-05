package org.goblinframework.core.compress

import java.lang.management.PlatformManagedObject

interface CompressionManagerMXBean : PlatformManagedObject {

  fun getCompressionList(): Array<CompressionMXBean>

}