package org.goblinframework.embedded.server

import java.lang.management.PlatformManagedObject

interface EmbeddedServerMXBean : PlatformManagedObject {

  fun getUpTime(): String?

  fun getMode(): EmbeddedServerMode

  fun getName(): String

}