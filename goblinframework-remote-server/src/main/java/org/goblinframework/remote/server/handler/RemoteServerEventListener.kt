package org.goblinframework.remote.server.handler

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.core.event.GoblinEventContext
import org.goblinframework.core.event.GoblinEventListener
import org.goblinframework.remote.core.protocol.RemoteRequest

@Install
@GoblinEventChannel("/goblin/remote/server")
class RemoteServerEventListener : GoblinEventListener {

  override fun accept(context: GoblinEventContext): Boolean {
    return context.event is RemoteServerEvent
  }

  override fun onEvent(context: GoblinEventContext) {
    val event = context.event as RemoteServerEvent
    val request = event.ctx.requestReader.readPayload()!! as RemoteRequest
    println("==========================")
    println(request)
    println("==========================")
  }
}