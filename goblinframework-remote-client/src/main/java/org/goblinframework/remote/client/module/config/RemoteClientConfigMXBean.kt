package org.goblinframework.remote.client.module.config

import java.lang.management.PlatformManagedObject

interface RemoteClientConfigMXBean : PlatformManagedObject {

  fun getRegistry(): String?

}