package org.goblinframework.dao.core.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleBootstrapContext
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInstallContext
import org.goblinframework.dao.core.mapping.EntityMappingBuilderManager

@Install
class DaoModule : GoblinModule {

  override fun name(): String {
    return "DAO"
  }

  override fun install(ctx: GoblinModuleInstallContext) {
    ctx.createChildModuleManager()
        .module("DAO:MYSQL")
        .initialize(ctx)
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    ctx.createChildModuleManager()
        .module("DAO:MYSQL")
        .bootstrap(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager()
        .module("DAO:MYSQL")
        .finalize(ctx)
    EntityMappingBuilderManager.INSTANCE.dispose()
  }
}