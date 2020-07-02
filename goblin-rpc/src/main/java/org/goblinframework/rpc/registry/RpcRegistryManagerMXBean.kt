package org.goblinframework.rpc.registry

import java.lang.management.PlatformManagedObject

interface RpcRegistryManagerMXBean : PlatformManagedObject {

  fun getRpcRegistry(): RpcRegistryMXBean?

}