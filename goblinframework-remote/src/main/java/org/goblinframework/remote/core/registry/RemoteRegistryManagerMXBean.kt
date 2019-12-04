package org.goblinframework.remote.core.registry

import java.lang.management.PlatformManagedObject

interface RemoteRegistryManagerMXBean : PlatformManagedObject {

  fun getRemoteRegistry(): RemoteRegistryMXBean?

}