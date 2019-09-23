package org.goblinframework.registry.zookeeper.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.system.GoblinSubModule
import org.goblinframework.api.system.ISubModule
import org.goblinframework.api.system.ModuleFinalizeContext
import org.goblinframework.api.system.ModuleInstallContext
import org.goblinframework.registry.zookeeper.client.ZookeeperClientManager
import org.goblinframework.registry.zookeeper.module.config.ZookeeperConfigManager

@Install
class RegistryZookeeperModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.REGISTRY_ZOOKEEPER
  }

  override fun install(ctx: ModuleInstallContext) {
    ctx.registerConfigParser(ZookeeperConfigManager.INSTANCE.configParser)
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    ZookeeperClientManager.INSTANCE.dispose()
    ZookeeperConfigManager.INSTANCE.dispose()
  }
}