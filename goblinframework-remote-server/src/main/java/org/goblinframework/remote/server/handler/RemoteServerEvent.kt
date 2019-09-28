package org.goblinframework.remote.server.handler

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.core.event.GoblinEventChannel
import org.goblinframework.transport.server.handler.TransportRequestContext

@GoblinEventChannel("/goblin/remote/server")
class RemoteServerEvent(val ctx: TransportRequestContext) : GoblinEvent()