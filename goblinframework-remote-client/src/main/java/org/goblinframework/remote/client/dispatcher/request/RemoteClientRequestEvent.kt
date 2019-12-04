package org.goblinframework.remote.client.dispatcher.request

import org.goblinframework.core.event.GoblinEvent
import org.goblinframework.remote.client.invocation.RemoteClientInvocation

class RemoteClientRequestEvent(val invocation: RemoteClientInvocation) : GoblinEvent()