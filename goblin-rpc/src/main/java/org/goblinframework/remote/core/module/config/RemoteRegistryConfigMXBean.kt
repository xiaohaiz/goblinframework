package org.goblinframework.remote.core.module.config

import org.goblinframework.api.core.SerializerMode
import java.lang.management.PlatformManagedObject

interface RemoteRegistryConfigMXBean : PlatformManagedObject {

  fun getZookeeper(): String

  fun getConnectionTimeout(): Int

  fun getSessionTimeout(): Int

  fun getSerializer(): SerializerMode

}