package org.goblinframework.transport.server.manager

import java.lang.management.PlatformManagedObject

interface TransportServerManagerMXBean : PlatformManagedObject {

  fun getTransportServerList(): Array<TransportServerMXBean>

}