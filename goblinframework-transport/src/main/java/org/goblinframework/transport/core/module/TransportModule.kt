package org.goblinframework.transport.core.module

import org.goblinframework.core.bootstrap.GoblinModule
import org.goblinframework.core.bootstrap.GoblinModuleBootstrapContext
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInitializeContext
import org.goblinframework.core.transcoder.DecodedObjectManager
import org.goblinframework.transport.core.protocol.*

class TransportModule : GoblinModule {

  override fun name(): String {
    return "TRANSPORT"
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    DecodedObjectManager.INSTANCE.register("1", HandshakeRequest::class.java)
    DecodedObjectManager.INSTANCE.register("-1", HandshakeResponse::class.java)
    DecodedObjectManager.INSTANCE.register("2", HeartbeatRequest::class.java)
    DecodedObjectManager.INSTANCE.register("-2", HeartbeatResponse::class.java)
    DecodedObjectManager.INSTANCE.register("3", ShutdownRequest::class.java)
    DecodedObjectManager.INSTANCE.register("4", TransportRequest::class.java)
    DecodedObjectManager.INSTANCE.register("-4", TransportResponse::class.java)

    ctx.createChildModuleManager()
        .module("TRANSPORT:CLIENT")
        .next()
        .module("TRANSPORT:SERVER")
        .initialize(ctx)
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    ctx.createChildModuleManager()
        .module("TRANSPORT:CLIENT")
        .next()
        .module("TRANSPORT:SERVER")
        .bootstrap(ctx)
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    ctx.createChildModuleManager()
        .module("TRANSPORT:SERVER")
        .next()
        .module("TRANSPORT:CLIENT")
        .finalize(ctx)
  }
}