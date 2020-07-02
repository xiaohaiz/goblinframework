package org.goblinframework.core.container

import java.lang.management.PlatformManagedObject

interface SpringContainerMXBean : PlatformManagedObject {

  fun getUniqueId(): String

}