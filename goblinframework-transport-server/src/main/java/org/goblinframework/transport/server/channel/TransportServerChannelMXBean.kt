package org.goblinframework.transport.server.channel

import java.lang.management.PlatformManagedObject

interface TransportServerChannelMXBean : PlatformManagedObject {

  fun getClientId(): String?

  fun getClientReceiveShutdown(): Boolean

}