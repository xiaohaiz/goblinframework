package org.goblinframework.remote.server.handler

import org.goblinframework.transport.server.channel.TransportServerMXBean
import java.lang.management.PlatformManagedObject

interface RemoteServerMXBean : PlatformManagedObject {

  fun getTransportServer(): TransportServerMXBean

}