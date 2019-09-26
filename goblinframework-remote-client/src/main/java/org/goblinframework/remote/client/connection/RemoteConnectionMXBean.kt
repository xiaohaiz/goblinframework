package org.goblinframework.remote.client.connection

import java.lang.management.PlatformManagedObject

interface RemoteConnectionMXBean : PlatformManagedObject {

  fun getName(): String

}