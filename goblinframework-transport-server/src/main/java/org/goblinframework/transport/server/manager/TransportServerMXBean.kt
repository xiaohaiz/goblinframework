package org.goblinframework.transport.server.manager

import java.lang.management.PlatformManagedObject

interface TransportServerMXBean : PlatformManagedObject {

  fun getUpTime(): String

}