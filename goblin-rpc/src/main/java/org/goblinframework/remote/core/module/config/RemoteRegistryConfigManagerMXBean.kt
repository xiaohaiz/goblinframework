package org.goblinframework.remote.core.module.config

import java.lang.management.PlatformManagedObject

interface RemoteRegistryConfigManagerMXBean : PlatformManagedObject {

  fun getRemoteRegistryConfig(): RemoteRegistryConfigMXBean?

}