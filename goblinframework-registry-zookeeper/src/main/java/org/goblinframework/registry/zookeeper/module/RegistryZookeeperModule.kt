package org.goblinframework.registry.zookeeper.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinChildModule

@Install
class RegistryZookeeperModule : GoblinChildModule {

  override fun name(): String {
    return "REGISTRY:ZOOKEEPER"
  }
}