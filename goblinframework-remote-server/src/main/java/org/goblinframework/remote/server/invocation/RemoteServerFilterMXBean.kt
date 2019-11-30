package org.goblinframework.remote.server.invocation

import java.lang.management.PlatformManagedObject

interface RemoteServerFilterMXBean : PlatformManagedObject {

  fun getName(): String

  fun getExecutionCount(): Long

}