package org.goblinframework.registry.zookeeper.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinChildModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInitializeContext
import org.goblinframework.registry.zookeeper.client.ZkTranscoderManager
import org.goblinframework.registry.zookeeper.module.config.ZookeeperConfigManager

@Install
class RegistryZookeeperModule : GoblinChildModule {

  override fun name(): String {
    return "REGISTRY:ZOOKEEPER"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    ZookeeperConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ZkTranscoderManager.INSTANCE.dispose()
    ZookeeperConfigManager.INSTANCE.dispose()
  }
}