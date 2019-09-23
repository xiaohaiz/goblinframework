package org.goblinframework.registry.zookeeper.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.system.*
import org.goblinframework.registry.zookeeper.client.ZkTranscoderManager
import org.goblinframework.registry.zookeeper.module.config.ZookeeperConfigManager

@Install
class RegistryZookeeperModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REGISTRY_ZOOKEEPER
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerConfigParser(ZookeeperConfigManager.INSTANCE.configParser)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    ZookeeperConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ZkTranscoderManager.INSTANCE.dispose()
    ZookeeperConfigManager.INSTANCE.dispose()
  }
}