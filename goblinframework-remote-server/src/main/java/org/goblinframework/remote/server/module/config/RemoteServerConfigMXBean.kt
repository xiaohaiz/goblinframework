package org.goblinframework.remote.server.module.config

import java.lang.management.PlatformManagedObject

interface RemoteServerConfigMXBean : PlatformManagedObject {

  fun getName(): String

  fun getHost(): String

  fun getPort(): Int

}