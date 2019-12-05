package org.goblinframework.remote.server.dispatcher.request

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.remote.server.invocation.RemoteServerInvocation

class RemoteServerRequestEvent(val invocation: RemoteServerInvocation) : GoblinEvent()