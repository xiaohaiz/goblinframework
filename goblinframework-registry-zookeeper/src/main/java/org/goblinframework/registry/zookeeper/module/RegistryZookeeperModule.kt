package org.goblinframework.registry.zookeeper.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.system.GoblinSubModule
import org.goblinframework.api.system.ISubModule
import org.goblinframework.api.system.ModuleFinalizeContext
import org.goblinframework.api.system.ModuleInitializeContext
import org.goblinframework.registry.zookeeper.client.ZkTranscoderManager
import org.goblinframework.registry.zookeeper.module.config.ZookeeperConfigManager

@Install
class RegistryZookeeperModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REGISTRY_ZOOKEEPER
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ZookeeperConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ZkTranscoderManager.INSTANCE.dispose()
    ZookeeperConfigManager.INSTANCE.dispose()
  }
}