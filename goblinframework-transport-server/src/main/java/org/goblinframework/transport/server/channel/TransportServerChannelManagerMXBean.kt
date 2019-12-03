package org.goblinframework.transport.server.channel

import java.lang.management.PlatformManagedObject

interface TransportServerChannelManagerMXBean : PlatformManagedObject {

  fun getTransportServerChannelList(): Array<TransportServerChannelMXBean>

}