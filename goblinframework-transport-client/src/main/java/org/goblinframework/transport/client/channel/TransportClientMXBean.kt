package org.goblinframework.transport.client.channel

import java.lang.management.PlatformManagedObject

interface TransportClientMXBean : PlatformManagedObject {

  fun getServerId(): String?

  fun getServerName(): String?

  fun getServerHost(): String

  fun getServerPort(): Int

}