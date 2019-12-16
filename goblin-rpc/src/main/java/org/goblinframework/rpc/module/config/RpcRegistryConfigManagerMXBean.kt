package org.goblinframework.rpc.module.config

import java.lang.management.PlatformManagedObject

interface RpcRegistryConfigManagerMXBean : PlatformManagedObject {

  fun getRpcRegistryConfig(): RpcRegistryConfigMXBean?

}