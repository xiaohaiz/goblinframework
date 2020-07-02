package org.goblinframework.embedded.server

import java.lang.management.PlatformManagedObject

interface EmbeddedServerManagerMXBean : PlatformManagedObject {

  fun getEmbeddedServerList(): Array<EmbeddedServerMXBean>

}