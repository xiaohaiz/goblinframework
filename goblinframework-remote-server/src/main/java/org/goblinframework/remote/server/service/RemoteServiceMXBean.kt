package org.goblinframework.remote.server.service

import java.lang.management.PlatformManagedObject

interface RemoteServiceMXBean : PlatformManagedObject {

  fun getMode(): String

}