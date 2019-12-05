package org.goblinframework.remote.server.transport

import org.goblinframework.transport.server.channel.TransportServerMXBean
import java.lang.management.PlatformManagedObject

interface RemoteTransportServerMXBean : PlatformManagedObject {

  fun getTransportServer(): TransportServerMXBean

}