package org.goblinframework.transport.server.channel

import java.lang.management.PlatformManagedObject

interface TransportServerChannelMXBean : PlatformManagedObject {

  fun getClientId(): String?

  fun getClientName(): String?

  fun getClientHost(): String

  fun getClientPort(): Int

  fun getClientReceiveShutdown(): Boolean

}