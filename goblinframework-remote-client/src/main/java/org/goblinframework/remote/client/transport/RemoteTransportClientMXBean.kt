package org.goblinframework.remote.client.transport

import java.lang.management.PlatformManagedObject

interface RemoteTransportClientMXBean : PlatformManagedObject {

  fun getId(): String

  fun getWeight(): Int

}