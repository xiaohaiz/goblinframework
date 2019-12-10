package org.goblinframework.embedded.server

import java.lang.management.PlatformManagedObject

interface EmbeddedServerMXBean : PlatformManagedObject {

  fun getUpTime(): String?

  fun getMode(): EmbeddedServerMode

  fun getName(): String

  fun getRunning(): Boolean

  fun getHost(): String?

  fun getPort(): Int?

}