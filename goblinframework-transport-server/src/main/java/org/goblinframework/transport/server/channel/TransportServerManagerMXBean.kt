package org.goblinframework.transport.server.channel

import java.lang.management.PlatformManagedObject

interface TransportServerManagerMXBean : PlatformManagedObject {

  fun getTransportServerList(): Array<TransportServerMXBean>

}