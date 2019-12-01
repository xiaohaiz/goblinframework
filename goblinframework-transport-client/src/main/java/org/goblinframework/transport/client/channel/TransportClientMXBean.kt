package org.goblinframework.transport.client.channel

import java.lang.management.PlatformManagedObject

interface TransportClientMXBean : PlatformManagedObject {

  fun getWeight(): Int

}