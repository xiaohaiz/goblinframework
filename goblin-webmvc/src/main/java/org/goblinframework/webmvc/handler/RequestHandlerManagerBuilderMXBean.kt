package org.goblinframework.webmvc.handler

import java.lang.management.PlatformManagedObject

interface RequestHandlerManagerBuilderMXBean : PlatformManagedObject {

  fun getRequestHandlerManagerList(): Array<RequestHandlerManagerMXBean>

}