package org.goblinframework.rpc.module.config

import org.goblinframework.api.core.SerializerMode
import java.lang.management.PlatformManagedObject

interface RpcRegistryConfigMXBean : PlatformManagedObject {

  fun getZookeeper(): String

  fun getConnectionTimeout(): Int

  fun getSessionTimeout(): Int

  fun getSerializer(): SerializerMode

}