package org.goblinframework.registry.zookeeper.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinChildModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.registry.zookeeper.client.ZkTranscoderManager

@Install
class RegistryZookeeperModule : GoblinChildModule {

  override fun name(): String {
    return "REGISTRY:ZOOKEEPER"
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ZkTranscoderManager.INSTANCE.close()
  }
}