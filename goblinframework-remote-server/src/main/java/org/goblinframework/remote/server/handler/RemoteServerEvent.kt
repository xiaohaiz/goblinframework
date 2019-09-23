package org.goblinframework.remote.server.handler

import org.goblinframework.api.event.GoblinEvent
import org.goblinframework.api.event.GoblinEventChannel
import org.goblinframework.transport.server.handler.TransportRequestContext

@GoblinEventChannel("/goblin/remote/server")
class RemoteServerEvent(val ctx: TransportRequestContext) : GoblinEvent()