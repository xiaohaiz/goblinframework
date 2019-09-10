package org.goblinframework.webmvc.handler

import java.lang.management.PlatformManagedObject

interface RequestHandlerManagerMXBean : PlatformManagedObject {

  fun getName(): String


}