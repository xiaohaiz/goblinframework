package org.goblinframework.remote.server.dispatcher.response

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.remote.server.invocation.RemoteServerInvocation

class RemoteServerResponseEvent(val invocation: RemoteServerInvocation) : GoblinEvent()