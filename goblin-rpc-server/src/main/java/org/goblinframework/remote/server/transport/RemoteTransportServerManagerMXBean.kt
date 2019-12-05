package org.goblinframework.remote.server.transport

import java.lang.management.PlatformManagedObject

interface RemoteTransportServerManagerMXBean : PlatformManagedObject {

  fun getRemoteTransportServer(): RemoteTransportServerMXBean?

}