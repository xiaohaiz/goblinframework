package org.goblinframework.remote.client.dispatcher.response

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.remote.client.invocation.RemoteClientInvocation

class RemoteClientResponseEvent(val invocation: RemoteClientInvocation) : GoblinEvent()