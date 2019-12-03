package org.goblinframework.transport.server.channel

import java.lang.management.PlatformManagedObject

interface TransportServerMXBean : PlatformManagedObject {

  fun getUpTime(): String?

  fun getName(): String

  fun getRunning(): Boolean

  fun getHost(): String?

  fun getPort(): Int?

  fun getTransportServerChannelManager(): TransportServerChannelManagerMXBean?
}