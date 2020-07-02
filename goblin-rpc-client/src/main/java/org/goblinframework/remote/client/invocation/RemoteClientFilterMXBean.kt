package org.goblinframework.remote.client.invocation

import java.lang.management.PlatformManagedObject

interface RemoteClientFilterMXBean : PlatformManagedObject {

  fun getName(): String

  fun getExecutionCount(): Long

}